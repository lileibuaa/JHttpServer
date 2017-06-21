package com.lei;

import com.lei.http.HttpParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lei on 17/06/2017.
 * you can you up
 */
public class HttpServer {

    public static void main(String[] args) {
        long ts;
        try (ServerSocket serverSocket = new ServerSocket(9202)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new HttpParser(socket).parse();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DateFormat DATE_FORMAT = new SimpleDateFormat(
            "MM-dd HH:mm:ss SSS"
    );

    public static String toDate(long ts) {
        return DATE_FORMAT.format(new Date(ts));
    }

    public static String toDate() {
        return DATE_FORMAT.format(new Date(System.currentTimeMillis()));
    }
}
