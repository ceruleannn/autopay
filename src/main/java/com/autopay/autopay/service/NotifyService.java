package com.autopay.autopay.service;



import com.autopay.autopay.domain.dto.PayInfo;
import com.autopay.autopay.domain.web.JDRequest;
import com.autopay.autopay.domain.web.WebResponse;
import com.autopay.autopay.utils.UrlUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.autopay.autopay.utils.OKHttpUtils.jdHttp;


@Service
public class NotifyService {

    private final static ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(200);
    private	final static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 200, 5, TimeUnit.SECONDS, queue, new ThreadPoolExecutor.DiscardOldestPolicy ());

    //TODO 数据库记录
    public void notify(PayInfo payInfo){

        Runnable notifyTask = () -> {
            int retry = 3;
            while (retry-- > 0){
                Map<String, String> map = new HashMap<>();
                map.put("order_no", payInfo.getOrderId());
                map.put("order_amount", new BigDecimal(payInfo.getPayprice()).multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString());

                String url = payInfo.getNotifyUrl() + "?" + UrlUtils.mapToParamString(map);
                JDRequest notify = new JDRequest(payInfo.getSession());
                notify.setCleanUrl(url);

                WebResponse queryResp = null;
                try {
                    queryResp = jdHttp(notify);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (queryResp != null && "success".equals(queryResp.getHtml())){
                    //TODO DB操作
                    break;
                }else {
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };

        threadPool.execute(notifyTask);
    }
}
