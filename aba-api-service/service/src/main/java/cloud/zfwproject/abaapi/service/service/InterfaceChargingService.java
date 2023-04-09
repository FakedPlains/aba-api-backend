package cloud.zfwproject.abaapi.service.service;


import cloud.zfwproject.abaapi.service.model.dto.interfacecharging.InterfaceChargingRequest;
import cloud.zfwproject.abaapi.service.model.po.InterfaceCharging;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 46029
 * @description 针对表【interface_charging(接口计费信息)】的数据库操作Service
 * @createDate 2023-04-09 10:35:08
 */
public interface InterfaceChargingService extends IService<InterfaceCharging> {

    /**
     * 添加接口计费信息
     *
     * @param interfaceChargingRequest 接口计费信息请求数据
     * @return id
     */
    Long addInterfaceCharging(InterfaceChargingRequest interfaceChargingRequest);

    /**
     * 根据接口 id 获取接口计费信息
     *
     * @param interfaceId 接口 id
     * @return 接口计费信息
     */
    InterfaceCharging getInterfaceChargingByInterfaceId(Long interfaceId);

    /**
     * 更新接口计费信息
     *
     * @param interfaceChargingRequest 接口计费信息请求
     */
    void updateInterfaceCharging(InterfaceChargingRequest interfaceChargingRequest);

}
