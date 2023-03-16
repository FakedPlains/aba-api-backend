package cloud.zfwproject.abaapi.common.model;

import lombok.Data;

import java.util.List;

/**
 * 隐藏敏感信息的简单用户类
 *
 * @author 张富玮
 * @date 2022-10-27 15:20
 */
@Data
public class SimpleUser {
    /**
     * 用户 id, 主键
     */
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户名
     */
    private String userName;

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
     * 用户权限
     */
    private List<String> permissions;

}
