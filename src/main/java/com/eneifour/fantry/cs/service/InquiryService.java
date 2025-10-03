package com.eneifour.fantry.cs.service;

import com.eneifour.fantry.common.util.file.FileMeta;
import com.eneifour.fantry.common.util.file.FileService;
import com.eneifour.fantry.cs.domain.CsAttachment;
import com.eneifour.fantry.cs.domain.CsType;
import com.eneifour.fantry.cs.domain.Inquiry;
import com.eneifour.fantry.cs.dto.InquiryCreateRequest;
import com.eneifour.fantry.cs.dto.InquiryDetailResponse;
import com.eneifour.fantry.cs.dto.InquirySummaryResponse;
import com.eneifour.fantry.cs.dto.InquirySearchCondition;
import com.eneifour.fantry.cs.exception.CsErrorCode;
import com.eneifour.fantry.cs.exception.CsException;
import com.eneifour.fantry.cs.repository.CsAttachmentRepository;
import com.eneifour.fantry.cs.repository.CsTypeRepository;
import com.eneifour.fantry.cs.repository.InquiryRepository;
import com.eneifour.fantry.cs.repository.InquirySpecification;
import com.eneifour.fantry.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquirySpecification inquirySpecification;
    private final FileService fileService;
    private final CsAttachmentRepository csAttachmentRepository;
    private final CsTypeRepository csTypeRepository;
    private final String SUB_DIRECTORY = "cs/inquiry";

    public InquirySummaryResponse create(InquiryCreateRequest request, Member member) {
        CsType csType = csTypeRepository.findById(request.csTypeId())
                .orElseThrow(() -> new CsException(CsErrorCode.CSTYPE_NOT_FOUND));
        Inquiry inquiry = request.toEntity(member, csType);
        Inquiry saveInquiry = inquiryRepository.save(inquiry);

        return InquirySummaryResponse.from(saveInquiry);
    }

    public void addAttachments(int inquiryId, List<MultipartFile> files, Member member) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new CsException(CsErrorCode.INQUIRY_NOT_FOUND));

        if(inquiry.getInquiredBy().getMemberId() != member.getMemberId()){
            throw new CsException(CsErrorCode.ACCESS_DENIED);
        }

        List<FileMeta> savedFileMetas = fileService.uploadFiles(files, SUB_DIRECTORY, member);

        savedFileMetas.forEach(fileMeta -> {
            CsAttachment attachment = CsAttachment.builder()
                    .inquiry(inquiry)
                    .filemeta(fileMeta)
                    .build();
            csAttachmentRepository.save(attachment);
        });
    }

    @Transactional(readOnly = true)
    public InquiryDetailResponse getInquiry(int id) {
        // 1. 문의 정보를 조회합니다.
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new CsException(CsErrorCode.INQUIRY_NOT_FOUND));

        // 2. 해당 문의에 대한 첨부파일 목록을 별도로 조회합니다.
        List<CsAttachment> attachments = csAttachmentRepository.findByInquiry(inquiry);

        // 3. 첨부파일 목록에서 fileMetaId 목록을 추출합니다.
        List<Integer> fileMetaIds = attachments.stream()
                .map(attachment -> attachment.getFilemeta().getFilemetaId())
                .collect(Collectors.toList());

        // 4. FileService를 사용하여 URL 목록을 가져옵니다. (첨부파일이 있을 경우에만)
        List<String> urls;
        if (fileMetaIds.isEmpty()) {
            urls = Collections.emptyList();
        } else {
            // getFileAccessUrls는 Map<Integer, String>을 반환하므로, value들만 List로 변환합니다.
            urls = new ArrayList<>(fileService.getFileAccessUrls(fileMetaIds).values());
        }

        // 5. 조회된 문의 정보와 URL 목록을 DTO로 조합하여 반환합니다.
        return InquiryDetailResponse.from(inquiry, urls);
    }

    @Transactional(readOnly = true)
    public Page<InquirySummaryResponse> searchInquires(InquirySearchCondition condition, Pageable pageable){
        Specification<Inquiry> spec = inquirySpecification.toSpecification(condition);
        return inquiryRepository.findAll(spec, pageable)
                .map(InquirySummaryResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<InquirySummaryResponse> findMyInquiries(Member member, Pageable pageable) {
        return inquiryRepository.findByInquiredByOrderByInquiredAtDesc(member, pageable)
                .map(InquirySummaryResponse::from);
    }
}
