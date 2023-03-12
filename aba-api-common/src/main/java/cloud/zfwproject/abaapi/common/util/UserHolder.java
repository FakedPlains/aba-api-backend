package cloud.zfwproject.abaapi.common.util;


import cloud.zfwproject.abaapi.common.model.SimpleUser;

/**
 * 保存当前请求用户信息
 *
 * @author 张富玮
 * @date 2022-10-27 15:18
 */
public class UserHolder {
    /**
     * 本地线程，用于保存当前请求的用户信息
     */
    private static final ThreadLocal<SimpleUser> USER_DTO_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 保存用户到本地线程
     *
     * @param simpleUser 用户数据对象
     */
    public static void saveUser(SimpleUser simpleUser) {
        USER_DTO_THREAD_LOCAL.set(simpleUser);
    }

    /**
     * 从本地线程获取用户信息
     *
     * @return 用户数据对象
     */
    public static SimpleUser getUser() {
        return USER_DTO_THREAD_LOCAL.get();
    }

    /**
     * 从本地线程移除用户信息
     */
    public static void removeUser() {
        USER_DTO_THREAD_LOCAL.remove();
    }
}
