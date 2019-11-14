package com.alex.rta.producer;

import com.alex.rta.producer.service.AccountServlet;
import com.alex.rta.subscriber.ITransferSubscriber;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class TransferMoneyWebEndpoint implements ITransferRequestEndpoint {
    private final AccountServlet accountServlet;
    private Server server;

    public TransferMoneyWebEndpoint() {
        server = new Server();
        accountServlet = new AccountServlet();
    }

    public void setSubscriber(ITransferSubscriber subscriber) {
        this.accountServlet.setSubscriber(subscriber);
    }

    public void start() {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8090);
        server.setConnectors(new Connector[]{connector});

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        ServletHolder servletHolder = new ServletHolder(accountServlet);
        handler.addServletWithMapping(servletHolder, "/*");

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
