package com.swygbro.housemate.controller;

import com.swygbro.housemate.group.message.GroupResponse;
import com.swygbro.housemate.group.service.GroupFactory;
import com.swygbro.housemate.util.response.domain.SingleResult;
import com.swygbro.housemate.util.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupFactory groupFactory;
    private final ResponseService responseService;

    @PostMapping("/create")
    public SingleResult<GroupResponse> createGroup(@RequestParam String currentMemberId,
                                                  @RequestParam String groupName) {
        return responseService.getSingleResult(groupFactory.create(currentMemberId, groupName));
    }

    @PostMapping("/join/{linkId}")
    public SingleResult<GroupResponse> joinGroup(@PathVariable String linkId,
                            @RequestParam String addMemberId) {
        return responseService.getSingleResult(groupFactory.join(linkId, addMemberId));
    }
}
