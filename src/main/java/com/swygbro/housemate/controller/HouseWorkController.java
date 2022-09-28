package com.swygbro.housemate.controller;

import com.swygbro.housemate.housework.message.CreateHouseWork;
import com.swygbro.housemate.housework.message.HoseWorkRes;
import com.swygbro.housemate.housework.message.SearchHouseWorkAtDate;
import com.swygbro.housemate.housework.service.HouseWorkCreateProcess;
import com.swygbro.housemate.housework.service.SearchDateProcess;
import com.swygbro.housemate.util.response.domain.SingleResult;
import com.swygbro.housemate.util.response.service.ResponseService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/house-work")
@RequiredArgsConstructor
public class HouseWorkController {

    private final HouseWorkCreateProcess houseWorkCreateProcess;
    private final SearchDateProcess searchDateProcess;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/create")
    public SingleResult<HoseWorkRes> create(@RequestBody CreateHouseWork createHouseWork) throws ParseException {
        return responseService.getSingleResult(houseWorkCreateProcess.execute(createHouseWork));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/search-date")
    public SingleResult<HoseWorkRes> searchDateByGroupORMy(@RequestBody SearchHouseWorkAtDate searchHouseWorkAtDate) {
        return responseService.getSingleResult(searchDateProcess.executeByGroup(searchHouseWorkAtDate));
    }

}
