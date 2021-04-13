package com.autopay.autopay.domain.web;

import lombok.Data;
import lombok.ToString;
import org.apache.http.Header;
import org.apache.http.cookie.Cookie;

import java.util.List;


@Data
@ToString
public class WebResponse {

    private int statusCode;
    private String location;
    private byte[] bytes;
    private String html;
    private List<Cookie> cookies;
    private List<Header> headers;

}
