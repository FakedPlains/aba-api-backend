package cloud.zfwproject.abaapi.server.controller;

import cloud.zfwproject.abaapi.server.model.PostDTO;
import org.springframework.web.bind.annotation.*;

/**
 * @author 46029
 * @version 1.0
 * @description TODO
 * @date 2023/3/16 21:41
 */
@RestController
@RequestMapping("test")
public class TestController {
    @GetMapping
    public String get(String name) {
        return "get " + name;
    }

    @PostMapping
    public String post(@RequestBody PostDTO postDTO) {
        return "post " + postDTO.getName();
    }

    @DeleteMapping("{id}")
    public boolean delete(@PathVariable Long id) {
        return id >= 10;
    }
}
