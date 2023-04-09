package cloud.zfwproject.abaapi.service.service.impl;


import cloud.zfwproject.abaapi.service.mapper.UserInterfaceOrderMapper;
import cloud.zfwproject.abaapi.service.model.po.UserInterfaceOrder;
import cloud.zfwproject.abaapi.service.service.UserInterfaceOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author 46029
* @description 针对表【user_interface_order(用户购买接口表)】的数据库操作Service实现
* @createDate 2023-04-09 10:35:14
*/
@Service("userInterfaceOrderService")
public class UserInterfaceOrderServiceImpl extends ServiceImpl<UserInterfaceOrderMapper, UserInterfaceOrder>
    implements UserInterfaceOrderService {

}




