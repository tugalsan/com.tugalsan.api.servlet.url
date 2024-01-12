package com.tugalsan.api.servlet.url.server.handler;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;
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

    public void png(TGS_CallableType1<RenderedImage, TS_SURLHandler02ForFilePng> png) {
        TGS_UnSafe.run(() -> {
            var handler = TS_SURLHandler02ForFilePng.of(hs, rq, rs, noCache);
            var renderedImage = png.call(handler);
            try (var os = handler.rs.getOutputStream()) {
                ImageIO.write(renderedImage, "png", os);
            }
        });
    }

    public void text(TGS_RunnableType1<TS_SURLHandler02ForPlainText> text) {
        try {
            try (var pw = rs.getWriter()) {
                var handler = TS_SURLHandler02ForPlainText.of(hs, rq, rs, noCache, pw);
                text.run(handler);
                pw.flush();
            }
        } catch (IOException ex) {
        }
    }

    public void css(TGS_RunnableType1<TS_SURLHandler02ForPlainCss> css) {
        try {
            try (var pw = rs.getWriter()) {
                var handler = TS_SURLHandler02ForPlainCss.of(hs, rq, rs, noCache, pw);
                css.run(handler);
                pw.flush();
            }
        } catch (IOException ex) {
        }
    }

    public void html(TGS_RunnableType1<TS_SURLHandler02ForPlainHtml> html) {
        try {
            try (var pw = rs.getWriter()) {
                var handler = TS_SURLHandler02ForPlainHtml.of(hs, rq, rs, noCache, pw);
                html.run(handler);
                pw.flush();
            }
        } catch (IOException ex) {
        }
    }

    public void js(TGS_RunnableType1<TS_SURLHandler02ForPlainJs> js) {
        try {
            try (var pw = rs.getWriter()) {
                var handler = TS_SURLHandler02ForPlainJs.of(hs, rq, rs, noCache, pw);
                js.run(handler);
                pw.flush();
            }
        } catch (IOException ex) {
        }
    }
}
