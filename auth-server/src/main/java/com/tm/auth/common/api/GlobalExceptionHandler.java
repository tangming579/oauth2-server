package com.tm.auth.common.api;

import lombok.extern.slf4j.Slf4j;
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
    public CommonResult handleBusinessException(OAuthExecption e) {
        log.error("error", e);
        return CommonResult.failed("error");
    }

    @ExceptionHandler(value = Exception.class)
    public CommonResult handleException(Exception e) {
        log.error("error", e);
        return CommonResult.failed("error");
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CommonResult handleValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField()+fieldError.getDefaultMessage();
            }
        }
        return CommonResult.failed(message);
    }
    @ExceptionHandler(value = BindException.class)
    public CommonResult handleValidException(BindException e) {
        log.error("error", e);
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        return CommonResult.failed("error");
    }
}
