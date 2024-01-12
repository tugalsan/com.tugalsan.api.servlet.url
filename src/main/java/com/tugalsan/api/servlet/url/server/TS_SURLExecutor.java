package com.tugalsan.api.servlet.url.server;

import com.tugalsan.api.runnable.client.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

abstract public class TS_SURLExecutor implements TGS_RunnableType3<HttpServlet, HttpServletRequest, HttpServletResponse> {

    abstract public String name();
}
