package cloud.zfwproject.abaapi.service.model.dto.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户创建请求
 *
 * @author yupi
 */
@Data
public class UserAddDTO implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    @NotBlank(message = "用户账号不能为空")
    @Length(min = 6, max = 16, message = "用户账号长度必须在 6 - 16 之间")
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    /**
     * 密码
     */
    private String userPassword;

    private static final long serialVersionUID = 1L;
}
