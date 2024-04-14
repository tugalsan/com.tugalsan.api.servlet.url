package com.tugalsan.api.servlet.url.server.handler;

import com.tugalsan.api.union.client.TGS_UnionExcuse;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TS_SURLHandler02ForFileImg extends TS_SURLHandler02ForAbstract {

//    final private static TS_Log d = TS_Log.of(TS_SURLHandler02ForFilePng.class);
    private TS_SURLHandler02ForFileImg(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, String formatName) {
        super(hs, rq, rs, noCache);
    }

    protected static TGS_UnionExcuse<TS_SURLHandler02ForFileImg> of(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, String formatName) {
        try {
            var o = new TS_SURLHandler02ForFileImg(hs, rq, rs, noCache, formatName);
            if (o.u_clientIp.isExcuse()){
                return o.u_clientIp.toExcuse();
            }
            if (o.u_servletName.isExcuse()){
                return o.u_servletName.toExcuse();
            }
            rq.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setContentType("image/" + formatName);
            return TGS_UnionExcuse.of(o);
        } catch (UnsupportedEncodingException ex) {
            return TGS_UnionExcuse.ofExcuse(ex);
        }
    }
}
