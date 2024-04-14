package com.tugalsan.api.servlet.url.server.handler;

import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.stream.server.TS_StreamUtils;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.union.client.TGS_UnionExcuseVoid;
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

    public TGS_UnionExcuseVoid download(TGS_CallableType1<TGS_UnionExcuse<Path>, TS_SURLHandler02ForFileDownload> download) {
        try {
            var handler = TS_SURLHandler02ForFileDownload.of(hs, rq, rs, noCache);
            if (handler.isExcuse()) {
                return handler.toExcuseVoid();
            }
            var u_filePath = download.call(handler.value());
            if (u_filePath.isExcuse()) {
                try {
                    rs.sendError(HttpServletResponse.SC_NOT_FOUND);
                } catch (IOException ex) {
                    return TGS_UnionExcuseVoid.ofExcuse(ex);
                }
                return u_filePath.toExcuseVoid();
            }
            rq.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setCharacterEncoding(StandardCharsets.UTF_8.name());
            var mimeType = hs.getServletContext().getMimeType(u_filePath.toString());
            rs.setContentType(mimeType == null ? "application/octet-stream" : mimeType);
            var contentLength = u_filePath.value().toFile().length();
            d.ci("run", "contentLength", contentLength);
            if (contentLength != -1) {
                rs.setContentLengthLong(contentLength);
            }
            var encodedFileName = URLEncoder.encode(u_filePath.value().getFileName().toString(), "UTF-8").replace("+", "%20");
            rs.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", encodedFileName));
            var result = TS_StreamUtils.transfer(Files.newInputStream(u_filePath.value()), rs.getOutputStream());
            if (result.isExcuse()) {
                try {
                    rs.sendError(HttpServletResponse.SC_NOT_FOUND);
                } catch (IOException ex) {
                    return TGS_UnionExcuseVoid.ofExcuse(ex);
                }
                return result;
            }
        } catch (IOException ex) {
            try {
                rs.sendError(HttpServletResponse.SC_NOT_FOUND);
            } catch (IOException ex1) {
                return TGS_UnionExcuseVoid.ofExcuse(ex1);
            }
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
        return TGS_UnionExcuseVoid.ofVoid();
    }

    //see TS_FileImageUtils.formatNames. Example "png"
    public TGS_UnionExcuseVoid img(String formatName, TGS_CallableType1<RenderedImage, TS_SURLHandler02ForFileImg> img) {
        var handler = TS_SURLHandler02ForFileImg.of(hs, rq, rs, noCache, formatName);
        if (handler.isExcuse()) {
            return handler.toExcuseVoid();
        }
        var renderedImage = img.call(handler.value());
        try (var os = handler.value().rs.getOutputStream()) {
            ImageIO.write(renderedImage, formatName, os);
        } catch (IOException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
        return TGS_UnionExcuseVoid.ofVoid();
    }

    public TGS_UnionExcuseVoid txt(TGS_RunnableType1<TS_SURLHandler02ForPlainText> txt) {
        try (var pw = rs.getWriter()) {
            var handler = TS_SURLHandler02ForPlainText.of(hs, rq, rs, noCache, pw);
            if (handler.isExcuse()) {
                return handler.toExcuseVoid();
            }
            txt.run(handler.value());
            pw.flush();
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (IOException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public TGS_UnionExcuseVoid css(TGS_RunnableType1<TS_SURLHandler02ForPlainCss> css) {
        try (var pw = rs.getWriter()) {
            var handler = TS_SURLHandler02ForPlainCss.of(hs, rq, rs, noCache, pw);
            if (handler.isExcuse()) {
                return handler.toExcuseVoid();
            }
            css.run(handler.value());
            pw.flush();
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (IOException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public TGS_UnionExcuseVoid html(TGS_RunnableType1<TS_SURLHandler02ForPlainHtml> html) {
        try (var pw = rs.getWriter()) {
            var handler = TS_SURLHandler02ForPlainHtml.of(hs, rq, rs, noCache, pw);
            if (handler.isExcuse()) {
                return handler.toExcuseVoid();
            }
            html.run(handler.value());
            pw.flush();
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (IOException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }

    public TGS_UnionExcuseVoid js(TGS_RunnableType1<TS_SURLHandler02ForPlainJs> js) {
        try (var pw = rs.getWriter()) {
            var handler = TS_SURLHandler02ForPlainJs.of(hs, rq, rs, noCache, pw);
            if (handler.isExcuse()) {
                return handler.toExcuseVoid();
            }
            js.run(handler.value());
            pw.flush();
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (IOException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }
}
