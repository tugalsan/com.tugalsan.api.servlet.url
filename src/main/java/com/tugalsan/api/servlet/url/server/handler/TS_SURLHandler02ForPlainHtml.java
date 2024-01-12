package com.tugalsan.api.servlet.url.server.handler;

import com.tugalsan.api.file.html.client.TGS_FileHtmlText;
import com.tugalsan.api.file.html.client.TGS_FileHtmlUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.string.client.TGS_StringUtils;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import com.tugalsan.api.url.client.TGS_Url;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TS_SURLHandler02ForPlainHtml extends TS_SURLHandler02ForPlainAbstract {

    final private static TS_Log d = TS_Log.of(TS_SURLHandler02ForPlainHtml.class);

    private TS_SURLHandler02ForPlainHtml(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, PrintWriter pw) {
        super(hs, rq, rs, noCache, pw);
        TGS_UnSafe.run(() -> {
            rq.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setCharacterEncoding(StandardCharsets.UTF_8.name());
            rs.setContentType("text/html; charset=" + StandardCharsets.UTF_8.name());
        });
    }

    public static TS_SURLHandler02ForPlainHtml of(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, PrintWriter pw) {
        return new TS_SURLHandler02ForPlainHtml(hs, rq, rs, noCache, pw);
    }

    public void html_error_msg(PrintWriter pw, CharSequence text) {
        println(pw, text);
        println(pw, TGS_FileHtmlUtils.endLines(true));
    }

    public void html_error_msg(PrintWriter pw, CharSequence text, CharSequence browserTitle, CharSequence favIcon, CharSequence optionalCustomDomain) {
        println(pw, TGS_FileHtmlUtils.beginLines(browserTitle, true, false, 5, 5, favIcon, true, optionalCustomDomain));
        html_error_msg(pw, text);
    }

    public void addHTML_HeaderBR(PrintWriter pw, CharSequence text) {
        println(pw, TGS_StringUtils.concat("<h3>", text, "</h3><br/>"));
    }

    public void addHTML_HeaderBR(PrintWriter pw, TGS_FileHtmlText text) {
        println(pw, TGS_StringUtils.concat("<h3>", text.toString(), "</h3><br/>"));
    }

    public void addHTML_P(PrintWriter pw, String text) {
        println(pw, TGS_StringUtils.concat("<p>", text, "</p>"));
    }

    public void addHTML_P(PrintWriter pw, TGS_FileHtmlText text) {
        println(pw, TGS_StringUtils.concat("<p>", text.toString(), "</p>"));
    }

    public void addHTML_TextBR(PrintWriter pw, List<String> texts) {
        texts.stream().forEachOrdered(s -> println(pw, s + "<br/>"));
    }

    public void addHTML_TextBR(PrintWriter pw, CharSequence text) {
        println(pw, text + "<br/>");
    }

    public void addHTML_TextBR(PrintWriter pw, TGS_FileHtmlText text) {
        println(pw, text.toString() + "<br/>");
    }

    public void addHTML_BR(PrintWriter pw, int count) {
        IntStream.range(0, count).forEachOrdered(i -> println(pw, "<br/>"));
    }

    public void addHTML_ImgBR(PrintWriter pw, TGS_Url source, Integer width, Integer height) {
        println(pw, "<img src=\"" + source + "\""
                + (width == null ? "" : TGS_StringUtils.concat(" width=\"", String.valueOf(width), "\""))
                + (height == null ? "" : TGS_StringUtils.concat(" height=\"", String.valueOf(height), "\""))
                + "/><br/>"
        );
    }

    public void addHTML_Validator(PrintWriter pw, CharSequence formName, CharSequence funcName,
            CharSequence[] varNames, CharSequence[] varLables, CharSequence errorNull, CharSequence errorMax, int maxChar) {
        println(pw, "<script>");
        println(pw, "function " + funcName + "() {");
        IntStream.range(0, varNames.length).forEachOrdered(i -> {
            var sb = new StringBuilder();
            sb.append("var p=document.forms[\"").append(formName).append("\"][\"").append(varNames[i]).append("\"].value;");
            sb.append("if (p==null || p==\"\") {alert(\"").append(errorNull).append(" -> (").append(varLables[i]).append(")\"); return false;}");
            sb.append("if (p.length > ").append(maxChar).append(") {alert(\"").append(errorMax).append(" -> (").append(varLables[i]).append(".maxChar: + ").append(maxChar).append(")\"); return false;}");
            println(pw, sb.toString());
        });
        println(pw, "}");
        println(pw, "</script>");
    }

    public void addHTML_LinkBR(PrintWriter pw, CharSequence text, TGS_Url url) {
        var html = TGS_StringUtils.concat("<a href=\"", url.toString(), "\">", text.toString(), "</a><br/>");
        d.ce("addHTML_LinkBR", html);
        println(pw, html);
    }

    public void addHTML_Hidden(PrintWriter pw, CharSequence label, CharSequence value) {
        println(pw, TGS_StringUtils.concat("<input id=\"", label, "\" name=\"", label, "\" type=\"hidden\" value=\"", value, "\">"));
    }
}
