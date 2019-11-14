package com.alex.rta.scheduler.disruptor;


import com.alex.rta.data.TransferRequest;

public class TransferRequestEvent {
    private TransferRequest transferRequest;

    public TransferRequest getTransferRequest() {
        return transferRequest;
    }

    public void setTransferRequest(TransferRequest transferRequest) {
        this.transferRequest = transferRequest;
    }
}
