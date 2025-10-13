package com.eneifour.fantry.faq.service;

import com.eneifour.fantry.common.util.HtmlSanitizer;
import com.eneifour.fantry.common.util.file.FileMeta;
import com.eneifour.fantry.common.util.file.FileService;
import com.eneifour.fantry.inquiry.domain.CsType;
import com.eneifour.fantry.faq.domain.Faq;
import com.eneifour.fantry.faq.dto.FaqCreateRequest;
import com.eneifour.fantry.faq.dto.FaqResponse;
import com.eneifour.fantry.faq.dto.FaqUpdateRequest;
import com.eneifour.fantry.faq.exception.FaqErrorCode;
import com.eneifour.fantry.faq.exception.FaqException;
import com.eneifour.fantry.inquiry.repository.CsTypeRepository;
import com.eneifour.fantry.faq.repository.FaqRepository;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.domain.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 관리자용 FAQ 관리 서비스를 담당합니다.
 * FAQ의 생성(Create), 수정(Update), 삭제(Delete) 및 상세 조회 로직을 포함합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FaqAdminService {

    private final FaqRepository faqRepository;
    private final CsTypeRepository csTypeRepository;
    private final FileService fileService;
    private final HtmlSanitizer htmlSanitizer;
    private final String SUB_DIRECTORY = "cs/faq";

    /**
     * 새로운 FAQ를 생성합니다.
     * 스마트에디터로 작성된 HTML 콘텐츠는 XSS 방지를 위해 소독 처리합니다.
     */
    public FaqResponse createFaq(FaqCreateRequest request, Member admin) {
        CsType csType = csTypeRepository.findById(request.csTypeId())
                .orElseThrow(() -> new FaqException(FaqErrorCode.CSTYPE_NOT_FOUND));

        // [보안] 외부로부터 받은 HTML 콘텐츠를 저장하기 전에 반드시 소독합니다.
        String sanitizedContent = htmlSanitizer.sanitizeWithImages(request.content());

        Faq faq = request.toEntity(admin, csType, sanitizedContent);
        Faq savedFaq = faqRepository.save(faq);

        // 생성된 엔티티로 응답 DTO를 만들어 즉시 반환합니다.
        return FaqResponse.from(savedFaq);
    }

    /**
     * 기존 FAQ를 수정합니다.
     */
    public FaqResponse updateFaq(int faqId, FaqUpdateRequest request, Member admin) {
        Faq faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new FaqException(FaqErrorCode.FAQ_NOT_FOUND));

        CsType csType = faq.getCsType();
        // 요청에 담긴 csTypeId가 기존과 다를 경우에만 DB에서 새로 조회합니다.
        if (request.csTypeId() != csType.getCsTypeId()) {
            csType = csTypeRepository.findById(request.csTypeId())
                    .orElseThrow(() -> new FaqException(FaqErrorCode.CSTYPE_NOT_FOUND));
        }

        String sanitizedContent = htmlSanitizer.sanitizeWithImages(request.content());

        // 엔티티의 비즈니스 메서드를 호출하여 상태 변경을 위임합니다.
        faq.update(request.title(), sanitizedContent, csType, admin);

        if (request.status() != null) {
            faq.changeStatus(request.status());
        }

        return FaqResponse.from(faq);
    }

    // 첨부파일 삭제
    public void deleteFaq(int faqId, Member admin) { // 삭제를 실행하는 주체(admin)가 필요하다.

        // 파일 삭제전 권한검사를 한번 더 수행한다.
        if (!admin.getRole().getRoleType().equals(RoleType.ADMIN) && !admin.getRole().getRoleType().equals(RoleType.SADMIN)) {
            throw new FaqException(FaqErrorCode.ACCESS_DENIED);
        }

        Faq faq = faqRepository.findWithAttachmentsById(faqId)
                .orElseThrow(() -> new FaqException(FaqErrorCode.FAQ_NOT_FOUND));


        // FAQ에 속한 모든 첨부파일을 순회하며 '논리적 삭제'를 수행
        faq.getAttachments().forEach(attachment -> {
            fileService.deleteFile(attachment.getFilemeta().getFilemetaId(), admin);
        });

        // 첨부파일 처리가 끝난 후, FAQ를 삭제한다.
        faqRepository.delete(faq);
    }

    /**
     * 특정 FAQ에 파일을 첨부합니다.
     */
    public void addAttachments(int faqId, List<MultipartFile> files, Member admin) {
        Faq faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new FaqException(FaqErrorCode.FAQ_NOT_FOUND));

        // FileService 공통 모듈을 사용하여 파일을 업로드합니다.
        List<FileMeta> savedFileMetas = fileService.uploadFiles(files, SUB_DIRECTORY, admin);

        // 엔티티의 비즈니스 메서드를 통해 첨부파일을 추가합니다.
        savedFileMetas.forEach(faq::addAttachment);
    }

    /**
     * FAQ 상세 정보를 조회합니다. (관리자용)
     */
    @Transactional(readOnly = true)
    public FaqResponse getFaq(int faqId) {
        Faq faq = faqRepository.findWithAttachmentsById(faqId)
                .orElseThrow(() -> new FaqException(FaqErrorCode.FAQ_NOT_FOUND));

        List<String> urls = getAttachmentUrls(faq);
        return FaqResponse.from(faq, urls);
    }

    /**
     * [헬퍼 메서드] Faq 엔티티로부터 첨부파일 URL 리스트를 조회하는 공통 로직입니다.
     * N+1 문제를 방지하기 위해 fileMetaId를 한 번에 모아 FileService에 요청합니다.
     * @param faq 첨부파일을 포함한 Faq 엔티티
     * @return 파일 접근 URL 리스트
     */
    private List<String> getAttachmentUrls(Faq faq) {
        if (faq.getAttachments() == null || faq.getAttachments().isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> fileMetaIds = faq.getAttachments().stream()
                .map(attachment -> attachment.getFilemeta().getFilemetaId())
                .toList();

        Map<Integer, String> urlMap = fileService.getFileAccessUrls(fileMetaIds);
        return new ArrayList<>(urlMap.values());
    }
}