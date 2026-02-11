package com.marketplace.common.errors.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {

    LISTING_NOT_FOUND(404, "4000001", ErrorType.NOT_FOUND, "listing.not.found"),
    SYSTEM_ERROR(500, "500000", ErrorType.SYSTEM_ERROR, "system.error");

    private final int httpStatus;
    private final String code;
    private final ErrorType type;
    private final String messageKey;

    ErrorCode(int httpStatus, String code, ErrorType type, String messageKey) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.type = type;
        this.messageKey = messageKey;
    }
}
