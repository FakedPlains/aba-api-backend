package cloud.zfwproject.abaapi.service.service;

import cloud.zfwproject.abaapi.service.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import cloud.zfwproject.abaapi.service.model.dto.interfaceinfo.InterfaceInfoInvokeDTO;
import cloud.zfwproject.abaapi.service.model.dto.interfaceinfo.InterfaceInfoQueryDTO;
import cloud.zfwproject.abaapi.service.model.dto.interfaceinfo.InterfaceInfoUpdateDTO;
import cloud.zfwproject.abaapi.service.model.po.InterfaceInfo;
import cloud.zfwproject.abaapi.service.model.vo.InterfaceInfoVO;
import cloud.zfwproject.abaapi.service.model.vo.InterfaceInvokeVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 46029
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service
 * @createDate 2023-03-12 21:18:55
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 分页获取接口列表
     *
     * @param interfaceInfoQueryDTO 接口查询书籍
     * @return 接口分页列表
     */
    Page<InterfaceInfoVO> getInterfaceInfoPages(InterfaceInfoQueryDTO interfaceInfoQueryDTO);

    /**
     * 创建
     *
     * @param interfaceInfoAddDTO 接口创建数据
     * @return 返回接口 id
     */
    Long addInterfaceInfo(InterfaceInfoAddRequest interfaceInfoAddDTO);

    /**
     * 删除
     *
     * @param id 接口 id
     * @return 是否成功
     */
    boolean deleteInterfaceInfo(Long id);

    boolean updateInterfaceInfo(InterfaceInfoUpdateDTO interfaceInfoUpdateDTO);

    /**
     * 根据 id 获取
     *
     * @param id 接口 id
     * @return 接口信息
     */
    InterfaceInfoVO getInterfaceInfoById(long id);

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceInfoQueryDTO 接口查询数据
     * @return 接口列表
     */
    List<InterfaceInfo> listInterfaceInfo(InterfaceInfoQueryDTO interfaceInfoQueryDTO);

    /**
     * 根据 dataId 获取接口信息
     * @param dataId md5(method:url)
     * @return 接口信息
     */
    InterfaceInfo getInterfaceInfoByDataId(String dataId);

    /**
     * 发布
     *
     * @param id 接口 id
     * @return 是否成功
     */
    boolean onlineInterfaceInfo(Long id);

    /**
     * 下线
     *
     * @param id 接口 id
     * @return 是否成功
     */
    boolean offlineInterfaceInfo(Long id);

    /**
     * 分页获取展示接口列表
     *
     * @param interfaceInfoQueryDTO 接口查询书籍
     * @return 接口分页列表
     */
    Page<InterfaceInfoVO> getShowingInterfaceInfo(InterfaceInfoQueryDTO interfaceInfoQueryDTO);

    /**
     * 测试调用
     *
     * @param invokeRequest 调用请求
     * @return 响应数据
     */
    InterfaceInvokeVO invokeInterface(InterfaceInfoInvokeDTO invokeRequest);
}
