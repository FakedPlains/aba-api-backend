package cloud.zfwproject.abaapi.service.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 删除请求
 *
 * @author yupi
 */
@Data
public class DeleteDTO implements Serializable {
    /**
     * id
     */
    @NotNull(message = "id 不能为空")
    private Long id;

    private static final long serialVersionUID = 1L;
}
