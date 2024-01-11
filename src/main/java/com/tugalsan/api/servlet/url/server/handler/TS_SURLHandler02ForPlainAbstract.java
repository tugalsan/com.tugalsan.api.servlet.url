package com.tugalsan.api.servlet.url.server.handler;

import com.tugalsan.api.log.server.TS_Log;
import static com.tugalsan.api.servlet.url.server.TS_SURLHelper.DISABLE_VERBOSE_ERROR_FOR_printWriter_FOR_IllegalStateException;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
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

    //BASIC-PRINTER---------------------------------------------------------------
    final public boolean flushAndContinue() {
        if (printWriterClosed) {
            return false;
        }
        var writer = getPrintWriter();
        if (writer == null) {
            return false;
        }
        writer.flush();
        return true;
    }
    
    final public boolean flushAndClose() {
        if (printWriterClosed) {
            return false;
        }
        var writer = getPrintWriter();
        if (writer == null) {
            return false;
        }
        printWriterClosed = Boolean.TRUE;
        writer.flush();
        writer.close();
        return true;
    }
    
    private PrintWriter getPrintWriter() {
        return TGS_UnSafe.call(() -> {
            if (printWriter == null) {
                printWriter = rs.getWriter();
            }
            if (printWriter == null) {
                printWriterClosed = true;
            }
            return printWriter;
        }, e -> {
            printWriterClosed = true;
            if (DISABLE_VERBOSE_ERROR_FOR_printWriter_FOR_IllegalStateException && e instanceof java.lang.IllegalStateException) {
                d.ce("getPrintWriter", "ERROR: java.lang.IllegalStateException detected", e.getMessage());
                return null;
            } else {
                return TGS_UnSafe.thrwReturns(e);
            }
        });//should throw!
    }
    private volatile PrintWriter printWriter;
    private volatile boolean printWriterClosed = false;
    
    final public boolean print(CharSequence s) {
        if (printWriterClosed) {
            d.cr("print", "printWriter closed already! for->", s);
            return false;
        }
        var writer = getPrintWriter();
        if (writer == null) {
            return false;
        }
        writer.write(s.toString());
        return true;
    }
    
    final public void println() {
        print("\n");
    }
    
    final public void println(CharSequence s) {
        if (print(s)) {
            println();
        }
    }
    
    public void println(Throwable t) {
        print("ERROR: ");
        println(t.getMessage());
        Arrays.stream(t.getStackTrace()).forEachOrdered(ste -> println(ste.toString()));
    }
    
    public void println(List<String> al) {
        al.stream().forEachOrdered(s -> println(s));
    }

    //BASIC-ERROR-HANDLER---------------------------------------------------------------
    public void handleError(Throwable t) {
        t.printStackTrace();
        d.ce("handleError", "url", url);
        t.printStackTrace();
        println("ERROR: " + t.toString());
        Arrays.stream(t.getStackTrace()).forEachOrdered(ste -> println(ste.toString()));
    }
    
}
