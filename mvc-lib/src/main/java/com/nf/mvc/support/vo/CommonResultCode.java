package com.nf.mvc.support.vo;

/**
 * <a href=" https://www.runoob.com/http/http-status-codes.htm">http状态码</a>l
 */
public enum CommonResultCode implements ResultCode {
    /**
     * 表示调用成功，对应Http 状态码200
     */
    SUCCESS(200, "调用成功"),
    /**
     * 永久移动。请求的资源已被永久的移动到新URI，返回信息会包括新的URI，
     * 浏览器会自动定向到新URI。今后任何新的请求都应使用新的URI代替
     */
    MovedPermanently(301, "永久移动"),
    /**
     * 临时移动。与301类似。但资源只是临时被移动。客户端应继续使用原有URI
     */
    Found(302, "临时移动"),
    /**
     * 客户端请求的语法错误，服务器无法理解
     */
    BadRequest(400, "错误请求"),
    /**
     * 请求要求用户的身份认证
     */
    Unauthorized(401, "未授权"),

    /**
     * 表示没有权限，对应Http 状态码403
     */
    FORBIDDEN(403, "没有权限访问资源"),
    /**
     * 服务器无法根据客户端的请求找到资源（网页）。
     * 通过此代码，网站设计人员可设置"您所请求的资源无法找到"的个性页面
     */
    NotFound(404, "资源找不到"),
    /**
     * 客户端请求中的方法被禁止
     */
    MethodNotAllowed(405, "客户端请求中的方法被禁止"),
    /**
     * 服务器内部错误，无法完成请求
     */
    FAILED(500, "Internal Server Error");
    private final Integer code;
    private final String message;

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
