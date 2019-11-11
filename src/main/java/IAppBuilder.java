import subscriber.ITransferSubscriber;
import producer.ITransferRequestProducer;

public interface IAppBuilder {
    IAppBuilder withProducer(ITransferRequestProducer listener);
    IAppBuilder withConsumer(ITransferSubscriber subscriber);
    IAppBuilder withStorage();
    void build();
}
