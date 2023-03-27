package cloud.zfwproject.abaapi.service.service.impl;


import cloud.zfwproject.abaapi.service.mapper.DictDataMapper;
import cloud.zfwproject.abaapi.service.model.po.DictData;
import cloud.zfwproject.abaapi.service.service.DictDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 46029
 * @description 针对表【dict_data(字典数据表)】的数据库操作Service实现
 * @createDate 2023-03-27 15:27:00
 */
@Service("dictDataService")
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData>
        implements DictDataService {

    @Override
    public List<DictData> getDictDataByType(Long typeId) {
        return this.lambdaQuery()
                .eq(typeId != null && typeId != 0, DictData::getDictTypeId, typeId)
                .list();
    }

}




