package cloud.zfwproject.abaapi.service.model.dto.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author yupi
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    @NotBlank
    @Length(min = 8, max = 16, message = "用户名长度必须在 8-16 位")
    private String userAccount;

    @NotBlank
    @Length(min = 8, max = 16, message = "密码长度必须在 8-16 位")
    private String userPassword;
}
