package com.baibei.shiyi.common.tool.validator2;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/27
 * @description: 验证器接口, 泛型T表示待验证对象的类型
 */
public interface Validator<T> {

    /**
     * 执行验证,如果验证失败,则抛出ValidateException异常
     *
     * @param context 验证上下文
     * @param t       待验证对象
     */
    void validate(ValidatorContext context, T t);
}
