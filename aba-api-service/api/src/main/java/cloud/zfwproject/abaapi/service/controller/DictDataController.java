package cloud.zfwproject.abaapi.service.controller;

import cloud.zfwproject.abaapi.common.model.ResponseResult;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import cloud.zfwproject.abaapi.service.model.po.DictData;
import cloud.zfwproject.abaapi.service.service.DictDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class DictDataController {

    @Resource
    private DictDataService dictDataService;

    @GetMapping("{typeId}/data")
    public ResponseResult<List<DictData>> getDictDataByType(@PathVariable Long typeId) {
        List<DictData> dictData = dictDataService.getDictDataByType(typeId);
        return ResponseUtils.success(dictData);
    }

}
