package com.tugalsan.api.servlet.url.server;

import com.tugalsan.api.runnable.client.*;
import com.tugalsan.api.servlet.url.server.handler.TS_SURLHandler;
import com.tugalsan.api.servlet.url.server.handler.TS_SURLHandler02ForPlainHtml;
import com.tugalsan.api.servlet.url.server.handler.TS_SURLHandler02ForPlainText;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

abstract public class TS_SURLExecutor implements TGS_RunnableType3<HttpServlet, HttpServletRequest, HttpServletResponse> {

    abstract public String name();

    protected void useOnceAsPlain(HttpServlet servlet, HttpServletRequest rq, HttpServletResponse rs, TGS_RunnableType2<TS_SURLHandler02ForPlainText, PrintWriter> pw) {
        var plain = TS_SURLHandler.of(servlet, rq, rs).permitNoCache().plain();
        TS_SURLPrintWriter.useOnce(plain, _pw -> pw.run(plain, _pw));
    }

    protected void useOnceAsHtml(HttpServlet servlet, HttpServletRequest rq, HttpServletResponse rs, TGS_RunnableType2<TS_SURLHandler02ForPlainHtml, PrintWriter> pw) {
        var html = TS_SURLHandler.of(servlet, rq, rs).permitNoCache().html();
        TS_SURLPrintWriter.useOnce(html, _pw -> pw.run(html, _pw));
    }
}
