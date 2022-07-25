package com.example.batchprocessing.job;

import com.example.batchprocessing.model.Person;
import com.example.batchprocessing.repository.PersonRepository;
import com.redis.spring.batch.DataStructure;
import com.redis.spring.batch.KeyValue;
import com.redis.spring.batch.RedisItemReader;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class ItemReaderConfig {

    public DataSource dataSource;

    @Autowired
    PersonRepository repo;

    @Autowired
    public ItemReaderConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names("firstName", "lastName")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                    setTargetType(Person.class);
                }})
                .build();
    }

    /*@Bean
    public JdbcCursorItemReader<Person> readerSQL(){
        JdbcCursorItemReader<Person> itemReader = new JdbcCursorItemReader<>();
        itemReader.setDataSource(dataSource);
        itemReader.setSql("SELECT * FROM people");
        itemReader.setRowMapper(new PersonRowMapper());
        return itemReader;
    }

    public static class PersonRowMapper implements RowMapper<Person> {
        public static final String ID_COLUMN = "id";
        public static final String FIRST_NAME_COLUMN = "first_name";
        public static final String LAST_NAME_COLUMN = "last_name";

        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person person = new Person();
            person.setId(rs.getString(ID_COLUMN));
            person.setFirstName(rs.getString(FIRST_NAME_COLUMN));
            person.setLastName(rs.getString(LAST_NAME_COLUMN));
            return person;
        }
    }
     */

}
