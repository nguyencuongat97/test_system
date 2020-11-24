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
        entityManagerFactoryRef = "nbbsfcEntityManagerFactory",
        transactionManagerRef = "nbbsfcTransactionManager",
        basePackages = "com.foxconn.fii.data.nbbsfc"
)
@EnableTransactionManagement
public class NbbSfcDataSourceConfig {

    @Autowired
    private Environment env;

    @Bean(name = "nbbsfcDSProperties")
    @ConfigurationProperties("nbbsfc.datasource")
    public DataSourceProperties nbbsfcDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "nbbsfcDS")
    @ConfigurationProperties("nbbsfc.datasource.configuration")
    public DataSource nbbsfcDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "nbbsfcEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean nbbsfcEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(nbbsfcDataSource(nbbsfcDataSourceProperties()));
        em.setPackagesToScan("com.foxconn.fii.data.nbbsfc");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("nbbsfc.datasource.hibernate.dialect"));
        properties.put("hibernate.jdbc.fetch_size", 1000);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "nbbsfcTransactionManager")
    public PlatformTransactionManager nbbsfcTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(nbbsfcEntityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean(name = "nbbsfcJdbcTemplate")
    public JdbcTemplate nbbsfcJdbcTemplate(@Qualifier("nbbsfcDS") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "nbbsfcNamedJdbcTemplate")
    public NamedParameterJdbcTemplate nbbsfcNamedJdbcTemplate(@Qualifier("nbbsfcDS") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
