package com.marketplace.common.errors;

import com.marketplace.common.errors.enums.ErrorCode;
import com.marketplace.common.errors.enums.ErrorType;

public record ErrorResponse(
        String code,
        ErrorType type,
        String message,
        String details
) {

    public static ErrorResponse of(ErrorCode error) {
        return new ErrorResponse(
                error.getCode(),
                error.getType(),
                error.getMessageKey(),
                null
        );
    }

    public static ErrorResponse of(ErrorCode error, String customMessage) {
        return new ErrorResponse(
                error.getCode(),
                error.getType(),
                customMessage != null ? customMessage : error.getMessageKey(),
                null
        );
    }

    public static ErrorResponse of(ErrorCode error, String message, String details) {
        return new ErrorResponse(
                error.getCode(),
                error.getType(),
                message,
                details
        );
    }
}
