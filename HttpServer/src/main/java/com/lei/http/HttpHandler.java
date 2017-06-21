package com.lei.http;

import com.google.common.base.Joiner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by lei on 20/06/2017.
 * you can you up
 */
public class HttpHandler {

    private final Logger LOG = LogManager.getLogger(HttpHandler.class);

    public static HttpHandler newInstance() {
        return new HttpHandler();
    }

    public void handle(OutputStream outputStream, String url, Map<String, String> headers) {
        LOG.info("start handle " + url + ", and headers is "
                + Joiner.on(", ").withKeyValueSeparator(" : ").join(headers));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
        out.append("HTTP/1.1 OK \r\n");
        out.append("Connection: keep-alive\r\n");
        out.append("\r\n");
        out.append("hello response");
        out.flush();
        out.close();
    }

}
