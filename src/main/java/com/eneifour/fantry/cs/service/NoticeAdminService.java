package com.eneifour.fantry.cs.service;

import com.eneifour.fantry.common.util.HtmlSanitizer;
import com.eneifour.fantry.common.util.file.FileMeta;
import com.eneifour.fantry.common.util.file.FileService;
import com.eneifour.fantry.cs.domain.CsType;
import com.eneifour.fantry.cs.domain.Notice;
import com.eneifour.fantry.cs.dto.NoticeCreateRequest;
import com.eneifour.fantry.cs.dto.NoticeDetailResponse;
import com.eneifour.fantry.cs.dto.NoticeUpdateRequest;
import com.eneifour.fantry.cs.exception.InquiryException;
import com.eneifour.fantry.cs.exception.NoticeErrorCode;
import com.eneifour.fantry.cs.exception.NoticeException;
import com.eneifour.fantry.cs.repository.CsTypeRepository;
import com.eneifour.fantry.cs.repository.NoticeRepository;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.domain.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 관리자용 공지사항 관리 서비스를 담당합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class NoticeAdminService {

    private final NoticeRepository noticeRepository;
    private final CsTypeRepository csTypeRepository;
    private final FileService fileService;
    private final HtmlSanitizer htmlSanitizer;
    private final String SUB_DIRECTORY = "cs/notice";

    /**
     * 새로운 공지사항을 생성합니다.
     * @param request 생성 요청 DTO
     * @param admin   생성자(관리자)
     * @return 생성된 공지사항의 상세 정보
     */
    public NoticeDetailResponse createNotice(NoticeCreateRequest request, Member admin) {
        CsType csType = csTypeRepository.findById(request.csTypeId())
                .orElseThrow(() -> new NoticeException(NoticeErrorCode.CSTYPE_NOT_FOUND));

        String sanitizedContent = htmlSanitizer.sanitizeWithImages(request.content());

        Notice notice = request.toEntity(admin, csType, sanitizedContent);
        Notice savedNotice = noticeRepository.save(notice);

        return NoticeDetailResponse.from(savedNotice);
    }

    /**
     * 기존 공지사항을 수정합니다.
     * @param noticeId  수정할 공지사항 ID
     * @param request   수정 요청 DTO
     * @param admin     수정자(관리자)
     * @return 수정된 공지사항의 상세 정보
     */
    public NoticeDetailResponse updateNotice(int noticeId, NoticeUpdateRequest request, Member admin) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));

        CsType csType = notice.getCsType();
        if (request.csTypeId() != csType.getCsTypeId()) {
            csType = csTypeRepository.findById(request.csTypeId())
                    .orElseThrow(() -> new NoticeException(NoticeErrorCode.CSTYPE_NOT_FOUND));
        }

        String sanitizedContent = htmlSanitizer.sanitizeWithImages(request.content());
        notice.update(request.title(), sanitizedContent, csType, admin);

        if (request.status() != null) {
            notice.changeStatus(request.status());
        }

        return NoticeDetailResponse.from(notice);
    }

    /**
     * 공지사항을 삭제합니다. 연관된 첨부파일도 함께 논리적으로 삭제합니다.
     * @param noticeId 삭제할 공지사항 ID
     * @param admin    삭제를 실행하는 관리자
     */
    public void deleteNotice(int noticeId, Member admin) {
        if (!admin.getRole().getRoleType().equals(RoleType.ADMIN) && !admin.getRole().getRoleType().equals(RoleType.SADMIN)) {
            throw new NoticeException(NoticeErrorCode.ACCESS_DENIED);
        }

        Notice notice = noticeRepository.findWithAttachmentsById(noticeId)
                .orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));

        notice.getAttachments().forEach(attachment ->
                fileService.deleteFile(attachment.getFilemeta().getFilemetaId(), admin));

        noticeRepository.delete(notice);
    }

    /**
     * 특정 공지사항에 파일을 첨부합니다.
     * @param noticeId  파일을 첨부할 공지사항 ID
     * @param files     업로드할 파일 목록
     * @param admin     업로드하는 관리자
     */
    public void addAttachments(int noticeId, List<MultipartFile> files, Member admin) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));

        List<FileMeta> savedFileMetas = fileService.uploadFiles(files, SUB_DIRECTORY, admin);
        savedFileMetas.forEach(notice::addAttachment);
    }
}
