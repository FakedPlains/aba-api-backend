package cloud.zfwproject.abaapi.service.service.impl;


import cloud.zfwproject.abaapi.common.exception.BusinessException;
import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.common.model.SimpleUser;
import cloud.zfwproject.abaapi.common.util.UserHolder;
import cloud.zfwproject.abaapi.service.mapper.DictTypeMapper;
import cloud.zfwproject.abaapi.service.model.dto.dict.DictTypeAddRequest;
import cloud.zfwproject.abaapi.service.model.dto.dict.DictTypeUpdateRequest;
import cloud.zfwproject.abaapi.service.model.po.DictType;
import cloud.zfwproject.abaapi.service.service.DictTypeService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

/**
 * @author 46029
 * @description 针对表【dict_type(字典类型表)】的数据库操作Service实现
 * @createDate 2023-03-27 15:26:52
 */
@Validated
@Service("dictTypeService")
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType>
        implements DictTypeService {

    @Override
    public List<DictType> getAllDictType() {
        return this.list();
    }

    @Override
    public boolean addDictType(@Validated DictTypeAddRequest dictTypeAddRequest) {
        DictType dictType = new DictType();
        BeanUtil.copyProperties(dictTypeAddRequest, dictType);
        SimpleUser user = UserHolder.getUser();
        dictType.setUserId(user.getId());
        return this.save(dictType);
    }

    @Override
    public boolean deleteDictType(Long id) {
        if (id == null || id == 0) {
            throw new BusinessException(ResponseCode.INVALID_PARAMS, "数据不存在");
        }
        DictType dictType = this.lambdaQuery()
                .eq(DictType::getId, id)
                .select(DictType::getUserId)
                .one();
        if (dictType == null) {
            throw new BusinessException(ResponseCode.INVALID_PARAMS, "数据不存在");
        }
        if (!Objects.equals(dictType.getUserId(), UserHolder.getUser().getId())) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR, "无法删除");
        }
        return this.removeById(id);
    }

    @Override
    public boolean updateDictType(@Validated DictTypeUpdateRequest dictTypeUpdateRequest) {
        Long userId = UserHolder.getUser().getId();
        DictType dictType = new DictType();
        BeanUtil.copyProperties(dictTypeUpdateRequest, dictType);

        return this.lambdaUpdate()
                .eq(DictType::getUserId, userId)
                .setEntity(dictType)
                .update();
    }
}




