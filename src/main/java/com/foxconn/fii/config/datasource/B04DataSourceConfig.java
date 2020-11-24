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
        entityManagerFactoryRef = "b04EntityManagerFactory",
        transactionManagerRef = "b04TransactionManager",
        basePackages = "com.foxconn.fii.data.b04"
)
@EnableTransactionManagement
public class B04DataSourceConfig {

    @Autowired
    private Environment env;

    @Bean(name = "b04DSProperties")
    @ConfigurationProperties("b04.datasource")
    public DataSourceProperties b04DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "B04DS")
    @ConfigurationProperties("b04.datasource.configuration")
    public DataSource b04DataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "b04EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean b04EntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(b04DataSource(b04DataSourceProperties()));
        em.setPackagesToScan("com.foxconn.fii.data.b04");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("b04.datasource.hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "b04TransactionManager")
    public PlatformTransactionManager b04TransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(b04EntityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean(name = "b04JdbcTemplate")
    public JdbcTemplate b04JdbcTemplate(@Qualifier("B04DS") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
