package com.tugalsan.api.servlet.url.server;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.awt.image.*;
import java.net.*;
import java.util.stream.*;
import javax.imageio.*;
import com.tugalsan.api.file.html.client.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.network.server.*;
import com.tugalsan.api.servlet.url.client.*;
import com.tugalsan.api.string.client.*;
import com.tugalsan.api.time.client.*;
import com.tugalsan.api.unsafe.client.*;
import com.tugalsan.api.url.client.*;
import com.tugalsan.api.url.server.*;

public class TS_SURLHelper {

    final private static TS_Log d = TS_Log.of(TS_SURLHelper.class);
    final public static boolean DISABLE_VERBOSE_ERROR_FOR_printWriter_FOR_IllegalStateException = true;

    public static boolean CONTENT_TYPE_PLAIN() {
        return true;
    }

    public static boolean CONTENT_TYPE_HTML() {
        return false;
    }

    //TXT----------------------------------------------------------------------------------------------
    public void addText_Line(Throwable t) {
        println(t.getMessage());
        Arrays.stream(t.getStackTrace()).forEachOrdered(ste -> println(ste.toString()));
    }

    public void addHTML_HeaderBR(CharSequence text) {
        println(TGS_StringUtils.concat("<h3>", text, "</h3><br/>"));
    }

    public void addHTML_HeaderBR(TGS_FileHtmlText text) {
        println(TGS_StringUtils.concat("<h3>", text.toString(), "</h3><br/>"));
    }

    public void addHTML_P(String text) {
        println(TGS_StringUtils.concat("<p>", text, "</p>"));
    }

    public void addHTML_P(TGS_FileHtmlText text) {
        println(TGS_StringUtils.concat("<p>", text.toString(), "</p>"));
    }

    public void addHTML_TextBR(List<String> texts) {
        texts.stream().forEachOrdered(s -> println(s + "<br/>"));
    }

    public void addHTML_TextBR(CharSequence text) {
        println(text + "<br/>");
    }

    public void addHTML_TextBR(TGS_FileHtmlText text) {
        println(text.toString() + "<br/>");
    }

    public void addHTML_BR(int count) {
        IntStream.range(0, count).forEachOrdered(i -> println("<br/>"));
    }

    public void addHTML_ImgBR(TGS_Url source, Integer width, Integer height) {
        println("<img src=\"" + source + "\""
                + (width == null ? "" : TGS_StringUtils.concat(" width=\"", String.valueOf(width), "\""))
                + (height == null ? "" : TGS_StringUtils.concat(" height=\"", String.valueOf(height), "\""))
                + "/><br/>"
        );
    }

    public void addHTML_Validator(CharSequence formName, CharSequence funcName,
            CharSequence[] varNames, CharSequence[] varLables, CharSequence errorNull, CharSequence errorMax, int maxChar) {
        println("<script>");
        println("function " + funcName + "() {");
        IntStream.range(0, varNames.length).forEachOrdered(i -> {
            var sb = new StringBuilder();
            sb.append("var p=document.forms[\"").append(formName).append("\"][\"").append(varNames[i]).append("\"].value;");
            sb.append("if (p==null || p==\"\") {alert(\"").append(errorNull).append(" -> (").append(varLables[i]).append(")\"); return false;}");
            sb.append("if (p.length > ").append(maxChar).append(") {alert(\"").append(errorMax).append(" -> (").append(varLables[i]).append(".maxChar: + ").append(maxChar).append(")\"); return false;}");
            println(sb.toString());
        });
        println("}");
        println("</script>");
    }

    public void addHTML_LinkBR(CharSequence text, TGS_Url url) {
        var html = TGS_StringUtils.concat("<a href=\"", url.toString(), "\">", text.toString(), "</a><br/>");
        d.ce("addHTML_LinkBR", html);
        println(html);
    }

    //PRINT--------------------------------------------------------------------------------------------
    public void addHTML_Hidden(CharSequence label, CharSequence value) {
        println(TGS_StringUtils.concat("<input id=\"", label, "\" name=\"", label, "\" type=\"hidden\" value=\"", value, "\">"));
    }

    //CONTENT--------------------------------------------------------------------------------------------
    public final TS_SURLHelper compileForFile(Path filePath) {
        return TGS_UnSafe.call(() -> {
            var mimeType = context.getMimeType(filePath.toString());
            setContentType(mimeType == null ? "application/octet-stream" : mimeType);
            rs.setCharacterEncoding("UTF-8");
            var contentLength = (int) filePath.toFile().length();
            d.ci("run", "contentLength", contentLength);
            rs.setContentLength(contentLength);
            var encodedFileName = URLEncoder.encode(filePath.getFileName().toString(), "UTF-8").replace("+", "%20");
            rs.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", encodedFileName));
            return this;
        }, e -> {
            e.printStackTrace();
            return TGS_UnSafe.thrwReturns(e);
        });
    }

    public final TS_SURLHelper compileForHtml() {
        setContentType("text/html; charset=" + StandardCharsets.UTF_8.name());
        rs.setCharacterEncoding("UTF-8");
        return this;
    }

    public final TS_SURLHelper compileForCSS() {
        setContentType("text/css; charset=" + StandardCharsets.UTF_8.name());
        rs.setCharacterEncoding("UTF-8");
        return this;
    }

    public final TS_SURLHelper compileForJS() {
        setContentType("text/javascript; charset=" + StandardCharsets.UTF_8.name());
        rs.setCharacterEncoding("UTF-8");
        return this;
    }

    public final TS_SURLHelper compileForPng() {
        setContentType("image/png");
        rs.setCharacterEncoding("UTF-8");
        return this;
    }

    public final TS_SURLHelper compileForPlain() {
        setContentType("text/plain; charset=" + StandardCharsets.UTF_8.name());
        rs.setCharacterEncoding("UTF-8");
        return this;
    }

    public final TS_SURLHelper addHeaderNoCache() {
        rs.setHeader("Cache-Control", "private,no-cache,no-store");
        return this;
    }

    public final boolean isCompiled() {
        return isCompiledAsPlain() || isCompiledAsHtml() || isCompiledAsFile();
    }

    public final boolean isCompiledAsPlain() {
        return compiledAsPlain;
    }
    private boolean compiledAsPlain = false;

    public final boolean isCompiledAsHtml() {
        return compiledAsHtml;
    }
    private boolean compiledAsHtml = false;

    public final boolean isCompiledAsFile() {
        return compiledAsFile;
    }
    private boolean compiledAsFile = false;

    private TS_SURLHelper setContentType(CharSequence mime) {
        return TGS_UnSafe.call(() -> {
            var mimeStr = mime.toString();
            if (isCompiledAsPlain()) {
                TGS_UnSafe.thrw(d.className, "setContentType(CharSequence mime)", "Already compiled as Plain");
            }
            if (isCompiledAsHtml()) {
                TGS_UnSafe.thrw(d.className, "setContentType(CharSequence mime)", "Already compiled as Html");
            }
            if (isCompiledAsFile()) {
                TGS_UnSafe.thrw(d.className, "setContentType(CharSequence mime)", "Already compiled as File");
            }
            if (mimeStr.startsWith("text/html")) {
                compiledAsHtml = true;
            } else if (mimeStr.startsWith("text/plain")) {
                compiledAsPlain = true;
            } else {
                compiledAsFile = true;
                printWriterClosed = Boolean.TRUE;
            }
            rs.setContentType(mimeStr);
            d.ci("setContentType", "mime", mimeStr);
            return this;
        });
    }

    public TS_SURLHelper(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs) {
        TGS_UnSafe.run(() -> {
            this.hs = hs;
            this.rq = rq;
            this.rs = rs;
            context = hs.getServletContext();
            rq.setCharacterEncoding(StandardCharsets.UTF_8.name());
            clientIp = TS_NetworkIPUtils.getIPClient(rq);
            url = TS_UrlUtils.toUrl(rq);
            servletName = TS_UrlServletRequestUtils.getParameterValue(rq, TGS_SURLUtils.PARAM_SERVLET_NAME(), true);
            if (TGS_StringUtils.isNullOrEmpty(servletName)) {
                servletName = TS_UrlServletRequestUtils.getParameterValue(rq, TGS_SURLUtils.PARAM_SERVLET_NAME_ALIAS0(), true);
            }
        });
    }
    public ServletContext context;
    public HttpServlet hs;
    public HttpServletRequest rq;
    public HttpServletResponse rs;
    public String clientIp, servletName;
    public TGS_Url url;

    //GET PARAMETER-----------------------------------------------------------------------------------------
    public String getParameterFromUrlSafe64(CharSequence paramName) {
        return TGS_StringUtils.toNullIfEmpty(TGS_UrlQueryUtils.param64UrlSafe_2_readable(getParameter(paramName, false)));
    }

    @Deprecated
    public String getParameter(CharSequence paramName, boolean assure) {
        var paramValue = TS_UrlServletRequestUtils.getParameterValue(rq, paramName, true);
        if (TGS_StringUtils.isNullOrEmpty(paramName)) {
            if (assure) {
                throwError(TGS_StringUtils.concat("Parameter ", paramName, " is null"));
            }
            return null;
        }
//        d.ce("getParameter", "url/param/result", url, paramName, paramValue);
        return paramValue;
    }

    @Deprecated
    public String getParameter(CharSequence paramName, CharSequence[] assureChoices) {
        var paramValue = TS_UrlServletRequestUtils.getParameterValue(rq, paramName, true);
        if (TGS_StringUtils.isNullOrEmpty(paramName)) {
            throwError(TGS_StringUtils.concat("Parameter ", paramName, " is null"));
        }
        for (var s : assureChoices) {
            if (s.toString().equals(paramValue)) {
                return paramValue;
            }
        }
        throwError(TGS_StringUtils.concat("Parameter ", paramName, " is not in the list of assureChoices: ", Arrays.toString(assureChoices)));
        return null;
    }

    @Deprecated
    public Boolean getParameterBoolean(CharSequence paramName, boolean assure) {
        return TGS_UnSafe.call(() -> {
            var paramValue = getParameter(paramName, new String[]{"true", "false"});
            if (paramValue == null) {
                return null;
            }
            return Boolean.valueOf(paramValue);
        }, e -> {
            if (assure) {
                return TGS_UnSafe.thrwReturns(e);
            }
            return null;
        });
    }

    @Deprecated
    public Integer getParameterInteger(CharSequence paramName, boolean assure) {
        return TGS_UnSafe.call(() -> {
            var paramValue = getParameter(paramName, assure);
            if (paramValue == null) {
                return null;
            }
            return Integer.valueOf(paramValue);
        }, e -> {
            if (assure) {
                return TGS_UnSafe.thrwReturns(e);
            }
            return null;
        });
    }

    @Deprecated
    public Long getParameterLong(CharSequence paramName, boolean assure) {
        return TGS_UnSafe.call(() -> {
            var paramValue = getParameter(paramName, assure);
            if (paramValue == null) {
                return null;
            }
            return Long.valueOf(paramValue);
        }, e -> {
            if (assure) {
                return TGS_UnSafe.thrwReturns(e);
            }
            return null;
        });
    }

    @Deprecated
    public TGS_Time getParameterDate(CharSequence paramName, boolean assure) {
        return TGS_UnSafe.call(() -> {
            var paramValue = getParameterLong(paramName, assure);
            if (paramValue == null) {
                return null;
            }
            return TGS_Time.ofDate(paramValue);
        }, e -> {
            if (assure) {
                return TGS_UnSafe.thrwReturns(e);
            }
            return null;
        });
    }

    //ERROR-HANDLER---------------------------------------------------------------
    final public void throwError(String text) {
        TGS_UnSafe.thrw(d.className, "throwError(String text)", text);
    }

    final public void throwError(Throwable t) {
        TGS_UnSafe.thrw(t);
    }

    final public void throwError(CharSequence className, CharSequence funcName, Object errorContent) {
        TGS_UnSafe.thrw(className, funcName, errorContent);
    }

    //BASIC-PRINTER---------------------------------------------------------------
    final public boolean flushAndContinue() {
        if (printWriterClosed || isCompiledAsFile()) {
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
        if (printWriterClosed || isCompiledAsFile()) {
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
        d.ce("handleError", "url", url);
        t.printStackTrace();
        if (isCompiledAsHtml()) {
            println("<b>");
        }
        println("ERROR: " + t.toString());
        Arrays.stream(t.getStackTrace()).forEachOrdered(ste -> println(ste.toString()));
        if (isCompiledAsHtml()) {
            println("</b>");
        }
        flushAndClose();
    }

    public TS_SURLHelper transferPng(BufferedImage image) {
        return TGS_UnSafe.call(() -> {
            try (var os = rs.getOutputStream()) {
                ImageIO.write(image, "png", os);
            }
            return this;
        });
    }

    //ERROR-MSG-HTML
    public void html_error_msg(CharSequence text) {
        println(text);
        println(TGS_FileHtmlUtils.endLines(true));
    }

    public void html_error_msg(CharSequence text, CharSequence browserTitle, CharSequence favIcon, CharSequence optionalCustomDomain) {
        println(TGS_FileHtmlUtils.beginLines(browserTitle, true, false, 5, 5, favIcon, true, optionalCustomDomain));
        html_error_msg(text);
    }
}
