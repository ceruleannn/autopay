package com.autopay.autopay.domain.web;

import com.autopay.autopay.constants.HttpReturnType;
import lombok.Data;
import org.apache.http.Header;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class WebRequest {

    protected final List<Header> headers = new ArrayList<>();
    protected final Map<String, String> urlParams = new LinkedHashMap<>();
    protected String body;
    protected String cleanUrl; //不带参数
    protected HttpMethod method = HttpMethod.GET;
    protected ContentType contentType = ContentType.APPLICATION_FORM_URLENCODED;
    protected HttpReturnType returnType = HttpReturnType.TEXT;
    protected int timeoutSeconds = 20;

    public void addReferer(String referer){
        this.getHeaders().add(new BasicHeader("referer", referer));
    }
    public void addUrlParams(Map<String, String> urlParams){
        this.urlParams.putAll(urlParams);
    }


}
