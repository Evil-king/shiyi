package com.baibei.shiyi.common.tool.validator2;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/27
 * @description: 验证器包装类
 */
public class ValidatorElement {
    /**
     * 待验证对象
     */
    private Object target;

    /**
     * 验证器
     */
    private Validator validator;

    public ValidatorElement(Object target, Validator validator) {
        this.target = target;
        this.validator = validator;
    }

    public Object getTarget() {
        return target;
    }

    public Validator getValidator() {
        return validator;
    }
}
