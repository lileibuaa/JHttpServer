package com.lei.def;

/**
 * Created by lei on 20/06/2017.
 * you can you up
 */
public enum Method {
    GET;

    public static Method parse(String symbol) {
        try {
            return Method.valueOf(symbol);
        } catch (Exception e) {
            return null;
        }
    }
}
