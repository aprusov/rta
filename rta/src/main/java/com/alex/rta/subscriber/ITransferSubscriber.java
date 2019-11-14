package com.alex.rta.subscriber;

import com.alex.rta.data.TransferRequest;

public interface ITransferSubscriber {
    void next(TransferRequest request);
}
