package com.tugalsan.api.servlet.url.server;

import com.tugalsan.api.coronator.client.*;
import java.util.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.list.client.*;
import com.tugalsan.api.servlet.url.client.*;
import com.tugalsan.api.servlet.url.server.handler.TS_SURLHandler;
import com.tugalsan.api.string.client.*;
import com.tugalsan.api.thread.server.async.TS_ThreadAsyncAwait;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.unsafe.client.*;
import com.tugalsan.api.url.server.*;

@WebServlet("/" + TGS_SURLUtils.LOC_NAME)//AS IN "/u"
public class TS_SURLWebServlet extends HttpServlet {

    final private static TS_Log d = TS_Log.of(TS_SURLWebServlet.class);
    public static volatile TS_ThreadSyncTrigger killTrigger = null;

    @Override
    public void doGet(HttpServletRequest rq, HttpServletResponse rs) {
        call(this, rq, rs);
    }

    @Override
    protected void doPost(HttpServletRequest rq, HttpServletResponse rs) {
        call(this, rq, rs);
    }

    public static void call(HttpServlet servlet, HttpServletRequest rq, HttpServletResponse rs) {
        TGS_UnSafe.run(() -> {
            var servletName = TGS_Coronator.ofStr().coronateAs(val -> {
                var tmp = TS_UrlServletRequestUtils.getParameterValue(rq, TGS_SURLUtils.PARAM_SERVLET_NAME(), true);
                if (TGS_StringUtils.isNullOrEmpty(tmp)) {
                    tmp = TS_UrlServletRequestUtils.getParameterValue(rq, TGS_SURLUtils.PARAM_SERVLET_NAME_ALIAS0(), true);
                }
                if (TGS_StringUtils.isNullOrEmpty(tmp)) {
                    TGS_UnSafe.thrw(d.className, "call", "servletName is empty");
                    return null;
                }
                return tmp;
            });
            var servletPack = TS_SURLExecutorList.get(servletName);
            if (servletPack != null) {
                TS_ThreadAsyncAwait.runUntil(killTrigger, servletPack.value1.timeout(), exe -> {
                    TGS_UnSafe.run(() -> {
                        servletPack.value1.run(TS_SURLHandler.of(servlet, rq, rs));
                    }, e -> d.ct("call", e));
                });
                return;
            }
            if (SKIP_ERRORS_FOR_SERVLETNAMES.stream().filter(sn -> Objects.equals(sn, servletName)).findAny().isPresent()) {
                TS_SURLHandler.of(servlet, rq, rs).txt(text -> text.pw.close());
                TS_SURLExecutorList.SYNC.forEach(item -> {
                    d.ce("call", "-", item.value0);
                });
                TGS_UnSafe.thrw(d.className, "call", "servletName not identified: [" + servletName + "]");
            }
        }, e -> d.ct("call", e));
    }
    public static List<String> SKIP_ERRORS_FOR_SERVLETNAMES = TGS_ListUtils.of();
}
