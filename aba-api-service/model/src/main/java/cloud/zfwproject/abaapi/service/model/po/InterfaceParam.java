package cloud.zfwproject.abaapi.service.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 接口请求参数信息表
 * @TableName interface_param
 */
@TableName(value ="interface_param")
@Data
public class InterfaceParam implements Serializable {
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
    private Integer type;

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
     * 父 id
     */
    private Long parentId;

    /**
     * 类型 0:path 1:query 2:body 3:header 4:返回参数
     */
    private Integer style;

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
