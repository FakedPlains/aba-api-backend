package cloud.zfwproject.abaapi.service.mapper;

import cloud.zfwproject.abaapi.service.model.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 46029
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2023-03-12 13:44:10
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




