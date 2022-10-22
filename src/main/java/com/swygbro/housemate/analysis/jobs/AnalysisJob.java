package com.swygbro.housemate.analysis.jobs;

import com.swygbro.housemate.analysis.steps.CalculateBestAndWorstHouseWork;
import com.swygbro.housemate.analysis.steps.CalculateMostHouseWork;
import com.swygbro.housemate.analysis.steps.CalculateShareRatio;
import com.swygbro.housemate.analysis.steps.LockTableProcesses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class AnalysisJob {
    private final JobBuilderFactory jobBuilderFactory;
    // steps
    private final LockTableProcesses lockTableProcesses;
    private final CalculateShareRatio calculateShareRatio;
    private final CalculateMostHouseWork calculateMostHouseWork;
    private final CalculateBestAndWorstHouseWork calculateBestAndWorstHouseWork;

    @Bean
    public Job analysis() {
        return jobBuilderFactory.get("analysis")
                .start(lockTableProcesses.start()) // lockTableProcesses Start 실행
                .on("FAILED") // 결과가 FAILED 일 경우
                .end() // 종료
                .from(lockTableProcesses.start()) // Start 의 결과로부터
                .on("*") // FAILED 를 제외한 모든 경우
                .to(calculateBestAndWorstHouseWork.executeByHouseWork()) // calculateShareRatioByGroup.execute() 실행
                .next(lockTableProcesses.end()) // execute() 의 결과에 상관없이 lockTableProcesses End 실행
                .on("*") // End 의 모든 결과에 상관없이
                .end() // FLOW 종료
                .end() // JOB 종료
                .build();
    }
}
