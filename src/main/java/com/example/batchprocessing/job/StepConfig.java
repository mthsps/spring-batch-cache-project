package com.example.batchprocessing.job;

import com.example.batchprocessing.model.Person;
import com.example.batchprocessing.processor.PersonItemProcessor;
import com.example.batchprocessing.tasklet.ClearCacheTasklet;
import com.example.batchprocessing.tasklet.ClearEvenIdTasklet;
import com.example.batchprocessing.tasklet.CountRowsTasklet;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class StepConfig {

    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    public StepConfig(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Step fromOriginToCache(
            @Qualifier("origin-reader") ItemReader<Person> readerOrigin,
            @Qualifier("cache-writer") ItemWriter<Person> writerCache
    ) {
        return stepBuilderFactory.get("fromOriginToCache")
                .<Person, Person> chunk(10)
                .reader(readerOrigin)
                .writer(writerCache)
                .build();
    }

    @Bean
    public Step fromCacheToDestinantionEvenId(
            @Qualifier("cache-reader-even-id") ItemReader<Person> readerEvenId,
            @Qualifier("destination-writer")ItemWriter<Person> writerDestination
    ) {
        return stepBuilderFactory.get("fromCacheToDestinantionEvenId")
                .<Person, Person> chunk(10)
                .reader(readerEvenId)
                .processor(personProcessor())
                .writer(writerDestination)
                .build();
    }

    @Bean
    public Step fromCacheToDestinantionBatches(
            @Qualifier("cache-reader-batches") ItemReader<Person> readerBatches,
            @Qualifier("destination-writer")ItemWriter<Person> writerDestination
    ) {
        return stepBuilderFactory.get("fromCacheToDestinantionBatches")
                .<Person, Person> chunk(10)
                .reader(readerBatches)
                .writer(writerDestination)
                .build();
    }

    @Bean
    public Step clearCache() {
        return stepBuilderFactory.get("clearCache")
                .tasklet(clearAllCache())
                .build();
    }

    @Bean
    public Step clearCacheEvenId() {
        return stepBuilderFactory.get("clearCacheEvenId")
                .tasklet(clearEvenId())
                .build();
    }

    @Bean
    public Step countRowsCache() {
        return stepBuilderFactory.get("countRowsCache")
                .tasklet(countRows())
                .build();
    }

    @Bean
    public PersonItemProcessor personProcessor() {
        return new PersonItemProcessor();
    }

    @Bean
    public ClearCacheTasklet clearAllCache() {
        return new ClearCacheTasklet();
    }

    @Bean
    public ClearEvenIdTasklet clearEvenId() {
        return new ClearEvenIdTasklet();
    }

    @Bean
    public CountRowsTasklet countRows() {
        return new CountRowsTasklet();
    }


}
