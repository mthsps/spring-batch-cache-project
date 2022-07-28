package com.example.batchprocessing.job;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class CleanCacheTasklet implements Tasklet {

    public static final String SQL_DELETE_PERSON = "DELETE FROM PEOPLE";
    @Autowired
    @Qualifier("cache-datasource")
    private DataSource cacheDataSource;

    public CleanCacheTasklet() {}

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        new JdbcTemplate(cacheDataSource).execute(SQL_DELETE_PERSON);
        return RepeatStatus.FINISHED;
    }
}
