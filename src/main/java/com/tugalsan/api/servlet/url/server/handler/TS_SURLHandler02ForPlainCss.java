package com.tugalsan.api.servlet.url.server.handler;

import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TS_SURLHandler02ForPlainCss extends TS_SURLHandler02ForPlainAbstract {

    final private static TS_Log d = TS_Log.of(TS_SURLHandler02ForPlainCss.class);

    private TS_SURLHandler02ForPlainCss(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache) {
        super(hs, rq, rs, noCache);
        TGS_UnSafe.run(() -> {
            rq.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setContentType("text/css; charset=" + StandardCharsets.UTF_8.name());
        });
    }

    public static TS_SURLHandler02ForPlainCss of(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache) {
        return new TS_SURLHandler02ForPlainCss(hs, rq, rs, noCache);
    }

}
