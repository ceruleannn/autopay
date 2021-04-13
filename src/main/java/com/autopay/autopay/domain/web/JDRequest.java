package com.autopay.autopay.domain.web;

import com.autopay.autopay.core.JDSessionCore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class JDRequest extends WebRequest{
    final JDSessionCore.JDSession session;

    public JDRequest(JDSessionCore.JDSession session){
        this.session = session;
        this.getHeaders().addAll(commonHeaders());
    }

    private List<Header> commonHeaders(){

        List<Header> hs = new ArrayList<>();
        hs.add(new BasicHeader("accept","application/json, text/javascript, */*; q=0.01"));
        hs.add(new BasicHeader("x-requested-with","XMLHttpRequest"));
        hs.add(new BasicHeader("user-agent","Mozilla/5.0 (iPhone; CPU iPhone OS 14_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.1 Mobile/15E148 Safari/604.1"));

        //hs.add(new BasicHeader("accept-encoding","gzip"));
        hs.add(new BasicHeader("accept-language","zh-CN,zh;q=0.9"));
        return hs;
    }
}
