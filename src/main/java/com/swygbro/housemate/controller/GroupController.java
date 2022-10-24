package com.swygbro.housemate.controller;

import com.swygbro.housemate.group.message.GroupCreator;
import com.swygbro.housemate.group.message.GroupInfo;
import com.swygbro.housemate.group.message.GroupResponse;
import com.swygbro.housemate.group.service.GroupFactory;
import com.swygbro.housemate.util.response.domain.SingleResult;
import com.swygbro.housemate.util.response.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(originPatterns = "http://222.112.83.120:3000")
@Api("Group 관련 API 입니다.")
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupFactory groupFactory;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    @ApiOperation("그룹을 생성합니다")
    @PostMapping("/create")
    public SingleResult<GroupResponse> createGroup(@RequestBody GroupCreator groupCreator) {
        return responseService.getSingleResult(groupFactory.create(groupCreator));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    @ApiOperation("그룹에 참가합니다.")
    @PostMapping("/join/{linkId}")
    public SingleResult<GroupResponse> joinGroup(@PathVariable String linkId, @RequestParam String memberName) {
        return responseService.getSingleResult(groupFactory.join(linkId, memberName));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    @ApiOperation("그룹의 정보를 가져옵니다.")
    @GetMapping("/info/{linkId}")
    public SingleResult<GroupInfo> groupInfo(@PathVariable String linkId) {
        return responseService.getSingleResult(groupFactory.info(linkId));
    }
}
