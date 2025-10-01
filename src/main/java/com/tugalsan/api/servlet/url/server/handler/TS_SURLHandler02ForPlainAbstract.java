package com.tugalsan.api.servlet.url.server.handler;

import module com.tugalsan.api.log;
import module javax.servlet.api;
import java.io.*;
import java.util.*;

abstract public class TS_SURLHandler02ForPlainAbstract extends TS_SURLHandler02ForAbstract {

    final private static TS_Log d = TS_Log.of(TS_SURLHandler02ForPlainAbstract.class);

    protected TS_SURLHandler02ForPlainAbstract(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, PrintWriter pw) {
        super(hs, rq, rs, noCache);
        this.pw = pw;
    }
    final public PrintWriter pw;

    final public void flush() {
        pw.flush();
    }

    final public void print(CharSequence s) {
        pw.print(s.toString());
    }

    final public void println() {
        print("\n");
    }

    final public void println(CharSequence s) {
        print(s);
        println();
    }

    public void println(Throwable t) {
        d.ce("handleError", "url", url);
        d.ct("handleError", t);
        print("ERROR: ");
        println(t.getMessage());
        println(Arrays.stream(t.getStackTrace()).map(ste -> ste.toString()).toList());
    }

    public void println(List<String> lst) {
        lst.stream().forEachOrdered(str -> println(str));
    }
}
