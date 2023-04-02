package cloud.zfwproject.abaapi.service.service;

import cloud.zfwproject.abaapi.service.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import cloud.zfwproject.abaapi.service.model.dto.interfaceinfo.InterfaceInfoQueryDTO;
import cloud.zfwproject.abaapi.service.model.dto.interfaceinfo.InterfaceInfoUpdateDTO;
import cloud.zfwproject.abaapi.service.model.po.InterfaceInfo;
import cloud.zfwproject.abaapi.service.model.vo.InterfaceInfoVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 46029
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service
 * @createDate 2023-03-12 21:18:55
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    Page<InterfaceInfo> getInterfaceInfoPages(InterfaceInfoQueryDTO interfaceInfoQueryDTO);

    /**
     * 创建
     *
     * @param interfaceInfoAddDTO 接口创建数据
     * @return 返回接口 id
     */
    Long addInterfaceInfo(InterfaceInfoAddRequest interfaceInfoAddDTO);

    boolean deleteInterfaceInfo(Long id);

    boolean updateInterfaceInfo(InterfaceInfoUpdateDTO interfaceInfoUpdateDTO);

    InterfaceInfoVO getInterfaceInfoById(long id);

    List<InterfaceInfo> listInterfaceInfo(InterfaceInfoQueryDTO interfaceInfoQueryDTO);

    /**
     * 根据 dataId 获取接口信息
     * @param dataId md5(method:url)
     * @return 接口 url
     */
    InterfaceInfo getInterfaceInfoByDataId(String dataId);

    boolean onlineInterfaceInfo(Long id);

    boolean offlineInterfaceInfo(Long id);
}
