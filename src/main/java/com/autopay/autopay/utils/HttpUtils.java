package com.autopay.autopay.utils;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpUtils {


    public static List<Header> cookieMap2Headers(Map<String, String> cookieMap){
        return  cookieMap.entrySet().stream().map(e -> new BasicHeader(e.getKey(), e.getValue())).collect(Collectors.toList());
    }
}
