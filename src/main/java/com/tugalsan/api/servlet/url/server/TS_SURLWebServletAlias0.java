package com.tugalsan.api.servlet.url.server;

import javax.servlet.annotation.*;
import javax.servlet.http.*;
import com.tugalsan.api.servlet.url.client.*;

@WebServlet("/" + TGS_SURLUtils.LOC_NAME_ALIAS0)//AS IN "/sh"
@MultipartConfig(//for TS_LibFileUploadUtils.upload that uses Apache.commons
        fileSizeThreshold = 1024 * 1024 * TS_SURLWebServlet.UPLOAD_MB_LIMIT_MEMORY,
        maxFileSize = 1024 * 1024 * TS_SURLWebServlet.UPLOAD_MB_LIMIT_FILE,
        maxRequestSize = 1024 * 1024 * TS_SURLWebServlet.UPLOAD_MB_LIMIT_REQUESTBALL,
        location = "/" + TGS_SURLUtils.LOC_NAME//means C:/bin/tomcat/home/work/Catalina/localhost/spi-xxx/u (do create it)
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
