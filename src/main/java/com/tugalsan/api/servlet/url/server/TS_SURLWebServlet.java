package com.tugalsan.api.servlet.url.server;

import java.util.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.list.client.*;
import com.tugalsan.api.servlet.url.client.*;
import com.tugalsan.api.unsafe.client.*;

@WebServlet("/" + TGS_SURLUtils.LOC_NAME)//AS IN "/u"
public class TS_SURLWebServlet extends HttpServlet {

    final private static TS_Log d = TS_Log.of(TS_SURLWebServlet.class.getSimpleName());

    @Override
    public void doGet(HttpServletRequest rq, HttpServletResponse rs) {
        call(this, rq, rs);
    }

    @Override
    protected void doPost(HttpServletRequest rq, HttpServletResponse rs) {
        call(this, rq, rs);
    }

    public static void call(HttpServlet servlet, HttpServletRequest rq, HttpServletResponse rs) {
        var shw = new TS_SURLHelper(servlet, rq, rs);
        TGS_UnSafe.execute(() -> {
            if (shw.servletName == null) {
                shw.throwError("servletName is null");
            } else {
                var si = TS_SURLExecutorList.get(shw.servletName);
                if (si == null) {
                    if (SKIP_ERRORS_FOR_SERVLETNAMES.stream().filter(sn -> Objects.equals(sn, shw.servletName)).findAny().isPresent()) {
                        shw.flushAndClose();
                        return;
                    }
                    TS_SURLExecutorList.SYNC.forEach(item -> {
                        d.ce("call", "-", item.value0);
                    });
                    shw.throwError("servletName not identified: [" + shw.servletName + "]");
                    shw.flushAndClose();
                    return;
                }
                si.value1.execute(shw);
            }
            shw.flushAndClose();
        }, e -> shw.handleError(e));
    }
    public static List<String> SKIP_ERRORS_FOR_SERVLETNAMES = TGS_ListUtils.of();
}
