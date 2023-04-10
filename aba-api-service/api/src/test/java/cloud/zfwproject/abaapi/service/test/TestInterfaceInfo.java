package cloud.zfwproject.abaapi.service.test;

import cloud.zfwproject.abaapi.service.service.InterfaceInfoService;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/4/10 16:30
 */
@SpringBootTest
public class TestInterfaceInfo {
    @Resource
    private InterfaceInfoService interfaceInfoService;

    @RepeatedTest(value = 10)
    void testGetInterfaceInfoById() {
        long start = System.currentTimeMillis();
        interfaceInfoService.getInterfaceInfoById(1L);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
