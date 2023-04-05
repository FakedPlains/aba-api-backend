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
     * 用户头像
     */
    private String userAvatar;


    private static final long serialVersionUID = 1L;
}
