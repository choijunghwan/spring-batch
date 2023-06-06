package io.springbatch.springbatchlecture.exception.retry;

import io.springbatch.springbatchlecture.exception.skip.SkipItemProcessor;
import io.springbatch.springbatchlecture.exception.skip.SkipItemWriter;
import io.springbatch.springbatchlecture.exception.skip.SkippableException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RetryConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job retryJob() {
        return jobBuilderFactory.get("retryJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(5)
                .reader(reader())
                .processor(processor())
                .writer(items -> items.forEach(item -> System.out.println(item)))
                .faultTolerant()
                .skip(RetryableException.class)
                .skipLimit(2)
//                .retry(RetryableException.class)
//                .retryLimit(2) // retry만 설정하면 다시 reader 부터 반복될때 동일한 에러가 발생하게 되어서 결국 프로그램이 실패하게 된다.
                .retryPolicy(retryPolicy())
                .build();
    }

    private ItemProcessor<? super String, String> processor() {
        return new RetryItemProcessor();
    }

    @Bean
    public RetryPolicy retryPolicy() {
        Map<Class<? extends Throwable>, Boolean> exceptionClass = new HashMap<>();
        exceptionClass.put(RetryableException.class, true);

        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(2, exceptionClass);

        return simpleRetryPolicy;
    }

    private ListItemReader<String> reader() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            items.add(String.valueOf(i));
        }
        return new ListItemReader<>(items);
    }
}
