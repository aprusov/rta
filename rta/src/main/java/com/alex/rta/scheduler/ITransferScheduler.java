package com.alex.rta.scheduler;

import com.alex.rta.subscriber.ITransferSubscriber;

public interface ITransferScheduler extends ITransferSubscriber {
    void start();
    void subscribe(ITransferSubscriber subscriber);
}
