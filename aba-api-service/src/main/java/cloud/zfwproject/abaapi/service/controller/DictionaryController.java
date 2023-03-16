package cloud.zfwproject.abaapi.service.controller;

import cloud.zfwproject.abaapi.common.model.ResponseResult;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import cloud.zfwproject.abaapi.service.model.po.Dictionary;
import cloud.zfwproject.abaapi.service.service.DictionaryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/13 20:42
 */
@RestController
@RequestMapping("dictionary")
public class DictionaryController {

    @Resource
    private DictionaryService dictionaryService;

    @GetMapping
    public ResponseResult<List<Dictionary>> getAllDictionaries() {
        List<Dictionary> dictionaries = dictionaryService.list();
        return ResponseUtils.success(dictionaries);
    }
}
