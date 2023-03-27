package cloud.zfwproject.abaapi.service.model.dto.user;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户更新请求
 *
 * @author yupi
 */
@Data
public class UserUpdateDTO implements Serializable {
    /**
     * id
     */
    @NotNull(message = "用户 id 不能为空")
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
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
