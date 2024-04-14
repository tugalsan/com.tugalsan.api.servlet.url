package com.tugalsan.api.servlet.url.server.handler;

import com.tugalsan.api.union.client.TGS_UnionExcuse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TS_SURLHandler02ForPlainJs extends TS_SURLHandler02ForPlainAbstract {

//    final private static TS_Log d = TS_Log.of(TS_SURLHandler02ForPlainJs.class);
    private TS_SURLHandler02ForPlainJs(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, PrintWriter pw) {
        super(hs, rq, rs, noCache, pw);
    }

    protected static TGS_UnionExcuse<TS_SURLHandler02ForPlainJs> of(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, PrintWriter pw) {
        try {
            var o = new TS_SURLHandler02ForPlainJs(hs, rq, rs, noCache, pw);
            if (o.u_clientIp.isExcuse()) {
                return o.u_clientIp.toExcuse();
            }
            if (o.u_servletName.isExcuse()){
                return o.u_servletName.toExcuse();
            }
            rq.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setContentType("text/javascript; charset=" + StandardCharsets.UTF_8.name());
            return TGS_UnionExcuse.of(o);
        } catch (UnsupportedEncodingException ex) {
            return TGS_UnionExcuse.ofExcuse(ex);
        }
    }
}
