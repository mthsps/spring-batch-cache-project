package com.example.batchprocessing.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public final class JobUtils {

    private static final int NUMBER_OF_EXECUTIONS = 5;

    private static final String SQL_STEP_READ_COUNT = "select ABS((select READ_COUNT\n" +
            "            from BATCH_STEP_EXECUTION\n" +
            "            where STEP_NAME = 'fromOriginToCache'\n" +
            "            order by STEP_EXECUTION_ID desc\n" +
            "            limit 1)\n" +
            "    -\n" +
            "           (select READ_COUNT\n" +
            "            from BATCH_STEP_EXECUTION\n" +
            "            where STEP_NAME = 'fromCacheToDestinationEvenId'\n" +
            "            order by STEP_EXECUTION_ID desc\n" +
            "            limit 1)) as READ_COUNT_ATUALIZADOS";

    private static DataSource originDataSource;

    @Autowired
    public JobUtils(@Qualifier("origin-datasource")DataSource originDataSource){
        JobUtils.originDataSource = originDataSource;
    }

    private static Integer getStepReadCount() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(originDataSource);
        return jdbcTemplate.queryForObject(SQL_STEP_READ_COUNT, Integer.class);
    }

    public static int getBatchSize() {
        return (int) Math.ceil((double) getStepReadCount() / NUMBER_OF_EXECUTIONS);
    }



}
