package com.tugalsan.api.servlet.url.server;


import com.tugalsan.api.callable.client.TGS_CallableType1Void;
import com.tugalsan.api.servlet.url.server.handler.TS_SURLHandler;
import java.time.Duration;

abstract public class TS_SURLExecutor implements TGS_CallableType1Void<TS_SURLHandler> {

    abstract public String name();

    public Duration timeout() {
        return Duration.ofMinutes(1);
    }
}
