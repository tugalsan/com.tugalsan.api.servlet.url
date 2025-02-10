package com.tugalsan.api.servlet.url.server.handler;

import com.tugalsan.api.function.client.maythrow.checkedexceptions.TGS_FuncMTCEUtils;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TS_SURLHandler02ForFileImg extends TS_SURLHandler02ForAbstract {

//    final private static TS_Log d = TS_Log.of(TS_SURLHandler02ForFilePng.class);

    private TS_SURLHandler02ForFileImg(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, String formatName) {
        super(hs, rq, rs, noCache);
        TGS_FuncMTCEUtils.run(() -> {
            rq.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setContentType("image/" + formatName);
        });
    }

    protected static TS_SURLHandler02ForFileImg of(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, String formatName) {
        return new TS_SURLHandler02ForFileImg(hs, rq, rs, noCache, formatName);
    }
}
