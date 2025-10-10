package com.eneifour.fantry.cs.service;

import com.eneifour.fantry.common.util.HtmlSanitizer;
import com.eneifour.fantry.common.util.file.FileMeta;
import com.eneifour.fantry.common.util.file.FileService;
import com.eneifour.fantry.cs.domain.CsType;
import com.eneifour.fantry.cs.domain.Inquiry;
import com.eneifour.fantry.cs.dto.*;
import com.eneifour.fantry.cs.exception.InquiryErrorCode;
import com.eneifour.fantry.cs.exception.InquiryException;
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
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquirySpecification inquirySpecification;
    private final FileService fileService;
    private final CsTypeRepository csTypeRepository;
    // 파일 저장경로
    private final String SUB_DIRECTORY = "cs/inquiry";
    // XSS 보안 적용을 위해 미리 정의해놓은 객체 주입
    private final HtmlSanitizer htmlSanitizer;

    /**************************
     * 유저 기능
     **************************/

    // 1:1 문의 작성(유저), Json 형식의 텍스트만 먼저 테이블에 insert
    public InquirySummaryResponse create(InquiryCreateRequest request, Member member) {
        CsType csType = csTypeRepository.findById(request.csTypeId())
                .orElseThrow(() -> new InquiryException(InquiryErrorCode.CSTYPE_NOT_FOUND));

        Inquiry inquiry = request.toEntity(member, csType);
        Inquiry saveInquiry = inquiryRepository.save(inquiry);

        return InquirySummaryResponse.from(saveInquiry);
    }

    // 1:1 문의 작성(유저), 텍스트 인서트 후 MultipartFile 파일을 인서트
    public void addAttachments(int inquiryId, List<MultipartFile> files, Member member) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new InquiryException(InquiryErrorCode.INQUIRY_NOT_FOUND));

        if(inquiry.getInquiredBy().getMemberId() != member.getMemberId()){
            throw new InquiryException(InquiryErrorCode.ACCESS_DENIED);
        }

        List<FileMeta> savedFileMetas = fileService.uploadFiles(files, SUB_DIRECTORY, member);

        // 조회된 inquiry 객체에게 직접 첨부파일을 추가
        savedFileMetas.forEach(inquiry::addAttachment);

        // @Transactional에 의해, 메서드가 끝나면 inquiry의 attachments 리스트에
        // 추가된 CsAttachment들이 자동으로 INSERT 된다. save()를 호출할 필요가 없다.
    }

    // 내 문의 목록 가져오기(유저 페이지)
    @Transactional(readOnly = true)
    public Page<InquirySummaryResponse> getMyInquiries(Member member, Pageable pageable) {
        return inquiryRepository.findByInquiredByOrderByInquiredAtDesc(member, pageable)
                .map(InquirySummaryResponse::from);
    }

    // 나의 문의 디테일 가져오기(유저 페이지)
    @Transactional(readOnly = true)
    public InquiryDetailUserResponse getMyInquiry(int inquiryId, Member member) {
        Inquiry inquiry = inquiryRepository.findWithAttachmentsById(inquiryId)
                .orElseThrow(() -> new InquiryException(InquiryErrorCode.INQUIRY_NOT_FOUND));


        validateOwnership(inquiry, member);

        List<String> urls = getAttachmentUrls(inquiry);

        return InquiryDetailUserResponse.from(inquiry, urls);

    }

    /**************************
     * 어드민 기능
     **************************/

    // 동적쿼리로 문의 목록 가져오기
    @Transactional(readOnly = true)
    public Page<InquirySummaryResponse> searchInquires(InquirySearchCondition condition, Pageable pageable){
        Specification<Inquiry> spec = inquirySpecification.toSpecification(condition);
        return inquiryRepository.findAll(spec, pageable)
                .map(InquirySummaryResponse::from);
    }

    // 문의 ID로 자세하게 문의 가져오기
    @Transactional(readOnly = true)
    public InquiryDetailAdminResponse getInquiryForAdmin(int id) {
        Inquiry inquiry = inquiryRepository.findWithAttachmentsById(id)
                .orElseThrow(() -> new InquiryException(InquiryErrorCode.INQUIRY_NOT_FOUND));

        List<String> urls = getAttachmentUrls(inquiry);

        return InquiryDetailAdminResponse.from(inquiry, urls);
    }

    /**
     * 관리자가 문의에 답변을 등록/수정합니다.
     * 외부로부터 받은 HTML 콘텐츠는 XSS 공격 방지를 위해 소독 처리합니다.
     */
    public InquiryDetailAdminResponse answerInquiry(int inquiryId, InquiryAnswerRequest answerRequest, Member admin) {
        Inquiry inquiry = inquiryRepository.findWithAttachmentsById(inquiryId)
                .orElseThrow(() -> new InquiryException(InquiryErrorCode.INQUIRY_NOT_FOUND));

        // 소독 처리
        String sanitizedAnswer = htmlSanitizer.sanitize(answerRequest.getAnswerContent());
        // String sanitizedComment = htmlSanitizer.sanitize(answerRequest.getComment()); // 코멘트는 스마트에디터 확장시를 대비해 주석처리

        switch (answerRequest.getReqStatus()) {
            case ANSWERED -> inquiry.answer(sanitizedAnswer, answerRequest.getComment(), admin);
            case ON_HOLD -> inquiry.putOnHold(answerRequest.getComment(), admin);
            case IN_PROGRESS -> inquiry.startProgress(answerRequest.getComment(), admin);
            case REJECTED -> inquiry.reject(answerRequest.getComment(), admin);
            case PENDING -> throw new InquiryException(InquiryErrorCode.IMPOSSIBLE_STATUS_CHANGE);
        }

        List<String> urls = getAttachmentUrls(inquiry);

        return InquiryDetailAdminResponse.from(inquiry, urls);
    }

    // 사용자 검증 헬퍼 메서드
    private void validateOwnership(Inquiry inquiry, Member member) {
        if (inquiry.getInquiredBy().getMemberId() != member.getMemberId()) {
            throw new InquiryException(InquiryErrorCode.ACCESS_DENIED);
        }
    }

    // 파일Urls 조회 헬퍼 메서드
    private List<String> getAttachmentUrls(Inquiry inquiry) {
        // 첨부파일이 없을경우 FileService 호출하지 않음.
        if (inquiry.getAttachments() == null || inquiry.getAttachments().isEmpty()) {
            return new ArrayList<>();
        }

        // 1. 필요한 모든 fileMetaId를 하나의 리스트로 수집.
        List<Integer> fileMetaIds = inquiry.getAttachments().stream()
                .map(attachment -> attachment.getFilemeta().getFilemetaId())
                .toList();

        // 2. FileService에 ID 리스트를 '한 번만' 넘겨서, 모든 URL을 '한 번에' 가져온다.
        Map<Integer, String> urlMap = fileService.getFileAccessUrls(fileMetaIds);

        // 3. DTO가 원하는 List<String> 형태로 변환하여 반환한다.
        return new ArrayList<>(urlMap.values());
    }

}