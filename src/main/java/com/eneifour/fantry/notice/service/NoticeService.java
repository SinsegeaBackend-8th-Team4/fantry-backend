package com.eneifour.fantry.notice.service;

import com.eneifour.fantry.notice.domain.Notice;
import com.eneifour.fantry.notice.dto.NoticeDetailResponse;
import com.eneifour.fantry.notice.dto.NoticeSearchRequest;
import com.eneifour.fantry.notice.dto.NoticeSummaryResponse;
import com.eneifour.fantry.notice.repository.NoticeRepository;
import com.eneifour.fantry.notice.repository.NoticeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeSpecification noticeSpecification;

    public NoticeDetailResponse getNotice(int noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다.")); // 예외 처리 고도화 필요
        return NoticeDetailResponse.from(notice);
    }

    public Page<NoticeSummaryResponse> searchNoticesForUser(NoticeSearchRequest request, Pageable pageable) {
        Specification<Notice> spec = noticeSpecification.toUserSpecification(request);
        return noticeRepository.findAll(spec, pageable).map(NoticeSummaryResponse::from);
    }
}
