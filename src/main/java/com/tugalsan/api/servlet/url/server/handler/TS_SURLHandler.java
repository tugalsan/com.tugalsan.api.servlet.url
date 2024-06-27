package com.tugalsan.api.servlet.url.server.handler;

//AutoClosable Version of Helper
import com.tugalsan.api.function.client.TGS_Func_OutTyped_In1;
import com.tugalsan.api.function.client.TGS_Func_In1;

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

    public void download(TGS_Func_OutTyped_In1<Path, TS_SURLHandler02ForFileDownload> download) {
        permitNoCache().download(download);
    }
    
    public void img(String formatName, TGS_Func_OutTyped_In1<RenderedImage, TS_SURLHandler02ForFileImg> img) {
        permitNoCache().img(formatName, img);
    }
    
    public void txt(TGS_Func_In1<TS_SURLHandler02ForPlainText> txt) {
        permitNoCache().txt(txt);
    }
    
    public void css(TGS_Func_In1<TS_SURLHandler02ForPlainCss> css) {
        permitNoCache().css(css);
    }
    
    public void html(TGS_Func_In1<TS_SURLHandler02ForPlainHtml> html) {
        permitNoCache().html(html);
    }
    
    public void js(TGS_Func_In1<TS_SURLHandler02ForPlainJs> js) {
        permitNoCache().js(js);
    }
}
