package com.marketplace.common.errors;

import com.marketplace.common.errors.enums.ErrorCode;
import com.marketplace.common.errors.exceptions.MpException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

/**
 * Central exception handler for all REST controllers.
 * Provides unified JSON structure for validation, system and domain-level errors.
 */
@Slf4j(topic = "GlobalExceptionHandler")
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    MessageSource messageSource;

    public GlobalExceptionHandler(@Nullable MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MpException.class)
    public ResponseEntity<ErrorResponse> handleMarketplaceException(
            MpException ex,
            ServerWebExchange exchange) {

        ErrorCode code = ex.getErrorCode();

        String message = ex.getMessage() != null
                ? ex.getMessage()
                : resolveMessage(code);

        logMpException(ex);

        return ResponseEntity.status(code.getHttpStatus())
                .body(ErrorResponse.of(
                        code,
                        message,
                        exchange.getRequest().getURI().getPath()
                ));
    }


    private String resolveMessage(ErrorCode code) {
        try {
            return messageSource.getMessage(code.getMessageKey(), null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            log.debug("Message for key '{}' not found, falling back to code name", code.getMessageKey());
            return code.name();
        }
    }

    private void logMpException(MpException ex) {
        switch (ex.getLogLevel()) {
            case FATAL, ERROR -> log.error(ex.getMessage(), ex);
            case WARN -> log.warn(ex.getMessage(), ex);
            case INFO -> log.info(ex.getMessage(), ex);
            case DEBUG -> log.debug(ex.getMessage(), ex);
            case TRACE -> log.trace(ex.getMessage(), ex);
            case OFF -> { /* no logging */ }
        }
    }
}
