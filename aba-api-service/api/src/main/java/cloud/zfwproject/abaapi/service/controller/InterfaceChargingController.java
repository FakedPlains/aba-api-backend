package cloud.zfwproject.abaapi.service.controller;

import cloud.zfwproject.abaapi.common.model.ResponseResult;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import cloud.zfwproject.abaapi.service.model.dto.interfacecharging.InterfaceChargingRequest;
import cloud.zfwproject.abaapi.service.model.po.InterfaceCharging;
import cloud.zfwproject.abaapi.service.service.InterfaceChargingService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
     * 根据接口 id 获取接口计费信息
     *
     * @param interfaceId 接口 id
     * @return 接口计费信息
     */
    @GetMapping("{interfaceId}")
    public ResponseResult<InterfaceCharging> getInterfaceChargingByInterfaceId(@PathVariable Long interfaceId) {
        InterfaceCharging interfaceCharging = interfaceChargingService.getInterfaceChargingByInterfaceId(interfaceId);
        return ResponseUtils.success(interfaceCharging);
    }

    /**
     * 添加接口计费信息
     *
     * @param interfaceChargingRequest 接口计费信息请求数据
     * @return id
     */
    @PostMapping
    public ResponseResult<Long> addInterfaceCharging(@RequestBody @Validated InterfaceChargingRequest interfaceChargingRequest) {
        Long id = interfaceChargingService.addInterfaceCharging(interfaceChargingRequest);
        return ResponseUtils.success(id);
    }

    /**
     * 更新接口计费信息
     *
     * @param interfaceChargingRequest 接口计费信息请求
     * @return
     */
    @PutMapping
    public ResponseResult<Void> updateInterfaceCharging(@RequestBody @Validated InterfaceChargingRequest interfaceChargingRequest) {
        interfaceChargingService.updateInterfaceCharging(interfaceChargingRequest);
        return ResponseUtils.success();
    }

}
