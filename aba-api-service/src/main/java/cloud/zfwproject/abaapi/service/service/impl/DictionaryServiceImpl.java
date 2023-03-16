package cloud.zfwproject.abaapi.service.service.impl;


import cloud.zfwproject.abaapi.service.mapper.DictionaryMapper;
import cloud.zfwproject.abaapi.service.model.po.Dictionary;
import cloud.zfwproject.abaapi.service.service.DictionaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author 46029
* @description 针对表【dictionary(数据字典表)】的数据库操作Service实现
* @createDate 2023-03-13 20:38:41
*/
@Service("dictionaryService")
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary>
    implements DictionaryService {

}




