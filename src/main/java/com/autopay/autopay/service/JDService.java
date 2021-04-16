package com.autopay.autopay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.autopay.autopay.core.JDSessionCore;
import com.autopay.autopay.domain.dto.PayInfo;
import com.autopay.autopay.domain.exception.AppException;
import com.autopay.autopay.domain.params.LoginParams;
import com.autopay.autopay.domain.params.OrderParams;
import com.autopay.autopay.domain.response.JDLoginResponse;
import com.autopay.autopay.domain.response.JDOrderResponse;
import com.autopay.autopay.domain.web.JDRequest;
import com.autopay.autopay.domain.web.WebResponse;
import com.autopay.autopay.utils.OKHttpUtils;
import com.autopay.autopay.utils.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@Slf4j
public class JDService {

    @Autowired
    QueryOrderService queryOrderService;

    @Autowired
    JDSessionCore jdSessionCore;

    public JDLoginResponse login(LoginParams loginParams) throws IOException, InterruptedException {

        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("pt_key", loginParams.getPt_key()));
        cookies.add(new Cookie("pt_pin", loginParams.getPt_pin()));
        cookies.add(new Cookie("unpl", loginParams.getUnpl()));

        return new JDLoginResponse(jdSessionCore.createSession(cookies));
    }


    public JDOrderResponse submitOrder(JDSessionCore.JDSession session, OrderParams orderParams) throws IOException, AppException {
        if (orderParams.getOrderType() == 1){
            return submitOrderNormal(session, orderParams);
        }else if(orderParams.getOrderType() == 2){
            return submitOrderGame(session, orderParams);
        }

        throw new AppException("无效的订单类型:" + orderParams.getOrderType());
    }

    /**
     * 普通商品提交 用户未登录
     * @return
     */
    private JDOrderResponse submitOrderNormal(JDSessionCore.JDSession session, OrderParams orderParams) throws IOException, AppException {

        String skuId = orderParams.getProductId();

        JDRequest cartRequest = new JDRequest(session);
        cartRequest.setCleanUrl("https://wq.jd.com/deal/confirmorder/main");
        Map<String, String> cartMap = new LinkedHashMap<>();
        cartMap.put("sceneval", "2");
        cartMap.put("bid", "");
        cartMap.put("wdref", "https://item.m.jd.com/product/"+ skuId +".html?sceneval=2&jxsid=16177879653561707174");
        cartMap.put("scene", "jd");
        cartMap.put("isCanEdit", "1");
        cartMap.put("EncryptInfo", "");
        cartMap.put("Token", "");
        //cartMap.put("commlist", "12351982,,1,12351982,1,0,0");
        cartMap.put("locationid", "19-1601-3634");
        cartMap.put("type", "0");
        cartMap.put("lg", "0");
        cartMap.put("supm", "0");
        cartMap.put("favorablerate", "99");
        cartMap.put("jxsid", "16177879653561707174");
        cartRequest.getUrlParams().putAll(cartMap);
        cartRequest.getUrlParamsNoUrlEncode().put("commlist",  skuId + ",,1," + skuId +",1,0,0");

        WebResponse cartResponse = OKHttpUtils.jdHttp(cartRequest);

        String token2 = "";
        String traceId = "";
        String pattern3 = "\"traceId\":\"([\\S]*?)\",";
        Pattern r3 = Pattern.compile(pattern3);
        Matcher m3 = r3.matcher(cartResponse.getHtml());
        if (m3.find()) {
            traceId = m3.group(1);
        }
        String pattern4 = "\"token2\":\"([\\S]*?)\",";
        Pattern r4 = Pattern.compile(pattern4);
        Matcher m4 = r4.matcher(cartResponse.getHtml());
        if (m4.find( )) {
            token2 =  m4.group(1);
        }

       // https://wq.jd.com/deal/msubmit/confirm?paytype=0&paychannel=1&action=1&reg=1&type=0
        // &token2=F800B7F8971BD346BACA50E1F037682E&dpid=&skulist=12351982&scan_orig=&gpolicy=
        // &platprice=0
        // &ship=0|65|4|||0||1||2021-4-9|09:00-15:00|{%221%22:%221%22,%22161%22:%220%22,%22237%22:%221%22,%22278%22:%220%22,%2230%22:%221%22,%2235%22:%221%22}|1|||||0||0||0|2,-1998921520,937022322894225408,937022322973917184,101612_937022325157961728,101613_937022325225070593||
        // &pick=&savepayship=0&valuableskus=12351982,1,3490,4761&commlist=12351982,,1,12351982,1,0,0&sceneval=2&setdefcoupon=0
        // &r=0.6645454793318879&callback=confirmCbA&traceid=1156812766854997839

        JDRequest sumbitRequest = new JDRequest(session);
        sumbitRequest.setCleanUrl("https://wq.jd.com/deal/msubmit/confirm");
        sumbitRequest.addReferer(cartRequest.generateFullUrl());
        Map<String, String> submitMap = new LinkedHashMap<>();
        submitMap.put("paytype", "0");
        submitMap.put("paychannel", "1");
        submitMap.put("action", "1");
        submitMap.put("reg", "1");
        submitMap.put("type", "0");
        submitMap.put("token2", token2);
        submitMap.put("dpid", "");
        submitMap.put("skulist", skuId);
        submitMap.put("scan_orig", "");
        submitMap.put("gpolicy", "");
        submitMap.put("platprice", "0");
        submitMap.put("pick", "");
        submitMap.put("savepayship", "0");
        //submitMap.put("valuableskus", "12351982,1,3490,4761");
        //submitMap.put("commlist", "12351982,,1,12351982,1,0,0");
        submitMap.put("sceneval", "2");
        submitMap.put("setdefcoupon", "0");
        submitMap.put("r", "0.6645454793318879");
        submitMap.put("callback", "confirmCbA");
        submitMap.put("traceid", traceId);
        sumbitRequest.getUrlParams().putAll(submitMap);
        sumbitRequest.getUrlParamsNoUrlEncode().put("ship", "0|65|4|||0||1||2021-4-9|09:00-15:00|{\"1\":\"1\",\"161\":\"0\",\"237\":\"1\",\"278\":\"0\",\"30\":\"1\",\"35\":\"1\"}|1|||||0||0||0|2,-1998921520,937022322894225408,937022322973917184,101612_937022325157961728,101613_937022325225070593||".replaceAll("\"", "%22"));
        sumbitRequest.getUrlParamsNoUrlEncode().put("valuableskus", skuId + ",1,3490,4761");
        sumbitRequest.getUrlParamsNoUrlEncode().put("commlist", skuId + ",,1," + skuId + ",1,0,0");

        WebResponse sumbitResponse = OKHttpUtils.jdHttp(sumbitRequest);

        String dealId = "";
        String pattern5 = "\"dealId\":\"([\\S]*?)\",";
        Pattern r5 = Pattern.compile(pattern5);
        Matcher m5 = r5.matcher(sumbitResponse.getHtml());
        if (m5.find( )) {
            dealId =  m5.group(1);
        }

        //https://wq.jd.com/jdpaygw/jdappmpay?dealId=164544863296&ufc=&callback=jdappmpayCb&r=0.9922903267337776&sceneval=2
        // &backUrl=https%3A%2F%2Fwqs.jd.com%2Forder%2Fpaysuc.shtml%3Fsceneval%3D2%26jxsid%3D16183875537870393742%26dealId%3D164544863296%26gift_cid%3D4761%26gift_skuid%3D12016907%26gift_venderid%3D0%26normal%3D1%26fromPay%3D1%26ptag%3D7039.27.14
        // &setdefcoupon=0&traceid=1161277239330286879

        JDRequest payRequest = new JDRequest(session);
        payRequest.setCleanUrl("https://wq.jd.com/jdpaygw/jdappmpay");
        payRequest.addReferer(cartRequest.generateFullUrl());
        Map<String, String> payMap = new LinkedHashMap<>();
        payMap.put("dealId", dealId);
        payMap.put("ufc", "");
        payMap.put("callback", "jdappmpayCb");
        payMap.put("r", "0.9922903267337776");
        payMap.put("sceneval", "2");
        payMap.put("backUrl", "https://wqs.jd.com/order/paysuc.shtml?sceneval=2&jxsid=16183875537870393742&dealId=164544863296&gift_cid=4761&gift_skuid=12016907&gift_venderid=0&normal=1&fromPay=1&ptag=7039.27.14\n");
        payMap.put("setdefcoupon", "0");
        payMap.put("traceid", traceId);

        payRequest.getUrlParams().putAll(payMap);
        WebResponse  payResponse = OKHttpUtils.jdHttp(payRequest);
        String pattern2 = "\"jumpurl\":\"([\\S]*?)\"";
        Pattern r2 = Pattern.compile(pattern2);
        Matcher m2 = r2.matcher(payResponse.getHtml());
        String jumpurl = "";
        if (m2.find( )) {
            jumpurl =  m2.group(1);
        }

        if (!StringUtils.hasLength(jumpurl)){
            throw new AppException("session失效, 请重新登录");
        }

        String payId = UrlUtils.getQueryMap(jumpurl).get("payId");
        String appId = UrlUtils.getQueryMap(jumpurl).get("appId");

        JDRequest syt = new JDRequest(session);
        syt.setCleanUrl(jumpurl);
        WebResponse sytResponse = OKHttpUtils.jdHttp(syt);

        JDRequest paychannel = new JDRequest(session);
        paychannel.addReferer(jumpurl);
        Map<String, String> channelMap  = new LinkedHashMap<>();
        channelMap.put("lastPage", "");
        channelMap.put("appId", appId);
        channelMap.put("payId", payId);
        channelMap.put("_format_", "JSON");
        paychannel.setCleanUrl("https://pay.m.jd.com/newpay/index.action");
        paychannel.setBody(UrlUtils.mapToParamString(channelMap));
        paychannel.setMethod(HttpMethod.POST);
        WebResponse channelResponse = OKHttpUtils.jdHttp( paychannel);
        JSONObject channelJson = JSONObject.parseObject(channelResponse.getHtml());
        String orderId = channelJson.getJSONObject("payParamsObject").getString("orderId");
        String payprice = channelJson.getJSONObject("payParamsObject").getString("payprice");

        Map<String, String> bodyMap  = new LinkedHashMap<>();
        bodyMap.put("payId", payId);
        bodyMap.put("appId", appId);

        Map<String, String> wxMap  = new LinkedHashMap<>();
        wxMap.put("functionId", "wapWeiXinPay");
        wxMap.put("body", JSON.toJSONString(bodyMap));
        wxMap.put("appId", appId);
        wxMap.put("payId", payId);
        wxMap.put("_format_", "JSON");

        JDRequest wxRequest = new JDRequest(session);
        wxRequest.setCleanUrl("https://pay.m.jd.com/index.action");
        wxRequest.addReferer(jumpurl);
        wxRequest.addUrlParams(wxMap);
        WebResponse wxResponse = OKHttpUtils.jdHttp(wxRequest);
        JSONObject wxJson = JSONObject.parseObject(wxResponse.getHtml());
        String deepLink = wxJson.getString("deepLink");
        String jdPrePayId = wxJson.getString("jdPrePayId");
        String payEnum = wxJson.getString("payEnum");

        if (StringUtils.hasLength(deepLink)){
            log.info("普通商品下单成功 deeplink=" + deepLink);

            PayInfo payInfo = new PayInfo();
            payInfo.setNotifyUrl(orderParams.getNotifyUrl());
            payInfo.setSession(session);
            payInfo.setQueryUrl("https://pay.m.jd.com/newpay/index.action");
            payInfo.setOrderId(orderId);
            payInfo.setPayprice(payprice);
            payInfo.setOrderType(1);
            payInfo.setPayId(payId);
            payInfo.setJdPrePayId(jdPrePayId);
            payInfo.setPayEnum(payEnum);

            queryOrderService.addQuery(payInfo);
        }

        JDOrderResponse jdOrderResponse = new JDOrderResponse();
        jdOrderResponse.setStatus(StringUtils.hasLength(deepLink) && StringUtils.hasLength(orderId) && StringUtils.hasLength(payprice) ? 0 : 1);
        jdOrderResponse.setJdOrderNo(orderId);
        jdOrderResponse.setPayPrice(payprice);
        jdOrderResponse.setWxDeepLink(deepLink);

        return jdOrderResponse;
    }


    /**
     * 游戏商品(一卡通)
     * @return
     */
    private JDOrderResponse submitOrderGame(JDSessionCore.JDSession session, OrderParams orderParams) throws IOException, AppException {
        JDRequest webRequest = new JDRequest(session);
        webRequest.setCleanUrl("https://gamerecg.m.jd.com/game/submitOrder.action");
        webRequest.setMethod(HttpMethod.POST);

        Map<String, String> submitMap = new HashMap<>();
        submitMap.put("chargeType", "4835");
        submitMap.put("skuId", orderParams.getProductId());
        submitMap.put("brandId", "166758");
        submitMap.put("payPwd", "");
        submitMap.put("customs", "");
        submitMap.put("gamearea", "");
        submitMap.put("gamesrv", "");
        submitMap.put("accounttype", "");
        submitMap.put("chargetype", "");
        submitMap.put("eid", "WR65KAQOBINEOW2P6DUDHQH65NFWJC2FQFV6JODPCRFCTDH5WZ4E67WYAVWWJKECXNK2RP2M45P3IS7BYEV4E7WIFM");
        submitMap.put("skuName", "完美一卡通15元");
        submitMap.put("buyNum", String.valueOf(orderParams.getOrderNum()));
        submitMap.put("type", "1");
        submitMap.put("couponIds", "");
        submitMap.put("useBean", "");
        submitMap.put("payMode", "0");
        submitMap.put("totalPrice", "14.95");

        webRequest.setBody(UrlUtils.mapToParamString(submitMap));

        WebResponse response = OKHttpUtils.jdHttp(webRequest);
        if (response.getLocation().trim().startsWith("https://plogin.m.jd.com/user/login.action")){
            jdSessionCore.removeSession(session.getSessionId());
            throw new AppException("该账号需要重新登录: " + session.getSessionId());
        }
        String payId = UrlUtils.getQueryMap(response.getLocation()).get("payId");

        JDRequest syt = new JDRequest(session);
        syt.setCleanUrl(response.getLocation());
        WebResponse sytResponse = OKHttpUtils.jdHttp(syt);

        JDRequest paychannel = new JDRequest(session);
        paychannel.addReferer(response.getLocation());
        Map<String, String> channelMap  = new LinkedHashMap<>();
        channelMap.put("lastPage", "");
        channelMap.put("appId", "jd_m_yxdk");
        channelMap.put("payId", payId);
        channelMap.put("_format_", "JSON");
        paychannel.setCleanUrl("https://pay.m.jd.com/newpay/index.action");
        paychannel.setBody(UrlUtils.mapToParamString(channelMap));
        paychannel.setMethod(HttpMethod.POST);
        WebResponse channelResponse = OKHttpUtils.jdHttp( paychannel);
        JSONObject channelJson = JSONObject.parseObject(channelResponse.getHtml());
        String orderId = channelJson.getJSONObject("payParamsObject").getString("orderId");
        String payprice = channelJson.getJSONObject("payParamsObject").getString("payprice");

        Map<String, String> bodyMap  = new LinkedHashMap<>();
        bodyMap.put("payId", payId);
        bodyMap.put("appId", "jd_m_yxdk");

        Map<String, String> wxMap  = new LinkedHashMap<>();
        wxMap.put("functionId", "wapWeiXinPay");
        wxMap.put("body", JSON.toJSONString(bodyMap));
        wxMap.put("appId", "jd_m_yxdk");
        wxMap.put("payId", payId);
        wxMap.put("_format_", "JSON");

        JDRequest wxRequest = new JDRequest(session);
        wxRequest.setCleanUrl("https://pay.m.jd.com/index.action");
        wxRequest.addReferer(response.getLocation());
        wxRequest.addUrlParams(wxMap);
        WebResponse wxResponse = OKHttpUtils.jdHttp(wxRequest);

        JSONObject jsonObject = JSONObject.parseObject(wxResponse.getHtml());
        JDRequest dowx = new JDRequest(session);
        dowx.setCleanUrl(jsonObject.getString("mweb_url"));
        dowx.addReferer("https://pay.m.jd.com/index.action");

        WebResponse dowxResp = OKHttpUtils.jdHttp(dowx);

        String deepLink = "";
        String queryLink = "";
        String pattern = "var url=\"([\\S]*)\";";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(dowxResp.getHtml());
        if (m.find()) {
            deepLink = m.group(1);
        }

        String pattern2 = "var redirect_url=\"([\\S]*)\";";
        Pattern r2 = Pattern.compile(pattern2);
        Matcher m2 = r2.matcher(dowxResp.getHtml());
        if (m2.find( )) {
            queryLink =  m2.group(1);
        }
        if (StringUtils.hasLength(deepLink) && StringUtils.hasLength(queryLink)){
            log.info("下单成功 deeplink=" + deepLink);

            PayInfo payInfo = new PayInfo();
            payInfo.setNotifyUrl(orderParams.getNotifyUrl());
            payInfo.setSession(session);
            payInfo.setQueryUrl(queryLink);
            payInfo.setOrderId(orderId);
            payInfo.setPayprice(payprice);
            payInfo.setOrderType(2);

            queryOrderService.addQuery(payInfo);
        }

        JDOrderResponse jdOrderResponse = new JDOrderResponse();
        jdOrderResponse.setStatus(StringUtils.hasLength(deepLink) && StringUtils.hasLength(orderId) && StringUtils.hasLength(payprice) ? 0 : 1);
        jdOrderResponse.setJdOrderNo(orderId);
        jdOrderResponse.setPayPrice(payprice);
        jdOrderResponse.setWxDeepLink(deepLink);

        return jdOrderResponse;
    }

}
