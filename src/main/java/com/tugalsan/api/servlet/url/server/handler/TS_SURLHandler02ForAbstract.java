package com.tugalsan.api.servlet.url.server.handler;

import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.network.server.TS_NetworkIPUtils;
import com.tugalsan.api.servlet.url.client.TGS_SURLUtils;
import com.tugalsan.api.string.client.TGS_StringUtils;
import com.tugalsan.api.time.client.TGS_Time;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
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
        clientIp = TS_NetworkIPUtils.getIPClient(rq);
        url = TS_UrlUtils.toUrl(rq);
        servletName = TS_UrlServletRequestUtils.getParameterValue(rq, TGS_SURLUtils.PARAM_SERVLET_NAME(), true);
        if (TGS_StringUtils.cmn().isNullOrEmpty(servletName)) {
            servletName = TS_UrlServletRequestUtils.getParameterValue(rq, TGS_SURLUtils.PARAM_SERVLET_NAME_ALIAS0(), true);
        }
    }
    public HttpServlet hs;
    public HttpServletRequest rq;
    public HttpServletResponse rs;
    public boolean noCache;
    public ServletContext context;
    public String servletName;
    public TGS_UnionExcuse<String> clientIp;
    public TGS_Url url;

    //GET PARAMETER-----------------------------------------------------------------------------------------
    public String getParameterFromUrlSafe64(CharSequence paramName) {
        return TGS_StringUtils.cmn().toNullIfEmpty(TGS_UrlQueryUtils.param64UrlSafe_2_readable(getParameter(paramName, false)));
    }

    @Deprecated
    public String getParameter(CharSequence paramName, boolean assure) {
        var paramValue = TS_UrlServletRequestUtils.getParameterValue(rq, paramName, true);
        if (TGS_StringUtils.cmn().isNullOrEmpty(paramName)) {
            if (assure) {
                throwError(TGS_StringUtils.cmn().concat("Parameter ", paramName, " is null"));
            }
            return null;
        }
//        d.ce("getParameter", "url/param/result", url, paramName, paramValue);
        return paramValue;
    }

    @Deprecated
    public String getParameter(CharSequence paramName, CharSequence[] assureChoices) {
        var paramValue = TS_UrlServletRequestUtils.getParameterValue(rq, paramName, true);
        if (TGS_StringUtils.cmn().isNullOrEmpty(paramName)) {
            throwError(TGS_StringUtils.cmn().concat("Parameter ", paramName, " is null"));
        }
        for (var s : assureChoices) {
            if (s.toString().equals(paramValue)) {
                return paramValue;
            }
        }
        throwError(TGS_StringUtils.cmn().concat("Parameter ", paramName, " is not in the list of assureChoices: ", Arrays.toString(assureChoices)));
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
                return TGS_UnSafe.thrw(e);
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
                return TGS_UnSafe.thrw(e);
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
                return TGS_UnSafe.thrw(e);
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
                return TGS_UnSafe.thrw(e);
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
}
