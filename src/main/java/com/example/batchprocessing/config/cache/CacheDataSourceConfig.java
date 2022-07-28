package com.example.batchprocessing.config.cache;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class CacheDataSourceConfig {

    @Bean(name = "cache-datasource")
    @ConfigurationProperties(prefix = "cache.datasource")
    public DataSource createCacheDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
}
