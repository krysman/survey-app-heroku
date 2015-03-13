package com.saprykin.surveyapp.configuration;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;


@Configuration // indicates that this class contains one or more bean methods annotated with @Bean producing beans manageable by spring container
@EnableTransactionManagement // is equivalent to Spring’s tx:* XML namespace, enabling Spring’s annotation-driven transaction management capability.
@ComponentScan({ "com.saprykin.surveyapp.configuration" }) // providing with where to look for spring managed beans/classes
public class HibernateConfiguration {


    @Autowired
    private Environment environment;

    //hib
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "com.saprykin.surveyapp.model" });
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }


    /*//jpa
    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource());
        builder
                .scanPackages("com.mkyong.users.model")
                .addProperties(getHibernateProperties());

        return builder.buildSessionFactory();
    }*/

    //hib
    @Bean
    public DataSource dataSource() {

        URI dbUri = null;
        try {
            dbUri = new URI(System.getenv("DATABASE_URL"));
        } catch(URISyntaxException e) {
            e.printStackTrace();
        }

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver"); // "org.postgresql.Driver"
        dataSource.setUrl("jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath()); // "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath()
        dataSource.setUsername(dbUri.getUserInfo().split(":")[0]); // dbUri.getUserInfo().split(":")[0]
        dataSource.setPassword(dbUri.getUserInfo().split(":")[1]); // dbUri.getUserInfo().split(":")[1]
        return dataSource;
    }


    /*
            String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();
     */

    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    /*@Bean
    private URI dbUrl() {
        URI dbUri = null;
        try {
            dbUri = new URI(System.getenv("DATABASE_URL"));
        } catch(URISyntaxException e) {
            e.printStackTrace();
        }
        return dbUri;
    }*/
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    /*
    //hib
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/spring_jpa");
        dataSource.setUsername( "tutorialuser" );
        dataSource.setPassword( "tutorialmy5ql" );
        return dataSource;
    }

    //jpa
    @Bean(name = "dataSource")
    public BasicDataSource dataSource() {

        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/test");
        ds.setUsername("root");
        return ds;
    }
    */
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "create");
        properties.put("hibernate.connection.characterEncoding", "UTF-8");
        properties.put("hibernate.connection.useUnicode", "true");
        /*Properties properties = new Properties(); // name="hibernate.hbm2ddl.auto" value="update"
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));

        <property key="hibernate.connection.characterEncoding">UTF-8</property>
<property key="hibernate.connection.useUnicode">true</property>

        */
        return properties;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(s);
        return txManager;
    }







}
