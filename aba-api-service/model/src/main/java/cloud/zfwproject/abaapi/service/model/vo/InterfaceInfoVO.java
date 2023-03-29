package cloud.zfwproject.abaapi.service.model.vo;

import cloud.zfwproject.abaapi.service.model.po.InterfaceInfo;
import cloud.zfwproject.abaapi.service.model.po.InterfaceParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 * @TableName product
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoVO extends InterfaceInfo implements Serializable {

    /**
     * 请求头
     */
    private List<InterfaceParam> requestHeaders;

    /**
     * 请求参数
     */
    private List<InterfaceParam> requestParams;

    /**
     * 响应参数
     */
    private List<InterfaceParam> responseParams;

    /**
     * 错误码
     */
    private List<InterfaceParam> errorCode;

    private static final long serialVersionUID = 1L;
}
