package cloud.zfwproject.abaapi.service.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典数据表
 * @TableName dict_data
 */
@TableName(value ="dict_data")
@Data
public class DictData implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 字典类型 id
     */
    private Long dictTypeId;

    /**
     * 字典数据名称
     */
    private String name;

    /**
     * 字典数据代码
     */
    private String code;

    /**
     * 字典数据值
     */
    private String value;

    /**
     * 是否默认
     */
    private Integer isDefault;

    /**
     * 样式
     */
    private String style;

    /**
     * 状态
     */
    private Integer status;

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
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
