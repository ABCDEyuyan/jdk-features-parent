package com.nf.mvc.support;

public enum CommonResultCode implements ResultCode {
    SUCCESS(200,"调用成功"),
    FAILED(500,"调用失败"),
    FORBIDDEN(403,"没有权限访问资源");

    private Integer code;
    private String message;

    CommonResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
