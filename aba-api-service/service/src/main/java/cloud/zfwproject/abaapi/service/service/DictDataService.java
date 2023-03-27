package cloud.zfwproject.abaapi.service.service;


import cloud.zfwproject.abaapi.service.model.po.DictData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 46029
* @description 针对表【dict_data(字典数据表)】的数据库操作Service
* @createDate 2023-03-27 15:27:00
*/
public interface DictDataService extends IService<DictData> {

    List<DictData> getDictDataByType(Long typeId);
}
