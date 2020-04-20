package com.baibei.shiyi.common.tool.validator2;


import com.baibei.shiyi.common.tool.api.ResultEnum;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/27
 * @description: 验证失败异常
 */
public class ValidateException extends RuntimeException {
    // 异常码
    private Integer code;

    public ValidateException() {
    }

    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
