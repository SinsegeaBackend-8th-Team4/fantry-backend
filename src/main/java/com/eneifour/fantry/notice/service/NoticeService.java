package com.eneifour.fantry.notice.service;

import com.eneifour.fantry.common.util.file.FileService;
import com.eneifour.fantry.notice.domain.Notice;
import com.eneifour.fantry.notice.dto.NoticeDetailResponse;
import com.eneifour.fantry.notice.dto.NoticeSearchRequest;
import com.eneifour.fantry.notice.dto.NoticeSummaryResponse;
import com.eneifour.fantry.notice.exception.NoticeErrorCode;
import com.eneifour.fantry.notice.exception.NoticeException;
import com.eneifour.fantry.notice.repository.NoticeRepository;
import com.eneifour.fantry.notice.repository.NoticeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 사용자용 공지사항 조회 서비스를 담당합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeSpecification noticeSpecification;
    private final FileService fileService;

    /**
     * 공지사항 목록을 동적 조건과 페이징을 적용하여 조회합니다.
     * @param request  검색 조건
     * @param pageable 페이징 정보
     * @return 페이징된 공지사항 요약 DTO
     */
    public Page<NoticeSummaryResponse> searchNotices(NoticeSearchRequest request, Pageable pageable) {
        Specification<Notice> spec = noticeSpecification.toSpecification(request);
        Page<Notice> noticePage = noticeRepository.findAll(spec, pageable);
        return noticePage.map(NoticeSummaryResponse::from);
    }

    /**
     * 공지사항 상세 정보를 조회합니다.
     * @param noticeId 조회할 공지사항 ID
     * @return 공지사항 상세 정보 DTO
     */
    public NoticeDetailResponse getNotice(int noticeId) {
        Notice notice = noticeRepository.findWithAttachmentsById(noticeId)
                .orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));

        List<String> urls = getAttachmentUrls(notice);
        return NoticeDetailResponse.from(notice, urls);
    }

    private List<String> getAttachmentUrls(Notice notice) {
        if (notice.getAttachments() == null || notice.getAttachments().isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> fileMetaIds = notice.getAttachments().stream()
                .map(attachment -> attachment.getFilemeta().getFilemetaId())
                .toList();

        Map<Integer, String> urlMap = fileService.getFileAccessUrls(fileMetaIds);
        return new ArrayList<>(urlMap.values());
    }
}
