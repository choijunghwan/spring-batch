package io.springbatch.springbatchlecture.batchtest;

import io.springbatch.springbatchlecture.TestBatchConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest(classes = {SimpleJobConfiguration.class, TestBatchConfig.class})
class SimpleJobConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void simpleJob_test() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name", "user1")
                .addLong("date", new Date().getTime())
                .toJobParameters();

        // when
//        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        
        // step을 실행
        JobExecution jobExecution1 = jobLauncherTestUtils.launchStep("step1");

        // then
//        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
//        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        StepExecution stepExecution = (StepExecution) ((List) jobExecution1.getStepExecutions()).get(0);
        assertThat(stepExecution.getCommitCount()).isEqualTo(11);
        assertThat(stepExecution.getReadCount()).isEqualTo(1000);
        assertThat(stepExecution.getWriteCount()).isEqualTo(1000);
    }

    @AfterAll
    public void clear() {
        jdbcTemplate.execute("delete from customer2");
    }
}