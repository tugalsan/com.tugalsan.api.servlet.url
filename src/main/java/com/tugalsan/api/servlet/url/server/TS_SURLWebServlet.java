package com.tugalsan.api.servlet.url.server;

import com.tugalsan.api.coronator.client.*;
import java.util.*;
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
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

@WebServlet("/" + TGS_SURLUtils.LOC_NAME)//AS IN "/u"
@MultipartConfig(//for TS_LibFileUploadUtils.upload that uses Apache.commons
        fileSizeThreshold = 1024 * 1024 * TS_SURLWebServlet.UPLOAD_MB_LIMIT_MEMORY,
        maxFileSize = 1024 * 1024 * TS_SURLWebServlet.UPLOAD_MB_LIMIT_FILE,
        maxRequestSize = 1024 * 1024 * TS_SURLWebServlet.UPLOAD_MB_LIMIT_REQUESTBALL,
        location = "/" + TGS_SURLUtils.LOC_NAME//means C:/bin/tomcat/home/work/Catalina/localhost/spi-xxx/u (do create it)
)
/*
String appPath = request.getServletContext().getRealPath("");
// constructs path of the directory to save uploaded file
String savePath = appPath + File.separator + SAVE_DIR;

// creates the save directory if it does not exists
File fileSaveDir = new File(savePath);
if (!fileSaveDir.exists()) {
        fileSaveDir.mkdir();
}
 */
public class TS_SURLWebServlet extends HttpServlet {

    final public static int UPLOAD_MB_LIMIT_MEMORY = 10;
    final public static int UPLOAD_MB_LIMIT_FILE = 25;
    final public static int UPLOAD_MB_LIMIT_REQUESTBALL = 50;
    final private static TS_Log d = TS_Log.of(false, TS_SURLWebServlet.class);
    public static volatile TS_ThreadSyncTrigger killTrigger = null;
    public static volatile TS_SURLConfig config = TS_SURLConfig.of();

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
                var handler = TS_SURLHandler.of(servlet, rq, rs);
                if (config.enableTimeout) {
                    var await = TS_ThreadAsyncAwait.runUntil(killTrigger, servletPack.value1.timeout(), exe -> {
                        TGS_UnSafe.run(() -> {
                            servletPack.value1.run(handler);
                        }, e -> d.ct("call.await", e));
                    });
                    if (await.timeout()) {
                        var errMsg = "ERROR(AWAIT) timeout";
                        d.ce("call", servletName, errMsg);
                        return;
                    }
                    if (await.hasError()) {
                        d.ce("call", servletName, "ERROR(AWAIT)", await.exceptionIfFailed.get().getMessage());
                        return;
                    }
                } else {
                    TGS_UnSafe.run(() -> {
                        servletPack.value1.run(handler);
                    }, e -> d.ct("call", e));
                }
                d.ci("call", "executed", "config.enableTimeout", config.enableTimeout, servletName);
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
