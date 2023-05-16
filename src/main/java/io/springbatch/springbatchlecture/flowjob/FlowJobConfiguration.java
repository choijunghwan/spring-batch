package io.springbatch.springbatchlecture.flowjob;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FlowJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flowJob() {
        return jobBuilderFactory.get("flowJob")
                // Flow 시작하는 Step 설정
                .start(step1())

                // Step의 실행 결과로 돌려받는 종료상태(ExitStatus)를 캐치하여 매칭하는 패턴
                // step1의 종료상태가 COMPLETED이면 다음으로 step3를 실행
                .on("COMPLETED").to(step3())

                // from은 이전단계인 step1에 대한 조건을 추가적으로 정의할 수 있다.
                .from(step1())

                // step1의 종료상태가 FAILED이면 다음으로 step2를 실행
                .on("FAILED").to(step2())

                // flow를 종료
                .end()
                .build();
    }

    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step1 hash executed");
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
                        System.out.println("step2 hash executed");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step3 hash executed");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
}
