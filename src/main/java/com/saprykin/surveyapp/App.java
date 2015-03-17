package com.saprykin.surveyapp;

import com.saprykin.surveyapp.configuration.AppConfig;
import com.saprykin.surveyapp.model.Role;
import com.saprykin.surveyapp.model.User;
import com.saprykin.surveyapp.service.RoleService;
import com.saprykin.surveyapp.service.UserService;

import com.saprykin.surveyapp.util.JsonTransformer;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.Random;
import static spark.Spark.get;
import static spark.SparkBase.setPort;
import static spark.SparkBase.staticFileLocation;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        setPortForApp();
        setUpLog4jProperties();
        staticFileLocation("/public");
        staticFileLocation("/public/images");
        staticFileLocation("/public/css");

        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = (UserService) context.getBean("userService");
        RoleService roleService = (RoleService) context.getBean("roleService");

        //final String dbTestString = testDb();

        Role userRole = new Role();
        userRole.setRole("user");
        Role adminRole = new Role();
        adminRole.setRole("admin");

        roleService.saveRole(userRole);
        roleService.saveRole(adminRole);

        Random random = new Random();

        User user1 = new User();
        user1.setEmail("foo" + random.nextInt(10000) * random.nextDouble() + "@bar.com");
        user1.setEmailConfirmation(false);
        user1.setEmailNotifications(false);
        user1.setRole(userRole);

        User user2 = new User();
        user2.setEmail("bar" + random.nextInt(10000) * random.nextDouble() + "@foo.com");
        user2.setEmailConfirmation(true);
        user2.setEmailNotifications(true);
        user2.setRole(adminRole);


        userService.saveUser(user1);
        userService.saveUser(user2);

        /*get("/", (request, response) -> {
            logger.info("Called hhtp GET method, User-Agent is:" + request.headers("User-Agent"));

            return "<html><head><h1>Hello, world!</h1></head><body><h2> <a href=/users>Users</a> </h2></body></html>";
        });*/

        get("/users", "application/json", (request, response) -> {
            logger.info("Called hhtp GET method    /users");

            return userService.findAllUsers();
        }, new JsonTransformer());
    }

    /**
     * Heroku assigns different port each time, hence reading it from process.
     */
    private static void setPortForApp() {
        logger.info("setting port for application...");
        ProcessBuilder process = new ProcessBuilder();
        Integer port;
        if(process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 8080;
        }
        setPort(port);
        logger.info("port is: " + port);
    }

    private static void setUpLog4jProperties() {
        String log4jConfPath = "web/WEB-INF/classes/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
    }
}
