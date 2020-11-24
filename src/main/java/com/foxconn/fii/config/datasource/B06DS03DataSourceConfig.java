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
        entityManagerFactoryRef = "b06ds03EntityManagerFactory",
        transactionManagerRef = "b06ds03TransactionManager",
        basePackages = "com.foxconn.fii.data.b06ds03"
)
@EnableTransactionManagement
public class B06DS03DataSourceConfig {

    @Autowired
    private Environment env;

    @Bean(name = "b06ds03DSProperties")
    @ConfigurationProperties("b06ds03.datasource")
    public DataSourceProperties b06ds03DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "b06ds03DS")
    @ConfigurationProperties("b06ds03.datasource.configuration")
    public DataSource b06ds03DataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "b06ds03EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean b06ds03EntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(b06ds03DataSource(b06ds03DataSourceProperties()));
        em.setPackagesToScan("com.foxconn.fii.data.b06ds03");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("b06ds03.datasource.hibernate.dialect"));
        properties.put("hibernate.jdbc.fetch_size", 1000);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "b06ds03TransactionManager")
    public PlatformTransactionManager b06ds03TransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(b06ds03EntityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean(name = "b06ds03JdbcTemplate")
    public JdbcTemplate b06ds03JdbcTemplate(@Qualifier("b06ds03DS") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
