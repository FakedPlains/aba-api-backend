package cloud.zfwproject.abaapi.service.model.dto.dict;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/27 17:05
 */
@Data
public class DictTypeAddRequest {

    @NotBlank(message = "名称不能为空")
    @Length(max = 20, message = "长度必须不大于20")
    private String name;

    @Length(max = 100, message = "长度必须不大于100")
    private String desc;

}
