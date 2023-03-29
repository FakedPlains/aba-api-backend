package cloud.zfwproject.abaapi.service.controller;

import cloud.zfwproject.abaapi.common.exception.BusinessException;
import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.common.model.ResponseResult;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import cloud.zfwproject.abaapi.service.model.dto.DeleteDTO;
import cloud.zfwproject.abaapi.service.model.dto.dict.DictTypeAddRequest;
import cloud.zfwproject.abaapi.service.model.dto.dict.DictTypeUpdateRequest;
import cloud.zfwproject.abaapi.service.model.po.DictType;
import cloud.zfwproject.abaapi.service.service.DictTypeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/27 15:39
 */
@RestController
@RequestMapping("dict")
public class DictTypeController {

    @Resource
    private DictTypeService dictTypeService;

    @GetMapping("type/{typeId}")
    public ResponseResult<DictType> getDictTypeById(@PathVariable Long typeId) {
        DictType dictType = dictTypeService.getDictTypeById(typeId);
        return ResponseUtils.success(dictType);
    }

    @GetMapping("type")
    public ResponseResult<List<DictType>> getAllDictType() {
        List<DictType> dictTypes = dictTypeService.getAllDictType();
        return ResponseUtils.success(dictTypes);
    }

    @PostMapping("type")
    public ResponseResult<Boolean> addDictType(@RequestBody @Validated DictTypeAddRequest dictTypeAddRequest) {
        boolean result = dictTypeService.addDictType(dictTypeAddRequest);
        return ResponseUtils.success(result);
    }

    @DeleteMapping("type")
    public ResponseResult<Boolean> deleteDictType(@RequestBody @Validated DeleteDTO deleteDTO) {
        Long id = deleteDTO.getId();
        if (id == null || id == 0) {
            throw new BusinessException(ResponseCode.INVALID_PARAMS, "数据不存在");
        }
        boolean result = dictTypeService.deleteDictType(id);
        return ResponseUtils.success(result);
    }

    @PutMapping("type")
    public ResponseResult<Boolean> updateDictType(@RequestBody @Validated DictTypeUpdateRequest dictTypeUpdateRequest) {
        Long id = dictTypeUpdateRequest.getId();
        if (id == null || id == 0) {
            throw new BusinessException(ResponseCode.INVALID_PARAMS, "数据不存在");
        }
        boolean result = dictTypeService.updateDictType(dictTypeUpdateRequest);
        return ResponseUtils.success(result);
    }

}
