package cloud.zfwproject.abaapi.service.mapper;


import cloud.zfwproject.abaapi.service.model.po.Dictionary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 46029
* @description 针对表【dictionary(数据字典表)】的数据库操作Mapper
* @createDate 2023-03-13 20:38:41
* @Entity generator.domain.Dictionary
*/
@Mapper
public interface DictionaryMapper extends BaseMapper<Dictionary> {

}




