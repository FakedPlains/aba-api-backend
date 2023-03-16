package cloud.zfwproject.abaapi.service.model.dto.userinterfaceinfo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 更新请求
 *
 * @TableName product
 */
@Data
public class UserInterfaceInfoUpdateDTO implements Serializable {

    /**
     * id
     */
    @NotNull(message = "id 不能为空")
    private Long id;

    /**
     * 调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 0-正常 1-禁用
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
