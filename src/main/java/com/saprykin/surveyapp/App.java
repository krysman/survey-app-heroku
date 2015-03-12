package com.saprykin.surveyapp;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

import static spark.Spark.get;
import static spark.SparkBase.setPort;

/**
 * Created by Ivan on 3/12/2015.
 */
public class App {


    public static void main(String[] args) {

        //Heroku assigns different port each time, hence reading it from process.
        ProcessBuilder process = new ProcessBuilder();
        Integer port;
        if(process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 8080;
        }
        setPort(port);

        final String dbTestString = testDb();

        get("/", (request, response) -> "<html><head><h1>Hello World!</h1></head><body>" + "<h2>" +  dbTestString + "</h2>" + "</body></html>");
    }




    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
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

}
