package cloud.zfwproject.abaapi.common.exception;


import cloud.zfwproject.abaapi.common.model.ResponseCode;

/**
 * @author 46029
 * @version 1.0
 * @description 自定义异常类
 * @date 2023/3/6 14:38
 */
public class BusinessException extends RuntimeException {
    private String code;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
        this.code = ResponseCode.FAIL.getCode();
    }

    public BusinessException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.code = responseCode.getCode();
    }

    public BusinessException(ResponseCode responseCode, String message) {
        super(message);
        this.code = responseCode.getCode();
    }

    public String getCode() {
        return code;
    }
}
