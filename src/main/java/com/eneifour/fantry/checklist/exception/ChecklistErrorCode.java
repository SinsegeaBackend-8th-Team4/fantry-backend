package com.eneifour.fantry.checklist.exception;

import com.eneifour.fantry.inspection.support.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChecklistErrorCode implements BaseErrorCode {
    BASELINE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHK001", "최신 가격 기준을 찾을 수 없습니다."),
    CHECKLIST_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHK002", "존재하지 않는 체크리스트 항목입니다."),
    PRICING_RULE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHK003", "적용할 수 있는 가격 정책이 없습니다."),
    INVALID_SELECTION(HttpStatus.BAD_REQUEST, "CHK004", "체크리스트 선택 항목이 올바르지 않습니다."),
    TEMPLATE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHK005", "존재하지 않는 체크리스트 템플릿이 입니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}