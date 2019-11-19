package com.alex.rta.scheduler.simple;

import com.alex.rta.scheduler.IScheduler;
import com.alex.rta.subscriber.ISubscriber;

public class SimpleCompositeScheduler<T> implements IScheduler<T> {
    private ISubscriber<T>[] subscribers;

    @Override
    public void start() {
    }

    @Override
    public void subscribe(ISubscriber<T>... subscribers) {
        this.subscribers = subscribers;
    }

    @Override
    public void next(T request) {
        if (subscribers == null) {
            return;
        }

        for (ISubscriber<T> subscriber : subscribers) {
            try {
                subscriber.next(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
