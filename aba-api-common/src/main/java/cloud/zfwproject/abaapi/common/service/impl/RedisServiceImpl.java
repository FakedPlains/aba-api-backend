package cloud.zfwproject.abaapi.common.service.impl;

import cloud.zfwproject.abaapi.common.service.RedisService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/2 14:30
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void setWithString(String key, Object value, long timeout, TimeUnit timeUnit) {
        String jsonStr = JSONUtil.toJsonStr(value);
        stringRedisTemplate.opsForValue().set(key, jsonStr, timeout, timeUnit);
    }

    @Override
    public <T> T getFromString(String key, Class<T> clazz) {
        String str = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isBlank(str)) {
            return null;
        }
        return JSONUtil.toBean(str, clazz);
    }

    @Override
    public void setWithHash(String key, String hashKey, Object value) {
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public String getFromHashKey(String key, String hashKey) {
        Object o = stringRedisTemplate.opsForHash().get(key, hashKey);
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    @Override
    public boolean deleteHashKey(String key, Object... hashKeys) {
        Long res = stringRedisTemplate.opsForHash().delete(key, hashKeys);
        return res >= hashKeys.length;
    }

    @Override
    public void expire(String key, long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.expire(key, timeout, timeUnit);
    }

    @Override
    public boolean delete(String... key) {
        Long delete = stringRedisTemplate.delete(Arrays.asList(key));
        if (delete == null) {
            return false;
        }
        return delete == key.length;
    }

}
