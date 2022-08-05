package com.example.batchprocessing.job;

import com.example.batchprocessing.model.Person;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class ItemReaderConfig {

    private static final String SQL_SELECT_FROM_CACHE = "SELECT * FROM PEOPLE LIMIT {}";
    private static final String SQL_COUNT_ROWS = "Select ABS((select READ_COUNT\n" +
            "            from BATCH_STEP_EXECUTION\n" +
            "            where STEP_NAME = 'fromOriginToCache'\n" +
            "            order by STEP_EXECUTION_ID desc\n" +
            "            limit 1)\n" +
            "    -\n" +
            "           (select READ_COUNT\n" +
            "            from BATCH_STEP_EXECUTION\n" +
            "            where STEP_NAME = 'clearCacheEvenId'\n" +
            "            order by STEP_EXECUTION_ID desc\n" +
            "            limit 1)) as READ_COUNT_ATUALIZADOS";
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
    public JdbcCursorItemReader<Person> readerOrigin(){
        JdbcCursorItemReader<Person> itemReader = new JdbcCursorItemReader<>();
        int randomLimit = (int) (Math.random() * (200 - 50)) + 50;
        String sql = SQL_SELECT_FROM_ORIGIN.replace("{}", randomLimit + "");
        itemReader.setDataSource(originDataSource);
        itemReader.setSql(sql);
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
    public JdbcCursorItemReader<Person> readerCacheInBatches() {
        JdbcCursorItemReader<Person> itemReader = new JdbcCursorItemReader<>();
        Integer rowsCount = countRows(originDataSource);
        System.out.println("Rows count: " + rowsCount);
        String sql = SQL_SELECT_FROM_CACHE.replace("{}", rowsCount.toString());
        itemReader.setDataSource(cacheDataSource);
        itemReader.setSql(sql);
        itemReader.setRowMapper(new ItemReaderConfig.PersonRowMapper());
        return itemReader;

    }

    public Integer countRows(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.queryForObject(SQL_COUNT_ROWS, Integer.class);
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
