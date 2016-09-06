package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import sun.net.www.protocol.http.logging.HttpLogFormatter;
import webhandlers.WebHandler;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by vladislav on 06.09.16.
 */
public class Main {

    @SuppressWarnings("OverlyBroadThrowsClause")
    public static void main(String[] args) throws Exception {
        final int port = 8080;

        final Server server = new Server(port);
        final ServletContextHandler contextHandler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        final ResourceConfig config = new ResourceConfig(WebHandler.class);
        contextHandler.addServlet(new ServletHolder(new ServletContainer(config)), "/*");
        server.setHandler(contextHandler);

        final FileHandler fileHandler = new FileHandler(System.getProperty("user.home") + "/logger.log", true);
        fileHandler.setFormatter(new HttpLogFormatter());
        final Logger logger = Logger.getLogger("myLogger");
        logger.addHandler(fileHandler);
        config.register(new LoggingFilter(logger, true));

        server.start();
        server.join();
    }
}
