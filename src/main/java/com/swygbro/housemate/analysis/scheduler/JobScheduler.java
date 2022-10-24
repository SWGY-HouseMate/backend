package com.swygbro.housemate.analysis.scheduler;

import com.swygbro.housemate.analysis.jobs.AnalysisJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobScheduler {

    private final JobLauncher jobLauncher;
    private final AnalysisJob analysisJob;

//    @Scheduled(cron = "0 * * * * *")
//    public void analysisScheduler() {
//
//        Map<String, JobParameter> confMap = new HashMap<>();
//        confMap.put("time", new JobParameter(System.currentTimeMillis()));
//        JobParameters jobParameters = new JobParameters(confMap);
//
//        try {
//            jobLauncher.run(analysisJob.analysis(), jobParameters);
//        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
//                 | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {
//
//            log.error(e.getMessage());
//        }
//    }
}