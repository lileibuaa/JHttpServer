package com.lei.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by lei on 17/06/2017.
 * you can you up
 */
public class TestIO {

    public static void main(String[] args) {
        byte[] bytes = "hello".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        byte[] newBytes = new byte[1024];
        try {
            inputStream.read(newBytes);
            System.out.println(new String(newBytes, "utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
