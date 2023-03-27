package cloud.zfwproject.abaapi.service.model.dto.dict;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/27 17:05
 */
@Data
public class DictTypeUpdateRequest {

    @NotNull
    private Long id;

    @Max(value = 20, message = "长度必须不大于20")
    private String name;

    @Length(max = 100, message = "长度必须不大于100")
    private String desc;

}
