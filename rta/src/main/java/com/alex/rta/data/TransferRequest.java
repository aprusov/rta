package com.alex.rta.data;

import java.util.concurrent.atomic.AtomicLong;

public class TransferRequest {
    private static AtomicLong ID = new AtomicLong();

    public TransferRequest(long sourceSystemId, long sourceAccountId, long targetAccountId, double amount) {
        this.id = ID.getAndIncrement();
        this.timestamp = System.currentTimeMillis();

        this.sourceSystemId = sourceSystemId;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
    }

    private final long id;
    private final long timestamp;
    private final long sourceSystemId ;
    private final long sourceAccountId;
    private final long targetAccountId;
    private final double amount;

    public long getId() {
        return id;
    }

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
