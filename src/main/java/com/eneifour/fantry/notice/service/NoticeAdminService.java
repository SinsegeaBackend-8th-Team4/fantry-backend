package com.eneifour.fantry.notice.service;

import com.eneifour.fantry.common.util.HtmlSanitizer;
import com.eneifour.fantry.common.util.file.FileMeta;
import com.eneifour.fantry.common.util.file.FileService;
import com.eneifour.fantry.notice.domain.Notice;
import com.eneifour.fantry.notice.domain.NoticeType;
import com.eneifour.fantry.notice.dto.NoticeCreateRequest;
import com.eneifour.fantry.notice.dto.NoticeDetailResponse;
import com.eneifour.fantry.notice.dto.NoticeSearchRequest;
import com.eneifour.fantry.notice.dto.NoticeSummaryResponse;
import com.eneifour.fantry.notice.dto.NoticeUpdateRequest;
import com.eneifour.fantry.notice.exception.NoticeErrorCode;
import com.eneifour.fantry.notice.exception.NoticeException;
import com.eneifour.fantry.notice.repository.NoticeRepository;
import com.eneifour.fantry.notice.repository.NoticeSpecification;
import com.eneifour.fantry.notice.repository.NoticeTypeRepository;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.domain.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.eneifour.fantry.inquiry.domain.CsStatus;
import com.eneifour.fantry.notice.dto.NoticeStatsAdminResponse;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 관리자용 공지사항 관리 서비스를 담당합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class NoticeAdminService {

    private final NoticeRepository noticeRepository;
    private final NoticeSpecification noticeSpecification;
    private final NoticeTypeRepository noticeTypeRepository;
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
        NoticeType noticeType = noticeTypeRepository.findById(request.noticeTypeId())
                .orElseThrow(() -> new NoticeException(NoticeErrorCode.CSTYPE_NOT_FOUND));

        String sanitizedContent = htmlSanitizer.sanitizeWithImages(request.content());

        Notice notice = request.toEntity(admin, noticeType, sanitizedContent);
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

        NoticeType noticeType = notice.getNoticeType();
        if (request.noticeTypeId() != noticeType.getNoticeTypeId()) {
            noticeType = noticeTypeRepository.findById(request.noticeTypeId())
                    .orElseThrow(() -> new NoticeException(NoticeErrorCode.CSTYPE_NOT_FOUND));
        }

        String sanitizedContent = htmlSanitizer.sanitizeWithImages(request.content());
        notice.update(request.title(), sanitizedContent, noticeType, admin);

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

    @Transactional(readOnly = true)
    public Page<NoticeSummaryResponse> searchNoticesForAdmin(NoticeSearchRequest request, Pageable pageable) {
        Specification<Notice> spec = noticeSpecification.toSpecification(request);
        return noticeRepository.findAll(spec, pageable).map(NoticeSummaryResponse::from);
    }

    @Transactional(readOnly = true)
    public NoticeStatsAdminResponse getNoticeStatsForAdmin() {
        Map<CsStatus, Long> statusCounts = Arrays.stream(CsStatus.values())
                .collect(Collectors.toMap(
                        status -> status,
                        noticeRepository::countByStatus
                ));
        return NoticeStatsAdminResponse.from(statusCounts);
    }
}
