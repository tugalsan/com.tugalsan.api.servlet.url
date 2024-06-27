package com.tugalsan.api.servlet.url.server;


import com.tugalsan.api.function.client.TGS_Func_In1;
import com.tugalsan.api.servlet.url.server.handler.TS_SURLHandler;
import java.time.Duration;

abstract public class TS_SURLExecutor implements TGS_Func_In1<TS_SURLHandler> {

    abstract public String name();

    public Duration timeout() {
        return Duration.ofMinutes(1);
    }
}
