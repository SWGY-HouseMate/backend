package com.swygbro.housemate.controller;

import com.swygbro.housemate.util.member.CurrentMemberInfo;
import com.swygbro.housemate.util.member.CurrentMemberUtil;
import com.swygbro.housemate.util.member.GroupPersonInfo;
import com.swygbro.housemate.util.member.MemberDto;
import com.swygbro.housemate.util.response.domain.SingleResult;
import com.swygbro.housemate.util.response.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(originPatterns = "*")
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
    @ApiOperation("현재 로그인된 사용자의 정보를 가져옵니다.")
    @GetMapping("/login-member")
    public SingleResult<MemberDto> currentMember() {
        return responseService.getSingleResult(currentMemberUtil.getCurrentMemberDtoObject());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    @ApiOperation("현재 로그인된 사용자와 그룹에 대한 정보를 가져옵니다.")
    @GetMapping("/login-member-group")
    public SingleResult<CurrentMemberInfo> currentMemberAndGroup() {
        return responseService.getSingleResult(currentMemberUtil.getCurrentMemberInfoAndGroupInfoObject());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    @ApiOperation("그룹에 속해 있는 MemberId 구하는 API 추가")
    @GetMapping("/group-member")
    public SingleResult<GroupPersonInfo> currentGroupMemberInfo() {
        return responseService.getSingleResult(currentMemberUtil.getMembersOfTheGroup());
    }
}
