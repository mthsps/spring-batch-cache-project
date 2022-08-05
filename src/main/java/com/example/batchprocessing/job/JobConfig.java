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
    public Job importPersonWithEvenId(
            JobCompletionNotificationListener listener,
            Step fromOriginToCache,
            Step fromCacheToDestinationEvenId,
            Step clearCache,
            Step clearCacheEvenId
    ) {
        return jobBuilderFactory.get("importPersonWithEvenId")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(clearCache)
                .next(fromOriginToCache)
                .next(fromCacheToDestinationEvenId)
                .next(clearCacheEvenId)
                .build();
    }


    @Bean
    @Order(2)
    public Job importAllPeopleFromCacheInBatches(
            JobCompletionNotificationListener listener,
            Step fromCacheToDestinationBatches,
            Step countRowsCache
    ) {
        return jobBuilderFactory.get("importAllPeopleFromCacheInBatches")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(countRowsCache)
                .next(fromCacheToDestinationBatches)
                .build();
    }


}
