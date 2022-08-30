package com.tm.auth.common.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author tangming
 * @date 2022/8/26
 */
@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = OAuthExecption.class)
    public AipResult handleBusinessException(OAuthExecption e) {
        log.error("error", e);
        return AipResult.failed(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public AipResult handleException(Exception e) {
        log.error("error", e);
        String msg = StringUtils.hasText(e.getMessage()) ? e.getMessage() : e.toString();
        return AipResult.failed(e.getMessage());
    }

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
        return AipResult.failed(message);
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
        return AipResult.failed(message);
    }
}
