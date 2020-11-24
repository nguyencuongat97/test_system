package com.foxconn.fii.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "b05EntityManagerFactory",
        transactionManagerRef = "b05TransactionManager",
        basePackages = "com.foxconn.fii.data.b05"
)
@EnableTransactionManagement
public class B05DataSourceConfig {

    @Autowired
    private Environment env;

    @Bean(name = "b05DSProperties")
    @ConfigurationProperties("b05.datasource")
    public DataSourceProperties b05DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "B05DS")
    @ConfigurationProperties("b05.datasource.configuration")
    public DataSource b05DataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "b05EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean b05EntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(b05DataSource(b05DataSourceProperties()));
        em.setPackagesToScan("com.foxconn.fii.data.b05");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("b05.datasource.hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "b05TransactionManager")
    public PlatformTransactionManager b05TransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(b05EntityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean(name = "b05JdbcTemplate")
    public JdbcTemplate b05JdbcTemplate(@Qualifier("B05DS") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
