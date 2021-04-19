package com.autopay.autopay.core;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import java.net.Proxy;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JDSessionCore {

    //TODO REMOVE CONS STATIC
    @Data
    @NoArgsConstructor
    public static class JDSession{
        private String sessionId;
        private Map<String, String> cookies = new LinkedHashMap<>();
    }

    public final LinkedHashMap<String, JDSession> sessionMap = new LinkedHashMap<>();

    @PostConstruct
    public void init(){
//        //测试使用
//        List<Cookie> cookies = new ArrayList<>();
//        cookies.add(new Cookie("pt_key", "AAJgfO_HADB0kDR6ItpgBZLKJarcwxqGuX_-gTvZAZR49SZ-QI4U1IOBPsABx1vfWOwe8ruBhrI"));
//        cookies.add(new Cookie("pt_pin", "18219111805_p"));
//        cookies.add(new Cookie("unpl", "V2_ZzNtbUtVFEd8XEVTLEwJV2JUQVhLU0YXcg5EBH8cXAQzVxFbclRCFnUUR1FnGVQUZwYZWUVcRhBFCEdkeBBVAWMDE1VGZxBFLV0CFSNGF1wjU00zEQdEQiYAT1cpTVUGYlQbX0tUFxB9CkVUfkoMVmVQElxyZ0AVRQhHZH8aWwJhAhpdRGdzEkU4dlF%2bGF0BYzMTbUNnAUEpDEFQex1aSGMAFVpEVksVczhHZHg%3d"));
//        createSession(cookies);
    }

    public JDSession randomSession(){
        int size = sessionMap.size();
        if (size == 0){
            return null;
        }

        Random random = new Random();
        String key = new ArrayList<>(sessionMap.keySet()).get(random.nextInt(size));
        return sessionMap.get(key);
    }

    public String createSession(List<Cookie> cookies){

        String pt_pin = checkExist(cookies);

        JDSession session = new JDSession();
        session.setSessionId(pt_pin);
        for (Cookie cookie : cookies) {
            session.getCookies().put(cookie.getName(), cookie.getValue());
        }
        //session.getCookies().put("3AB9D23F7A4B3C9B", session.getDeviceId());
        sessionMap.put(session.getSessionId(), session);

        return session.getSessionId();
    }

    public JDSession getSession(String sessionId){
        return sessionMap.get(sessionId);
    }

    public JDSession removeSession(String sessionId){
        return sessionMap.remove(sessionId);
    }

    private String checkExist(List<Cookie> cookies){

        String pt_pin = cookies.stream().collect(Collectors.toMap(Cookie::getName, Cookie::getValue)).get("pt_pin");

        String expireSessionId = null;
        for (JDSession session : sessionMap.values()) {
            if (Objects.equals(session.getCookies().get("pt_pin"), pt_pin)){
                expireSessionId = session.getSessionId();
                break;
            }
        }
        
        if (StringUtils.hasLength(expireSessionId)){
            sessionMap.remove(expireSessionId);
        }
        return pt_pin;
    }

}
