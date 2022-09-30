package com.evita.infra;

import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "EntityManagerFactory", basePackages = { "com.evita.repository" })
@EnableTransactionManagement
public class HibernateConf {

	Logger logger = Logger.getLogger(HibernateConf.class.getName());

	private String s = System.getProperty("file.separator");

	/*
	 * @Bean(name="EntityManagerFactory")
	 * 
	 * @Primary public LocalSessionFactoryBean sessionFactory() {
	 * LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
	 * sessionFactory.setHibernateProperties(hibernateProperties());
	 * sessionFactory.setDataSource(dataSource());
	 * sessionFactory.setPackagesToScan(new String[] { "com.evita.model" }); return
	 * sessionFactory; }
	 */

	@Bean
	@Primary
	public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		Properties applicationProperties = getProperties();
		dataSource.setDriverClassName(applicationProperties.getProperty("spring.datasource.driver"));
		dataSource.setUrl(applicationProperties.getProperty("spring.datasource.url"));
		dataSource.setUsername(applicationProperties.getProperty("spring.datasource.username"));
		dataSource.setPassword(applicationProperties.getProperty("spring.datasource.password"));

		return dataSource;
	}

	@Bean(name = "EntityManagerFactory")
	@Primary
	public LocalContainerEntityManagerFactoryBean dbEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan(new String[] { "com.evita.model" });
		em.setPersistenceUnitName("dbEntityManager");
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(getHibernateProperties());
		return em;
	}

	@Bean(name = "transactionManager")
	@Primary
	public PlatformTransactionManager hibernateTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(dbEntityManager().getObject());
		return transactionManager;
	}

	private Properties getHibernateProperties() {
		Properties properties = new Properties();
		Properties applicationProperties = getProperties();
		properties.setProperty("hibernate.hbm2ddl.auto", applicationProperties.getProperty("hibernate.hbm2ddl.auto"));
		properties.setProperty("hibernate.show_sql", applicationProperties.getProperty("hibernate.show_sql"));
		properties.setProperty("hibernate.dialect", applicationProperties.getProperty("hibernate.dialect"));
		return properties;
	}
	
	private File getFile() {
		return new File(System.getProperty("user.home") + s + "appservers" + s + "private" + s + "springboot" + s
		+ "properties" + s + "application.properties");
	}

	private Properties getProperties() {
		Properties properties = new Properties();
		try {
			File file =  getFile();
			logger.log(Level.INFO, "lendo: " + file.getPath() + ", isFile=" + file.isFile());
			InputStream input = new FileInputStream(file);
			properties.load(input);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage() + ", " + ex.getClass().getName());
		}
		return properties;
	}

}

