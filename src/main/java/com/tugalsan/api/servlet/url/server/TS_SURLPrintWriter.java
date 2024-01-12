package com.tugalsan.api.servlet.url.server;

import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.servlet.url.server.handler.TS_SURLHandler02ForPlainAbstract;
import java.io.IOException;
import java.io.PrintWriter;

public class TS_SURLPrintWriter {

    public static void useOnce(TS_SURLHandler02ForPlainAbstract plain, TGS_RunnableType1<PrintWriter> pw) {
        try {
            try (var printWriter = plain.rs.getWriter()) {
                pw.run(printWriter);
                printWriter.flush();
            }
        } catch (IOException ex) {
        }
    }
}
