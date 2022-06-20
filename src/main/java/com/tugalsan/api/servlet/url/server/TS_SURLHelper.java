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
import com.tugalsan.api.list.client.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.network.server.*;
import com.tugalsan.api.servlet.url.client.*;
import com.tugalsan.api.string.client.*;
import com.tugalsan.api.time.client.*;
import com.tugalsan.api.url.client.*;
import com.tugalsan.api.url.server.*;

public class TS_SURLHelper {

    final private static TS_Log d = TS_Log.of(TS_SURLHelper.class.getSimpleName());

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

    public void addHTML_LinkBR(CharSequence text, CharSequence url) {
        println(TGS_StringUtils.concat("<a href=\"", url, "\">", text, "</a><br/>"));
    }

    //PRINT--------------------------------------------------------------------------------------------
    public void addHTML_Hidden(CharSequence label, CharSequence value) {
        println(TGS_StringUtils.concat("<input id=\"", label, "\" name=\"", label, "\" type=\"hidden\" value=\"", value, "\">"));
    }

    //CONTENT--------------------------------------------------------------------------------------------
    public final TS_SURLHelper compileForFile(Path filePath) {
        try {
            var mimeType = context.getMimeType(filePath.toString());
            setContentType(mimeType == null ? "application/octet-stream" : mimeType);
            rs.setCharacterEncoding("UTF-8");
            var contentLength = (int) filePath.toFile().length();
            d.ci("execute", "contentLength", contentLength);
            rs.setContentLength(contentLength);
            var encodedFileName = URLEncoder.encode(filePath.getFileName().toString(), "UTF-8").replace("+", "%20");
            rs.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", encodedFileName));
            return this;
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
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
        var mimeStr = mime.toString();
        if (isCompiledAsPlain()) {
            throw new RuntimeException(TS_SURLHelper.class.getSimpleName() + ".setContentType(mime).Already compiled as Plain");
        }
        if (isCompiledAsHtml()) {
            throw new RuntimeException(TS_SURLHelper.class.getSimpleName() + ".setContentType(mime).Already compiled as Html");
        }
        if (isCompiledAsFile()) {
            throw new RuntimeException(TS_SURLHelper.class.getSimpleName() + ".setContentType(mime).Already compiled as Mime");
        }
        try {
            if (mimeStr.startsWith("text/html")) {
                compiledAsHtml = true;
            } else if (mimeStr.startsWith("text/plain")) {
                compiledAsPlain = true;
            } else {
                compiledAsFile = true;
            }
            rs.setContentType(mimeStr);
            d.ci("setContentType", "mime", mimeStr);
            return this;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TS_SURLHelper(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs) {
        try {
            this.hs = hs;
            this.rq = rq;
            this.rs = rs;
            context = hs.getServletContext();
            rq.setCharacterEncoding(StandardCharsets.UTF_8.name());
            clientIp = TS_NetworkIPUtils.getIPClient(rq);
            url = TS_UrlUtils.toUrl(rq).url.toString();
            servletName = TS_UrlServletRequestUtils.getParameterValue(rq, TGS_SURLUtils.PARAM_SERVLET_NAME(), true);
            if (servletName == null || servletName.isEmpty()) {
                servletName = TS_UrlServletRequestUtils.getParameterValue(rq, TGS_SURLUtils.PARAM_SERVLET_NAME_ALIAS0(), true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public ServletContext context;
    public HttpServlet hs;
    public HttpServletRequest rq;
    public HttpServletResponse rs;
    public String clientIp, servletName, url;

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
        RuntimeException e1 = null;
        String paramValue = null;
        try {
            paramValue = getParameter(paramName, new String[]{"true", "false"});
        } catch (Exception e) {
            e1 = new RuntimeException(e);
        }
        if (paramValue == null) {
            if (assure) {
                throw e1;
            } else {
                return null;
            }
        }
        return Boolean.valueOf(paramValue);
    }

    @Deprecated
    public Integer getParameterInteger(CharSequence paramName, boolean assure) {
        RuntimeException e1 = null;
        String paramValue = null;
        try {
            paramValue = getParameter(paramName, assure);
        } catch (Exception e) {
            e1 = new RuntimeException(e);
        }
        if (paramValue == null) {
            if (assure) {
                throw e1;
            } else {
                return null;
            }
        }
        return Integer.valueOf(paramValue);
    }

    @Deprecated
    public Long getParameterLong(CharSequence paramName, boolean assure) {
        RuntimeException e1 = null;
        String paramValue = null;
        try {
            paramValue = getParameter(paramName, assure);
        } catch (Exception e) {
            e1 = new RuntimeException(e);
        }
        if (paramValue == null) {
            if (assure) {
                throw e1;
            } else {
                return null;
            }
        }
        return Long.valueOf(paramValue);
    }

    @Deprecated
    public TGS_Time getParameterDate(CharSequence paramName, boolean assure) {
        RuntimeException e1 = null;
        Integer paramValue = null;
        try {
            paramValue = getParameterInteger(paramName, assure);
        } catch (Exception e) {
            e1 = new RuntimeException(e);
        }
        if (paramValue == null) {
            if (assure) {
                throw e1;
            } else {
                return null;
            }
        }
        return new TGS_Time(paramValue);
    }

    //ERROR-HANDLER---------------------------------------------------------------
    final public void throwError(String text) {
        throwError(null, text);
    }

    final public void throwError(Throwable t) {
        throwError(t, null);
    }

    final public void throwError(Throwable t, CharSequence text) {
        if (t != null) {//fill error if empty
            if (text == null) {
                text = t.getMessage();
            } else {
                text = text + " - " + t.getMessage();
            }
        }
        throw new RuntimeException(text.toString(), t);//throw error & exception
    }

    //BASIC-PRINTER---------------------------------------------------------------
    final public void flush() {
        var exists = printWriter.get();
        if (exists != null) {
            exists.flush();
        }
    }

    final public void flushAndclose() {
        var exists = printWriter.get();
        if (exists != null) {
            exists.flush();
            exists.close();
        }
    }

    private PrintWriter getPrintWriter() {
        try {
            var exists = printWriter.get();
            if (exists == null) {
                printWriter.set(rs.getWriter());
                exists = printWriter.get();
            }
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    private TGS_ListSyncItem<PrintWriter> printWriter = new TGS_ListSyncItem();

    final public void print(CharSequence s) {
        getPrintWriter().write(s.toString());
    }

    final public void println() {
        print("\n");
    }

    final public void println(CharSequence s) {
        print(s);
        println();
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
        flushAndclose();
    }

    public TS_SURLHelper transferPng(BufferedImage image) {
        try ( var os = rs.getOutputStream()) {
            ImageIO.write(image, "png", os);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    //ERROR-MSG-HTML
    public void html_error_msg(CharSequence text) {
        println(text);
        println(TGS_FileHtmlUtils.endLines(true));
    }

    public void html_error_msg(CharSequence text, CharSequence browserTitle, CharSequence favIcon) {
        println(TGS_FileHtmlUtils.beginLines(browserTitle, true, false, 5, 5, favIcon, true));
        html_error_msg(text);
    }
}
