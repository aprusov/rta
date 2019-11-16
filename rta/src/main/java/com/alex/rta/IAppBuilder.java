package com.alex.rta;

public interface IAppBuilder {
    IEventBuilder withEvent();
    void buildAndStart();
}

