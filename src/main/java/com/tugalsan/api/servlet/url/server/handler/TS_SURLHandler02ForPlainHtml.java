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

    protected static TS_SURLHandler02ForPlainHtml of(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache, PrintWriter pw) {
        return new TS_SURLHandler02ForPlainHtml(hs, rq, rs, noCache, pw);
    }

    public void html_error_msg(CharSequence text) {
        println(text);
        println(TGS_FileHtmlUtils.endLines(true));
    }

    public void html_error_msg(CharSequence text, CharSequence browserTitle, CharSequence favIcon, CharSequence optionalCustomDomain) {
        println(TGS_FileHtmlUtils.beginLines(browserTitle, true, false, 5, 5, favIcon, true, optionalCustomDomain));
        html_error_msg(text);
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

    public void addHTML_Hidden(CharSequence label, CharSequence value) {
        println(TGS_StringUtils.concat("<input id=\"", label, "\" name=\"", label, "\" type=\"hidden\" value=\"", value, "\">"));
    }
}
