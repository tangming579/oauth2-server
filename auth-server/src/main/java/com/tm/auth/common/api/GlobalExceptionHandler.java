package com.tm.auth.common.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.jwt.crypto.sign.InvalidSignatureException;
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
    public ApiResult handleValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        return ApiResult.failed(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    @ExceptionHandler(value = BindException.class)
    public ApiResult handleValidException(BindException e) {
        log.error("error", e);
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        return ApiResult.failed(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    @ExceptionHandler({Exception.class})
    public ApiResult handleException(Exception e) {
        String msg = StringUtils.hasText(e.getMessage()) ? e.getMessage() : e.toString();
        return ApiResult.failed(msg);
    }

    @ExceptionHandler({InvalidSignatureException.class})
    public ApiResult handleClientRegistrationException(InvalidSignatureException e) {
        return ApiResult.failed(ResultCode.TOKEN_ILLEGAL.getCode(), e.getMessage());
    }

    @ExceptionHandler({OAuthExecption.class})
    public ApiResult handleClientRegistrationException(OAuthExecption e) {
        int status = e.getStatus() == 0 ? -1 : e.getStatus();
        return ApiResult.failed(status, e.getMessage());
    }

    @ExceptionHandler({HttpClientErrorException.class})
    public ApiResult handleClientRegistrationException(HttpClientErrorException e) {
        if (e.getStatusCode() == HttpStatus.UNAUTHORIZED)
            return ApiResult.failed(ResultCode.UNAUTHORIZED);
        return ApiResult.failed(e.getMessage());
    }
}
