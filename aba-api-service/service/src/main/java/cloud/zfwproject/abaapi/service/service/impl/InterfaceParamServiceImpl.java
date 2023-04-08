package cloud.zfwproject.abaapi.service.service.impl;


import cloud.zfwproject.abaapi.common.exception.BusinessException;
import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.service.mapper.InterfaceRequestParamsMapper;
import cloud.zfwproject.abaapi.service.model.po.InterfaceParam;
import cloud.zfwproject.abaapi.service.service.InterfaceParamService;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
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

    /**
     * 删除接口相关参数信息
     *
     * @param interfaceId 接口 id
     */
    @Override
    public void deleteInterfaceParamsByInterfaceId(Long interfaceId) {
        LambdaQueryChainWrapper<InterfaceParam> wrapper = this.lambdaQuery()
                .eq(InterfaceParam::getInterfaceInfoId, interfaceId);
        boolean res = this.remove(wrapper);
        if (!res) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR);
        }
    }
}




