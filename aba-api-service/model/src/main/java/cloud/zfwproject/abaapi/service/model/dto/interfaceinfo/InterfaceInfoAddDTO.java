package cloud.zfwproject.abaapi.service.model.dto.interfaceinfo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class InterfaceInfoAddDTO implements Serializable {

    /**
     * 接口名称
     */
    @NotBlank(message = "接口名称不能为空")
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    @NotBlank(message = "接口 url 不能为空")
    @Pattern(regexp = "^(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?$", message = "url 不合法")
    private String url;

    /**
     * 接口参数
     */
    @NotBlank(message = "接口名称不能为空")
    private String requestParams;

    /**
     * 请求类型
     */
    @NotBlank(message = "接口名称不能为空")
    private String method;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    private static final long serialVersionUID = 1L;
}
