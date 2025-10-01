package com.tugalsan.api.servlet.url.server.handler;

//AutoClosable Version of Helper
import module com.tugalsan.api.function;
import module java.desktop;
import module javax.servlet.api;
import java.nio.file.*;

public class TS_SURLHandler {

//    final private static TS_Log d = TS_Log.of(TS_SURLHandler.class);
    private TS_SURLHandler(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs) {
        this.hs = hs;
        this.rq = rq;
        this.rs = rs;
    }
    final public HttpServlet hs;
    final public HttpServletRequest rq;
    final public HttpServletResponse rs;

    public static TS_SURLHandler of(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs) {
        return new TS_SURLHandler(hs, rq, rs);
    }

    private TS_SURLHandler01WCachePolicy permitNoCache() {
        return TS_SURLHandler01WCachePolicy.of(hs, rq, rs, true);
    }

    public TS_SURLHandler01WCachePolicy permitCache() {
        return TS_SURLHandler01WCachePolicy.of(hs, rq, rs, false);
    }

    public void download(TGS_FuncMTU_OutTyped_In1<Path, TS_SURLHandler02ForFileDownload> download) {
        permitNoCache().download(download);
    }

    public void img(String formatName, TGS_FuncMTU_OutTyped_In1<RenderedImage, TS_SURLHandler02ForFileImg> img) {
        permitNoCache().img(formatName, img);
    }

    public void txt(TGS_FuncMTU_In1<TS_SURLHandler02ForPlainText> txt) {
        permitNoCache().txt(txt);
    }

    public void css(TGS_FuncMTU_In1<TS_SURLHandler02ForPlainCss> css) {
        permitNoCache().css(css);
    }

    public void html(TGS_FuncMTU_In1<TS_SURLHandler02ForPlainHtml> html) {
        permitNoCache().html(html);
    }

    public void js(TGS_FuncMTU_In1<TS_SURLHandler02ForPlainJs> js) {
        permitNoCache().js(js);
    }
}
