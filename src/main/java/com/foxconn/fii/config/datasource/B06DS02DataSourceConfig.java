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

//@Configuration
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "b06ds02EntityManagerFactory",
//        transactionManagerRef = "b06ds02TransactionManager",
//        basePackages = "com.foxconn.fii.data.b06ds02"
//)
//@EnableTransactionManagement
public class B06DS02DataSourceConfig {

    @Autowired
    private Environment env;

    @Bean(name = "b06ds02DSProperties")
    @ConfigurationProperties("b06ds02.datasource")
    public DataSourceProperties b06ds02DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "b06ds02DS")
    @ConfigurationProperties("b06ds02.datasource.configuration")
    public DataSource b06ds02DataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "b06ds02EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean b06ds02EntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(b06ds02DataSource(b06ds02DataSourceProperties()));
        em.setPackagesToScan("com.foxconn.fii.data.b06ds02");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("b06ds02.datasource.hibernate.dialect"));
        properties.put("hibernate.jdbc.fetch_size", 1000);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "b06ds02TransactionManager")
    public PlatformTransactionManager b06ds02TransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(b06ds02EntityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean(name = "b06ds02JdbcTemplate")
    public JdbcTemplate b06ds02JdbcTemplate(@Qualifier("b06ds02DS") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
