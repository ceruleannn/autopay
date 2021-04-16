package com.autopay.autopay.utils;

import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class UrlUtils {

    public static String mapToParamString(Map<String, String> map) {
        return mapToParamString(map, true);
    }

    @SneakyThrows
    public static String mapToParamString(Map<String, String> map, boolean urlEncode) {
        StringBuilder data = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {

            if (urlEncode){
                String value = "";
                if (StringUtils.hasLength(entry.getValue())){
                    value = URLEncoder.encode(entry.getValue(), "UTF-8");
                }
                data.append("&").append(URLEncoder.encode(entry.getKey(), "UTF-8")).append("=").append(value);
            }else {
                data.append("&").append(entry.getKey()).append("=").append(entry.getValue());
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
