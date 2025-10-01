package com.tugalsan.api.servlet.url.server.handler;

import module com.tugalsan.api.function;
import module javax.servlet.api;
import java.nio.charset.*;


public class TS_SURLHandler02ForFileImg extends TS_SURLHandler02ForAbstract {

//    final private static TS_Log d = TS_Log.of(TS_SURLHandler02ForFilePng.class);

    private TS_SURLHandler02ForFileImg(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, String formatName) {
        super(hs, rq, rs, noCache);
        TGS_FuncMTCUtils.run(() -> {
            rq.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setContentType("image/" + formatName);
        });
    }

    protected static TS_SURLHandler02ForFileImg of(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, String formatName) {
        return new TS_SURLHandler02ForFileImg(hs, rq, rs, noCache, formatName);
    }
}
