package cloud.zfwproject.abaapi.service.model.dto.userinterfaceinfo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class UserInterfaceInfoAddDTO implements Serializable {

    /**
     * 调用用户
     */
    @NotNull(message = "用户 id 不能为空")
    private Long userId;

    /**
     * 接口信息
     */
    @NotNull(message = "接口信息 id 不能为空")
    private Long interfaceInfoId;

    /**
     * 调用次数
     */
    @NotNull(message = "调用次数不能为空")
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    @NotNull(message = "剩余调用次数不能为空")
    private Integer leftNum;

    private static final long serialVersionUID = 1L;
}
