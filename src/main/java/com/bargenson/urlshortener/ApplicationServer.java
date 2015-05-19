package com.bargenson.urlshortener;

import com.bargenson.urlshortener.rest.UrlResource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * Created by bargenson on 2015-05-16.
 */
public class ApplicationServer {

    private Server server;

    public void start() throws Exception {
        server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", UrlResource.class.getCanonicalName());
        context.addEventListener(new ContextLoaderListener());
        context.setInitParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());
        context.setInitParameter("contextConfigLocation", ApplicationConfiguration.class.getName());

        server.setHandler(context);

        server.start();
    }

    public void join() throws InterruptedException {
        server.join();
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            server.destroy();
        }
    }

    public static void main(String[] args) throws Exception {
        final ApplicationServer applicationServer = new ApplicationServer();
        try {
            applicationServer.start();
            applicationServer.join();
        } finally {
            applicationServer.stop();
        }
    }

}
