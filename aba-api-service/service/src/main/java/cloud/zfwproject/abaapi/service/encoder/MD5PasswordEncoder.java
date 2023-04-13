package cloud.zfwproject.abaapi.service.encoder;

import cn.hutool.crypto.SecureUtil;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/13 13:20
 */
public class MD5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(String password) {
        return SecureUtil.md5(password);
    }
}
