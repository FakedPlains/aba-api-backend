package cloud.zfwproject.abaapi.common.model;

import lombok.Builder;
import lombok.Data;

/**
 * 统一响应结果集
 *
 * @author 张富玮
 * @date 2022-10-27 15:05
 */
@Data
@Builder
public class ResponseResult<T> {

    /**
     * 响应成功
     */
    private Boolean success;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应状态码
     */
    private String code;


    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应时间戳
     */
    private long timestamp;

}
