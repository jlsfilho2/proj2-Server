package com.evita.infra;

import java.util.HashMap;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(
		entityManagerFactoryRef = "EntityManagerFactory",
        basePackages = {"com.evita.repository"})
@EnableTransactionManagement
public class HibernateConf {


	/*@Bean(name="EntityManagerFactory")
	  @Primary
	public LocalSessionFactoryBean sessionFactory() {
	    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
	    sessionFactory.setHibernateProperties(hibernateProperties());
	    sessionFactory.setDataSource(dataSource());
	    sessionFactory.setPackagesToScan(new String[] { "com.evita.model" });
	    return sessionFactory;
	} */



    @Bean
    @Primary
    public DataSource dataSource() {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/oauth");
        dataSource.setUsername("sistema");
        dataSource.setPassword("admin");

        return dataSource;
    }
    
    @Bean(name="EntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean dbEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[]{"com.evita.model"});
        em.setPersistenceUnitName("dbEntityManager");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);


        em.setJpaProperties(hibernateProperties());
        return em;
    }

    @Bean(name="transactionManager")
    @Primary
    public PlatformTransactionManager hibernateTransactionManager() {
         JpaTransactionManager transactionManager
        = new JpaTransactionManager();
         transactionManager.setEntityManagerFactory(
                 dbEntityManager().getObject());
        return transactionManager;
    }
    



    private Properties hibernateProperties() {
         Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto","update");
        hibernateProperties.setProperty("hibernate.show_sql","true");
        hibernateProperties.setProperty("hibernate.dialect","org.hibernate.dialect.PostgreSQLDialect");

        return hibernateProperties;
    }
}