package com.tugalsan.api.servlet.url.server.handler;

import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.network.server.TS_NetworkIPUtils;
import com.tugalsan.api.servlet.url.client.TGS_SURLUtils;
import com.tugalsan.api.string.client.TGS_StringUtils;
import com.tugalsan.api.time.client.TGS_Time;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.url.client.TGS_Url;
import com.tugalsan.api.url.client.TGS_UrlQueryUtils;
import com.tugalsan.api.url.server.TS_UrlServletRequestUtils;
import com.tugalsan.api.url.server.TS_UrlUtils;
import java.util.Arrays;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TS_SURLHandler02ForAbstract {

    final private static TS_Log d = TS_Log.of(TS_SURLHandler02ForAbstract.class);

    protected TS_SURLHandler02ForAbstract(HttpServlet hs, HttpServletRequest rq, HttpServletResponse rs, boolean noCache) {
        this.hs = hs;
        this.rq = rq;
        this.rs = rs;
        this.noCache = noCache;
        //CACHE
        if (noCache) {
            rs.setHeader("Cache-Control", "private,no-cache,no-store");
        }
        //CREATE
        context = hs.getServletContext();
        u_clientIp = TS_NetworkIPUtils.getIPClient(rq);
        url = TS_UrlUtils.toUrl(rq);
        u_servletName = TS_UrlServletRequestUtils.getParameterValue(rq, TGS_SURLUtils.PARAM_SERVLET_NAME(), true);
        if (u_servletName.isExcuse()) {
            u_servletName = TS_UrlServletRequestUtils.getParameterValue(rq, TGS_SURLUtils.PARAM_SERVLET_NAME_ALIAS0(), true);
        }
    }
    public HttpServlet hs;
    public HttpServletRequest rq;
    public HttpServletResponse rs;
    public boolean noCache;
    public ServletContext context;
    public TGS_UnionExcuse<String> u_clientIp;
    public TGS_UnionExcuse<String> u_servletName;
    public TGS_Url url;

    //GET PARAMETER-----------------------------------------------------------------------------------------
    public TGS_UnionExcuse<String> getParameterFromUrlSafe64(CharSequence paramName) {
        var paramValue = getParameter(paramName);
        if (paramValue.isExcuse()) {
            return paramValue.toExcuse();
        }
        return TGS_UrlQueryUtils.param64UrlSafe_2_readable(paramValue.value());
    }

    @Deprecated
    public TGS_UnionExcuse<String> getParameter(CharSequence paramName) {
        if (TGS_StringUtils.isNullOrEmpty(paramName)) {
            return TGS_UnionExcuse.ofExcuse(d.className, "getParameter", "TGS_StringUtils.isNullOrEmpty(paramName)");
        }
        return TS_UrlServletRequestUtils.getParameterValue(rq, paramName, true);
    }

    @Deprecated
    public TGS_UnionExcuse<String> getParameter(CharSequence paramName, CharSequence[] choices) {
        if (TGS_StringUtils.isNullOrEmpty(paramName)) {
            return TGS_UnionExcuse.ofExcuse(d.className, "getParameter", "TGS_StringUtils.isNullOrEmpty(paramName)");
        }
        var paramValue = TS_UrlServletRequestUtils.getParameterValue(rq, paramName, true);
        if (paramValue.isExcuse()) {
            return paramValue;
        }
        for (var s : choices) {
            if (s.toString().equals(paramValue.value())) {
                return paramValue;
            }
        }
        return TGS_UnionExcuse.ofExcuse(d.className, "getParameter", "Parameter " + paramName + " is not in the list of choices: " + Arrays.toString(choices));
    }

    @Deprecated
    public TGS_UnionExcuse<Boolean> getParameterBoolean(CharSequence paramName) {
        if (TGS_StringUtils.isNullOrEmpty(paramName)) {
            return TGS_UnionExcuse.ofExcuse(d.className, "getParameterBoolean", "TGS_StringUtils.isNullOrEmpty(paramName)");
        }
        var paramValue = getParameter(paramName, new String[]{"true", "false"});
        if (paramValue.isExcuse()) {
            return paramValue.toExcuse();
        }
        return TGS_UnionExcuse.of(Boolean.valueOf(paramValue.value()));
    }

    @Deprecated
    public TGS_UnionExcuse<Integer> getParameterInteger(CharSequence paramName) {
        if (TGS_StringUtils.isNullOrEmpty(paramName)) {
            return TGS_UnionExcuse.ofExcuse(d.className, "getParameterInteger", "TGS_StringUtils.isNullOrEmpty(paramName)");
        }
        var paramValue = getParameter(paramName);
        if (paramValue.isExcuse()) {
            return paramValue.toExcuse();
        }
        return TGS_UnionExcuse.of(Integer.valueOf(paramValue.value()));
    }

    @Deprecated
    public TGS_UnionExcuse<Long> getParameterLong(CharSequence paramName) {
        if (TGS_StringUtils.isNullOrEmpty(paramName)) {
            return TGS_UnionExcuse.ofExcuse(d.className, "getParameterLong", "TGS_StringUtils.isNullOrEmpty(paramName)");
        }
        var paramValue = getParameter(paramName);
        if (paramValue.isExcuse()) {
            return paramValue.toExcuse();
        }
        return TGS_UnionExcuse.of(Long.valueOf(paramValue.value()));
    }

    @Deprecated
    public TGS_UnionExcuse<TGS_Time> getParameterDate(CharSequence paramName) {
        if (TGS_StringUtils.isNullOrEmpty(paramName)) {
            return TGS_UnionExcuse.ofExcuse(d.className, "getParameterDate", "TGS_StringUtils.isNullOrEmpty(paramName)");
        }
        var paramValue = getParameter(paramName);
        if (paramValue.isExcuse()) {
            return paramValue.toExcuse();
        }
        return TGS_Time.ofDate(paramValue.value());
    }

//    //ERROR-HANDLER---------------------------------------------------------------
//    final public void throwError(String text) {
//        TGS_UnSafe.thrw(d.className, "throwError(String text)", text);
//    }
//
//    final public void throwError(Throwable t) {
//        TGS_UnSafe.thrw(t);
//    }
//
//    final public void throwError(CharSequence className, CharSequence funcName, Object errorContent) {
//        TGS_UnSafe.thrw(className, funcName, errorContent);
//    }
}
