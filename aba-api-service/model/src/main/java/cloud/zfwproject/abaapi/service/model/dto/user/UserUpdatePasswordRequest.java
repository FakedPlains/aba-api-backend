package cloud.zfwproject.abaapi.service.model.dto.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/5 20:53
 */
@Data
public class UserUpdatePasswordRequest {

    /**
     * id
     */
    @NotNull(message = "用户 id 不能为空")
    @Min(value = 1, message = "id 非法")
    private Long id;


    /**
     * 用户密码
     */
    @NotBlank(message = "旧密码不能为空")
    @Length(min = 6, max = 16, message = "密码长度必须在 6 - 16 之间")
    private String oldPassword;

    /**
     * 用户密码
     */
    @NotBlank(message = "新密码不能为空")
    @Length(min = 6, max = 16, message = "密码长度必须在 6 - 16 之间")
    private String newPassword;

    /**
     * 用户密码
     */
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 16, message = "密码长度必须在 6 - 16 之间")
    private String repeatPassword;

}
