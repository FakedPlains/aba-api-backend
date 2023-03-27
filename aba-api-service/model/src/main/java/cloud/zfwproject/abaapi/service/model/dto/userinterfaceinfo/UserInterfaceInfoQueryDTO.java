package cloud.zfwproject.abaapi.service.model.dto.userinterfaceinfo;

import cloud.zfwproject.abaapi.common.model.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author yupi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInterfaceInfoQueryDTO extends PageParam implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 调用用户
     */
    private Long userId;

    /**
     * 接口信息
     */
    private Long interfaceInfoId;

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
