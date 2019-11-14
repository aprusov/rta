package com.alex.rta.producer;

import com.alex.rta.subscriber.ITransferSubscriber;

public interface ITransferRequestEndpoint {
    void start();
    void setSubscriber(ITransferSubscriber subscriber);
}
