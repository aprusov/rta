import com.alex.rta.AppBuilder;
import com.alex.rta.IAppBuilder;
import com.alex.rta.producer.TransferMoneyWebEndpoint;
import com.alex.rta.scheduler.disruptor.DisruptorConsumer;

public class App {

    public static void main(String[] args){
        IAppBuilder appBuilder = new AppBuilder()
                .withScheduler(new DisruptorConsumer())
                .withProducer(new TransferMoneyWebEndpoint());

        appBuilder.buildAndStart();
    }
}
