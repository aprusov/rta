package com.alex.rta;


import com.alex.rta.producer.ITransferRequestEndpoint;
import com.alex.rta.scheduler.ITransferScheduler;
import com.alex.rta.subscriber.ITransferSubscriber;

import java.util.HashSet;
import java.util.Set;

public class AppBuilder implements IAppBuilder {

    private Set<ITransferRequestEndpoint> endpoints = new HashSet<>();
    private Set<ITransferSubscriber> subscribers = new HashSet<>();
    private ITransferScheduler scheduler;

    @Override
    public IAppBuilder withProducer(ITransferRequestEndpoint endpoint) {
        this.endpoints.add(endpoint);
        return this;
    }

    @Override
    public IAppBuilder withScheduler(ITransferScheduler scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    @Override
    public IAppBuilder withConsumer(ITransferSubscriber subscriber) {
        this.subscribers.add(subscriber);
        return this;
    }

    @Override
    public IAppBuilder withStorage() {
        return this;
    }

    @Override
    public void buildAndStart() {
        this.subscribers.forEach(x->scheduler.subscribe(x));

        this.endpoints.forEach(x -> {
            x.setSubscriber(scheduler);
            x.start();
        });
    }

}
