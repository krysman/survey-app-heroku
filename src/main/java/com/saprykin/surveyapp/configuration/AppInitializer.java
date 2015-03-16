package com.saprykin.surveyapp.configuration;


import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/*
With Servlet 3.0, the web.xml is optional.
 Normally, we configure Spring WebMVC dispatcher servlet in web.xml but now we can
 programmatically configure it using WebApplicationInitializer.

 From Spring 3.1, Spring provides an implementation of the ServletContainerInitializer interface
 called SpringServletContainerInitializer.

 The SpringServletContainerInitializer class delegates to an implementation of
org.springframework.web.WebApplicationInitializer that you provide.

There is just one method that you need to implement:
WebApplicationInitializer#onStartup(ServletContext).

You are handed the ServletContext that you need to initialize.
 */

public class AppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(javax.servlet.ServletContext servletContext) throws ServletException {

        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(AppConfig.class);
        ctx.setServletContext(servletContext);

        ServletRegistration.Dynamic servlet = servletContext.addServlet(
                "dispatcher", new DispatcherServlet(ctx));

        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");

    }
}