module com.tugalsan.api.servlet.url {
    requires javax.servlet.api;
    requires java.desktop;
    requires com.tugalsan.api.url;
    requires com.tugalsan.api.validator;
    requires com.tugalsan.api.compiler;
    requires com.tugalsan.api.unsafe;
    requires com.tugalsan.api.executable;
    requires com.tugalsan.api.pack;
    requires com.tugalsan.api.log;
    requires com.tugalsan.api.thread;
    requires com.tugalsan.api.list;
    requires com.tugalsan.api.network;
    requires com.tugalsan.api.string;
    requires com.tugalsan.api.time;
    requires com.tugalsan.api.file.html;
    exports com.tugalsan.api.servlet.url.client;
    exports com.tugalsan.api.servlet.url.server;
}
