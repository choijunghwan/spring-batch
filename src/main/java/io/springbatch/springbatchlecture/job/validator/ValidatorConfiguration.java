package io.springbatch.springbatchlecture.job.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ValidatorConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job validatorJob() {
        return jobBuilderFactory.get("jobBuilderFactory")
                .start(step1())
                .next(step2())
                .next(step3())
                .incrementer(new RunIdIncrementer())
                .validator(new CustomJobParametersValidator())
                .validator(new DefaultJobParametersValidator(new String[]{"name", "date"}, new String[]{"count"}))
                .preventRestart()
                .build();
    }

    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step1 was executed");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step2 was executed");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet((contribution, chunkContext) -> {
                    chunkContext.getStepContext().getStepExecution().setStatus(BatchStatus.FAILED);
                    contribution.setExitStatus(ExitStatus.STOPPED);
                    System.out.println("step3 has executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
