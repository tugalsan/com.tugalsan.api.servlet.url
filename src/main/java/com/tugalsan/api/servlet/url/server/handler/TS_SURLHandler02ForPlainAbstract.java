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

    public TS_SURLHandler02ForPlainAbstract(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache) {
        super(hs, rq, rs, noCache);

    }

    final public void flush(PrintWriter pw) {
        pw.flush();
    }

    final public void print(PrintWriter pw, CharSequence s) {
        pw.print(s.toString());
    }

    final public void println(PrintWriter pw) {
        print(pw, "\n");
    }

    final public void println(PrintWriter pw, CharSequence s) {
        print(pw, s);
        println(pw);
    }

    public void println(PrintWriter pw, Throwable t) {
        d.ce("handleError", "url", url);
        d.ct("handleError", t);
        print(pw, "ERROR: ");
        println(pw, t.getMessage());
        println(pw, Arrays.stream(t.getStackTrace()).map(ste -> ste.toString()).toList());
    }

    public void println(PrintWriter pw, List<String> lst) {
        lst.stream().forEachOrdered(str -> println(pw, str));
    }
}
