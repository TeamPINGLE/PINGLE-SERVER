package org.pingle.pingleserver.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableConfigurationProperties
public class DataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.jpa")
    DataSource jpaDataSource() {
        return DataSourceBuilder
                .create()
                .build();
    }

    @Bean
    @Qualifier("lockDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.lock")
    DataSource lockDataSource() {
        return DataSourceBuilder
                .create()
                .build();
    }
}
