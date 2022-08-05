package com.example.batchprocessing.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class CountRowsTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(CountRowsTasklet.class);

    public static final String SQL_COUNT_ROWS = "SELECT COUNT(*) FROM PEOPLE";
    @Autowired
    @Qualifier("cache-datasource")
    private DataSource cacheDataSource;


    public CountRowsTasklet() {}

    @Override
    @StepScope
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(cacheDataSource);

        Integer count = jdbcTemplate.queryForObject(SQL_COUNT_ROWS, Integer.class);

        log.info("=====================COUNTED {} ROWS=====================", count);

        ExecutionContext executionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
        executionContext.put("count", count);

        return RepeatStatus.FINISHED;
    }

}
