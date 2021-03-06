package com.alex.rta;

import com.alex.rta.data.requests.get.GetRequest;
import com.alex.rta.data.requests.transfer.TransferRequest;
import com.alex.rta.data.storage.api.account.DbAccountService;
import com.alex.rta.data.storage.db.DbConnectionTarget;
import com.alex.rta.producer.WebService;
import com.alex.rta.producer.service.get.GetServlet;
import com.alex.rta.producer.service.transfer.TransferServlet;
import com.alex.rta.scheduler.disruptor.DisruptorConsumer;
import com.alex.rta.subscriber.ProcessGetRequestSubscriber;
import com.alex.rta.subscriber.ProcessTransferRequestSubscriber;
import com.alex.rta.subscriber.StoreRequestSubscriber;
import org.jooq.SQLDialect;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class App {

    public static final String dbUrl = "jdbc:sqlite:rta.db";
    public static final int webEndpointPort = 8090;

    public static final String transferUrl = "/transfer";
    public static final String getUrl = "/get";

    public static void main(String[] args) {

        WebService webService = new WebService(webEndpointPort);
        DbConnectionTarget dbConnectionTarget = new DbConnectionTarget(dbUrl, SQLDialect.SQLITE);
        DbAccountService dbAccountService = new DbAccountService(dbConnectionTarget);

        // actual executor should guarantee queue
        Executor reactor = Executors.newSingleThreadExecutor();

        IAppBuilder appBuilder = new AppBuilder()
                .<TransferRequest>withEvent(x -> x
                        .withScheduler(new DisruptorConsumer())
                        .withProducer(() -> {
                            TransferServlet servlet = new TransferServlet();
                            webService.registerServlet(transferUrl, servlet);
                            return servlet;
                        })
                        // should have a separate storage
                        .withConsumer(new StoreRequestSubscriber<>(dbConnectionTarget, (byte) 1))
                        .withConsumer(new ProcessTransferRequestSubscriber(dbAccountService, reactor))
                )
                .<GetRequest>withEvent(x -> x
                        .withProducer(() -> {
                            GetServlet servlet = new GetServlet();
                            webService.registerServlet(getUrl, servlet);
                            return servlet;
                        })
                        .withConsumer(new ProcessGetRequestSubscriber(dbAccountService)))
                ;

        appBuilder.buildAndStart();
        webService.start();

        CountDownLatch signal = new CountDownLatch(1);
        try {
            signal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
