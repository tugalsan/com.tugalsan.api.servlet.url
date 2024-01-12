package com.tugalsan.api.servlet.url.server;

import com.tugalsan.api.runnable.client.*;
import com.tugalsan.api.servlet.url.server.handler.TS_SURLHandler;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

abstract public class TS_SURLExecutor implements TGS_RunnableType3<HttpServlet, HttpServletRequest, HttpServletResponse> {

    abstract public String name();

    protected void useOnceAsPlain(HttpServlet servlet, HttpServletRequest rq, HttpServletResponse rs, TGS_RunnableType1<PrintWriter> pw) {
        TS_SURLPrintWriter.useOnce(TS_SURLHandler.of(servlet, rq, rs).permitNoCache().plain(), pw);
    }

    protected void useOnceAsHtml(HttpServlet servlet, HttpServletRequest rq, HttpServletResponse rs, TGS_RunnableType1<PrintWriter> pw) {
        TS_SURLPrintWriter.useOnce(TS_SURLHandler.of(servlet, rq, rs).permitNoCache().html(), pw);
    }
}
