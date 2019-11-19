import com.alex.rta.AppBuilder;
import com.alex.rta.IAppBuilder;
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

import java.io.Console;
import java.util.concurrent.CountDownLatch;

public class App {

    static final String dbUrl = "db/rta.db";
    static final int webEndpointPort = 8090;

    public static void main(String[] args) {

        WebService webService = new WebService(webEndpointPort);
        DbConnectionTarget dbConnectionTarget = new DbConnectionTarget(dbUrl);
        DbAccountService dbAccountService = new DbAccountService(dbConnectionTarget);

        IAppBuilder appBuilder = new AppBuilder()
                .<TransferRequest>withEvent(x -> x
                        .withScheduler(new DisruptorConsumer())
                        .withProducer(() -> {
                            TransferServlet servlet = new TransferServlet();
                            webService.registerServlet("/transfer", servlet);
                            return servlet;
                        })
                        .withConsumer(new StoreRequestSubscriber<>(dbConnectionTarget, (byte) 1))
                        .withConsumer(new ProcessTransferRequestSubscriber(dbAccountService))
                )
                .<GetRequest>withEvent(x -> x
                        .withProducer(() -> {
                            GetServlet servlet = new GetServlet();
                            webService.registerServlet("/get", servlet);
                            return servlet;
                        })
                .withConsumer(new ProcessGetRequestSubscriber(dbAccountService)));

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
