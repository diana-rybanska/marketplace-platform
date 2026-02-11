package com.marketplace.common.errors.exceptions;

import com.marketplace.common.errors.enums.ErrorCode;
import lombok.Getter;
import org.springframework.boot.logging.LogLevel;

@Getter
public class MpException extends RuntimeException {

    private final ErrorCode errorCode;
    private final LogLevel logLevel;

    public MpException(ErrorCode errorCode) {
        this(errorCode, errorCode.getMessageKey(), null, LogLevel.ERROR);
    }

    public MpException(ErrorCode errorCode, String message) {
        this(errorCode, message, null, LogLevel.ERROR);
    }

    public MpException(ErrorCode errorCode, Throwable cause) {
        this(errorCode, errorCode.getMessageKey(), cause, LogLevel.ERROR);
    }

    public MpException(ErrorCode errorCode, String message, Throwable cause, LogLevel logLevel) {
        super(message, cause);
        this.errorCode = errorCode;
        this.logLevel = logLevel;
    }
}
