package com.tugalsan.api.servlet.url.server.handler;

import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.function.client.maythrowexceptions.checked.TGS_FuncMTCUtils;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TS_SURLHandler02ForFileDownload extends TS_SURLHandler02ForAbstract {

    final private static TS_Log d = TS_Log.of(TS_SURLHandler02ForFileDownload.class);

    private TS_SURLHandler02ForFileDownload(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache) {
        super(hs, rq, rs, noCache);
    }

    protected static TS_SURLHandler02ForFileDownload of(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache) {
        return new TS_SURLHandler02ForFileDownload(hs, rq, rs, noCache);
    }

    public static void throwFileNotFound(HttpServletResponse rs, CharSequence msg) {
        TGS_FuncMTCUtils.run(() -> {
            d.ce("throwFileNotFound", msg);
            rs.sendError(HttpServletResponse.SC_NOT_FOUND);
        }, e -> d.ct("throwFileNotFound", e));
    }
}
