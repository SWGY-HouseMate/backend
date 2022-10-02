package com.swygbro.housemate.controller;

import com.swygbro.housemate.login.message.MemberInfo;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import com.swygbro.housemate.util.response.domain.SingleResult;
import com.swygbro.housemate.util.response.service.ResponseService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/util")
@RequiredArgsConstructor
public class EtcController {

    private final ResponseService responseService;
    private final CurrentMemberUtil currentMemberUtil;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/login-member")
    public SingleResult<MemberInfo> currentMember() {
        return responseService.getSingleResult(currentMemberUtil.getCurrentMemberInfoObject());
    }

}
