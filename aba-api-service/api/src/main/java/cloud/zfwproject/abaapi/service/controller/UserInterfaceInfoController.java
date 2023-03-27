package cloud.zfwproject.abaapi.service.controller;


import cloud.zfwproject.abaapi.common.model.ResponseResult;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import cloud.zfwproject.abaapi.service.model.dto.DeleteDTO;
import cloud.zfwproject.abaapi.service.model.dto.userinterfaceinfo.UserInterfaceInfoAddDTO;
import cloud.zfwproject.abaapi.service.model.dto.userinterfaceinfo.UserInterfaceInfoQueryDTO;
import cloud.zfwproject.abaapi.service.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateDTO;
import cloud.zfwproject.abaapi.service.model.po.UserInterfaceInfo;
import cloud.zfwproject.abaapi.service.service.UserInterfaceInfoService;
import cloud.zfwproject.abaapi.service.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 帖子接口
 *
 * @author yupi
 */
@RestController
@RequestMapping("/user-interface-info")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param userInterfaceInfoAddDTO 创建数据
     * @return id
     */
    @PostMapping()
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public ResponseResult<Long> addUserInterfaceInfo(@Validated @RequestBody UserInterfaceInfoAddDTO userInterfaceInfoAddDTO) {
        long id = userInterfaceInfoService.addUserInterfaceInfo(userInterfaceInfoAddDTO);
        return ResponseUtils.success(id);
    }

    /**
     * 删除
     *
     * @param deleteDTO 删除数据
     * @return 是否成功
     */
    @DeleteMapping()
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public ResponseResult<Boolean> deleteUserInterfaceInfo(@Validated @RequestBody DeleteDTO deleteDTO) {
        boolean b = userInterfaceInfoService.deleteUserInterfaceInfo(deleteDTO.getId());
        return ResponseUtils.success(b);
    }

    /**
     * 更新
     *
     * @param userInterfaceInfoUpdateDTO
     * @return
     */
    @PutMapping()
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public ResponseResult<Boolean> updateUserInterfaceInfo(@Validated @RequestBody UserInterfaceInfoUpdateDTO userInterfaceInfoUpdateDTO) {
        boolean result = userInterfaceInfoService.updateUserInterfaceInfo(userInterfaceInfoUpdateDTO);
        return ResponseUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id id
     * @return
     */
    @GetMapping("/{id}")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public ResponseResult<UserInterfaceInfo> getUserInterfaceInfoById(@PathVariable("id") long id) {
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getUserInterfaceInfoById(id);
        return ResponseUtils.success(userInterfaceInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param userInterfaceInfoQueryDTO
     * @return
     */
//    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public ResponseResult<List<UserInterfaceInfo>> listUserInterfaceInfo(@Validated UserInterfaceInfoQueryDTO userInterfaceInfoQueryDTO) {
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoService.listUserInterfaceInfo(userInterfaceInfoQueryDTO);
        return ResponseUtils.success(userInterfaceInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param userInterfaceInfoQueryDTO
     * @return
     */
    @GetMapping("/page")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public ResponseResult<Page<UserInterfaceInfo>> listUserInterfaceInfoByPage(@Validated UserInterfaceInfoQueryDTO userInterfaceInfoQueryDTO) {
        Page<UserInterfaceInfo> userInterfaceInfoPage = userInterfaceInfoService.listUserInterfaceInfoByPage(userInterfaceInfoQueryDTO);
        return ResponseUtils.success(userInterfaceInfoPage);
    }

    // endregion

}
