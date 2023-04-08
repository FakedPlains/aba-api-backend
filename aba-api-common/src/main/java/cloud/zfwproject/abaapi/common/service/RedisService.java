package cloud.zfwproject.abaapi.common.service;

import java.util.concurrent.TimeUnit;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/2 14:30
 */
public interface RedisService {

    /**
     * 存储 String 类型
     *
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     */
    void setWithString(String key, Object value, long timeout, TimeUnit timeUnit);

    /**
     * 从 String 中获取数据
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getFromString(String key, Class<T> clazz);

    /**
     * 存储 hash 类型
     *
     * @param key     redis 键
     * @param hashKey hash 键
     * @param value   hash 值
     */
    void setWithHash(String key, String hashKey, Object value);

    /**
     * 获取 hash 中指定 key 的数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    String getFromHashKey(String key, String hashKey);

    /**
     * 删除 hash 中指定 key 的数据
     * @param key
     * @param hashKeys
     * @return
     */
    boolean deleteHashKey(String key, Object... hashKeys);

    /**
     * 设置过期时间
     *
     * @param key
     * @param timeout
     * @param timeUnit
     */
    void expire(String key, long timeout, TimeUnit timeUnit);

    /**
     * 删除一个或多个 key
     *
     * @param key
     * @return
     */
    boolean delete(String... key);

}
