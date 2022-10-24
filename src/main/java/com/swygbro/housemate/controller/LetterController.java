package com.swygbro.housemate.controller;

import com.swygbro.housemate.heart.messages.*;
import com.swygbro.housemate.heart.service.WireLetterProcess;
import com.swygbro.housemate.util.response.domain.ListResult;
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
@Api("Letter 관련 API 입니다.")
@RequestMapping("/letter")
@RequiredArgsConstructor
public class LetterController {

    private final ResponseService responseService;

    private final WireLetterProcess wireLetterProcess;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    @ApiOperation("A가 편지를 처음 생성합니다.")
    @PostMapping("/write-first")
    public SingleResult<CreateHeartLetter> create(@RequestBody InputFirstHeartLetter inputFirstHeartLetter) {
        return responseService.getSingleResult(wireLetterProcess.writeFirst(inputFirstHeartLetter));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    @ApiOperation("B가 상대방이 보낸 읽지 않는 편지를 조회합니다. <MemberId -> 그룹에 속해있는 MemberId 확인하기 API 하고 오기>")
    @GetMapping("/not-read")
    public ListResult<PrivateViewMessage> viewNotRead(@RequestParam String userId) {
        return responseService.getListResult(wireLetterProcess.notReadMessageView(userId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    @ApiOperation("B가 편지를 씁니다. -> 읽음 처리까지 동시에 가능")
    @PostMapping("/write-second")
    public SingleResult<CreateHeartLetter> writeSecond(@RequestParam String heartId, @RequestBody InputSecondHeartLetter inputSecondHeartLetter) {
        return responseService.getSingleResult(wireLetterProcess.writeSecond(heartId, inputSecondHeartLetter));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    @ApiOperation("서로 답장을 완료한 편지 전체 보기")
    @PostMapping("/view-all-letter")
    public ListResult<ViewMessage> viewMessage() {
        return responseService.getListResult(wireLetterProcess.viewMessage());
    }
}
