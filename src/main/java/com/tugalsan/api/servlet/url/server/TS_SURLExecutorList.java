package com.tugalsan.api.servlet.url.server;

import java.util.*;
import com.tugalsan.api.list.server.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.pack.client.*;

public class TS_SURLExecutorList {

    final private static TS_Log d = TS_Log.of(TS_SURLExecutorList.class);

    final public static TS_ListSync<TGS_Pack2<String, TS_SURLExecutor>> SYNC = new TS_ListSync();

    public static TS_SURLExecutor add(TS_SURLExecutor exe) {
        SYNC.add(new TGS_Pack2(exe.name(), exe));
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

    public static TGS_Pack2<String, TS_SURLExecutor> get(CharSequence name) {
        return SYNC.findFirst(item -> Objects.equals(item.value0, name));
    }
}
