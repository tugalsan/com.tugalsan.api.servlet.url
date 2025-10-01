package com.tugalsan.api.servlet.url.server.handler;

import module com.tugalsan.api.function;
import module javax.servlet.api;
import java.io.*;
import java.nio.charset.*;

public class TS_SURLHandler02ForPlainText extends TS_SURLHandler02ForPlainAbstract {

//    final private static TS_Log d = TS_Log.of(TS_SURLHandler02ForPlainText.class);
    private TS_SURLHandler02ForPlainText(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, PrintWriter pw) {
        super(hs, rq, rs, noCache, pw);
        TGS_FuncMTCUtils.run(() -> {
            rq.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setContentType("text/plain; charset=" + StandardCharsets.UTF_8.name());
        });
    }

    protected static TS_SURLHandler02ForPlainText of(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, PrintWriter pw) {
        return new TS_SURLHandler02ForPlainText(hs, rq, rs, noCache, pw);
    }
}
