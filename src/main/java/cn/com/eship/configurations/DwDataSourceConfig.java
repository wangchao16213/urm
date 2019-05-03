package cn.com.eship.configurations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DwDataSourceConfig {
    @Bean(name = "dwJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(
            @Qualifier("dwDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
