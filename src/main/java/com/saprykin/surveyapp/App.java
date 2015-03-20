package com.saprykin.surveyapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saprykin.surveyapp.configuration.AppConfig;
import com.saprykin.surveyapp.model.Pool;
import com.saprykin.surveyapp.model.Role;
import com.saprykin.surveyapp.model.User;
import com.saprykin.surveyapp.service.PoolService;
import com.saprykin.surveyapp.service.RoleService;
import com.saprykin.surveyapp.service.UserService;

import com.saprykin.surveyapp.util.JsonTransformer;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import static spark.Spark.get;
import static spark.SparkBase.setPort;
import static spark.SparkBase.staticFileLocation;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        // Observe: this method must be called before all other methods.
        staticFileLocation("/public"); // Static files

        setPortForApp();
        setUpLog4jProperties();

        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = (UserService) context.getBean("userService");
        RoleService roleService = (RoleService) context.getBean("roleService");
        PoolService poolService = (PoolService) context.getBean("poolService");

        Pool pool1 = new Pool();
        pool1.setCreationDate(LocalDate.now());
        pool1.setTitle("New pool!");
        pool1.setDurationInDays(7);

        poolService.savePool(pool1);

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

        get("/pools", "application/json", (request, response) -> {
            logger.info("Called hhtp GET method    /pools");

            return poolService.findAllPools();
        }, new JsonTransformer());

        get("/pool", "application/json", (request, response) -> {
            logger.info("Called hhtp GET method    /pool");
            List<Pool> pools = poolService.findAllPools();
            Pool pool = pools.get(0);
            Random r = new Random();
            pool.setTitle("Brand new pool! " + r.nextInt(10000));
            poolService.updatePool(pool);

            return poolService.findAllPools();
        }, new JsonTransformer());

        get("/info", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                Session session = request.session();

                return session.attributes();
            }
        }, new JsonTransformer());


        get("/login", (request, response) -> {
            logger.info("Called hhtp GET method    /login");


            BufferedReader br = new BufferedReader(new InputStreamReader(request.raw().getInputStream()));
            String json = br.readLine();

            // 2. initiate jackson mapper
            ObjectMapper mapper = new ObjectMapper();

            // 3. Convert received JSON to String
            String userInputString = mapper.readValue(json, String.class);

            Session session = request.session();
            logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! session.isNew = " + session.isNew());
            session.attribute("userAuthentication", true);
            session.attribute("userRole", "user");
            session.attribute("userEmail", userInputString);

            return null;
        });
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
