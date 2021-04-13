package com.autopay.autopay.domain.params.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthPageParams extends AuthParams {
    private int page = 1;
    private int pageSize = 10;
}
