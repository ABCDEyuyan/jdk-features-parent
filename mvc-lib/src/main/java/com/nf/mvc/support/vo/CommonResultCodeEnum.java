package com.nf.mvc.support.vo;

/**
 * 常见的http状态码实现,此类中枚举成员的code属性值是标准的Http状态码,message属性是状态码对应
 * 的英文名称,成员名称基本与英文名一样,但也有不一样的,比如SUCCESS就不是状态码200的英文名字
 * <p>HTTP 状态码由三个十进制数字组成，第一个十进制数字定义了状态码的类型。状态码分为五类：
 * <ul>
 *     <li>1开头(100-199):信息，服务器收到请求，需要请求者继续执行操作</li>
 *     <li>2开头(200-299):成功，操作被成功接收并处理</li>
 *     <li>3开头(300-399):重定向，需要进一步的操作以完成请求</li>
 *     <li>4开头(400-499):客户端错误，请求包含语法错误或无法完成请求</li>
 *     <li>5开头(500-599):服务器错误，服务器在处理请求的过程中发生了错误</li>
 * </ul>
 * </p>
 * <p>按照alibaba编码规范对于枚举的要求,类名以Enum结尾,成员必须全大写,单词之间用下划线分隔,
 * 每个枚举成员都必须添加文档化注释</p>
 * <p>关于字符串的大小写转换操作,推荐使用插件string manipulation来完成</p>
 * <a href="https://www.runoob.com/http/http-status-codes.html">http状态码</a>l
 */
public enum CommonResultCodeEnum implements ResultCode {
    /**
     * 表示调用成功，对应Http 状态码200
     */
    SUCCESS(200, "Ok"),
    /**
     * 已创建。成功请求并创建了新的资源
     */
    CREATED(201, "Created"),
    /**
     * 永久移动。请求的资源已被永久的移动到新URI，返回信息会包括新的URI，
     * 浏览器会自动定向到新URI。今后任何新的请求都应使用新的URI代替
     */
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    /**
     * 临时移动。与301类似。但资源只是临时被移动。客户端应继续使用原有URI
     */
    FOUND(302, "Found"),
    /**
     * 临时重定向。与302类似。使用GET请求重定向
     */
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),
    /**
     * 客户端请求的语法错误，服务器无法理解
     */
    BAD_REQUEST(400, "Bad Request"),
    /**
     * 请求要求用户的身份认证
     */
    UNAUTHORIZED(401, "Unauthorized"),

    /**
     * 表示没有权限，服务器理解请求客户端的请求，但是拒绝执行此请求
     */
    FORBIDDEN(403, "Forbidden"),
    /**
     * 服务器无法根据客户端的请求找到资源（网页）。
     * 通过此代码，网站设计人员可设置"您所请求的资源无法找到"的个性页面
     */
    NOT_FOUND(404, "Not Found"),
    /**
     * 客户端请求中的方法被禁止
     */
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    /**
     * 服务器内部错误，无法完成请求
     */
    FAILED(500, "Internal Server Error"),
    /**
     * 由于超载或系统维护，服务器暂时的无法处理客户端的请求。
     * 延时的长度可包含在服务器的Retry-After头信息中
     */
    SERVICE_UNAVAILABLE(503, "Service Unavailable");
    private final Integer code;
    private final String message;

    CommonResultCodeEnum(Integer code, String message) {
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
