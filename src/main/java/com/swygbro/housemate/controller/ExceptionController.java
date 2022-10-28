package com.swygbro.housemate.controller;

import com.swygbro.housemate.exception.forbidden.CAuthenticationEntryPointException;
import com.swygbro.housemate.exception.forbidden.ForbiddenType;
import com.swygbro.housemate.util.response.domain.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exception")
public class ExceptionController {

    @GetMapping(value = "/entrypoint")
    public CommonResult entrypointException() {
        throw new CAuthenticationEntryPointException(ForbiddenType.DEFAULT);
    }

    @GetMapping(value = "/accessdenied")
    public CommonResult accessdeniedException() {
        throw new AccessDeniedException("권한 오류 입니다");
    }
}