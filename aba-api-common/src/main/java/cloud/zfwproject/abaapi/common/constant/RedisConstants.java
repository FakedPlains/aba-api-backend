package cloud.zfwproject.abaapi.common.constant;


import static cloud.zfwproject.abaapi.common.constant.CommonConstant.REDIS_KEY_PREFIX;

/**
 * Redis 常量类
 *
 * @author 张富玮
 * @date 2022-10-28 15:13
 */
public interface RedisConstants {

    /**
     * 自增 id 计数
     */
    String ICR_ID_KEY_PREFIX = REDIS_KEY_PREFIX + "icr:";

    /**
     * 用户登录信息
     */
    String USER_LOGIN_KEY_PREFIX = REDIS_KEY_PREFIX + "user:login:token:";

    /**
     * 用户注册验证码
     */
    String USER_REGISTER_CODE_PREFIX = REDIS_KEY_PREFIX + "user:register:code:";

    /**
     * 接口信息
     */
    String INTERFACE_INFO_PREFIX = REDIS_KEY_PREFIX + "interface:info";

    /**
     * 接口限流
     */
    String API_ACCESS_LIMIT_PREFIX = REDIS_KEY_PREFIX + "access:limit:";

    /**
     * 用户登录过期时间
     */
    Long LOGIN_USER_TTL = 30L;

    /**
     * 用户注册验证码过期时间
     */
    Long REGISTER_CODE_TTL = 10L;

}
