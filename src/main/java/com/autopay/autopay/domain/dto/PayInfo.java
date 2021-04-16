package com.autopay.autopay.domain.dto;

import com.autopay.autopay.core.JDSessionCore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Objects;

@Data
@ToString
public class PayInfo{

    private JDSessionCore.JDSession session;
    private int orderType; //1=普通商品 2=一卡通
    private String orderId;
    private String payprice;
    private int status;//0=未支付,1=已支付

    //private String deepLink;
    private String queryUrl; //轮询查状态地址
    private String notifyUrl; //支付成功回调地址

    //用于普通商品订单订单查询
    private String payId; //;
    private String jdPrePayId;
    private String payEnum;
}
