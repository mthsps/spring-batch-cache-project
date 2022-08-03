package com.example.batchprocessing.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ClearEvenIdTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(ClearEvenIdTasklet.class);

    public static final String SQL_DELETE_EVEN_ID = "DELETE FROM PEOPLE WHERE id % 2 = 0";
    @Autowired
    @Qualifier("cache-datasource")
    private DataSource cacheDataSource;

    public ClearEvenIdTasklet() {}

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        new JdbcTemplate(cacheDataSource).execute(SQL_DELETE_EVEN_ID);
        log.info("=====================CLEARED EVEN ID=====================");
        return RepeatStatus.FINISHED;
    }
}
