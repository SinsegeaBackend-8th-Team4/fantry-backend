package com.eneifour.fantry.notice.dto;

import com.eneifour.fantry.inquiry.domain.CsStatus;

import java.util.List;

public record NoticeSearchRequest(
        Integer noticeTypeId,
        String keyword,
        List<CsStatus> status
) {
}
