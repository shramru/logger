package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import webhandlers.WebHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Map;

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

        final File logFile = new File(System.getProperty("user.home") + "/logger.log");
        //noinspection ResultOfMethodCallIgnored
        logFile.createNewFile();

        final FileOutputStream fos = new FileOutputStream(logFile, true);
        //noinspection resource,IOResourceOpenedButNotSafelyClosed
        final PrintWriter pw = new PrintWriter(fos);

        server.setRequestLog((request, response) -> {
            final StringBuilder sb = new StringBuilder();
            sb.append(request.getMethod());
            sb.append("\nFrom Host: ");
            sb.append(request.getRemoteHost());
            sb.append("; IP: ");
            sb.append(request.getScheme());
            sb.append("://");
            sb.append(request.getRemoteAddr());
            sb.append(':');
            sb.append(request.getRemotePort());
            sb.append("\nTo server: ");
            sb.append(request.getServerName());
            sb.append(':');
            sb.append(request.getServerPort());
            sb.append("\nUser-Agent: ");
            sb.append(request.getHeader("User-Agent"));

            final Map<String, String[]> params = request.getParameterMap();
            sb.append("\nRequest parameters:\n");
            if (params.isEmpty()) {
                sb.append("None\n");
            } else {
                for (Map.Entry<String, String[]> entry : params.entrySet()) {
                    for (String val : entry.getValue()) {
                        sb.append(entry.getKey());
                        sb.append(" -> ");
                        sb.append(val);
                        sb.append(";\n");
                    }
                }
            }
            final String record = sb.toString();
            pw.println(record);
            System.out.println(record);
            pw.flush();
        });

        server.start();
        server.join();
        pw.close();
    }
}
