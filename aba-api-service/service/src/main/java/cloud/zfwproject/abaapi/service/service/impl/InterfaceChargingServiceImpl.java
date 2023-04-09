package cloud.zfwproject.abaapi.service.service.impl;


import cloud.zfwproject.abaapi.service.mapper.InterfaceChargingMapper;
import cloud.zfwproject.abaapi.service.model.po.InterfaceCharging;
import cloud.zfwproject.abaapi.service.service.InterfaceChargingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author 46029
* @description 针对表【interface_charging(接口计费信息)】的数据库操作Service实现
* @createDate 2023-04-09 10:35:08
*/
@Service("interfaceChargingService")
public class InterfaceChargingServiceImpl extends ServiceImpl<InterfaceChargingMapper, InterfaceCharging>
    implements InterfaceChargingService {

}




