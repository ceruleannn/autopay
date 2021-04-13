package com.autopay.autopay.domain.params;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(description="OrderParams")
public class OrderParams {

    @NotBlank
    @ApiModelProperty(value="productId",name="productId",required=true, example="")
    String productId;

    @ApiModelProperty(value="orderType",name="orderType",required=false, example="1")
    Integer orderType = 1; //1=普通商品 2=一卡通

    @ApiModelProperty(value="orderNum",name="orderNum",required=false, example="1")
    Integer orderNum;

    @ApiModelProperty(value="notifyUrl",name="notifyUrl",required=true, example="")
    String notifyUrl;
}
