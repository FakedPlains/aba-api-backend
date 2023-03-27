package cloud.zfwproject.abaapi.service.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典类型表
 * @TableName dict_type
 */
@TableName(value ="dict_type")
@Data
public class DictType implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 字典类型名称
     */
    private String name;

    /**
     * 字典类型描述
     */
    private String description;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建用户
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

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
