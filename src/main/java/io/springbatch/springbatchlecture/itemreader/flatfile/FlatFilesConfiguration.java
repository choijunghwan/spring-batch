package io.springbatch.springbatchlecture.itemreader.flatfile;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class FlatFilesConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flatFileJob() {
        return jobBuilderFactory.get("flatFileJob")
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(5)
                .reader(itemReader())
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> items) throws Exception {
                        System.out.println("items = " + items);
                    }
                })
                .build();
    }

//    @Bean
//    public ItemReader itemReader() {
//        FlatFileItemReader<CustomerFileField> itemReader = new FlatFileItemReader<>();
//        // ClassPathResource를 이용해 파일을 읽어옴
//        itemReader.setResource(new ClassPathResource("/customer.csv"));
//
//        DefaultLineMapper<CustomerFileField> lineMapper = new DefaultLineMapper<>();
//        // LineTokenizer 객체 설정
//        lineMapper.setLineTokenizer(new DelimitedLineTokenizer());
//        // FieldSetMapper 객체 설정
//        lineMapper.setFieldSetMapper(new CustomerFieldSetMapper());
//
//        itemReader.setLineMapper(lineMapper);
//        // 파일 상단에 있는 한줄 라인 무시
//        itemReader.setLinesToSkip(1);
//        return itemReader;
//    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2 has executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public ItemReader itemReader() {
        return new FlatFileItemReaderBuilder<CustomerFileField>()
                .name("flatFile")
                .resource(new ClassPathResource("/custoemr.csv"))
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>()) // 객체 field 맵핑이 가능
                .targetType(CustomerFileField.class)  // field 맵핑할 Class 지정
                .linesToSkip(1)
                .delimited().delimiter(",")
                .names("name", "age", "year") // index가 아닌 name을 기준으로 값을 가져옴
                .build();
    }

    public FlatFileItemReader fixedItemReader() {
        return new FlatFileItemReaderBuilder<CustomerFileField>()
                .name("fixedFile")
                .resource(new FileSystemResource("/Users/charlie/IdeaProjects/springbatchlecture/src/main/resources/fixedLength.txt"))
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>())
                .targetType(CustomerFileField.class)
                .linesToSkip(1)
                .fixedLength()
                .addColumns(new Range(1,5))
                .addColumns(new Range(6,9))
                .addColumns(new Range(10,11))
                .names("name", "year", "age")
                .build();
    }
}
