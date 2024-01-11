package com.tugalsan.api.servlet.url.server.handler;

import com.tugalsan.api.log.server.TS_Log;
import java.nio.file.Path;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TS_SURLHandler01WCachePolicy {

    final private static TS_Log d = TS_Log.of(TS_SURLHandler01WCachePolicy.class);

    private TS_SURLHandler01WCachePolicy(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache) {
        this.hs = hs;
        this.rq = rq;
        this.rs = rs;
        this.noCache = noCache;
    }
    final private HttpServlet hs;
    final private HttpServletRequest rq;
    final private HttpServletResponse rs;
    final private boolean noCache;

    public static TS_SURLHandler01WCachePolicy of(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache) {
        return new TS_SURLHandler01WCachePolicy(hs, rq, rs, noCache);
    }

    public TS_SURLHandler02ForFileDownload download(Path filePath) {
        return TS_SURLHandler02ForFileDownload.of(hs, rq, rs, noCache, filePath);
    }

    public TS_SURLHandler02ForPlainCss css() {
        return TS_SURLHandler02ForPlainCss.of(hs, rq, rs, noCache);
    }

    public TS_SURLHandler02ForPlainHtml html() {
        return TS_SURLHandler02ForPlainHtml.of(hs, rq, rs, noCache);
    }

    public TS_SURLHandler02ForPlainJs js() {
        return TS_SURLHandler02ForPlainJs.of(hs, rq, rs, noCache);
    }

    public TS_SURLHandler02ForPlainText plain() {
        return TS_SURLHandler02ForPlainText.of(hs, rq, rs, noCache);
    }

    public TS_SURLHandler02ForFilePng png() {
        return TS_SURLHandler02ForFilePng.of(hs, rq, rs, noCache);
    }
}
