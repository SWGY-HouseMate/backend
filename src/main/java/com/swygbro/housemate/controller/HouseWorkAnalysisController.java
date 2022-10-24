package com.swygbro.housemate.controller;

import com.swygbro.housemate.analysis.message.result.AnalysisComplete;
import com.swygbro.housemate.analysis.service.AnalysisService;
import com.swygbro.housemate.util.response.domain.ListResult;
import com.swygbro.housemate.util.response.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(originPatterns = "*")
@Api("Analysis 관련 API 입니다.")
@RequestMapping("/analysis")
@RequiredArgsConstructor
public class HouseWorkAnalysisController {
    private final ResponseService responseService;
    private final AnalysisService analysisService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ResponseBody
    @ApiOperation("그룹 분석 결과를 가져옵니다.")
    @PostMapping("/create")
    public ListResult<AnalysisComplete> createGroup() {
        return responseService.getListResult(analysisService.execute());
    }
}
