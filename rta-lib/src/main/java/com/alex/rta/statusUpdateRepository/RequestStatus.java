package com.alex.rta.statusUpdateRepository;

import com.alex.rta.data.requests.RequestState;

public class RequestStatus<T> {

    private final RequestState state;
    private final T value;

    public RequestStatus(RequestState state, T value) {

        this.state = state;
        this.value = value;
    }

    public RequestState getState() {
        return state;
    }

    public T getValue() {
        return value;
    }
}
