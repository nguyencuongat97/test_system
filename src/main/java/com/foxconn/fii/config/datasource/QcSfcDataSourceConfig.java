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
        entityManagerFactoryRef = "qcsfcEntityManagerFactory",
        transactionManagerRef = "qcsfcTransactionManager",
        basePackages = "com.foxconn.fii.data.qcsfc"
)
@EnableTransactionManagement
public class QcSfcDataSourceConfig {

    @Autowired
    private Environment env;

    @Bean(name = "qcsfcDSProperties")
    @ConfigurationProperties("qcsfc.datasource")
    public DataSourceProperties qcsfcDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "qcsfcDS")
    @ConfigurationProperties("qcsfc.datasource.configuration")
    public DataSource qcsfcDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "qcsfcEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean qcsfcEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(qcsfcDataSource(qcsfcDataSourceProperties()));
        em.setPackagesToScan("com.foxconn.fii.data.qcsfc");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("qcsfc.datasource.hibernate.dialect"));
        properties.put("hibernate.jdbc.fetch_size", 1000);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "qcsfcTransactionManager")
    public PlatformTransactionManager qcsfcTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(qcsfcEntityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean(name = "qcsfcJdbcTemplate")
    public JdbcTemplate qcsfcJdbcTemplate(@Qualifier("qcsfcDS") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "qcsfcNamedJdbcTemplate")
    public NamedParameterJdbcTemplate qcsfcNamedJdbcTemplate(@Qualifier("qcsfcDS") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
