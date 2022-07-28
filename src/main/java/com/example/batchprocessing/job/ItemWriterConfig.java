package com.example.batchprocessing.job;

import com.example.batchprocessing.model.Person;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ItemWriterConfig {


    private DataSource destinationDataSource;

    private DataSource cacheDataSource;

    @Autowired
    public ItemWriterConfig(
            @Qualifier(value = "destination-datasource") DataSource destinationDataSource,
            @Qualifier(value = "cache-datasource") DataSource cacheDataSource) {
        this.destinationDataSource = destinationDataSource;
        this.cacheDataSource = cacheDataSource;
    }

    @Bean(name = "cache-writer")
    public JdbcBatchItemWriter<Person> writerCache() {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
                .dataSource(cacheDataSource)
                .build();
    }

    @Bean(name = "destination-writer")
    public JdbcBatchItemWriter<Person> writerDestination() {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
                .dataSource(destinationDataSource)
                .build();
    }


}
