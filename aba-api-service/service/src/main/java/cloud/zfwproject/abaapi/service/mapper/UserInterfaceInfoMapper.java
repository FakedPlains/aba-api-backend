package cloud.zfwproject.abaapi.service.mapper;

import cloud.zfwproject.abaapi.service.model.po.UserInterfaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 46029
 * @description 针对表【user_interface_info(用户接口关系表)】的数据库操作Mapper
 * @createDate 2023-03-12 21:18:58
 * @Entity generator.domain.UserInterfaceInfo
 */
@Mapper
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    /**
     * 根据接口 id 查询总调用次数
     *
     * @param interfaceInfoId 接口 id
     * @return 总调用次数
     */
    Long selectInvokeCountByInterfaceId(@Param("interfaceId") Long interfaceInfoId);

    /**
     * 增加接口调用次数
     *
     * @param userId      用户 id
     * @param interfaceId 接口 id
     * @param count       调用次数
     * @return 返回数据库受影响行数
     */
    Long increaseInvokeCount(@Param("userId") Long userId, @Param("interfaceId") Long interfaceId, @Param("count") Long count);
}




