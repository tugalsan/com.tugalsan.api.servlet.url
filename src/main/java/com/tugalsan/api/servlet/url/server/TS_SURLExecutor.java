package com.tugalsan.api.servlet.url.server;

import module com.tugalsan.api.function;
import module com.tugalsan.api.servlet.url;
import module com.tugalsan.api.thread;
import java.time.*;

abstract public class TS_SURLExecutor implements TGS_FuncMTU_In2<TS_ThreadSyncTrigger, TS_SURLHandler> {

    abstract public String name();

    public Duration timeout() {
        return Duration.ofMinutes(1);
    }
}
