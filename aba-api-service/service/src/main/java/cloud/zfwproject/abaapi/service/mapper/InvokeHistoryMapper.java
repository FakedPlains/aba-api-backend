package cloud.zfwproject.abaapi.service.mapper;


import cloud.zfwproject.abaapi.service.model.po.InvokeHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 46029
* @description 针对表【invoke_history(调用历史记录表)】的数据库操作Mapper
* @createDate 2023-04-09 10:35:21
* @Entity generator.domain.InvokeHistory
*/
@Mapper
public interface InvokeHistoryMapper extends BaseMapper<InvokeHistory> {

}




