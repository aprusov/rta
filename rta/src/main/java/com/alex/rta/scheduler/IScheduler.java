package com.alex.rta.scheduler;

import com.alex.rta.subscriber.ISubscriber;

public interface IScheduler<T> extends ISubscriber<T> {
    void start();
    void subscribe(ISubscriber<T> subscriber);
}
