package com.tugalsan.api.servlet.url.server.handler;

import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.union.client.TGS_UnionExcuseVoid;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TS_SURLHandler02ForFileDownload extends TS_SURLHandler02ForAbstract {

    final private static TS_Log d = TS_Log.of(TS_SURLHandler02ForFileDownload.class);

    private TS_SURLHandler02ForFileDownload(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache) {
        super(hs, rq, rs, noCache);
    }

    protected static TGS_UnionExcuse<TS_SURLHandler02ForFileDownload> of(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache) {
        var o = new TS_SURLHandler02ForFileDownload(hs, rq, rs, noCache);
        if (o.u_clientIp.isExcuse()) {
            return o.u_clientIp.toExcuse();
        }
        if (o.u_servletName.isExcuse()) {
            return o.u_servletName.toExcuse();
        }
        return TGS_UnionExcuse.of(o);
    }

    public static TGS_UnionExcuseVoid throwFileNotFound(HttpServletResponse rs, CharSequence msg) {
        try {
            rs.sendError(HttpServletResponse.SC_NOT_FOUND);
            return TGS_UnionExcuseVoid.ofVoid();
        } catch (IOException ex) {
            return TGS_UnionExcuseVoid.ofExcuse(ex);
        }
    }
}
