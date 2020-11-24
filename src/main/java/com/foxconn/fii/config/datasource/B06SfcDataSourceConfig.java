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
        entityManagerFactoryRef = "b06sfcEntityManagerFactory",
        transactionManagerRef = "b06sfcTransactionManager",
        basePackages = "com.foxconn.fii.data.b06sfc"
)
@EnableTransactionManagement
//@Profile("alpha")
public class B06SfcDataSourceConfig {

    @Autowired
    private Environment env;

    @Bean(name = "b06sfcDSProperties")
    @ConfigurationProperties("b06sfc.datasource")
    public DataSourceProperties b06sfcDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "b06sfcDS")
    @ConfigurationProperties("b06sfc.datasource.configuration")
    public DataSource b06sfcDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "b06sfcEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean b06sfcEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(b06sfcDataSource(b06sfcDataSourceProperties()));
        em.setPackagesToScan("com.foxconn.fii.data.b06sfc");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.properties.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("b06sfc.datasource.hibernate.dialect"));
        properties.put("hibernate.jdbc.fetch_size", 1000);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "b06sfcTransactionManager")
    public PlatformTransactionManager b06sfcTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(b06sfcEntityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean(name = "b06sfcJdbcTemplate")
    public JdbcTemplate b06sfcJdbcTemplate(@Qualifier("b06sfcDS") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "b06sfcNamedJdbcTemplate")
    public NamedParameterJdbcTemplate b06sfcNamedJdbcTemplate(@Qualifier("b06sfcDS") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
