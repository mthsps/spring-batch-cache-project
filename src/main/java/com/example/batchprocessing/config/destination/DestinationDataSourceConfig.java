package com.example.batchprocessing.config.destination;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.sql.DataSource;

@Configuration
public class DestinationDataSourceConfig {

    @Bean(name = "destination-datasource")
    @ConfigurationProperties(prefix = "destination.datasource")
    public DataSource createDestinationDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

}
