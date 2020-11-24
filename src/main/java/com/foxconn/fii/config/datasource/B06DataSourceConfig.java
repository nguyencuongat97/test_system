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
        entityManagerFactoryRef = "b06EntityManagerFactory",
        transactionManagerRef = "b06TransactionManager",
        basePackages = "com.foxconn.fii.data.b06"
)
@EnableTransactionManagement
public class B06DataSourceConfig {

    @Autowired
    private Environment env;

    @Bean(name = "b06DSProperties")
    @ConfigurationProperties("b06.datasource")
    public DataSourceProperties b06DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "B06DS")
    @ConfigurationProperties("b06.datasource.configuration")
    public DataSource b06DataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "b06EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean b06EntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(b06DataSource(b06DataSourceProperties()));
        em.setPackagesToScan("com.foxconn.fii.data.b06");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("b06.datasource.hibernate.dialect"));
        properties.put("hibernate.jdbc.fetch_size", 1000);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "b06TransactionManager")
    public PlatformTransactionManager b06TransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(b06EntityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean(name = "b06JdbcTemplate")
    public JdbcTemplate b06JdbcTemplate(@Qualifier("B06DS") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
