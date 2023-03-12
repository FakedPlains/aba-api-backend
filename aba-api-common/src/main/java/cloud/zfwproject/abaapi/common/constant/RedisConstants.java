package cloud.zfwproject.abaapi.common.constant;


import static cloud.zfwproject.abaapi.common.constant.CommonConstant.REDIS_KEY_PREFIX;

/**
 * Redis 常量类
 *
 * @author 张富玮
 * @date 2022-10-28 15:13
 */
public interface RedisConstants {

    public static final String ICR_ID_KEY_PREFIX = REDIS_KEY_PREFIX + "icr:";

    public static final String USER_LOGIN_KEY_PREFIX = REDIS_KEY_PREFIX + "user:login:token:";

    public static final String USER_REGISTER_CODE_PREFIX = REDIS_KEY_PREFIX + "user:register:code:";

    public static final String API_ACCESS_LIMIT_PREFIX = REDIS_KEY_PREFIX + "access:limit:";

    public static final Long LOGIN_USER_TTL = 30L;

    public static final Long REGISTER_CODE_TTL = 10L;

}
