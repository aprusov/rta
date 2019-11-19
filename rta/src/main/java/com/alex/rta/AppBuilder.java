package com.alex.rta;


import com.alex.rta.producer.IRequestEndpoint;
import com.alex.rta.scheduler.IScheduler;
import com.alex.rta.subscriber.ISubscriber;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AppBuilder implements IAppBuilder {

    Set<IEventBuilder> eventRegistrations = new HashSet<>();

    @Override
    public <T> IAppBuilder withEvent(Consumer<IEventBuilder<T>> eventRegistration) {
        EventBuilder<T> x = new EventBuilder<>();
        eventRegistration.accept(x);
        eventRegistrations.add(x);
        return this;
    }

    private static class EventBuilder<T> implements IEventBuilder<T>{

        @Override
        public IEventBuilder<T> withProducer(Supplier<IRequestEndpoint<T>> endpoint) {
            return this;
        }

        @Override
        public IEventBuilder<T> withScheduler(IScheduler<T> scheduler) {
            return this;
        }

        @Override
        public IEventBuilder<T> withConsumer(ISubscriber<T> subscriber) {
            return this;
        }

        @Override
        public IEventBuilder<T> withStorage() {
            return this;
        }

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
