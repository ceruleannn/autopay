package com.autopay.autopay.utils;

import com.autopay.autopay.domain.web.WebRequest;
import com.autopay.autopay.domain.web.WebResponse;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpUtils {


    public static List<Header> cookieMap2Headers(Map<String, String> cookieMap){
        return  cookieMap.entrySet().stream().map(e -> new BasicHeader(e.getKey(), e.getValue())).collect(Collectors.toList());
    }


    public static void main(String[] args) throws IOException {
        WebRequest webRequest = new WebRequest();
        webRequest.setMethod(HttpMethod.GET);
        webRequest.setCleanUrl("http://api.faka168.com/api/gateway.jsonp");
        Map<String, String> map = new HashMap<>();
        map.put("wtype", "geetest");
        map.put("secretkey", "40287ddd9d1348f79166961c51158113");
        map.put("referer", "https://servicewechat.com/wx71a6af1f91734f18/61/page-frame.html");
        map.put("gt", "a53a5b6472732e344c776ba27d65302e");
        map.put("challenge", "bf6d914e6e0f17cea8de91655ae9a681");

        webRequest.getUrlParams().putAll(map);
       // webRequest.setBody(UrlUtils.mapToParamString(map));
        WebResponse webResponse  = OKHttpUtils.okhttp(webRequest, null);
        System.out.println(webResponse.getHtml());
    }
}
