package com.alex.rta.data.requests;

import java.util.concurrent.atomic.AtomicLong;

public class RequestBase {
    private static AtomicLong ID = new AtomicLong();
    private final long id;

    public RequestBase() {
        this.id = ID.getAndIncrement();
    }

    public long getId() {
        return id;
    }
}
