package com.alex.rta;

import com.alex.rta.producer.ITransferRequestEndpoint;
import com.alex.rta.scheduler.ITransferScheduler;
import com.alex.rta.subscriber.ITransferSubscriber;

public interface IAppBuilder {
    IAppBuilder withProducer(ITransferRequestEndpoint endpoint);
    IAppBuilder withScheduler(ITransferScheduler scheduler);
    IAppBuilder withConsumer(ITransferSubscriber subscriber);
    IAppBuilder withStorage();
    void buildAndStart();
}

