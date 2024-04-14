package com.tugalsan.api.servlet.url.server;

import com.tugalsan.api.coronator.client.*;
import java.util.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.list.client.*;
import com.tugalsan.api.servlet.url.client.*;
import com.tugalsan.api.servlet.url.server.handler.TS_SURLHandler;
import com.tugalsan.api.thread.server.async.TS_ThreadAsyncAwait;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.union.client.TGS_UnionExcuseVoid;
import com.tugalsan.api.url.server.*;

@WebServlet("/" + TGS_SURLUtils.LOC_NAME)//AS IN "/u"
public class TS_SURLWebServlet extends HttpServlet {

    final private static TS_Log d = TS_Log.of(TS_SURLWebServlet.class);
    public static volatile TS_ThreadSyncTrigger killTrigger = null;
    public static volatile TS_SURLConfig config = TS_SURLConfig.of(false);

    @Override
    public void doGet(HttpServletRequest rq, HttpServletResponse rs) {
        call(this, rq, rs);
    }

    @Override
    protected void doPost(HttpServletRequest rq, HttpServletResponse rs) {
        call(this, rq, rs);
    }

    public static TGS_UnionExcuseVoid call(HttpServlet servlet, HttpServletRequest rq, HttpServletResponse rs) {
        TGS_UnionExcuse<String> u_servletName = TGS_Coronator.of(TGS_UnionExcuse.class).coronateAs(val -> {
            var tmp = TS_UrlServletRequestUtils.getParameterValue(rq, TGS_SURLUtils.PARAM_SERVLET_NAME(), true);
            if (tmp.isExcuse()) {
                tmp = TS_UrlServletRequestUtils.getParameterValue(rq, TGS_SURLUtils.PARAM_SERVLET_NAME_ALIAS0(), true);
            }
            return tmp;
        });
        if (u_servletName.isExcuse()) {
            return u_servletName.toExcuseVoid();
        }
        var servletPack = TS_SURLExecutorList.get(u_servletName.value());
        if (servletPack != null) {
            if (config.enableTimeout) {
                TS_ThreadAsyncAwait.runUntil(killTrigger, servletPack.executor().timeout(), exe -> {
                    servletPack.executor().run(TS_SURLHandler.of(servlet, rq, rs));
                });
            } else {
                servletPack.executor().run(TS_SURLHandler.of(servlet, rq, rs));
            }
            return TGS_UnionExcuseVoid.ofVoid();
        }
        TS_SURLHandler.of(servlet, rq, rs).txt(text -> text.pw.close());
        if (SKIP_ERRORS_FOR_SERVLETNAMES.stream().filter(sn -> Objects.equals(sn, u_servletName.value())).findAny().isPresent()) {
            return TGS_UnionExcuseVoid.ofVoid();
        }
        TS_SURLExecutorList.SYNC.forEach(item -> {
            d.ce("call", "-", item.name());
        });
        return TGS_UnionExcuseVoid.ofExcuse(d.className, "call", "servletName not identified: [" + u_servletName.value() + "]");
    }
    public static List<String> SKIP_ERRORS_FOR_SERVLETNAMES = TGS_ListUtils.of();
}
