package cloud.zfwproject.abaapi.service.service.impl;


import cloud.zfwproject.abaapi.common.exception.BusinessException;
import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.service.mapper.InterfaceChargingMapper;
import cloud.zfwproject.abaapi.service.model.dto.interfacecharging.InterfaceChargingRequest;
import cloud.zfwproject.abaapi.service.model.enums.InterfaceInfoEnum;
import cloud.zfwproject.abaapi.service.model.po.InterfaceCharging;
import cloud.zfwproject.abaapi.service.service.InterfaceChargingService;
import cn.hutool.core.bean.BeanUtil;
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

    /**
     * 添加接口计费信息
     *
     * @param interfaceChargingRequest 接口计费信息请求数据
     * @return id
     */
    @Override
    public Long addInterfaceCharging(InterfaceChargingRequest interfaceChargingRequest) {
        // 1.判断是否收费
        if (interfaceChargingRequest.getIsCharge().equals(InterfaceInfoEnum.Charging.CHARGING.getValue())) {
            // 2.判断价格是否合法
            Double price = interfaceChargingRequest.getPrice();
            if (price == null || price <= 0) {
                throw new BusinessException(ResponseCode.INVALID_PARAMS, "收费不能为 0");
            }
        }

        // 3.保存数据
        InterfaceCharging interfaceCharging = new InterfaceCharging();
        BeanUtil.copyProperties(interfaceChargingRequest, interfaceCharging);
        boolean save = this.save(interfaceCharging);
        if (!save) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR, "添加失败");
        }

        return interfaceCharging.getId();
    }

    /**
     * 根据接口 id 获取接口计费信息
     *
     * @param interfaceId 接口 id
     * @return 接口计费信息
     */
    @Override
    public InterfaceCharging getInterfaceChargingByInterfaceId(Long interfaceId) {
        return this.lambdaQuery()
                .eq(InterfaceCharging::getInterfaceInfoId, interfaceId)
                .one();
    }

    /**
     * 更新接口计费信息
     *
     * @param interfaceChargingRequest 接口计费信息请求
     */
    @Override
    public void updateInterfaceCharging(InterfaceChargingRequest interfaceChargingRequest) {
        InterfaceCharging interfaceCharging = new InterfaceCharging();
        BeanUtil.copyProperties(interfaceChargingRequest, interfaceCharging);

        boolean update = this.lambdaUpdate()
                .eq(InterfaceCharging::getInterfaceInfoId, interfaceChargingRequest.getInterfaceInfoId())
                .setEntity(interfaceCharging)
                .update();
        if (!update) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR, "更新失败");
        }
    }

}




