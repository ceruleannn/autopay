package com.autopay.autopay.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JDOrderResponse {

    int status; // 0=下单成功 1=下单失败
    String jdOrderNo;
    String payPrice;
    String wxDeepLink;
}
