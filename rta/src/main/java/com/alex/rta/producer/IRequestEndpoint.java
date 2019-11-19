package com.alex.rta.producer;

import com.alex.rta.subscriber.ISubscriber;

public interface IRequestEndpoint<T> {
    void setSubscriber(ISubscriber<T> subscriber);
}
