package com.alex.rta;

import com.alex.rta.producer.IRequestEndpoint;
import com.alex.rta.scheduler.IScheduler;
import com.alex.rta.subscriber.ISubscriber;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IAppBuilder {
    <T> IAppBuilder withEvent(Consumer<IEventBuilder<T>> eventRegistration);
    void buildAndStart();

    interface IEventBuilder<T>{
        IEventBuilder<T> withProducer(Supplier<IRequestEndpoint<T>> endpointSupplier);
        IEventBuilder<T> withScheduler(IScheduler<T> scheduler);
        IEventBuilder<T> withConsumer(ISubscriber<T> subscriber);
        IEventBuilder<T> withStorage();
    }

}

