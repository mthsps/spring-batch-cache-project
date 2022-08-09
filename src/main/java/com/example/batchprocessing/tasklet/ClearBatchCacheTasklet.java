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

import static com.example.batchprocessing.util.JobUtils.*;

@Configuration
public class ClearBatchCacheTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(ClearBatchCacheTasklet.class);

    public static final String SQL_DELETE_PERSON = "DELETE FROM PEOPLE LIMIT {}";

    @Autowired
    @Qualifier("cache-datasource")
    private DataSource cacheDataSource;

    public ClearBatchCacheTasklet() {}

    @Autowired
    public ClearBatchCacheTasklet(
            @Qualifier("cache-datasource") DataSource cacheDataSource) {
        this.cacheDataSource = cacheDataSource;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String sql = SQL_DELETE_PERSON.replace("{}", getBatchSize()+"");

        new JdbcTemplate(cacheDataSource).execute(sql);
        log.info("=====================BATCH OFt {} PEOPLE CLEARED=====================", getBatchSize());
        return RepeatStatus.FINISHED;
    }
}
