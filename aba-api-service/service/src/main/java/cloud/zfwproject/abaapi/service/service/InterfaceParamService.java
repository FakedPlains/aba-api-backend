package cloud.zfwproject.abaapi.service.service;


import cloud.zfwproject.abaapi.service.model.po.InterfaceParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 46029
* @description 针对表【interface_request_params(接口请求参数信息表)】的数据库操作Service
* @createDate 2023-03-13 19:39:47
*/
public interface InterfaceParamService extends IService<InterfaceParam> {

    List<InterfaceParam> getInterfaceParamsByInterfaceId(Long interfaceId);

}
