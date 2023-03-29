package cloud.zfwproject.abaapi.service.mapper;

import cloud.zfwproject.abaapi.service.model.po.InterfaceParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 46029
* @description 针对表【interface_request_params(接口请求参数信息表)】的数据库操作Mapper
* @createDate 2023-03-13 19:39:47
* @Entity generator.domain.InterfaceRequestParams
*/
@Mapper
public interface InterfaceRequestParamsMapper extends BaseMapper<InterfaceParam> {

}




