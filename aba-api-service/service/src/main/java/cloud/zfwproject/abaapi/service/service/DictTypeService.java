package cloud.zfwproject.abaapi.service.service;


import cloud.zfwproject.abaapi.service.model.dto.dict.DictTypeAddRequest;
import cloud.zfwproject.abaapi.service.model.dto.dict.DictTypeUpdateRequest;
import cloud.zfwproject.abaapi.service.model.po.DictType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 46029
* @description 针对表【dict_type(字典类型表)】的数据库操作Service
* @createDate 2023-03-27 15:26:52
*/
public interface DictTypeService extends IService<DictType> {

    List<DictType> getAllDictType();

    boolean addDictType(DictTypeAddRequest dictTypeAddRequest);

    boolean deleteDictType(Long id);

    boolean updateDictType(DictTypeUpdateRequest dictTypeUpdateRequest);

    DictType getDictTypeById(Long typeId);
}
