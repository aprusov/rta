package com.alex.rta;


import com.alex.rta.producer.IRequestEndpoint;
import com.alex.rta.scheduler.IScheduler;
import com.alex.rta.scheduler.simple.SimpleCompositeScheduler;
import com.alex.rta.subscriber.ISubscriber;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AppBuilder implements IAppBuilder {

    Set<EventBuilder> eventRegistrations = new HashSet<>();

    @Override
    public <T> IAppBuilder withEvent(Consumer<IEventBuilder<T>> eventRegistration) {
        EventBuilder<T> x = new EventBuilder<>();
        eventRegistration.accept(x);
        eventRegistrations.add(x);
        return this;
    }

    private static class EventBuilder<T> implements IEventBuilder<T> {

        private Supplier<IRequestEndpoint<T>> endpointSupplier;
        private IScheduler<T> scheduler;
        private Set<ISubscriber<T>> subscribers = new HashSet<>();

        @Override
        public IEventBuilder<T> withProducer(Supplier<IRequestEndpoint<T>> endpointSupplier) {
            this.endpointSupplier = endpointSupplier;
            return this;
        }

        @Override
        public IEventBuilder<T> withScheduler(IScheduler<T> scheduler) {
            this.scheduler = scheduler;
            return this;
        }

        @Override
        public IEventBuilder<T> withConsumer(ISubscriber<T> subscriber) {
            this.subscribers.add(subscriber);
            return this;
        }

        @Override
        public IEventBuilder<T> withStorage() {
            return this;
        }

        void bind() {
            if (scheduler == null) {
                scheduler = new SimpleCompositeScheduler<>();
            }
            this.subscribers.forEach(x -> scheduler.subscribe(x));
            this.endpointSupplier.get().setSubscriber(scheduler);
            scheduler.start();
        }
    }

    @Override
    public void buildAndStart() {

        this.eventRegistrations.forEach(EventBuilder::bind);
    }

}
