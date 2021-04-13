package com.autopay.autopay.domain.response.common;


import com.autopay.autopay.constants.ReturnCode;
import lombok.Data;

/**
 * 统一响应
 */
@Data
public final class RestResponse<T> {

    /**
     * 返回码
     */
    private Integer code;
    /**
     * 描述
     */
    private String msg;
    /**
     * 返回数据
     */
    private T data;

    /**
     * 不允许外部创建
     */
    private RestResponse(T data, Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> RestResponse<T> result(T data, Integer code, String msg) {
        return new RestResponse<>(data, code, msg);

    }

    /**
     * 成功返回
     *
     * @return Result<T>
     */
    public static <T> RestResponse<T> success() {
        return new RestResponse<>(null, ReturnCode.OK, "ok");
    }

    /**
     * 成功返回
     *
     * @param data 返回内容
     * @return Result<T>
     */
    public static <T> RestResponse<T> success(T data) {
        return new RestResponse<>(data, ReturnCode.OK, "ok");
    }

    /**
     * 成功返回
     *
     * @param data 返回内容
     * @param msg  描述
     * @return Result<T>
     */
    public static <T> RestResponse<T> success(T data, String msg) {
        return new RestResponse<>(data, ReturnCode.OK, msg);
    }

    /**
     * 错误返回
     *
     * @param msg 描述
     * @return Result<T>
     */
    public static <T> RestResponse<T> error(String msg) {
        return new RestResponse<>(null, ReturnCode.APP_ERROR, msg);
    }

    /**
     * 错误返回
     *
     * @param code 返回码
     * @param msg  描述
     * @return 错误返回
     */
    public static <T> RestResponse<T> error(Integer code, String msg) {
        return new RestResponse<>(null, code, msg);
    }

}
