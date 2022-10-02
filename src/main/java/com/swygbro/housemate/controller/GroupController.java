package com.swygbro.housemate.controller;

import com.swygbro.housemate.group.message.GroupCreator;
import com.swygbro.housemate.group.message.GroupInfo;
import com.swygbro.housemate.group.message.GroupResponse;
import com.swygbro.housemate.group.service.GroupFactory;
import com.swygbro.housemate.util.response.domain.SingleResult;
import com.swygbro.housemate.util.response.service.ResponseService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupFactory groupFactory;
    private final ResponseService responseService;

    @PostMapping("/create")
    public SingleResult<GroupResponse> createGroup(@RequestBody GroupCreator groupCreator) {
        return responseService.getSingleResult(groupFactory.create(groupCreator));
    }

    @ApiOperation("그룹에 참가한다.")
    @PostMapping("/join/{linkId}")
    public SingleResult<GroupResponse> joinGroup(@PathVariable String linkId,
                            @RequestParam String addMemberId) {
        return responseService.getSingleResult(groupFactory.join(linkId, addMemberId));
    }

    @ApiOperation("그룹의 정보를 가져온다.")
    @GetMapping("/info/{linkId}")
    public SingleResult<GroupInfo> groupInfo(@PathVariable String linkId) {
        return responseService.getSingleResult(groupFactory.info(linkId));
    }
}
