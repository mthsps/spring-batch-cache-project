package com.example.batchprocessing.job;

import com.example.batchprocessing.model.Person;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.batchprocessing.util.JobUtils.*;

@Configuration
public class ItemReaderConfig {

    private static final int NUMBER_OF_EXECUTIONS = 5;
    private static final String SQL_SELECT_FROM_CACHE = "SELECT * FROM PEOPLE LIMIT {}";
    private static final String SQL_SELECT_FROM_ORIGIN = "SELECT * FROM PEOPLE LIMIT {}";
    private static final String SQL_SELECT_EVEN_ID = "SELECT * FROM PEOPLE WHERE id % 2 = 0";

    private DataSource originDataSource;
    private DataSource cacheDataSource;

    @Autowired
    public ItemReaderConfig(
            @Qualifier("origin-datasource")DataSource originDataSource,
            @Qualifier("cache-datasource") DataSource cacheDataSource) {
        this.originDataSource = originDataSource;
        this.cacheDataSource = cacheDataSource;
    }

    @Bean(name = "origin-reader")
    @StepScope
    public JdbcCursorItemReader<Person> readerOrigin(){
        JdbcCursorItemReader<Person> itemReader = new JdbcCursorItemReader<>();

        // Randomize the number of people sent from originDataSource to the cacheDataSource
        int randomLimit = (int) (Math.random() * (200 - 50)) + 50;
        String sql = SQL_SELECT_FROM_ORIGIN.replace("{}", randomLimit + "");

        itemReader.setDataSource(originDataSource);
        itemReader.setSql(sql);
        itemReader.setRowMapper(new PersonRowMapper());
        return itemReader;
    }

    @Bean(name = "cache-reader-even-id")
    @StepScope
    public JdbcCursorItemReader<Person> readerCacheEvenId(){
        JdbcCursorItemReader<Person> itemReader = new JdbcCursorItemReader<>();
        itemReader.setDataSource(cacheDataSource);
        itemReader.setSql(SQL_SELECT_EVEN_ID);
        itemReader.setRowMapper(new PersonRowMapper());
        return itemReader;
    }

    @Bean(name = "cache-reader-batches")
    @StepScope
    public JdbcCursorItemReader<Person> readerCacheInBatches() {
        String sql = SQL_SELECT_FROM_CACHE.replace("{}", getBatchSize() +"");
        JdbcCursorItemReader<Person> itemReader = new JdbcCursorItemReader<>();
        itemReader.setDataSource(cacheDataSource);
        itemReader.setSql(sql);
        itemReader.setRowMapper(new ItemReaderConfig.PersonRowMapper());

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
