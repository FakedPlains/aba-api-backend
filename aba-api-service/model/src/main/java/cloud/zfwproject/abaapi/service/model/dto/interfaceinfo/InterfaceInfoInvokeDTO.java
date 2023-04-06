package cloud.zfwproject.abaapi.service.model.dto.interfaceinfo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * 更新请求
 *
 * @TableName product
 */
@Data
public class InterfaceInfoInvokeDTO implements Serializable {

    /**
     * id
     */
    @NotNull(message = "id 不能为空")
    @Min(value = 1, message = "id 不合法")
    private Long id;

    /**
     * 路径参数
     */
    private Map<String, String> pathParams;

    /**
     * 查询参数
     */
    private Map<String, Object> queryParams;

    /**
     * 请求体参数
     */
    private String bodyParams;

    /**
     * 请求头参数
     */
    private Map<String, String> headerParams;

    private static final long serialVersionUID = 1L;
}
