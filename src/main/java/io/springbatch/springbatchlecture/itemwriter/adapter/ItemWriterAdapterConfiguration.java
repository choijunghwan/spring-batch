package io.springbatch.springbatchlecture.itemwriter.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class ItemWriterAdapterConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job itemWriterAdapterJob() throws Exception {
        return jobBuilderFactory.get("itemWriterAdapterJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(10)
                .reader(new ItemReader<String>() {
                    int i = 0;

                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        i++;
                        return i > 10 ? null : "item" + i;
                    }
                })
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public ItemWriter<? super String> customItemWriter() {
        ItemWriterAdapter<String> writer = new ItemWriterAdapter<>();
        writer.setTargetObject(customeService());
        writer.setTargetMethod("customWrite");

        return writer;
    }

    private CustomService customeService() {
        return new CustomService();
    }
}
