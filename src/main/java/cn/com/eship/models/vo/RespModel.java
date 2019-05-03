package cn.com.eship.models.vo;

import java.io.Serializable;

public class RespModel implements Serializable {

    public RespModel(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private String code;
    private String message;
    private Object data;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
