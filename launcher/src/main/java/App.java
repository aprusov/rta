import com.alex.rta.AppBuilder;
import com.alex.rta.IAppBuilder;
import com.alex.rta.data.requests.get.GetRequest;
import com.alex.rta.data.requests.transfer.TransferRequest;
import com.alex.rta.data.storage.db.DbConnectionTarget;
import com.alex.rta.producer.WebService;
import com.alex.rta.producer.service.get.GetServlet;
import com.alex.rta.producer.service.transfer.TransferServlet;
import com.alex.rta.scheduler.disruptor.DisruptorConsumer;
import com.alex.rta.subscriber.StoreRequestSubscriber;

public class App {

    static final String dbUrl = "db/rta.db";
    static final int webEndpointPort = 8090;

    public static void main(String[] args) {

        WebService webService = new WebService(webEndpointPort);
        DbConnectionTarget dbConnectionTarget = new DbConnectionTarget(dbUrl);

        IAppBuilder appBuilder = new AppBuilder()
                .<TransferRequest>withEvent(x -> x
                        .withScheduler(new DisruptorConsumer())
                        .withProducer(() -> {
                            TransferServlet servlet = new TransferServlet();
                            webService.registerServlet("transfer", servlet);
                            return servlet;
                        })
                        .withConsumer(new StoreRequestSubscriber<TransferRequest>(dbConnectionTarget, (byte) 1))
                )
                .<GetRequest>withEvent(x -> x
                        .withProducer(() -> {
                            GetServlet servlet = new GetServlet();
                            webService.registerServlet("get", servlet);
                            return servlet;
                        }));

        appBuilder.buildAndStart();
    }
}
