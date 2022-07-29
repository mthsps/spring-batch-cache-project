package com.example.batchprocessing.job;

import com.example.batchprocessing.listener.JobCompletionNotificationListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@EnableBatchProcessing
public class JobConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    @Order(1)
    public Job importPesonWithEvenId(JobCompletionNotificationListener listener,
                             Step fromOriginToCache,
                             Step fromCacheToDestinantionEvenId,
                             Step clearCache) {
        return jobBuilderFactory.get("importPesonWithEvenId")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(clearCache)
                .start(fromOriginToCache)
                .next(fromCacheToDestinantionEvenId)
                .build();
    }


    @Bean
    @Order(2)
    public Job importAllPeopleFromCacheInBatches(
            JobCompletionNotificationListener listener, Step fromCacheToDestinantionBatches) {
        return jobBuilderFactory.get("importAllPeopleFromCacheInBatches")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(fromCacheToDestinantionBatches)
                .build();
    }


}
