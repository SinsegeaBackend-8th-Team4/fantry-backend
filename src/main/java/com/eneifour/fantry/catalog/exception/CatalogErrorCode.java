package com.eneifour.fantry.catalog.exception;

import com.eneifour.fantry.inspection.support.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CatalogErrorCode implements BaseErrorCode {
    ARTIST_NOT_FOUND(HttpStatus.NOT_FOUND, "CAT001", "존재하지 않는 아티스트입니다."),
    ALBUM_NOT_FOUND(HttpStatus.NOT_FOUND, "CAT002", "존재하지 않는 앨범입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CAT003", "존재하지 않는 굿즈 카테고리입니다."),
    INVALID_PARAM(HttpStatus.BAD_REQUEST, "CAT004", "요청 파라미터가 올바르지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}