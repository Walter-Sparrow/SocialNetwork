package com.dataart.secondmonth.utils;

public class CommonURLUtils {

    public static String addHttpToUrlString(String url) {
        return url.startsWith("https://") ? url : "https://".concat(url);
    }

}
