package cloud.zfwproject.abaapi.service.service.impl;


import cloud.zfwproject.abaapi.service.mapper.InterfaceRequestParamsMapper;
import cloud.zfwproject.abaapi.service.model.po.InterfaceParam;
import cloud.zfwproject.abaapi.service.service.InterfaceParamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 46029
 * @description 针对表【interface_request_params(接口请求参数信息表)】的数据库操作Service实现
 * @createDate 2023-03-13 19:39:47
 */
@Service("interfaceParamService")
public class InterfaceParamServiceImpl extends ServiceImpl<InterfaceRequestParamsMapper, InterfaceParam>
        implements InterfaceParamService {

    @Override
    public List<InterfaceParam> getInterfaceParamsByInterfaceId(Long interfaceId) {
        return this.lambdaQuery()
                .eq(InterfaceParam::getInterfaceInfoId, interfaceId)
                .list();
    }
}




