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
        entityManagerFactoryRef = "b06teEntityManagerFactory",
        transactionManagerRef = "b06teTransactionManager",
        basePackages = "com.foxconn.fii.data.b06te"
)
@EnableTransactionManagement
public class B06TeDataSourceConfig {

    @Autowired
    private Environment env;

    @Bean(name = "b06teDSProperties")
    @ConfigurationProperties("b06te.datasource")
    public DataSourceProperties b06teDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "b06teDS")
    @ConfigurationProperties("b06te.datasource.configuration")
    public DataSource b06teDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "b06teEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean b06teEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(b06teDataSource(b06teDataSourceProperties()));
        em.setPackagesToScan("com.foxconn.fii.data.b06te");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("b06te.datasource.hibernate.dialect"));
        properties.put("hibernate.jdbc.fetch_size", 1000);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "b06teTransactionManager")
    public PlatformTransactionManager b06teTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(b06teEntityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean(name = "b06teJdbcTemplate")
    public JdbcTemplate b06teJdbcTemplate(@Qualifier("b06teDS") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "b06teNamedJdbcTemplate")
    public NamedParameterJdbcTemplate b06teNamedJdbcTemplate(@Qualifier("b06teDS") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

}
