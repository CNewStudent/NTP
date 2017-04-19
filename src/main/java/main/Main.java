package main;

import accounts.AccountService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.DownloadImageServlet;
import servlets.SignInServlet;
import servlets.SignUpServlet;
import servlets.WorkWithImageServlet;

import java.util.logging.Logger;

public class Main  {
    public static void main(String[] argc) throws Exception{
        AccountService accountService = new AccountService();

        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/signup");
        context.addServlet(new ServletHolder(new SignInServlet(accountService)), "/signin");
        context.addServlet(new ServletHolder(new DownloadImageServlet()), "/download");
        context.addServlet(new ServletHolder(new WorkWithImageServlet()), "/change");


        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase("webapp");


        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(8080);
        server.setHandler(handlers);

        server.start();
        Logger.getGlobal().info("Server started");
        server.join();
    }
}
