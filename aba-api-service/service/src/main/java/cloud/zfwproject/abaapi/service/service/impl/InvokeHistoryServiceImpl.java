package cloud.zfwproject.abaapi.service.service.impl;


import cloud.zfwproject.abaapi.service.mapper.InvokeHistoryMapper;
import cloud.zfwproject.abaapi.service.model.po.InvokeHistory;
import cloud.zfwproject.abaapi.service.service.InvokeHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author 46029
* @description 针对表【invoke_history(调用历史记录表)】的数据库操作Service实现
* @createDate 2023-04-09 10:35:22
*/
@Service("invokeHistoryService")
public class InvokeHistoryServiceImpl extends ServiceImpl<InvokeHistoryMapper, InvokeHistory>
    implements InvokeHistoryService {

}




