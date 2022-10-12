package com.swygbro.housemate.analysis.jobs;

import com.swygbro.housemate.analysis.steps.LockTableProcesses;
import com.swygbro.housemate.analysis.steps.PerDayDataFatoryStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class AnalysisJob {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    // steps
    private final LockTableProcesses lockTableProcesses;
    private final PerDayDataFatoryStep perDayDataFatoryStep;

    @Bean
    public Job analysis() {
        return jobBuilderFactory.get("analysis")
                .start(lockTableProcesses.start()) // lockTableProcesses Start 실행
                .on("FAILED") // 결과가 FAILED 일 경우
                .end() // 종료
                .from(lockTableProcesses.start()) // Start 의 결과로부터
                .on("*") // FAILED 를 제외한 모든 경우
                .to(perDayDataFatoryStep.execute()) // perDayDataFatoryStep.execute() 실행
                .on("FAILED") // 결과가 FAILED 일 경우
                .end() // 종료
                .on("*") // FAILED 를 제외한 모든 경우
                .to(aaaa()) // aaaa 실행
                .next(lockTableProcesses.end()) // aaaa 의 결과에 상관없이 lockTableProcesses End 실행
                .on("*") // End 의 모든 결과에 상관없이
                .end() // FLOW 종료
                .end() // JOB 종료
                .build();
    }

    @Bean
    public Step aaaa() {
        return stepBuilderFactory.get("aaaa")
                .tasklet((contribution, chunkContext) -> {
                    log.info("======= 비즈니스 마지막 로직 시작 =======");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
