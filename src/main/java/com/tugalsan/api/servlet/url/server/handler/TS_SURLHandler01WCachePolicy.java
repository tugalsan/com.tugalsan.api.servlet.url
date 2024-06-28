package com.tugalsan.api.servlet.url.server.handler;

import com.tugalsan.api.function.client.TGS_Func_OutTyped_In1;
import com.tugalsan.api.function.client.TGS_Func_In1;
import com.tugalsan.api.log.server.TS_Log;

import com.tugalsan.api.stream.server.TS_StreamUtils;
import com.tugalsan.api.tuple.client.TGS_Tuple1;
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
    final public HttpServlet hs;
    final public HttpServletRequest rq;
    final public HttpServletResponse rs;
    final private boolean noCache;

    protected static TS_SURLHandler01WCachePolicy of(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache) {
        return new TS_SURLHandler01WCachePolicy(hs, rq, rs, noCache);
    }

    public void download(TGS_Func_OutTyped_In1<Path, TS_SURLHandler02ForFileDownload> download) {
        TGS_Tuple1<Path> filePathHolder = TGS_Tuple1.of();
        TGS_UnSafe.run(() -> {
            var handler = TS_SURLHandler02ForFileDownload.of(hs, rq, rs, noCache);
            filePathHolder.value0 = download.call(handler);
            var filePath = filePathHolder.value0;
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
        }, e_download -> {
            TGS_UnSafe.run(() -> {
                d.ce("download", "filePath", filePathHolder.value0);
                if (DOWNLOAD_HIDE_ERROR_DETAILS) {
                    d.ce("download", "e_download", e_download.getMessage());
                } else {
                    d.ct("download", e_download);
                }
                rs.sendError(HttpServletResponse.SC_NOT_FOUND);
            }, e_sendError -> {
                if (DOWNLOAD_HIDE_ERROR_DETAILS && e_sendError.getMessage().contains("Cannot call sendError() after the response has been committed")) {
                    //DO NOTHING
                } else {
                    d.ce("download", "e_sendError", e_sendError.getMessage());
                }
            });
        });
    }
    public static boolean DOWNLOAD_HIDE_ERROR_DETAILS = true;

    //see TS_FileImageUtils.formatNames. Example "png"
    public void img(String formatName, TGS_Func_OutTyped_In1<RenderedImage, TS_SURLHandler02ForFileImg> img) {
        TGS_UnSafe.run(() -> {
            var handler = TS_SURLHandler02ForFileImg.of(hs, rq, rs, noCache, formatName);
            var renderedImage = img.call(handler);
            try (var os = handler.rs.getOutputStream()) {
                ImageIO.write(renderedImage, formatName, os);
            }
        });
    }

    public void txt(TGS_Func_In1<TS_SURLHandler02ForPlainText> txt) {
        try {
            try (var pw = rs.getWriter()) {
                var handler = TS_SURLHandler02ForPlainText.of(hs, rq, rs, noCache, pw);
                txt.run(handler);
                pw.flush();
            }
        } catch (IOException ex) {
        }
    }

    public void css(TGS_Func_In1<TS_SURLHandler02ForPlainCss> css) {
        try {
            try (var pw = rs.getWriter()) {
                var handler = TS_SURLHandler02ForPlainCss.of(hs, rq, rs, noCache, pw);
                css.run(handler);
                pw.flush();
            }
        } catch (IOException ex) {
        }
    }

    public void html(TGS_Func_In1<TS_SURLHandler02ForPlainHtml> html) {
        try {
            try (var pw = rs.getWriter()) {
                var handler = TS_SURLHandler02ForPlainHtml.of(hs, rq, rs, noCache, pw);
                html.run(handler);
                pw.flush();
            }
        } catch (IOException ex) {
        }
    }

    public void js(TGS_Func_In1<TS_SURLHandler02ForPlainJs> js) {
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
