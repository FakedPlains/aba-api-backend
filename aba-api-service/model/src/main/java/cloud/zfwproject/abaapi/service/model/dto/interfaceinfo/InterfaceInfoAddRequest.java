package cloud.zfwproject.abaapi.service.model.dto.interfaceinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterfaceInfoAddRequest implements Serializable {

    /**
     * 接口名称
     */
    @NotBlank(message = "接口名称不能为空")
    @Length(max = 20, message = "长度必须不大于20")
    private String name;

    /**
     * 描述
     */
    @Length(max = 100, message = "长度必须不大于100")
    private String description;

    /**
     * 接口地址
     */
    @NotBlank(message = "接口 url 不能为空")
    @URL(message = "url 不合法")
    private String url;

    /**
     * 请求类型
     */
    @NotNull(message = "请求类型不能为空")
    private Integer method;

    /**
     * 内容类型
     */
    @NotNull(message = "内容类型不能为空")
    private Integer contentType;

    /**
     * 请求头
     */
    private List<RequestHeader> requestHeaders;

    /**
     * 请求参数
     */
    private List<RequestParam> requestParams;

    /**
     * 响应参数
     */
    private List<ResponseParam> responseParams;

    /**
     * 错误码
     */
    private List<ErrorCode> errorCode;

    private static final long serialVersionUID = 1L;
}
