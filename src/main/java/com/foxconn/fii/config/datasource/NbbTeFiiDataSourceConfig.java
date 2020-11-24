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
        entityManagerFactoryRef = "nbbtefiiEntityManagerFactory",
        transactionManagerRef = "nbbtefiiTransactionManager",
        basePackages = "com.foxconn.fii.data.nbbtefii"
)
@EnableTransactionManagement
public class NbbTeFiiDataSourceConfig {

    @Autowired
    private Environment env;

    @Bean(name = "nbbtefiiDSProperties")
    @ConfigurationProperties("nbbtefii.datasource")
    public DataSourceProperties nbbtefiiDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "nbbtefiiDS")
    @ConfigurationProperties("nbbtefii.datasource.configuration")
    public DataSource nbbtefiiDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "nbbtefiiEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean nbbtefiiEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(nbbtefiiDataSource(nbbtefiiDataSourceProperties()));
        em.setPackagesToScan("com.foxconn.fii.data.nbbtefii");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("nbbtefii.datasource.hibernate.dialect"));
        properties.put("hibernate.jdbc.fetch_size", 1000);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "nbbtefiiTransactionManager")
    public PlatformTransactionManager nbbtefiiTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(nbbtefiiEntityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean(name = "nbbtefiiJdbcTemplate")
    public JdbcTemplate nbbtefiiJdbcTemplate(@Qualifier("nbbtefiiDS") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "nbbtefiiNamedJdbcTemplate")
    public NamedParameterJdbcTemplate nbbtefiiNamedJdbcTemplate(@Qualifier("nbbtefiiDS") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

}
