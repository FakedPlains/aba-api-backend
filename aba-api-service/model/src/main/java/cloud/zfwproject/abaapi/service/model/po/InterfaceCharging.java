package cloud.zfwproject.abaapi.service.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 接口计费信息
 * @TableName interface_charging
 */
@TableName(value ="interface_charging")
@Data
public class InterfaceCharging implements Serializable {
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
     * 是否收费 0-免费 1-收费
     */
    private Integer isCharge;

    /**
     * 单价（元/次）
     */
    private BigDecimal price;

    /**
     * 免费试用次数
     */
    private Integer freeCount;

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
