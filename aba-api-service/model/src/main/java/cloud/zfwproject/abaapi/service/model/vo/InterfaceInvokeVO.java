package cloud.zfwproject.abaapi.service.model.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/6 16:33
 */
@Data
public class InterfaceInvokeVO {

    private String responseBody;

    private Map<String, List<String>> responseHeaders;

}
