import com.alex.rta.AppBuilder;
import com.alex.rta.IAppBuilder;
import com.alex.rta.data.storage.db.DbConnectionTarget;
import com.alex.rta.producer.TransferMoneyWebEndpoint;
import com.alex.rta.producer.service.AccountServlet;
import com.alex.rta.scheduler.disruptor.DisruptorConsumer;

import java.util.HashMap;

public class App {

    static final String dbUrl = "db/rta.db";
    static final int webEndpointPort = 8090;

    public static void main(String[] args){

        DbConnectionTarget dbConnectionTarget = new DbConnectionTarget(dbUrl);
        IAppBuilder appBuilder = new AppBuilder()
                .withStorage()
                .withScheduler(new DisruptorConsumer())
                .withProducer(new TransferMoneyWebEndpoint(webEndpointPort, new HashMap<>(){{
                    put("", new AccountServlet());
                }}));

        appBuilder.buildAndStart();
    }
}
