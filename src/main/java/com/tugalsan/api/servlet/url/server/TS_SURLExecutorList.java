package com.tugalsan.api.servlet.url.server;

import java.util.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncLst;

public class TS_SURLExecutorList {

    final private static TS_Log d = TS_Log.of(TS_SURLExecutorList.class);

    final public static TS_ThreadSyncLst<TS_SURLExecutorItem> SYNC = TS_ThreadSyncLst.of();

    public static TS_SURLExecutor add(TS_SURLExecutor exe) {
        SYNC.add(new TS_SURLExecutorItem(exe.name(), exe));
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

    public static TS_SURLExecutorItem get(CharSequence name) {
        return SYNC.findFirst(item -> Objects.equals(item.name(), name));
    }
}
