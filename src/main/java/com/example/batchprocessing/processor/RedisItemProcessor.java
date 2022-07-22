package com.example.batchprocessing.processor;

import com.example.batchprocessing.model.Person;
import com.example.batchprocessing.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisItemProcessor implements ItemProcessor <Person, Person> {

    private static final Logger log = LoggerFactory.getLogger(Person.class);
    @Autowired
    private PersonRepository repository;

    @Override
    public Person process(Person person) throws Exception {
        return repository.save(person);
    }
}