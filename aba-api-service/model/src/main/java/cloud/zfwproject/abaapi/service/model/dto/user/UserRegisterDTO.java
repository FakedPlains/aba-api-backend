package cloud.zfwproject.abaapi.service.model.dto.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author 46029
 * @version 1.0
 * @description 用户注册请求对象
 * @date 2023/3/12 14:17
 */
@Data
public class UserRegisterDTO {
    /**
     * 用户账号
     */
    @NotBlank(message = "用户账号不能为空")
    @Length(min = 6, max = 16, message = "用户账号长度必须在 6 - 16 之间")
    private String userPassword;

    /**
     * 用户密码
     */
    @NotBlank(message = "用户账号不能为空")
    @Length(min = 6, max = 16, message = "密码长度必须在 6 - 16 之间")
    private String repeatPassword;

    /**
     * 重复密码
     */
    @NotBlank(message = "用户账号不能为空")
    @Length(min = 6, max = 16, message = "密码账号长度必须在 6 - 16 之间")
    private String userAccount;
}
