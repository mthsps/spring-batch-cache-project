package com.example.batchprocessing.config.origin;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class OriginDataSourceConfig {

    @Primary
    @Bean(name = "origin-datasource")
    @ConfigurationProperties(prefix = "origin.datasource")
    public DataSource createOriginDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
}
