package com.tm.auth.common.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author tangming
 * @date 2022/8/26
 */
@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public AipResult handleValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        return AipResult.failed(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    @ExceptionHandler(value = BindException.class)
    public AipResult handleValidException(BindException e) {
        log.error("error", e);
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        return AipResult.failed(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    @ExceptionHandler({Exception.class})
    public AipResult handleException(Exception e) {
        String msg = StringUtils.hasText(e.getMessage()) ? e.getMessage() : e.toString();
        return AipResult.failed(msg);
    }

    @ExceptionHandler({HttpClientErrorException.class})
    public AipResult handleClientRegistrationException(HttpClientErrorException e) {
        if (e.getStatusCode() == HttpStatus.UNAUTHORIZED)
            return AipResult.failed(ResultCode.UNAUTHORIZED);
        return AipResult.failed(e.getMessage());
    }
}
