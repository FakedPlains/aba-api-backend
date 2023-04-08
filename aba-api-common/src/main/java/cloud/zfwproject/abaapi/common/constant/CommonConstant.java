package cloud.zfwproject.abaapi.common.constant;

/**
 * @author 46029
 * @version 1.0
 * @description 通用常量
 * @date 2023/3/6 14:31
 */
public interface CommonConstant {
    /**
     * 升序
     */
    String SORT_ORDER_ASC = "ascend";

    /**
     * 降序
     */
    String SORT_ORDER_DESC = " descend";

    /**
     * 用户名前缀
     */
    String USER_NAME_PREFIX = "demo_";

    /**
     * Redis key 前缀
     */
    String REDIS_KEY_PREFIX = "abapi:";

    /**
     * 请求头 Token 标识
     */
    String REQUEST_HEADER_TOKEN = "authorization";
}
