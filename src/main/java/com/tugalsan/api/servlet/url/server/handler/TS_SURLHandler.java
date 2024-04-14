package com.tugalsan.api.servlet.url.server.handler;

//AutoClosable Version of Helper
import com.tugalsan.api.callable.client.TGS_CallableType1;
import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.union.client.TGS_UnionExcuseVoid;
import java.awt.image.RenderedImage;
import java.nio.file.Path;
import javax.servlet.http.*;

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

    public TGS_UnionExcuseVoid download(TGS_CallableType1<TGS_UnionExcuse<Path>, TS_SURLHandler02ForFileDownload> download) {
        return permitNoCache().download(download);
    }

    public TGS_UnionExcuseVoid img(String formatName, TGS_CallableType1<RenderedImage, TS_SURLHandler02ForFileImg> img) {
        return permitNoCache().img(formatName, img);
    }

    public TGS_UnionExcuseVoid txt(TGS_RunnableType1<TS_SURLHandler02ForPlainText> txt) {
        return permitNoCache().txt(txt);
    }

    public TGS_UnionExcuseVoid css(TGS_RunnableType1<TS_SURLHandler02ForPlainCss> css) {
        return permitNoCache().css(css);
    }

    public TGS_UnionExcuseVoid html(TGS_RunnableType1<TS_SURLHandler02ForPlainHtml> html) {
        return permitNoCache().html(html);
    }

    public TGS_UnionExcuseVoid js(TGS_RunnableType1<TS_SURLHandler02ForPlainJs> js) {
        return permitNoCache().js(js);
    }
}
