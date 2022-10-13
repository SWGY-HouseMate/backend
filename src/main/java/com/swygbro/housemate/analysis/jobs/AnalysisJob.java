package com.swygbro.housemate.analysis.jobs;

import com.swygbro.housemate.analysis.steps.CalculateShareRatioByGroup;
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
    private final CalculateShareRatioByGroup calculateShareRatioByGroup;

    @Bean
    public Job calculateShareRatioByGroupJob() {
        return jobBuilderFactory.get("CalculateShareRatioByGroupJob")
                .start(lockTableProcesses.start()) // lockTableProcesses Start 실행
                .on("FAILED") // 결과가 FAILED 일 경우
                .end() // 종료
                .from(lockTableProcesses.start()) // Start 의 결과로부터
                .on("*") // FAILED 를 제외한 모든 경우
                .to(calculateShareRatioByGroup.execute()) // calculateShareRatioByGroup.execute() 실행
                .next(lockTableProcesses.end()) // execute() 의 결과에 상관없이 lockTableProcesses End 실행
                .on("*") // End 의 모든 결과에 상관없이
                .end() // FLOW 종료
                .end() // JOB 종료
                .build();
    }
}
