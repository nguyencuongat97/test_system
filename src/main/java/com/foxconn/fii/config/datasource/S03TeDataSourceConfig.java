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
        entityManagerFactoryRef = "s03teEntityManagerFactory",
        transactionManagerRef = "s03teTransactionManager",
        basePackages = "com.foxconn.fii.data.s03te"
)
@EnableTransactionManagement
public class S03TeDataSourceConfig {

    @Autowired
    private Environment env;

    @Bean(name = "s03teDSProperties")
    @ConfigurationProperties("s03te.datasource")
    public DataSourceProperties s03teDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "s03teDS")
    @ConfigurationProperties("s03te.datasource.configuration")
    public DataSource s03teDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "s03teEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean s03teEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(s03teDataSource(s03teDataSourceProperties()));
        em.setPackagesToScan("com.foxconn.fii.data.s03te");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("s03te.datasource.hibernate.dialect"));
        properties.put("hibernate.jdbc.fetch_size", 1000);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "s03teTransactionManager")
    public PlatformTransactionManager s03teTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(s03teEntityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean(name = "s03teJdbcTemplate")
    public JdbcTemplate s03teJdbcTemplate(@Qualifier("s03teDS") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "s03teNamedJdbcTemplate")
    public NamedParameterJdbcTemplate s03teNamedJdbcTemplate(@Qualifier("s03teDS") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

}
