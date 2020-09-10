package com.hwm.together.internet;

/**
 * <pre>
 *     @author: hanweiming
 *     time: 2020/8/11
 *     desc:
 *     version: 1.0
 * </pre>
 */
public class BaseResponse {
    private String code;
    private String message;
    private String data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
