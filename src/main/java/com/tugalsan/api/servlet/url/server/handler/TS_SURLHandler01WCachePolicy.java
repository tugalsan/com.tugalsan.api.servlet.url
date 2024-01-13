package com.tugalsan.api.servlet.url.server.handler;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.stream.server.TS_StreamUtils;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

    public void download(TGS_CallableType1<Path, TS_SURLHandler02ForFileDownload> download) {
        TGS_UnSafe.run(() -> {
            var handler = TS_SURLHandler02ForFileDownload.of(hs, rq, rs, noCache);
            var filePath = download.call(handler);
            if (filePath == null) {
                d.ce("download", "filePath == null");
                rs.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            rq.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setCharacterEncoding(StandardCharsets.UTF_8.name());
            var mimeType = hs.getServletContext().getMimeType(filePath.toString());
            rs.setContentType(mimeType == null ? "application/octet-stream" : mimeType);
            var contentLength = filePath.toFile().length();
            d.ci("run", "contentLength", contentLength);
            if (contentLength != -1) {
                rs.setContentLengthLong(contentLength);
            }
            var encodedFileName = URLEncoder.encode(filePath.getFileName().toString(), "UTF-8").replace("+", "%20");
            rs.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", encodedFileName));
            TS_StreamUtils.transfer(Files.newInputStream(filePath), rs.getOutputStream());
        }, e -> {
            TGS_UnSafe.run(() -> {
                d.ct("download", e);
                rs.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            });
        });
    }

    //see TS_FileImageUtils.formatNames. Example "png"
    public void img(String formatName, TGS_CallableType1<RenderedImage, TS_SURLHandler02ForFileImg> img) {
        TGS_UnSafe.run(() -> {
            var handler = TS_SURLHandler02ForFileImg.of(hs, rq, rs, noCache, formatName);
            var renderedImage = img.call(handler);
            try (var os = handler.rs.getOutputStream()) {
                ImageIO.write(renderedImage, formatName, os);
            }
        });
    }

    public void txt(TGS_RunnableType1<TS_SURLHandler02ForPlainText> txt) {
        try {
            try (var pw = rs.getWriter()) {
                var handler = TS_SURLHandler02ForPlainText.of(hs, rq, rs, noCache, pw);
                txt.run(handler);
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
