package com.autopay.autopay.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.autopay.autopay.core.JDSessionCore;
import com.autopay.autopay.domain.web.JDRequest;
import com.autopay.autopay.domain.web.WebRequest;
import com.autopay.autopay.domain.web.WebResponse;
import okhttp3.*;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OKHttpUtils {

    public static boolean useProxy = true;

    public static void main(String[] args) throws IOException {
        JDSessionCore.JDSession session = new JDSessionCore.JDSession();
        Map<String, String> cookies = new LinkedHashMap<>();
        cookies.put("pt_key", "AAJgbYS9ADBxFdnemcDRWPeWqllJ19XvVEQs5WGgVkRlET0SFdWvj4Ln7pw-Qgc58E4wbsOAl04");
        cookies.put("unpl", "V2_ZzNtbUpeSxEnAEBSLh0PBmIFEVxLVBYdJ11AVXlKWFdnCxRVclRCFnUUR1FnGVwUZAAZWENcRxdFCEdkeBBVAWMDE1VGZxBFLV0CFSNGF1wjU00zEQdEQiYAT1cpTVUGYlQbX0tUFxB9CkVUfkoMVmVQElxyZ0AVRQhHZHsRVAdiCxRYR1JzJXI4dmR4H10AZwsiXHJWc1chVEBXfR9cAyoDGlVAUksTcA1DZHopXw%3d%3d");

//        cookies.put("__jd_ref_cls", "MCashierNew_ConfirmPayment");
//        cookies.put("mba_muid", "1993497444");
//        cookies.put("mba_sid", "16181560633727854980467424072.1");
//        cookies.put("__jda", "123.1993497444.1617787964.1618152063.1618156089.11");
//        cookies.put("__jdb", "123.1.1993497444|11.1618156089");
//        cookies.put("__jdc", "123");
//        cookies.put("mobilev", "html5");
//        cookies.put("3AB9D23F7A4B3C9B", "WR65KAQOBINEOW2P6DUDHQH65NFWJC2FQFV6JODPCRFCTDH5WZ4E67WYAVWWJKECXNK2RP2M45P3IS7BYEV4E7WIFM");
//        cookies.put("_modc", "098f6bcd4621d373cade4e832627b4f6");
//        cookies.put("string123", "CA228D8D4D2B9CE8C14D046BA6E1BB0FaZd1036kBd");
//        cookies.put("wqmnx", "1bcd1c43jdm356a3e3a6hpea2b4e4130");
//        cookies.put("deviceName", "Safari");
//        cookies.put("deviceOS", "ios");
//        cookies.put("deviceOSVersion", "14.2");
//        cookies.put("deviceVersion", "604.1");
//        cookies.put("equipmentId", "WR65KAQOBINEOW2P6DUDHQH65NFWJC2FQFV6JODPCRFCTDH5WZ4E67WYAVWWJKECXNK2RP2M45P3IS7BYEV4E7WIFM");
//        cookies.put("fingerprint", "fad28ca90dde0a9628996f95c617ec34");
//        cookies.put("sk_history", "30705613502%2C");
//        cookies.put("wq_ug", "15");
//        cookies.put("shshshfp", "4af68d2db48848e5d0a96281d661154f");
//        cookies.put("shshshfpa", "35a73632-0f66-8448-87ac-8ea04a587256-1617601155");
//        cookies.put("shshshfpb", "xRPKD9CZo8%2FErIXAnVTN1Mg%3D%3D");
//        cookies.put("PPRD_P", "UUID.1993497444-LOGID.1618152376966.2090500731");
//        cookies.put("__wga", "1618152376964.1618152070392.1618152070392.1618152070392.13.1");
//        cookies.put("cid", "9");
//        cookies.put("jxsid_s_t", "1618152376978");
//        cookies.put("jxsid_s_u", "https%3A//item.m.jd.com/product/30705613502.html");
//        cookies.put("retina", "1");
//        cookies.put("wq_logid", "1618152376.397265377");
//        cookies.put("wxa_level", "1");
//        cookies.put("warehistory", "\"30705613502,30705169828,12256612,11967407,12876120,12520820,12351982,12930214,12901000,12045155,100006464197,10028467993260,100019867468,10028405976123,\"");
//        cookies.put("sbx_hot_h", "null");
//        cookies.put("autoOpenApp_downCloseDate_auto", "1618152180879_10800000");
//        cookies.put("__jdv", "123%7Cbaidu-pinzhuan%7Ct_288551095_baidupinzhuan%7Ccpc%7Cba6fb982ce824f8382e493214bab3b10_0_9895c977d5b2472082d9cd703b5c1979%7C1618152063156");
//        cookies.put("jxsid", "16181520614982338503");
//        cookies.put("unpl", "V2_ZzNtbUpeSxEnAEBSLh0PBmIFEVxLVBYdJ11AVXlKWFdnCxRVclRCFnUUR1FnGVwUZAAZWENcRxdFCEdkeBBVAWMDE1VGZxBFLV0CFSNGF1wjU00zEQdEQiYAT1cpTVUGYlQbX0tUFxB9CkVUfkoMVmVQElxyZ0AVRQhHZHsRVAdiCxRYR1JzJXI4dmR4H10AZwsiXHJWc1chVEBXfR9cAyoDGlVAUksTcA1DZHopXw%3d%3d");
//        cookies.put("cd_eid", "IY33LPMQGS654QFBSQ7MU5S7S6TVYLF3N5THI6WCJLFYH52RQNHHN7M4SWB3NG4HOJDJNLOTIPHXJP6SNV6U2Q4MAA");
//        cookies.put("addrId_1", "3269705462");
//        cookies.put("mitemAddrId", "19_1601_3634_63217");
//        cookies.put("mitemAddrName", "%u5E7F%u4E1C%u5E7F%u5DDE%u5E02%u6D77%u73E0%u533A%u5357%u77F3%u5934%u8857%u9053%u5357%u7B95%u8DEF%u4E2D%u7EA6%u5927%u885725%u53F7%u4E1C%u65B9%u7EA2%u5FEB%u9012%u67DC");
//        cookies.put("sc_width", "375");
//        cookies.put("webp", "1");
//        cookies.put("wq_addr", "3269705462%7C19_1601_3634_63217%7C%u5E7F%u4E1C_%u5E7F%u5DDE%u5E02_%u6D77%u73E0%u533A_%u5357%u77F3%u5934%u8857%u9053%7C%u5E7F%u4E1C%u5E7F%u5DDE%u5E02%u6D77%u73E0%u533A%u5357%u77F3%u5934%u8857%u9053%u5357%u7B95%u8DEF%u4E2D%u7EA6%u5927%u885725%u53F7%u4E1C%u65B9%u7EA2%u5FEB%u9012%u67DC%7C113270607%2C23066549");
//        cookies.put("visitkey", "277714203300962");
//        cookies.put("cartLastOpTime", "1617872937");
//        cookies.put("cartNum", "2");
//        cookies.put("TrackerID", "uDPSlAFzNZyRUCcFkT1XlZT4msl2SF7wiflXMMyMb5CU0koZb_rThhwV92gZqWyLqBiY5iQmuyyOTTODMeRckmVTk8ERSzAetJ0ejvlTugfy8TqBlyHVFAwa7f4m6NPqnfHy08GYMFTsa8B0SOT63A");
//        cookies.put("pt_key", "AAJgbYS9ADBxFdnemcDRWPeWqllJ19XvVEQs5WGgVkRlET0SFdWvj4Ln7pw-Qgc58E4wbsOAl04");
//        cookies.put("pt_pin", "18219111805_p");
//        cookies.put("pt_token", "aw20sjxz");
//        cookies.put("pwdt_id", "18219111805_p");
//        cookies.put("sfstoken", "tk01mc4e21cf7a8sMXgyYlBVTHQyq6ldP3a4GNO0YSqeCX9kGgZtDfuwEKegkxDTDT3WON2zjSLambguBn/ekpGWKFhr");
//        cookies.put("whwswswws", "");
//        cookies.put("jcap_dvzw_fp", "F7iWsUw_V-800An9EN1r3yT7O_y03wVKRHySgpAuymfFFOn_4R7_-KQlukfMRD1MBzYLKw==");
//        cookies.put("mt_xid", "V2_52007VwMTWlhfVFkYThlsVzVTFAdVUVRGFkEZXxliBhtWQVEFWBtVEFlSb1cbVlVfUA1LeRpdBmcfE1dBWVZLH0wSWQxsABJiX2hSah1NHFgMYAIXV1loUlkcSw%3D%3D");

        session.getCookies().putAll(cookies);

        JDRequest webRequest = new JDRequest(session);
        webRequest.setCleanUrl("https://gamerecg.m.jd.com/game/submitOrder.action");
        webRequest.setMethod(HttpMethod.POST);

        Map<String, String> submitMap = new HashMap<>();
        submitMap.put("chargeType", "4835");
        submitMap.put("skuId", "1264648572");
        submitMap.put("brandId", "166758");
        submitMap.put("payPwd", "");
        submitMap.put("customs", "");
        submitMap.put("gamearea", "");
        submitMap.put("gamesrv", "");
        submitMap.put("accounttype", "");
        submitMap.put("chargetype", "");
        submitMap.put("eid", "WR65KAQOBINEOW2P6DUDHQH65NFWJC2FQFV6JODPCRFCTDH5WZ4E67WYAVWWJKECXNK2RP2M45P3IS7BYEV4E7WIFM");
        submitMap.put("skuName", "完美一卡通15元");
        submitMap.put("buyNum", "1");
        submitMap.put("type", "1");
        submitMap.put("couponIds", "");
        submitMap.put("useBean", "");
        submitMap.put("payMode", "0");
        submitMap.put("totalPrice", "14.95");

        webRequest.setBody(UrlUtils.mapToParamString(submitMap));

        WebResponse response = jdHttp(webRequest);
        System.out.println(response.getStatusCode());
        System.out.println(response.getLocation());
        String payId = UrlUtils.getQueryMap(response.getLocation()).get("payId");

        JDRequest syt = new JDRequest(session);
        syt.setCleanUrl(response.getLocation());
        WebResponse sytResponse = jdHttp(syt);
        System.out.println(sytResponse.getHtml());

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
        WebResponse channelResponse = jdHttp( paychannel);

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
        WebResponse wxResponse = jdHttp(wxRequest);
        System.out.println(wxResponse.getHtml());

        JSONObject jsonObject = JSONObject.parseObject(wxResponse.getHtml());
        JDRequest dowx = new JDRequest(session);
        dowx.setCleanUrl(jsonObject.getString("mweb_url"));
        dowx.addReferer("https://pay.m.jd.com/index.action");

        WebResponse dowxResp = jdHttp(dowx);

        String deepLink = "";
        String queryLink = "";
        // 按指定模式在字符串查找
        String pattern = "var url=\"([\\S]*)\";";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(dowxResp.getHtml());
        if (m.find()) {
            deepLink = m.group(1);
        }

        String pattern2 = "var redirect_url=\"([\\S]*)\";";

        Pattern r2 = Pattern.compile(pattern2);

        // 现在创建 matcher 对象
        Matcher m2 = r2.matcher(dowxResp.getHtml());
        if (m2.find( )) {
            queryLink =  m2.group(1);
        }
        if (StringUtils.hasLength(deepLink) && StringUtils.hasLength(queryLink)){
            System.out.println(deepLink);

            JDRequest query = new JDRequest(session);
            query.setCleanUrl("https://pay.m.jd.com/wapWeiXinPay/weiXinH5PayQuery.action");
            Map<String, String> qmap  = new LinkedHashMap<>();
            qmap.put("appId", "jd_m_yxdk");
            qmap.put("payId", payId);
            query.addUrlParams(qmap);
            WebResponse queryResp = jdHttp(query);
            System.out.println(queryResp.getHtml());
        }
    }

    public static WebResponse jdHttp(JDRequest webRequest) throws IOException {
        JDSessionCore.JDSession jdSession = webRequest.getSession();
        CookieJar cookieJar = new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                for (Cookie cookie : cookies) {
                    jdSession.getCookies().put(cookie.name(), cookie.value());
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                return jdSession.getCookies()
                        .entrySet()
                        .stream()
                        .map(e -> new Cookie.Builder().hostOnlyDomain(url.host()).name(e.getKey()).value(e.getValue()).build())
                        .collect(Collectors.toList());
            }
        };

        return okhttp(webRequest, cookieJar);
    }

    //TODO 卡密提取/商品info  安卓唤起  回调

    public static WebResponse okhttp(WebRequest webRequest, CookieJar cookieJar) throws IOException {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.followRedirects(false);
        builder.cookieJar(cookieJar);
        if (useProxy){
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888)))
                    .connectTimeout(webRequest.getTimeoutSeconds(), TimeUnit.SECONDS)
                    .writeTimeout(webRequest.getTimeoutSeconds(), TimeUnit.SECONDS)
                    .readTimeout(webRequest.getTimeoutSeconds(), TimeUnit.SECONDS);
        }
        OkHttpClient client = builder.build();

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(webRequest.generateFullUrl());

        for (Header header : webRequest.getHeaders()) {
            requestBuilder.addHeader(header.getName(), header.getValue());
        }

        if (Objects.equals(webRequest.getMethod(), HttpMethod.GET)) {
            requestBuilder.get();
        } else {
            MediaType mediaType = MediaType.parse(webRequest.getContentType().getMimeType());
            RequestBody requestBody = RequestBody.create(webRequest.getBody(), mediaType);
            requestBuilder.post(requestBody);
        }
        Request request = requestBuilder.build();
        Response response = client.newCall(request).execute();

        WebResponse webResponse = new WebResponse();
        webResponse.setStatusCode(response.code());
        webResponse.setLocation(response.header("Location"));
        webResponse.setCookies(null);

        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            byte[] bodyBytes = responseBody.bytes();
            webResponse.setBytes(bodyBytes);
            webResponse.setHtml(new String(bodyBytes));
        }

        return webResponse;
    }
}

