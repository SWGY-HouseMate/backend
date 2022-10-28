package com.swygbro.housemate.analysis.steps;

import com.swygbro.housemate.analysis.domain.TableLock;
import com.swygbro.housemate.analysis.repository.TableLockRepository;
import com.swygbro.housemate.exception.datanotfound.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static com.swygbro.housemate.exception.datanotfound.DataNotFoundType.TableLock_을_찾을_수_없습니다;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LockTableProcesses {
    private final StepBuilderFactory stepBuilderFactory;

    private final TableLockRepository tableLockRepository;

    @Value("${spring.batch.application.name}")
    private String instanceId;

    @Bean
    public Step start() {
        return stepBuilderFactory.get("TableLockStartStep")
                .tasklet((contribution, chunkContext) -> {
                    TableLock tableLock = tableLockRepository.findByInstanceId(instanceId)
                            .orElse(TableLock.builder()
                                    .instanceId(instanceId)
                                    .useYn(false)
                                    .checkDataTime(LocalDateTime.now())
                                    .build());
                    if (tableLock.isUseYn()){
                        log.info("======= Table 사용 중으로 종료 =======");
                        contribution.setExitStatus(ExitStatus.FAILED);
                    } else {
                        tableLock.setUseYn(true);
                        tableLockRepository.save(tableLock);
                    }
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    @Transactional
    public Step end() {
        return stepBuilderFactory.get("TableLockEndStep")
                .tasklet((contribution, chunkContext) -> {
                    TableLock tableLock = tableLockRepository.findByInstanceId(instanceId)
                            .orElseThrow(() -> new DataNotFoundException(TableLock_을_찾을_수_없습니다));
                    tableLock.setUseYn(false);
                    tableLock.setCheckDataTime(LocalDateTime.now());
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
