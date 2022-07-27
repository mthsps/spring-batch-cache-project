package com.example.batchprocessing.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Primary
    @Bean(name = "origin-datasource")
    @ConfigurationProperties(prefix = "origin.datasource")
    public DataSource createOriginDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "cache-datasource")
    @ConfigurationProperties(prefix = "cache.datasource")
    public DataSource createCacheDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "destination-datasource")
    @ConfigurationProperties(prefix = "destination.datasource")
    public DataSource createDestinationDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }


}
