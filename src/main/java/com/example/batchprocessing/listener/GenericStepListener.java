package com.example.batchprocessing.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenericStepListener implements StepExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(GenericStepListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {}

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("====================={} FINISHED with SUMMARY: {}", stepExecution.getStepName(),  stepExecution.getSummary());
        return ExitStatus.COMPLETED;
    }



}
