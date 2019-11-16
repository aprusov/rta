package com.alex.rta;

import com.alex.rta.producer.ITransferRequestEndpoint;
import com.alex.rta.scheduler.ITransferScheduler;
import com.alex.rta.subscriber.ITransferSubscriber;

public interface IEventBuilder{
    IEventBuilder withProducer(ITransferRequestEndpoint endpoint);
    IEventBuilder withScheduler(ITransferScheduler scheduler);
    IEventBuilder withConsumer(ITransferSubscriber subscriber);
    IEventBuilder withStorage();
    IAppBuilder build();

}
