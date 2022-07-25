package com.example.batchprocessing.job;

import com.example.batchprocessing.model.Person;
import com.example.batchprocessing.processor.PersonItemProcessor;
import com.example.batchprocessing.processor.RedisItemProcessor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class StepConfig {

    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    public StepConfig(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Step step1(ItemReader<Person> reader, ItemWriter<Person> writer) {
        return stepBuilderFactory.get("step1")
                .<Person, Person> chunk(10)
                .reader(reader)
                .processor(compositeProcessor())
                .writer(writer)
                .build();
    }

    @Bean
    public CompositeItemProcessor<Person, Person> compositeProcessor() {
        List<ItemProcessor> delegates = new ArrayList<>(2);
        delegates.add(personProcessor());
        delegates.add(redisProcessor());

        CompositeItemProcessor processor = new CompositeItemProcessor();

        processor.setDelegates(delegates);

        return processor;
    }

    @Bean
    public RedisItemProcessor redisProcessor() {
        return new RedisItemProcessor();
    }

    @Bean
    public PersonItemProcessor personProcessor() {
        return new PersonItemProcessor();
    }

}
