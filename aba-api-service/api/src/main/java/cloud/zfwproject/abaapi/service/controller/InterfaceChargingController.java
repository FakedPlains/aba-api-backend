package cloud.zfwproject.abaapi.service.controller;

import cloud.zfwproject.abaapi.common.model.ResponseResult;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import cloud.zfwproject.abaapi.service.model.dto.interfacecharging.InterfaceChargingRequest;
import cloud.zfwproject.abaapi.service.service.InterfaceChargingService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 46029
 * @version 1.0
 * @description 接口计费相关控制器
 * @date 2023/4/9 10:45
 */
@RestController
@RequestMapping("interface/charging")
public class InterfaceChargingController {

    @Resource
    private InterfaceChargingService interfaceChargingService;

    /**
     * 添加接口计费信息
     *
     * @param interfaceChargingRequest 接口计费信息请求数据
     * @return id
     */
    public ResponseResult<Long> addInterfaceCharging(@RequestBody @Validated InterfaceChargingRequest interfaceChargingRequest) {
        Long id = interfaceChargingService.addInterfaceCharging(interfaceChargingRequest);
        return ResponseUtils.success(id);
    }

}
