package com.tugalsan.api.servlet.url.server.handler;

import module com.tugalsan.api.function;
import module javax.servlet.api;
import java.io.*;
import java.nio.charset.*;

public class TS_SURLHandler02ForPlainJs extends TS_SURLHandler02ForPlainAbstract {

//    final private static TS_Log d = TS_Log.of(TS_SURLHandler02ForPlainJs.class);

    private TS_SURLHandler02ForPlainJs(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, PrintWriter pw) {
        super(hs, rq, rs, noCache, pw);
        TGS_FuncMTCUtils.run(() -> {
            rq.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setContentType("text/javascript; charset=" + StandardCharsets.UTF_8.name());
        });
    }

    protected static TS_SURLHandler02ForPlainJs of(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, PrintWriter pw) {
        return new TS_SURLHandler02ForPlainJs(hs, rq, rs, noCache, pw);
    }
}
