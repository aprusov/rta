package com.alex.rta.data.requests.transfer;

import com.alex.rta.data.requests.RequestBase;

public class TransferRequest extends RequestBase {

    public TransferRequest(long sourceSystemId, long sourceAccountId, long targetAccountId, double amount) {
        super();
        this.timestamp = System.currentTimeMillis();
        this.sourceSystemId = sourceSystemId;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
    }

    private final long timestamp;
    private final long sourceSystemId;
    private final long sourceAccountId;
    private final long targetAccountId;
    private final double amount;


    public long getSourceSystemId() {
        return sourceSystemId;
    }

    public long getSourceAccountId() {
        return sourceAccountId;
    }

    public long getTargetAccountId() {
        return targetAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
