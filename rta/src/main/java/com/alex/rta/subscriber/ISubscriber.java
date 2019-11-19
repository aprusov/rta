package com.alex.rta.subscriber;

public interface ISubscriber<T> {
    void next(T request);
}
