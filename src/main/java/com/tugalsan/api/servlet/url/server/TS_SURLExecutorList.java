package com.tugalsan.api.servlet.url.server;

import module com.tugalsan.api.log;
import module com.tugalsan.api.thread;
import java.util.*;

public class TS_SURLExecutorList {
    
    private TS_SURLExecutorList(){
        
    }

    final private static TS_Log d = TS_Log.of(TS_SURLExecutorList.class);

    final public static TS_ThreadSyncLst<TS_SGWTExecutorListItem> SYNC = TS_ThreadSyncLst.ofSlowWrite();

    public static TS_SURLExecutor add(TS_SURLExecutor exe) {
        SYNC.add(new TS_SGWTExecutorListItem(exe.name(), exe));
        d.cr("add", exe.name());
        return exe;
    }

    public static TS_SURLExecutor[] add(TS_SURLExecutor... exe) {
        Arrays.stream(exe).forEachOrdered(f -> add(f));
        return exe;
    }

    public static List<TS_SURLExecutor> add(List<TS_SURLExecutor> exe) {
        exe.forEach(f -> add(f));
        return exe;
    }

    public static TS_SGWTExecutorListItem get(CharSequence name) {
        return SYNC.findFirst(item -> Objects.equals(item.name(), name));
    }
}
