package com.tugalsan.api.servlet.url.server.handler;

import com.tugalsan.api.log.server.TS_Log;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

abstract public class TS_SURLHandler02ForPlainAbstract extends TS_SURLHandler02ForAbstract {

    final private static TS_Log d = TS_Log.of(TS_SURLHandler02ForPlainAbstract.class);

    public TS_SURLHandler02ForPlainAbstract(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, PrintWriter pw) {
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
