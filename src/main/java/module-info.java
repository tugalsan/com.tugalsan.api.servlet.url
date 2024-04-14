module com.tugalsan.api.servlet.url {
    requires javax.servlet.api;
    requires java.desktop;
    requires com.tugalsan.api.url;
    requires com.tugalsan.api.validator;
    requires com.tugalsan.api.callable;
    requires com.tugalsan.api.runnable;
    requires com.tugalsan.api.file.common;
    requires com.tugalsan.api.file.txt;
    requires com.tugalsan.api.file.json;
    requires com.tugalsan.api.file;
    requires com.tugalsan.api.log;
    requires com.tugalsan.api.union;
    requires com.tugalsan.api.coronator;
    requires com.tugalsan.api.stream;
    requires com.tugalsan.api.thread;
    requires com.tugalsan.api.list;
    requires com.tugalsan.api.network;
    requires com.tugalsan.api.string;
    requires com.tugalsan.api.time;
    requires com.tugalsan.api.file.html;
    exports com.tugalsan.api.servlet.url.client;
    exports com.tugalsan.api.servlet.url.server;
    exports com.tugalsan.api.servlet.url.server.handler;
}
