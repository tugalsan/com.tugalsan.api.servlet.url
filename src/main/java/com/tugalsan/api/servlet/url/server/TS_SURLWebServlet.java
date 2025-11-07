package com.tugalsan.api.servlet.url.server;

import module com.tugalsan.api.function;
import module com.tugalsan.api.os;
import module com.tugalsan.api.log;
import module com.tugalsan.api.list;
import module com.tugalsan.api.servlet.url;
import module com.tugalsan.api.string;
import module com.tugalsan.api.thread;
import module com.tugalsan.api.url;
import module javax.servlet.api;
import java.util.*;

@WebServlet("/" + TGS_SURLUtils.LOC_NAME)//AS IN "/u"
public class TS_SURLWebServlet extends HttpServlet {

    final private static TS_Log d = TS_Log.of(false, TS_SURLWebServlet.class);
    private static volatile TS_ThreadSyncTrigger killTrigger = null;
    public static volatile TS_SURLConfig config = TS_SURLConfig.of();

    public static void warmUp(TS_ThreadSyncTrigger killTrigger) {
        TS_SURLWebServlet.killTrigger = killTrigger;
    }

    @Override
    public void doGet(HttpServletRequest rq, HttpServletResponse rs) {
        call(this, rq, rs);
    }

    @Override
    protected void doPost(HttpServletRequest rq, HttpServletResponse rs) {
        call(this, rq, rs);
    }

    public static void call(HttpServlet servlet, HttpServletRequest rq, HttpServletResponse rs) {
        TGS_FuncMTCUtils.run(() -> {
            var servletName = TGS_FuncMTUEffectivelyFinal.ofStr().coronateAs(val -> {
                var tmp = TS_UrlServletRequestUtils.getParameterValue(rq, TGS_SURLUtils.PARAM_SERVLET_NAME(), true);
                if (TGS_StringUtils.cmn().isNullOrEmpty(tmp)) {
                    tmp = TS_UrlServletRequestUtils.getParameterValue(rq, TGS_SURLUtils.PARAM_SERVLET_NAME_ALIAS0(), true);
                }
                if (TGS_StringUtils.cmn().isNullOrEmpty(tmp)) {
                    TGS_FuncMTUUtils.thrw(d.className(), "call", "servletName is empty");
                    return null;
                }
                return tmp;
            });
            var servletPack = TS_SURLExecutorList.get(servletName);
            if (servletPack != null) {
                TS_ThreadSyncRateLimitedRun.of(servletPack.exe().semaphore).run(() -> {
                    var handler = TS_SURLHandler.of(servlet, rq, rs);
                    var servletKillTrigger_wt = TS_ThreadSyncTrigger.of(servletName, killTrigger).newChild(d.className());
                    if (config.enableTimeout) {
                        var servletKillTrigger_await_wt = servletKillTrigger_wt.newChild("await");
                        var await = TS_ThreadAsyncAwait.runUntil(servletKillTrigger_await_wt, servletPack.exe().timeout(), exe -> {
                            TGS_FuncMTCUtils.run(() -> {
                                servletPack.exe().run(servletKillTrigger_await_wt, handler);
                                servletKillTrigger_await_wt.trigger("surl_run_await.ok");
                            }, e -> {
                                servletKillTrigger_await_wt.trigger("surl_run_await.failed");
                                d.ct("call.await", e);
                            });
                        });
                        servletKillTrigger_await_wt.trigger("surl_post_await");
                        if (await.timeout()) {
                            var errMsg = "ERROR(AWAIT) timeout " + servletPack.exe().timeout().toSeconds();
                            d.ce("call", servletName, errMsg);
                            return;
                        }
                        if (await.hasError()) {
                            d.ce("call", servletName, "ERROR(AWAIT)", await.exceptionIfFailed().get().getMessage());
                            d.ct("call", await.exceptionIfFailed().get());
                            return;
                        }
                    } else {
                        d.ce("call", servletName, "WARNING: enableTimeout=false");
                        var servletKillTrigger_run_wt = servletKillTrigger_wt.newChild("run");
                        TGS_FuncMTCUtils.run(() -> {
                            servletPack.exe().run(servletKillTrigger_run_wt, handler);
                            servletKillTrigger_run_wt.trigger("surl_post_run.ok");
                        }, e -> {
                            servletKillTrigger_run_wt.trigger("surl_post_run.failed");
                            d.ct("call", e);
                        });
                    }
                    d.ci("call", "executed", "config.enableTimeout", config.enableTimeout, servletName);
                });
                return;
            }
            if (SKIP_ERRORS_FOR_SERVLETNAMES.stream().anyMatch(sn -> Objects.equals(sn, servletName))) {
                TS_SURLHandler.of(servlet, rq, rs).txt(text -> text.pw.close());
                TS_SURLExecutorList.SYNC.forEach(false, item -> {
                    d.ce("call", "-", item.exe());
                });
                TGS_FuncMTUUtils.thrw(d.className(), "call", "servletName not identified: [" + servletName + "]");
            }
        }, e -> d.ct("call", e));
    }
    public static List<String> SKIP_ERRORS_FOR_SERVLETNAMES = TGS_ListUtils.of();
}
