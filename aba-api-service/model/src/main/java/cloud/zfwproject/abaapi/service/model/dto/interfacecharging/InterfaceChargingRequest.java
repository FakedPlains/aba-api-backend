package cloud.zfwproject.abaapi.service.model.dto.interfacecharging;

import cloud.zfwproject.abaapi.common.validation.EnumValue;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/9 14:59
 */
@Data
public class InterfaceChargingRequest {
    /**
     * 接口信息 id
     */
    @NotNull(message = "接口 id 不能为空")
    @Min(value = 1, message = "接口 id 非法")
    private Long interfaceInfoId;

    /**
     * 是否收费
     */
    @EnumValue(values = {0, 1}, message = "只能为指定枚举值")
    private Integer isCharge;

    /**
     * 价格/次
     */
    private Double price;

    /**
     * 免费调用次数
     */
    private Integer freeCount;
}
