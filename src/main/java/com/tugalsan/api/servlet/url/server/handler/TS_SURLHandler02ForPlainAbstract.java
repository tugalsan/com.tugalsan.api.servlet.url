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
    private volatile PrintWriter printWriter;

    final public boolean flushAndClose() {
        if (printWriter == null) {
            return false;
        }
        printWriter.flush();
        printWriter.close();
        printWriter = null;
        return true;
    }

    final public boolean flushAndContinue() {
        if (printWriter == null) {
            return false;
        }
        printWriter.flush();
        return true;
    }

    final public boolean print(CharSequence s) {
        if (printWriter == null) {
            d.cr("print", "printWriter closed already! for->", s);
            return false;
        }
        printWriter.write(s.toString());
        return true;
    }

    final public boolean println() {
        return print("\n");
    }

    final public boolean println(CharSequence s) {
        return print(s) && println();
    }

    public boolean println(Throwable t) {
        d.ce("handleError", "url", url);
        d.ct("handleError", t);
        if (!print("ERROR: ")) {
            return false;
        }
        if (!println(t.getMessage())) {
            return false;
        }
        return !Arrays.stream(t.getStackTrace())
                .map(ste -> println(ste.toString()))
                .filter(bool -> !bool)
                .findAny().isPresent();
    }

    public boolean println(List<String> lst) {
        return !lst.stream()
                .map(str -> println(str))
                .filter(bool -> !bool)
                .findAny().isPresent();
    }
}
