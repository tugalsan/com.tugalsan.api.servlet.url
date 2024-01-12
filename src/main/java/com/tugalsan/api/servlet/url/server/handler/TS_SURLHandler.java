package com.tugalsan.api.servlet.url.server.handler;

//AutoClosable Version of Helper
import com.tugalsan.api.log.server.TS_Log;
import javax.servlet.http.*;

public class TS_SURLHandler {

//    final private static TS_Log d = TS_Log.of(TS_SURLHandler.class);

    private TS_SURLHandler(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs) {
        this.hs = hs;
        this.rq = rq;
        this.rs = rs;
    }
    final private HttpServlet hs;
    final private HttpServletRequest rq;
    final private HttpServletResponse rs;

    public static TS_SURLHandler of(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs) {
        return new TS_SURLHandler(hs, rq, rs);
    }

    public TS_SURLHandler01WCachePolicy permitNoCache() {
        return TS_SURLHandler01WCachePolicy.of(hs, rq, rs, true);
    }

    public TS_SURLHandler01WCachePolicy permitCache() {
        return TS_SURLHandler01WCachePolicy.of(hs, rq, rs, false);
    }
}
