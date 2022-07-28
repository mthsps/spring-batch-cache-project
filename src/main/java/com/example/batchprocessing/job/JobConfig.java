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

@Configuration
@EnableBatchProcessing
public class JobConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener,
                             Step fromOriginToCache,
                             Step fromCacheToDestinantion,
                             Step clearCache) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(fromOriginToCache)
                .next(fromCacheToDestinantion)
                .next(clearCache)
                .build();
    }
}
