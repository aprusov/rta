package com.alex.rta.producer;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;
import java.util.HashMap;
import java.util.Map;

public class WebService {

    private final int port;
    private final Map<String, HttpServlet> servletHolderMap = new HashMap<>();
    private Server server;

    public WebService(int port) {
        this.port = port;
        server = new Server();
    }

    public void registerServlet(String url, HttpServlet servlet){
        servletHolderMap.put(url, servlet);
    }

    public void start() {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new Connector[]{connector});

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        servletHolderMap.keySet().forEach(x -> {
            ServletHolder servletHolder = new ServletHolder(servletHolderMap.get(x));
            handler.addServletWithMapping(servletHolder, x);
        });

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
