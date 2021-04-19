package com.autopay.autopay.controller;

import com.alibaba.fastjson.JSON;
import com.autopay.autopay.core.JDSessionCore;
import com.autopay.autopay.domain.exception.AppException;
import com.autopay.autopay.domain.params.LoginParams;
import com.autopay.autopay.domain.params.OrderParams;
import com.autopay.autopay.domain.response.JDLoginResponse;
import com.autopay.autopay.domain.response.JDOrderResponse;
import com.autopay.autopay.service.JDService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Validated
@RestController
@RequestMapping(value = "jd")
public class JDWapController {

    @Autowired
    JDService jdService;

    @Autowired
    JDSessionCore jdSessionCore;

    @ApiOperation(value = "京东登录", notes = "京东登录")
    @PostMapping(value = "/login")
    public JDLoginResponse login (@Valid @RequestBody LoginParams loginParams) throws IOException, InterruptedException {

        return jdService.login(loginParams);
    }

    @ApiOperation(value = "京东下单", notes = "京东下单")
    @GetMapping(value = "/order")
    public void order(@Valid OrderParams orderParams, HttpServletResponse httpServletResponse) throws AppException, IOException {

        JDSessionCore.JDSession jdSession = jdSessionCore.randomSession();
        if (jdSession == null){
            throw new AppException("无有效登录用户");
        }
        String wx = "";
        JDOrderResponse response = null;
        try {
            response  = jdService.submitOrder(jdSession,orderParams);
             wx = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<meta charset=\"utf-8\">\n" +
                    "<title>微信支付</title>\n" +
                    "<script> \n" +
                    "    window.onload = function(){\n" +
                    "             var gotoLink = document.createElement('a');\n" +
                    "             gotoLink.href = '" +response.getWxDeepLink() +"'\n" +
                    "             document.body.appendChild(gotoLink);\n" +
                    "             gotoLink.click();\n" +
                    "    }\n" +
                    "</script>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "</body>\n" +
                    "</html>";
        } catch (Exception e) {
            log.warn("下单失败", e);
            throw new AppException("下单失败");
        }
        if (StringUtils.hasLength(wx)){
            httpServletResponse.setContentType("text/html;charset=utf-8");
            OutputStream os = httpServletResponse.getOutputStream();
            os.write(wx.getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();
        }else {
            httpServletResponse.setContentType("application/json;charset=utf-8");
            OutputStream os = httpServletResponse.getOutputStream();
            os.write(JSON.toJSONString(response).getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();
        }
    }
}
