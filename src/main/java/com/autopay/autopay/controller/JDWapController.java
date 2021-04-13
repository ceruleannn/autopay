package com.autopay.autopay.controller;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

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
    @PostMapping(value = "/order")
    public JDOrderResponse order(@Valid  @RequestBody OrderParams orderParams) throws AppException {

        JDSessionCore.JDSession jdSession = jdSessionCore.randomSession();
        if (jdSession == null){
            throw new AppException("无有效登录用户");
        }
        try {
            return jdService.submitOrder(jdSession,orderParams);
        } catch (Exception e) {
            log.warn("下单失败", e);
            throw new AppException("下单失败");
        }
    }
}
