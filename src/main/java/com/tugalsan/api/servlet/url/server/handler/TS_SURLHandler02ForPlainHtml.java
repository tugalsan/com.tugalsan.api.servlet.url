package com.tugalsan.api.servlet.url.server.handler;

import module com.tugalsan.api.file.common;
import module com.tugalsan.api.file.html;
import module com.tugalsan.api.log;
import module com.tugalsan.api.string;
import module com.tugalsan.api.function;
import module com.tugalsan.api.url;
import module javax.servlet.api;
import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

public class TS_SURLHandler02ForPlainHtml extends TS_SURLHandler02ForPlainAbstract {

    final private static TS_Log d = TS_Log.of(TS_SURLHandler02ForPlainHtml.class);

    private TS_SURLHandler02ForPlainHtml(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, PrintWriter pw) {
        super(hs, rq, rs, noCache, pw);
        TGS_FuncMTCUtils.run(() -> {
            rq.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setContentType("text/html; charset=" + StandardCharsets.UTF_8.name());
        });
    }

    protected static TS_SURLHandler02ForPlainHtml of(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, PrintWriter pw) {
        return new TS_SURLHandler02ForPlainHtml(hs, rq, rs, noCache, pw);
    }

    public void html_error_msg(CharSequence text) {
        println(text);
        println(TGS_FileHtmlUtils.endLines(true));
    }

    public void html_error_msg(CharSequence text, CharSequence browserTitle, TGS_FileCommonFavIcon favIcon, TGS_Url bootLoaderJs) {
        println(TGS_FileHtmlUtils.beginLines(browserTitle, false, 5, 5, favIcon, true, bootLoaderJs));
        html_error_msg(text);
    }

    public void addHTML_HeaderBR(CharSequence text) {
        println(TGS_StringUtils.cmn().concat("<h3>", text, "</h3><br/>"));
    }

    public void addHTML_HeaderBR(TGS_FileHtmlText text) {
        println(TGS_StringUtils.cmn().concat("<h3>", text.toString(), "</h3><br/>"));
    }

    public void addHTML_P(String text) {
        println(TGS_StringUtils.cmn().concat("<p>", text, "</p>"));
    }

    public void addHTML_P(TGS_FileHtmlText text) {
        println(TGS_StringUtils.cmn().concat("<p>", text.toString(), "</p>"));
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

    public void addHTML_HR() {
        println("<hr/>");
    }

    public void addHTML_ImgBR(TGS_Url source, Integer width, Integer height) {
        println("<img src=\"" + source + "\""
                + (width == null ? "" : TGS_StringUtils.cmn().concat(" width=\"", String.valueOf(width), "\""))
                + (height == null ? "" : TGS_StringUtils.cmn().concat(" height=\"", String.valueOf(height), "\""))
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

    public void addHTML_Validator(CharSequence formName, CharSequence funcName,
            CharSequence[] varNames, CharSequence[] varLables, CharSequence errorNull, CharSequence errorMax, int[] maxChar) {
        println("<script>");
        println("function " + funcName + "() {");
        IntStream.range(0, varNames.length).forEachOrdered(i -> {
            var sb = new StringBuilder();
            sb.append("var p=document.forms[\"").append(formName).append("\"][\"").append(varNames[i]).append("\"].value;");
            sb.append("if (p==null || p==\"\") {alert(\"").append(errorNull).append(" -> (").append(varLables[i]).append(")\"); return false;}");
            sb.append("if (p.length > ").append(maxChar[i]).append(") {alert(\"").append(errorMax).append(" -> (").append(varLables[i]).append(".maxChar: + ").append(maxChar[i]).append(")\"); return false;}");
            println(sb.toString());
        });
        println("}");
        println("</script>");
    }

    public void addHTML_LinkBR(CharSequence text, TGS_Url url) {
        var html = TGS_StringUtils.cmn().concat("<a href=\"", url.toString(), "\">", text.toString(), "</a><br/>");
        d.ci("addHTML_LinkBR", html);
        println(html);
    }

    public void addHTML_Hidden(CharSequence label, CharSequence value) {
        println(TGS_StringUtils.cmn().concat("<input id=\"", label, "\" name=\"", label, "\" type=\"hidden\" value=\"", value, "\">"));
    }
}
