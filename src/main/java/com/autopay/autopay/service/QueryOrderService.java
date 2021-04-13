package com.autopay.autopay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.autopay.autopay.core.JDSessionCore;
import com.autopay.autopay.domain.dto.PayInfo;
import com.autopay.autopay.domain.web.JDRequest;
import com.autopay.autopay.domain.web.WebResponse;
import com.autopay.autopay.utils.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.autopay.autopay.utils.OKHttpUtils.jdHttp;

@Service
@Slf4j
public class QueryOrderService {

    @Autowired
    NotifyService notifyService;

    private final Map<String, AtomicInteger> counter = new LinkedHashMap<>();

    private final Map<String, PayInfo> infoMap = new LinkedHashMap<>();

    public void addQuery(PayInfo payInfo){
        counter.put(payInfo.getOrderId(), new AtomicInteger(60));
        infoMap.put(payInfo.getOrderId(), payInfo);
    }

    @Scheduled(cron = "0/5 * * * * ? ") // 间隔5秒执行
    public void query(){

        Iterator<Map.Entry<String, PayInfo>> it = infoMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, PayInfo> entry = it.next();
            String orderId = entry.getKey();
            PayInfo payInfo =  entry.getValue();
            AtomicInteger count = counter.get(orderId);

            JDRequest query = new JDRequest(payInfo.getSession());
            query.setCleanUrl(payInfo.getQueryUrl());

            String location = "";
            try {
                WebResponse queryResp = jdHttp(query);
                location = queryResp.getLocation().trim();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (StringUtils.hasLength(location) && location.startsWith("https://payfinish.m.jd.com/cpay/finish/cashier-finish.html")){
                //支付成功
                String successOrderId = UrlUtils.getQueryMap(location).get("orderId");
                log.info("支付成功 orderId = " + successOrderId);
                it.remove();
                counter.remove(orderId);

                payInfo.setStatus(1);
                notifyService.notify(payInfo);

            }else {
                if (count.getAndDecrement() <= 0){ //计数器自减
                    //5分钟未支付, 支付失败
                    log.info("支付失败 orderId = " + payInfo.getOrderId());
                    it.remove();
                    counter.remove(orderId);
                }else {
                    log.info("等待支付 orderId = " + payInfo.getOrderId());
                }
            }
        }
    }
}
