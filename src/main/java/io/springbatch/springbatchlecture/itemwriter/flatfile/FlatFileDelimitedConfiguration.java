package io.springbatch.springbatchlecture.itemwriter.flatfile;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class FlatFileDelimitedConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flatFileJob() {
        return jobBuilderFactory.get("flatFileJob")
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Customer, Customer>chunk(10)
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }

    private ItemWriter<? super Customer> customItemWriter() {
        return new FlatFileItemWriterBuilder<>()
                .name("flatFileWriter")
                .resource(new FileSystemResource("/Users/charlie/IdeaProjects/springbatchlecture/src/main/resources/fileflat.txt"))
                .append(true) // 데이터 이어쓰기
                .shouldDeleteIfEmpty(true) // 기록할 데이터가 없다면 파일 삭제
                .delimited().delimiter("|")
                .names(new String[]{"id", "name", "age"})
                .build();
    }

    private ItemWriter<? super Customer> customFormatItemWriter() {
        return new FlatFileItemWriterBuilder<>()
                .name("flatFileFormatWriter")
                .resource(new FileSystemResource("/Users/charlie/IdeaProjects/springbatchlecture/src/main/resources/fileflat.txt"))
                .formatted()
                .format("%-2d%-15s%-2d")
                .names(new String[]{"id", "name", "age"})
                .build();
    }

    private ItemReader<Customer> customItemReader() {
        List<Customer> customers = Arrays.asList(new Customer(1L, "hong gil dong1", 41),
                new Customer(2L, "hong gil dong2", 42),
                new Customer(3L, "hong gil dong3", 43)
                );

        ListItemReader<Customer> reader = new ListItemReader<>(customers);
        return reader;
    }

}
