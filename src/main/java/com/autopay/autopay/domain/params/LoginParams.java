package com.autopay.autopay.domain.params;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApiModel(value="LoginParams",description="LoginParams")
@Data
public class LoginParams{

    @ApiModelProperty(value="pt_pin",name="pt_pin",required=true, example="")
    @NotBlank
    String pt_pin;

    @ApiModelProperty(value="pt_key",name="pt_key",required=true, example="")
    @NotBlank
    String pt_key;

    @ApiModelProperty(value="unpl",name="unpl",required=true, example="")
    @NotBlank
    String unpl;

}
