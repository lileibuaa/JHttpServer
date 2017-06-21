package com.lei.http;

import com.lei.def.Method;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by lei on 20/06/2017.
 * you can you up
 */
public class HttpParser {

    private static final Logger LOG = LogManager.getLogger(HttpParser.class);

    private Socket socket;
    private Map<String, String> headers;
    private Method method;
    private String url;
    private String protocol;

    public HttpParser(Socket socket) {
        this.socket = socket;
    }

    public void parse() {
        LOG.info("start parse");
        try (InputStream inputStream = socket.getInputStream()) {
            byte[] buffer = new byte[1024 * 8];
            int readLength = 0;
            int readCount = inputStream.read(buffer, readLength, buffer.length);
            if (readCount <= 0) {
                throw new SocketException("socket shutdown");
            }
            int headerEnd = 0;
            while (readCount > 0) {
                readLength += readCount;
                headerEnd = findHeaderEnd(buffer, readLength);
                if (headerEnd > 0) {
                    break;
                }
                readCount = inputStream.read(buffer, readLength, buffer.length - readLength);
            }
            if (headerEnd <= 0) {
                throw new Exception("parse header error");
            }
            parseHeader(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buffer, 0, readLength))));

            HttpHandler.newInstance().handle(socket.getOutputStream(), url, headers);
//            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseHeader(BufferedReader bufferedReader) throws Exception {
        String firstLine = bufferedReader.readLine();
        StringTokenizer stringTokenizer = new StringTokenizer(firstLine);
        String methodStr;
        if (stringTokenizer.hasMoreElements()) {
            methodStr = stringTokenizer.nextToken();
            method = Method.parse(methodStr);
        } else {
            throw new Exception("parse header error");
        }
        if (method == null) {
            throw new Exception("method " + methodStr + " not support");
        }
        LOG.info("method:\t" + method);
        if (stringTokenizer.hasMoreElements()) {
            url = stringTokenizer.nextToken();
        } else {
            throw new Exception("parse header error");
        }
        LOG.info("url:\t" + url);
        if (stringTokenizer.hasMoreElements()) {
            protocol = stringTokenizer.nextToken();
            LOG.info("protocol:\t" + protocol);
        } else {
            protocol = "HTTP/1.1";
            LOG.info("default protocol:\t" + protocol);
        }
        headers = new HashMap<>();
        String headerStr = bufferedReader.readLine();
        while (headerStr != null && !headerStr.trim().isEmpty()) {
            int splitIndex = headerStr.indexOf(':');
            if (splitIndex > 0) {
                headers.put(headerStr.substring(0, splitIndex).trim(),
                        headerStr.substring(splitIndex + 1).trim());
            }
            headerStr = bufferedReader.readLine();
        }
    }

    private int findHeaderEnd(byte[] buffer, int readLength) {
        int splitIndex = 0;
        while (splitIndex + 1 < readLength) {
            if (buffer[splitIndex] == '\n' && buffer[splitIndex + 1] == '\n') {
                return splitIndex + 2;
            }
            if (buffer[splitIndex] == '\r' && buffer[splitIndex + 1] == '\n' &&
                    splitIndex + 3 < readLength && buffer[splitIndex + 2] == '\r' && buffer[splitIndex + 3] == '\n') {
                return splitIndex + 4;
            }
            splitIndex++;
        }
        return 0;
    }

}
