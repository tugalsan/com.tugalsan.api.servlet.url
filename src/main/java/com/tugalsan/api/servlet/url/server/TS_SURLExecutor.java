package com.tugalsan.api.servlet.url.server;

import module com.tugalsan.api.os;
import module com.tugalsan.api.function;
import module com.tugalsan.api.servlet.url;
import module com.tugalsan.api.thread;
import java.time.*;
import java.util.concurrent.*;

abstract public class TS_SURLExecutor implements TGS_FuncMTU_In2<TS_ThreadSyncTrigger, TS_SURLHandler> {

    abstract public String name();

    public Duration timeout() {
        return Duration.ofMinutes(1);
    }

    public TS_SURLExecutor(Semaphore semaphore) {
        this.semaphore = semaphore;
    }
    final public Semaphore semaphore;

    public TS_SURLExecutor() {
        this(new Semaphore(TS_OsCpuUtils.getProcessorCount() - 1));
    }
}
