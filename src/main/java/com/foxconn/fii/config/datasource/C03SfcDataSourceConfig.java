package com.foxconn.fii.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
        entityManagerFactoryRef = "c03sfcEntityManagerFactory",
        transactionManagerRef = "c03sfcTransactionManager",
        basePackages = "com.foxconn.fii.data.c03sfc"
)
@EnableTransactionManagement
//@Profile("alpha")
public class C03SfcDataSourceConfig {

    @Autowired
    private Environment env;

    @Bean(name = "c03sfcDSProperties")
    @ConfigurationProperties("c03sfc.datasource")
    public DataSourceProperties c03sfcDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "c03sfcDS")
    @ConfigurationProperties("c03sfc.datasource.configuration")
    public DataSource c03sfcDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "c03sfcEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean c03sfcEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(c03sfcDataSource(c03sfcDataSourceProperties()));
        em.setPackagesToScan("com.foxconn.fii.data.c03sfc");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("c03sfc.datasource.hibernate.dialect"));
        properties.put("hibernate.jdbc.fetch_size", 1000);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "c03sfcTransactionManager")
    public PlatformTransactionManager c03sfcTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(c03sfcEntityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean(name = "c03sfcJdbcTemplate")
    public JdbcTemplate c03sfcJdbcTemplate(@Qualifier("c03sfcDS") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "c03sfcNamedJdbcTemplate")
    public NamedParameterJdbcTemplate c03sfcNamedJdbcTemplate(@Qualifier("c03sfcDS") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
