package cloud.zfwproject.abaapi.service.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 接口请求参数信息表
 * @TableName interface_request_params
 */
@TableName(value ="interface_request_params")
@Data
public class InterfaceRequestParams implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数类型
     */
    private String type;

    /**
     * 是否必填
     */
    private Integer isRequired;

    /**
     * 最大长度
     */
    private Integer maxLength;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
