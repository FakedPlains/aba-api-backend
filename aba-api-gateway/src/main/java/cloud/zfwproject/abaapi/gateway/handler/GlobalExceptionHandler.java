package cloud.zfwproject.abaapi.gateway.handler;

import cloud.zfwproject.abaapi.common.exception.BusinessException;
import cloud.zfwproject.abaapi.common.model.ResponseCode;
import cloud.zfwproject.abaapi.common.model.ResponseResult;
import cloud.zfwproject.abaapi.common.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 46029
 * @version 1.0
 * @description 全局异常处理类
 * @date 2023/3/6 14:41
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理参数异常
     *
     * @param e 异常
     * @return 统一结果集
     */
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        log.warn("invalidParamsException: {}", errors);
        return ResponseUtils.fail(ResponseCode.INVALID_PARAMS.getCode(), errors.toString());
    }

    /**
     * 处理全局自定义异常
     *
     * @param e 全局自定义异常
     * @return 统一结果集
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseResult<?> processGlobalException(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResponseUtils.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理其他异常
     *
     * @param e 其他异常
     * @return 统一结果集
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult<?> processException(Exception e) {
        log.error("runtimeException", e);
        return ResponseUtils.fail(e.getMessage());
    }
}
