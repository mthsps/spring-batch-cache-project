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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class ItemReaderConfig {

    private static final String SQL_SELECT_FROM_CACHE = "SELECT * FROM PEOPLE LIMIT 25";
    private static final String SQL_SELECT_FROM_ORIGIN = "SELECT * FROM PEOPLE LIMIT 100";
    private static final String SQL_SELECT_EVEN_ID = "SELECT * FROM PEOPLE WHERE id % 2 = 0";


    public DataSource originDataSource;
    public DataSource cacheDataSource;

    @Autowired
    PersonRepository repo;


    @Autowired
    public ItemReaderConfig(
            @Qualifier("origin-datasource")DataSource originDataSource,
            @Qualifier("cache-datasource") DataSource cacheDataSource) {
        this.originDataSource = originDataSource;
        this.cacheDataSource = cacheDataSource;
    }

    @Bean(name = "origin-reader")
    public JdbcCursorItemReader<Person> readerOrigin(){
        JdbcCursorItemReader<Person> itemReader = new JdbcCursorItemReader<>();
        itemReader.setDataSource(originDataSource);
        itemReader.setSql(SQL_SELECT_FROM_ORIGIN);
        itemReader.setRowMapper(new PersonRowMapper());
        return itemReader;
    }

    @Bean(name = "cache-reader-even-id")
    public JdbcCursorItemReader<Person> readerCacheEvenId(){
        JdbcCursorItemReader<Person> itemReader = new JdbcCursorItemReader<>();
        itemReader.setDataSource(cacheDataSource);
        itemReader.setSql(SQL_SELECT_EVEN_ID);
        itemReader.setRowMapper(new PersonRowMapper());
        return itemReader;
    }

    @Bean(name = "cache-reader-batches")
    public JdbcCursorItemReader<Person> readerCacheInBatches(){
        JdbcCursorItemReader<Person> itemReader = new JdbcCursorItemReader<>();
        itemReader.setDataSource(cacheDataSource);
        itemReader.setSql(SQL_SELECT_FROM_CACHE);
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

}
