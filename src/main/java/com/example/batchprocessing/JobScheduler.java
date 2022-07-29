package com.example.batchprocessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class JobScheduler {

    private static final Logger log = LoggerFactory.getLogger(JobScheduler.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("importPesonWithEvenId")
    private Job jobImportPesonWithEvenId;

    @Autowired
    @Qualifier("importAllPeopleFromCacheInBatches")
    private Job jobImportAllPeopleFromCacheInBatches;

    //At every minute
    @Scheduled(cron = "0 * * * * *")
    public void firstJobScheduler(){
        runJob(jobImportPesonWithEvenId);
    }

    //At every 12 seconds
    @Scheduled(cron = "*/12 * * * * *")
    public void secondJobScheduler(){
        runJob(jobImportAllPeopleFromCacheInBatches);
    }

    private void runJob(Job job) {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        try {
            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            log.info("====================={} STARTED at {}=====================", job.getName(), jobExecution.getStartTime());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
