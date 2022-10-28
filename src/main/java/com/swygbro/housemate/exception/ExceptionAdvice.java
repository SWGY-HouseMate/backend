package com.swygbro.housemate.exception;

import com.swygbro.housemate.exception.badrequest.BadRequestException;
import com.swygbro.housemate.exception.datanotfound.DataNotFoundException;
import com.swygbro.housemate.exception.forbidden.CAuthenticationEntryPointException;
import com.swygbro.housemate.util.response.domain.CommonResult;
import com.swygbro.housemate.util.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    private final ResponseService responseService;

    // Default
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.ACCEPTED)
    protected CommonResult defaultException(Exception e) {
        return responseService.getFailResult(-1, e.getMessage());
    }

    // data not found
    @ExceptionHandler(DataNotFoundException.class)
    public CommonResult dataNotFoundException(DataNotFoundException e) {
        return responseService.getFailResult(-1, e.getMessage());
    }

    // bad request
    @ExceptionHandler(BadRequestException.class)
    public CommonResult badRequestException(BadRequestException e) {
        return responseService.getFailResult(-1, e.getMessage());
    }

    // 로그인 실패 상태
    @ExceptionHandler(CAuthenticationEntryPointException.class)
    public CommonResult authenticationEntryPointException(CAuthenticationEntryPointException e) {
        return responseService.getFailResult(-1, e.getMessage());
    }

    // 권한 거부 상태
    @ExceptionHandler(AccessDeniedException.class)
    public CommonResult AccessDeniedException(AccessDeniedException e) {
        return responseService.getFailResult(-1, e.getMessage());
    }
}