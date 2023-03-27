package cloud.zfwproject.abaapi.common.util;


import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.common.model.ResponseResult;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON;


/**
 * @author 46029
 * @version 1.0
 * @description 响应结果工具类
 * @date 2023/3/6 14:04
 */
public class ResponseUtils {

    /**
     * 包装成功响应结果
     *
     * @param <T> 响应数据类型
     * @return 响应结果
     */
    public static <T> ResponseResult<T> success() {
        return success(null);
    }

    public static <T> ResponseResult<T> success(T data) {
        return ResponseResult.<T>builder()
                .success(true)
                .data(data)
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 包装失败响应结果
     *
     * @param message 错误信息
     * @return 响应结果
     */
    public static ResponseResult<Void> fail(String message) {
        return fail(ResponseCode.FAIL.getCode(), message);
    }

    /**
     * 包装失败响应结果
     *
     * @param message 错误信息
     * @return 响应结果
     */
    public static ResponseResult<Void> fail(String code, String message) {
        return ResponseResult.<Void>builder()
                .success(false)
                .code(code)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 包装失败响应结果
     *
     * @param responseCode 错误状态
     * @return 响应结果
     */
    public static ResponseResult<Void> fail(ResponseCode responseCode) {
        return fail(responseCode.getCode(), responseCode.getMessage());
    }

    /**
     * 包装失败响应结果
     *
     * @param responseCode 错误状态
     * @return 响应结果
     */
    public static ResponseResult<Void> fail(ResponseCode responseCode, String message) {
        return fail(responseCode.getCode(), message);
    }


    public static <T> void out(HttpServletResponse response, ResponseResult<T> result) {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(String.valueOf(APPLICATION_JSON));
        try {
            mapper.writeValue(response.getWriter(), result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Mono<Void> outFailed(ServerHttpResponse response, ResponseResult<Void> result) {
        byte[] bytes = JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        // 指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        // 输出http响应
        return response.writeWith(Mono.just(buffer));
    }

}
