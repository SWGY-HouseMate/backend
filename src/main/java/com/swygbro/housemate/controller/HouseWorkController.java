package com.swygbro.housemate.controller;

import com.swygbro.housemate.housework.message.*;
import com.swygbro.housemate.housework.service.*;
import com.swygbro.housemate.util.response.domain.SingleResult;
import com.swygbro.housemate.util.response.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@Api("HouseWork 관련 API 입니다.")
@RequestMapping("/house-work")
@RequiredArgsConstructor
public class HouseWorkController {
    private final HouseWorkCreateProcess houseWorkCreateProcess;
    private final SearchDateProcess searchDateProcess;
    private final HouseWorkCountProcess houseWorkCountProcess;
    private final HouseWorkUtil houseWorkUtil;
    private final CycleUtil cycleUtil;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    @ApiOperation("집안일을 생성합니다.")
    @PostMapping("/create")
    public SingleResult<HoseWorkCreate> create(@RequestBody CreateHouseWork createHouseWork) throws ParseException {
        return responseService.getSingleResult(houseWorkCreateProcess.execute(createHouseWork));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    @ApiOperation("startAt, EndAt을 기준으로 집안일을 검색합니다. (그룹 멤버 별)")
    @PostMapping("/search-date")
    public SingleResult<HouseWorkByMember> searchDateByGroupORMy(@RequestBody SearchHouseWorkAtDate searchHouseWorkAtDate) {
        return responseService.getSingleResult(searchDateProcess.executeByGroup(searchHouseWorkAtDate));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    @ApiOperation("startAt, EndAt을 기준으로 집안일의 갯수를 검색[한일/안한일]합니다. (그룹 멤버 별)")
    @PostMapping("/count")
    public SingleResult<HouseWorkCountForGroup> houseWorkCountForGroup(@RequestBody SearchHouseWorkAtDate searchHouseWorkAtDate) {
        return responseService.getSingleResult(houseWorkCountProcess.executeByGroup(searchHouseWorkAtDate));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    @ApiOperation("집안일을 완료하거나, 롤백합니다.")
    @PostMapping("/complete")
    public SingleResult<String> updateCompleted(@RequestBody HouseWorkCompleted houseWorkCompleted) {
        return responseService.getSingleResult(houseWorkUtil.completion(houseWorkCompleted.getHouse_work_id(), houseWorkCompleted.getIsCompleted()));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    @ApiOperation("반복 주기를 삭제합니다. -> 삭제 할 경우 반복 주기에 관련된 집안일이 모두 삭제됩니다.")
    @DeleteMapping("cycle/{cycle_id}")
    public SingleResult<String> deleteCycle(@PathVariable String cycle_id) {
        return responseService.getSingleResult(cycleUtil.delete(cycle_id));
    }
}
