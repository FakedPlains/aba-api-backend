package cloud.zfwproject.abaapi.service.test;

import cloud.zfwproject.abaapi.service.service.UserInterfaceInfoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/9 11:04
 */
@SpringBootTest
public class TestUserInterfaceInfo {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Test
    void testGetInvokeCountByInterfaceId() {
        long count = userInterfaceInfoService.getInvokeCountByInterfaceId(2L);
        Assertions.assertEquals(0, count);
    }

}
