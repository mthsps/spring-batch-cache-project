package com.example.batchprocessing.listener;

import com.example.batchprocessing.model.Person;
import com.example.batchprocessing.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private final JdbcTemplate jdbcTemplate;

	private PersonRepository personRepository;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate, PersonRepository personRepository) {
		this.jdbcTemplate = jdbcTemplate;
		this.personRepository = personRepository;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {

			log.info("=====================JOB FINISHED=====================");
			//personRepository.findAll().forEach(System.out::println);

			jdbcTemplate.query("SELECT id, first_name, last_name FROM people",
				(rs, row) -> new Person(
					rs.getString(1),
					rs.getString(2),
					rs.getString(3))
			).forEach(person -> log.info(person.toString()));

		}
	}
}
