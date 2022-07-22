package com.example.batchprocessing.job;

import com.example.batchprocessing.model.Person;
import com.example.batchprocessing.processor.PersonItemProcessor;
import com.example.batchprocessing.processor.RedisItemProcessor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StepConfig {

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public PersonItemProcessor personProcessor() {
        return new PersonItemProcessor();
    }

    @Bean
    public RedisItemProcessor redisProcessor() {
        return new RedisItemProcessor();
    }

    @Bean
    public Step step1(ItemReader<Person> reader, ItemWriter<Person> writer) {
        return stepBuilderFactory.get("step1")
                .<Person, Person> chunk(10)
                .reader(reader)
                .processor(personProcessor())
                .processor(redisProcessor())
                .writer(writer)
                .build();
    }
}
