package cloud.zfwproject.abaapitestclient.controller;

import cloud.zfwproject.abaapi.client.sdk.client.AbaApiClient;
import cloud.zfwproject.abaapi.client.sdk.model.GetRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/21 11:19
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Resource
    private AbaApiClient abaApiClient;

    @GetMapping
    public String test() {
        GetRequest getRequest = new GetRequest();
        getRequest.setName("test");
        return abaApiClient.get(getRequest);
    }
}
