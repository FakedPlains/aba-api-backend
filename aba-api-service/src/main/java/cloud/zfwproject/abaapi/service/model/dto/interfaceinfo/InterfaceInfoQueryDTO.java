package cloud.zfwproject.abaapi.service.model.dto.interfaceinfo;

import cloud.zfwproject.abaapi.common.model.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/13 19:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoQueryDTO extends PageParam {
    /**
     * id
     */
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求类型
     */
    private String method;


    /**
     * 接口状态（0-关闭 1-开启）
     */
    private Integer status;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
