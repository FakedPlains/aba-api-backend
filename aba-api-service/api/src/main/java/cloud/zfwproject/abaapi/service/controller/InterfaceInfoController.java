package cloud.zfwproject.abaapi.service.controller;

import cloud.zfwproject.abaapi.common.model.ResponseResult;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import cloud.zfwproject.abaapi.service.model.dto.DeleteDTO;
import cloud.zfwproject.abaapi.service.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import cloud.zfwproject.abaapi.service.model.dto.interfaceinfo.InterfaceInfoInvokeDTO;
import cloud.zfwproject.abaapi.service.model.dto.interfaceinfo.InterfaceInfoQueryDTO;
import cloud.zfwproject.abaapi.service.model.dto.interfaceinfo.InterfaceInfoUpdateDTO;
import cloud.zfwproject.abaapi.service.model.po.InterfaceInfo;
import cloud.zfwproject.abaapi.service.model.vo.InterfaceInfoVO;
import cloud.zfwproject.abaapi.service.model.vo.InterfaceInvokeVO;
import cloud.zfwproject.abaapi.service.service.InterfaceInfoService;
import cloud.zfwproject.abaapi.service.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/13 19:44
 */
@RestController
@RequestMapping("interface/info")
public class InterfaceInfoController {

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 创建
     *
     * @param interfaceInfoAddDTO 接口创建数据
     * @return 返回接口 id
     */
    @PostMapping()
    public ResponseResult<Long> addInterfaceInfo(@Validated @RequestBody InterfaceInfoAddRequest interfaceInfoAddDTO) {
        Long id = interfaceInfoService.addInterfaceInfo(interfaceInfoAddDTO);
        return ResponseUtils.success(id);
    }

    /**
     * 删除
     *
     * @param deleteDTO 删除数据 id
     * @return 是否成功
     */
    @DeleteMapping()
    public ResponseResult<Boolean> deleteInterfaceInfo(@Validated @RequestBody DeleteDTO deleteDTO) {
        interfaceInfoService.deleteInterfaceInfo(deleteDTO.getId());
        return ResponseUtils.success(true);
    }

    /**
     * 更新
     *
     * @param interfaceInfoUpdateDTO 接口更新数据
     * @return 是否成功
     */
    @PutMapping()
    public ResponseResult<Boolean> updateInterfaceInfo(@Validated @RequestBody InterfaceInfoUpdateDTO interfaceInfoUpdateDTO) {
        boolean result = interfaceInfoService.updateInterfaceInfo(interfaceInfoUpdateDTO);
        return ResponseUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id 接口 id
     * @return 接口信息
     */
    @GetMapping("/{id}")
    public ResponseResult<InterfaceInfoVO> getInterfaceInfoById(@PathVariable long id) {
        InterfaceInfoVO interfaceInfo = interfaceInfoService.getInterfaceInfoById(id);
        return ResponseUtils.success(interfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceInfoQueryDTO 接口查询数据
     * @return 接口列表
     */
//    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public ResponseResult<List<InterfaceInfo>> listInterfaceInfo(@Validated InterfaceInfoQueryDTO interfaceInfoQueryDTO) {
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.listInterfaceInfo(interfaceInfoQueryDTO);
        return ResponseUtils.success(interfaceInfoList);
    }


    /**
     * 分页获取接口列表
     *
     * @param interfaceInfoQueryDTO 接口查询数据
     * @return 接口分页列表
     */
    @GetMapping("/show")
    public ResponseResult<Page<InterfaceInfoVO>> getInterfaceInfoPages(@Validated InterfaceInfoQueryDTO interfaceInfoQueryDTO) {
        Page<InterfaceInfoVO> page = interfaceInfoService.getInterfaceInfoPages(interfaceInfoQueryDTO);
        return ResponseUtils.success(page);
    }

    /**
     * 分页获取展示接口列表
     *
     * @param interfaceInfoQueryDTO 接口查询数据
     * @return 接口分页列表
     */
    @GetMapping("/page")
    public ResponseResult<Page<InterfaceInfoVO>> getShowingInterfaceInfo(@Validated InterfaceInfoQueryDTO interfaceInfoQueryDTO) {
        Page<InterfaceInfoVO> page = interfaceInfoService.getShowingInterfaceInfo(interfaceInfoQueryDTO);
        return ResponseUtils.success(page);
    }


    /**
     * 发布
     *
     * @param id 接口 id
     * @return 是否成功
     */
    @PostMapping("/{id}/online")
    public ResponseResult<Boolean> onlineInterfaceInfo(@PathVariable Long id) {
        boolean result = interfaceInfoService.onlineInterfaceInfo(id);
        return ResponseUtils.success(result);
    }

    /**
     * 下线
     *
     * @param id 接口 id
     * @return 是否成功
     */
    @PostMapping("/{id}/offline")
    public ResponseResult<Boolean> offlineInterfaceInfo(@PathVariable Long id) {
        boolean result = interfaceInfoService.offlineInterfaceInfo(id);
        return ResponseUtils.success(result);
    }

    /**
     * 测试调用
     *
     * @param invokeRequest 调用请求
     * @return 响应数据
     */
    @PostMapping("/invoke")
    public ResponseResult<InterfaceInvokeVO> invokeInterface(@RequestBody @Validated InterfaceInfoInvokeDTO invokeRequest) {
        InterfaceInvokeVO result = interfaceInfoService.invokeInterface(invokeRequest);
        return ResponseUtils.success(result);
    }

}
