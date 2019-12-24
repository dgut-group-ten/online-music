package com.groupten.online_music.common.utils.exception;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler({ApplicationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfo handleApplicationException(ApplicationException e) {
        this.logger.info("------------全局n/捕获ApplicationException异常开始------------");
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setMsg(e.getMessage());
        errorInfo.setErrors(e.getCause());
        this.logger.info("ErrorInfo==" + ToStringBuilder.reflectionToString(errorInfo));
        this.logger.info("------------全局捕获ApplicationException异常结束------------");
        return errorInfo;
    }

    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorInfo handleAuthenticationException(AuthenticationException e) {
        this.logger.info("------------AuthenticationException------------");
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setMsg(e.getMessage());
        errorInfo.setErrors(e.getCause());
        this.logger.info("ErrorInfo==" + ToStringBuilder.reflectionToString(errorInfo));
        this.logger.info("------------AuthenticationException------------");
        return errorInfo;
    }
}


















