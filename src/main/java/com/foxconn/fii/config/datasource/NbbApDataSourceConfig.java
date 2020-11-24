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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
        entityManagerFactoryRef = "nbbapEntityManagerFactory",
        transactionManagerRef = "nbbapTransactionManager",
        basePackages = "com.foxconn.fii.data.nbbap"
)
@EnableTransactionManagement
public class NbbApDataSourceConfig {

    @Autowired
    private Environment env;

    @Bean(name = "nbbapDSProperties")
    @ConfigurationProperties("nbbap.datasource")
    public DataSourceProperties nbbapDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "nbbapDS")
    @ConfigurationProperties("nbbap.datasource.configuration")
    public DataSource nbbapDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "nbbapEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean nbbapEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(nbbapDataSource(nbbapDataSourceProperties()));
        em.setPackagesToScan("com.foxconn.fii.data.nbbap");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("nbbap.datasource.hibernate.dialect"));
        properties.put("hibernate.jdbc.fetch_size", 1000);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "nbbapTransactionManager")
    public PlatformTransactionManager nbbapTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(nbbapEntityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean(name = "nbbapJdbcTemplate")
    public JdbcTemplate nbbapJdbcTemplate(@Qualifier("nbbapDS") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "nbbapNamedJdbcTemplate")
    public NamedParameterJdbcTemplate nbbapNamedJdbcTemplate(@Qualifier("nbbapDS") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}