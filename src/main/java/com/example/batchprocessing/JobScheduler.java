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
    @Qualifier("importPersonWithEvenId")
    private Job jobImportPersonWithEvenId;

    @Autowired
    @Qualifier("importAllPeopleFromCacheInBatches")
    private Job jobImportAllPeopleFromCacheInBatches;

    //At every 5 minutes
    @Scheduled(cron = "0 0/5 * * * ?")
    public void firstJobScheduler(){
        runJob(jobImportPersonWithEvenId);
    }

    //At every minute
    @Scheduled(cron = "0 0/1 * * * ?")
    public void secondJobScheduler(){
        runJob(jobImportAllPeopleFromCacheInBatches);
    }


    private void runJob(Job job) {
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        try {
            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            log.info("====================={} JOB FINISHED at {}=====================", job.getName(), jobExecution.getEndTime());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
