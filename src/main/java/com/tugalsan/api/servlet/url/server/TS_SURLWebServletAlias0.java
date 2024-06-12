package com.tugalsan.api.servlet.url.server;

import javax.servlet.annotation.*;
import javax.servlet.http.*;
import com.tugalsan.api.servlet.url.client.*;

@WebServlet("/" + TGS_SURLUtils.LOC_NAME_ALIAS0)//AS IN "/sh"
@MultipartConfig(//for TS_LibFileUploadUtils.upload that uses Apache.commons
        fileSizeThreshold = 1048576,
        maxFileSize = 20848820,
        maxRequestSize = 418018841,
        location = "/" + TGS_SURLUtils.LOC_NAME//means C:/bin/tomcat/home/work/Catalina/localhost/spi-table/u (do create it)
)
public class TS_SURLWebServletAlias0 extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest rq, HttpServletResponse rs) {
        TS_SURLWebServlet.call(this, rq, rs);
    }

    @Override
    protected void doPost(HttpServletRequest rq, HttpServletResponse rs) {
        TS_SURLWebServlet.call(this, rq, rs);
    }
}
