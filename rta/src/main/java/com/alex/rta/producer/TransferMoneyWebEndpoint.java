package com.alex.rta.producer;

import com.alex.rta.subscriber.ITransferSubscriber;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;
import java.util.Map;

public class TransferMoneyWebEndpoint implements ITransferRequestEndpoint {

    private final int port;
    private final Map<String, HttpServlet> servletHolderMap;
    private Server server;

    public TransferMoneyWebEndpoint(int port, Map<String, HttpServlet> servletHolderMap) {
        this.port = port;
        this.servletHolderMap = servletHolderMap;
        server = new Server();
    }

    public void setSubscriber(ITransferSubscriber subscriber) {
        this.accountServlet.setSubscriber(subscriber);
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
