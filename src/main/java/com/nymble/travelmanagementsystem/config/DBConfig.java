package com.nymble.travelmanagementsystem.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DBConfig {
    @ConfigurationProperties(prefix = "spring.datasource.url")
    public DataSource empPortalDataSource() {
        return DataSourceBuilder.create().build();
    }
}