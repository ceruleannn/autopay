package com.autopay.autopay.core;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
