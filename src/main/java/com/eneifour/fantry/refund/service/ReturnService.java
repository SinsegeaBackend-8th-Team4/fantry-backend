package com.eneifour.fantry.refund.service;

import com.eneifour.fantry.auction.domain.OrderStatus;
import com.eneifour.fantry.auction.domain.Orders;
import com.eneifour.fantry.auction.repository.OrdersRepository;
import com.eneifour.fantry.common.util.file.FileMeta;
import com.eneifour.fantry.common.util.file.FileService;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.refund.domain.ReturnRequest;
import com.eneifour.fantry.refund.domain.ReturnStatus;
import com.eneifour.fantry.refund.domain.ReturnStatusHistory;
import com.eneifour.fantry.refund.dto.ReturnCreateRequest;
import com.eneifour.fantry.refund.dto.ReturnDetailResponse;
import com.eneifour.fantry.refund.dto.ReturnSummaryResponse;
import com.eneifour.fantry.refund.exception.ReturnErrorCode;
import com.eneifour.fantry.refund.exception.ReturnException;
import com.eneifour.fantry.refund.repository.ReturnRepository;
import com.eneifour.fantry.refund.repository.ReturnStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ReturnService {

    private final ReturnRepository returnRepository;
    private final OrdersRepository ordersRepository;
    private final FileService fileService;
    private final ReturnStatusHistoryRepository historyRepository;

    public ReturnDetailResponse createReturnRequest(ReturnCreateRequest request, Member member) {
        Orders order = ordersRepository.findById(request.orderId())
                .orElseThrow(() -> new ReturnException(ReturnErrorCode.ORDER_NOT_FOUND));

        if (!order.getMember().getId().equals(member.getId())) {
            throw new ReturnException(ReturnErrorCode.ACCESS_DENIED);
        }

        if (returnRepository.existsByOrders(order)) {
            throw new ReturnException(ReturnErrorCode.DUPLICATE_REQUEST);
        }

        if (order.getOrderStatus() != OrderStatus.DELIVERED) {
            throw new ReturnException(ReturnErrorCode.NOT_REFUNDABLE_STATUS);
        }

        // 팀원 테이블 수정 예정
//        if (ChronoUnit.DAYS.between(order.getDeliveryCompletedAt(), LocalDateTime.now()) > 7) {
//            throw new ReturnException(ReturnErrorCode.REFUND_PERIOD_EXPIRED);
//        }

        ReturnRequest returnRequest = ReturnRequest.of(order, member, request);
        ReturnRequest savedReturnRequest = returnRepository.save(returnRequest);

        return ReturnDetailResponse.from(savedReturnRequest, Collections.emptyList());
    }

    public void addAttachments(int returnRequestId, List<MultipartFile> files, Member member) {
        ReturnRequest returnRequest = returnRepository.findById(returnRequestId)
                .orElseThrow(() -> new ReturnException(ReturnErrorCode.RETURN_REQUEST_NOT_FOUND));

        if (!returnRequest.getMember().getId().equals(member.getId())) {
            throw new ReturnException(ReturnErrorCode.ACCESS_DENIED);
        }

        String subDirectory = String.format("refund/%s/%d",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM")),
                member.getMemberId()
        );

        List<FileMeta> savedFileMetas = fileService.uploadFiles(files, subDirectory, member);
        savedFileMetas.forEach(returnRequest::addAttachment);
    }

    @Transactional(readOnly = true)
    public Page<ReturnSummaryResponse> getMyReturnRequests(Member member, Pageable pageable) {
        Page<ReturnRequest> returnRequestPage = returnRepository.findByMember(member, pageable);
        return returnRequestPage.map(ReturnSummaryResponse::from);
    }

    @Transactional(readOnly = true)
    public ReturnDetailResponse getMyReturnRequestDetail(int returnRequestId, Member member) {
        ReturnRequest returnRequest = returnRepository.findWithAttachmentsAndHistoriesById(returnRequestId)
                .orElseThrow(() -> new ReturnException(ReturnErrorCode.RETURN_REQUEST_NOT_FOUND));

        if (!returnRequest.getMember().getId().equals(member.getId())) {
            throw new ReturnException(ReturnErrorCode.ACCESS_DENIED);
        }

        List<String> urls = getAttachmentUrls(returnRequest);
        return ReturnDetailResponse.from(returnRequest, urls);
    }

    public void cancelReturnRequest(int returnRequestId, Member member) {
        ReturnRequest returnRequest = returnRepository.findById(returnRequestId)
                .orElseThrow(() -> new ReturnException(ReturnErrorCode.RETURN_REQUEST_NOT_FOUND));

        if (!returnRequest.getMember().getId().equals(member.getId())) {
            throw new ReturnException(ReturnErrorCode.ACCESS_DENIED);
        }

        ReturnStatus oldStatus = returnRequest.getStatus();
        returnRequest.cancelByUser(member);

        addStatusHistory(returnRequest, oldStatus, ReturnStatus.USER_CANCELLED, member, "사용자가 직접 요청을 철회함");
    }

    // --- Helper Methods ---
    private void addStatusHistory(ReturnRequest returnRequest, ReturnStatus oldStatus, ReturnStatus newStatus, Member updatedBy, String memo) {
        ReturnStatusHistory history = ReturnStatusHistory.builder()
                .returnRequest(returnRequest)
                .previousStatus(oldStatus)
                .newStatus(newStatus)
                .updatedBy(updatedBy)
                .memo(memo)
                .build();
        historyRepository.save(history);
    }

    private List<String> getAttachmentUrls(ReturnRequest returnRequest) {
        if (returnRequest.getAttachments() == null || returnRequest.getAttachments().isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> fileMetaIds = returnRequest.getAttachments().stream()
                .map(attachment -> attachment.getFilemeta().getFilemetaId())
                .toList();
        Map<Integer, String> urlMap = fileService.getFileAccessUrls(fileMetaIds);
        return new ArrayList<>(urlMap.values());
    }
}
