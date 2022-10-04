package com.swygbro.housemate.controller;

import com.swygbro.housemate.login.message.MemberInfo;
import com.swygbro.housemate.util.member.CurrentMemberInfo;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import com.swygbro.housemate.util.response.domain.SingleResult;
import com.swygbro.housemate.util.response.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("Util 관련 API 입니다.")
@RequestMapping("/util")
@RequiredArgsConstructor
public class UtilController {

    private final ResponseService responseService;
    private final CurrentMemberUtil currentMemberUtil;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    @ApiOperation("현재 로그인된 사용자와 그룹에 대한 정보를 가져옵니다.")
    @GetMapping("/login-member")
    public SingleResult<CurrentMemberInfo> currentMember() {
        return responseService.getSingleResult(currentMemberUtil.getCurrentMemberInfoAndGroupInfoObject());
    }

}
