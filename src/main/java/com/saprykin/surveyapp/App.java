package com.saprykin.surveyapp;

import com.saprykin.surveyapp.configuration.AppConfig;
import com.saprykin.surveyapp.model.Role;
import com.saprykin.surveyapp.model.User;
import com.saprykin.surveyapp.service.RoleService;
import com.saprykin.surveyapp.service.UserService;
import com.saprykin.surveyapp.util.DbSchemaCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import javax.jws.soap.SOAPBinding;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.List;

import static spark.Spark.get;
import static spark.SparkBase.setPort;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final DbSchemaCreator dbSchemaCreator = new DbSchemaCreator();

    public static void main(String[] args) {

        setPortForApp();

        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = (UserService) context.getBean("userService");
        RoleService roleService = (RoleService) context.getBean("roleService");

        //final String dbTestString = testDb();

        get("/", (request, response) -> {


            logger.info("Called hhtp GET method, User-Agent is:" + request.headers("User-Agent"));

            //dbSchemaCreator.createDbIfNotExist();
            //dbSchemaCreator.createSomeDataInDb();

            return "<html><head><h1>Hello, world!</h1></head><body><h2> <a href=/users>Users</a> </h2></body></html>";
        });

        get("/users", (request, response) -> {
            logger.info("Called hhtp GET method    /users");
            Role userRole = new Role();
            userRole.setRole("user");
            Role adminRole = new Role();
            adminRole.setRole("admin");


            User user1 = new User();
            user1.setEmail("foo@bar.com");
            user1.setEmailConfirmation(false);
            user1.setEmailNotifications(false);
            user1.setRole(userRole);

            User user2 = new User();
            user2.setEmail("bar@foo.com");
            user2.setEmailConfirmation(true);
            user2.setEmailNotifications(true);
            user2.setRole(adminRole);

            roleService.saveRole(userRole);
            roleService.saveRole(adminRole);

            userService.saveUser(user1);
            userService.saveUser(user2);

            List<User> allUsers = userService.findAllUsers();
            return "<html><head><h1>Users:</h1></head><body><br><h2>" + allUsers + "</h2></body></html>";
        });
    }

    /**
     * Heroku assigns different port each time, hence reading it from process.
     */
    private static void setPortForApp(){
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

/*
    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }

    private static void createDbIfNotExist(){
        String result = "testing...<br>";

        Connection connection = null;
        try {
            connection = getConnection();
            result += "<br>successfully get connection...";
        } catch(URISyntaxException | SQLException e) {
            result += "<br>couldn't get connection!";
        }
    }

    private static String testDb() {
        String result = "testing...<br>";

        Connection connection = null;
        try {
            connection = getConnection();
            result += "<br>successfully get connection...";
        } catch(URISyntaxException | SQLException e) {
            result += "<br>couldn't get connection!";
        }

        try {
            Statement stmt;
            if(connection != null) {
                stmt = connection.createStatement();
                result += "<br>successfully created statement...";
            }
            else {
                stmt = null;
                result += "<br>connection is null, and statement couldn't be created";
            }
            StringBuilder res = new StringBuilder();

            if(stmt != null) {
                stmt.executeUpdate("DROP TABLE IF EXISTS users");
                stmt.executeUpdate(
                        "CREATE TABLE users " +
                        "(id SERIAL NOT NULL PRIMARY KEY ," +
                        " email VARCHAR(100) NOT NULL )"
                );

                stmt.executeUpdate("INSERT INTO users (email) VALUES ('foo@bar.com'), ('bar@foo.com'), ('foobar@bf.com') ");
                ResultSet rs = stmt.executeQuery("SELECT * FROM users");

                res.append("Reading from DB:<br>");
                while(rs.next()) {
                    res.append("id: ");
                    res.append(rs.getInt("id"));
                    res.append(" e-mail: ");
                    res.append(rs.getString("email"));
                    res.append("<br>");
                }
            }

            result = res.toString();

        } catch(SQLException e) {
            result += "<br>";
            result += e.toString();
        }

        return result;
    }
*/
}
