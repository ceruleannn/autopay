package com.autopay.autopay.utils;

import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class UrlUtils {

    @SneakyThrows
    public static String mapToParamString(Map<String, String> map) {
        StringBuilder data = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (StringUtils.hasLength(entry.getValue())){
                data.append("&").append(URLEncoder.encode(entry.getKey(), "UTF-8")).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        }

        return  data.toString().replaceFirst("&", "");
    }


    public static Map<String, String> getQueryMap(String query) {
        query = query.substring(query.indexOf("?") + 1 , query.length());
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();

        for (String param : params) {
            int firstEqualIndex = param.indexOf("=");
            String name = param.substring(0, firstEqualIndex);
            String value = param.substring(firstEqualIndex + 1 , param.length());
            map.put(name, value);
        }
        return map;
    }
}
