package cloud.zfwproject.abaapi.common.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 响应状态枚举类
 *
 * @author 张富玮
 * @date 2022-10-27 15:03
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {
    // 成功失败错误码
    SUCCESS("20000", "success"),
    FAIL("20001", "failed"),

    // 通用错误码
    INVALID_PARAMS("30001", "invalid parameters"),
    REQUEST_TOO_FAST("30003", "request too fast"),
    NOT_FOUND_ERROR("30004", "数据不存在"),

    // 用户相关错误码
    USER_UNAUTHORIZED("40004", "用户未登录"),
    PERMISSION_DENIED("40005", "无权限"),

    // 系统错误
    SYSTEM_ERROR("50000", "系统内部异常"),
    OPERATION_ERROR("50001", "操作异常"),

    HTTP_STATUS_200("200", "ok"),
    HTTP_STATUS_400("400", "request error"),
    HTTP_STATUS_401("401", "no authentication"),
    HTTP_STATUS_403("403", "no authorities"),
    HTTP_STATUS_500("500", "server error");


    public static final List<ResponseCode> HTTP_STATUS_ALL = List.of(HTTP_STATUS_200, HTTP_STATUS_400, HTTP_STATUS_401, HTTP_STATUS_403, HTTP_STATUS_500);


    /**
     * 响应状态码
     */
    private final String code;

    /**
     * 响应信息
     */
    private final String message;
}
