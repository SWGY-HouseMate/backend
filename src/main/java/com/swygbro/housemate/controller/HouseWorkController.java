package com.swygbro.housemate.controller;

import com.swygbro.housemate.housework.message.CreateHouseWork;
import com.swygbro.housemate.housework.message.HoseWorkRes;
import com.swygbro.housemate.housework.service.HouseWorkProcess;
import com.swygbro.housemate.util.response.domain.SingleResult;
import com.swygbro.housemate.util.response.service.ResponseService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/house-work")
@RequiredArgsConstructor
public class HouseWorkController {

    private final HouseWorkProcess houseWorkProcess;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/create")
    public SingleResult<HoseWorkRes> create(@RequestBody CreateHouseWork createHouseWork) throws ParseException {
        return responseService.getSingleResult(houseWorkProcess.execute(createHouseWork));
    }


    @PostMapping("/create/date")
    public Object createDate(@RequestBody CreateHouseWork createHouseWork) {
        LocalDate startAt = createHouseWork.getStartAt();
        LocalDate endAt = createHouseWork.getEndAt();

        Date startDate = getDate(startAt);
        Date endDate = getDate(endAt);

        long differenceInMillis = endDate.getTime() - startDate.getTime();
        long aa = (differenceInMillis / (24 * 60 * 60 * 1000));

        return startAt.plusDays(aa);
    }

    private Date getDate(LocalDate localDate) {
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        Date date = Date.from(instant);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(date);
        return date;
    }

}
