package com.tugalsan.api.servlet.url.server.handler;

import module com.tugalsan.api.function;
import module javax.servlet.api;
import java.io.*;
import java.nio.charset.*;

public class TS_SURLHandler02ForPlainCss extends TS_SURLHandler02ForPlainAbstract {

//    final private static TS_Log d = TS_Log.of(TS_SURLHandler02ForPlainCss.class);
    private TS_SURLHandler02ForPlainCss(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, PrintWriter pw) {
        super(hs, rq, rs, noCache, pw);
        TGS_FuncMTCUtils.run(() -> {
            rq.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setContentType("text/css; charset=" + StandardCharsets.UTF_8.name());
        });
    }

    protected static TS_SURLHandler02ForPlainCss of(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, PrintWriter pw) {
        return new TS_SURLHandler02ForPlainCss(hs, rq, rs, noCache, pw);
    }

}
