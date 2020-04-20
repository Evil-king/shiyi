package com.baibei.shiyi.common.tool.validator2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/27
 * @description: 链式调用验证器
 */
public class FluentValidator {
    private static final Logger logger = LoggerFactory.getLogger(FluentValidator.class);

    /**
     * 验证器链，惰性求值期间就是不断的改变这个链表，及时求值期间就是遍历链表依次执行验证
     */
    private ValidatorElementList validatorElementList = new ValidatorElementList();

    /**
     * 验证器上下文
     */
    private ValidatorContext context = new ValidatorContext();

    /**
     * 私有构造方法,只能通过checkAll创建对象
     */
    private FluentValidator() {
    }

    /**
     * 创建FluentValidator对象
     *
     * @return
     */
    public static FluentValidator checkAll() {
        return new FluentValidator();
    }

    /**
     * 使用验证器进行验证
     *
     * @param validator 验证器
     * @return
     */
    public <T> FluentValidator on(Validator<T> validator) {
        validatorElementList.add(new ValidatorElement(null, validator));
        return this;
    }

    /**
     * 使用验证器验证指定对象
     *
     * @param t         待验证对象
     * @param validator 验证器
     * @return
     */
    public <T> FluentValidator on(T t, Validator<T> validator) {
        validatorElementList.add(new ValidatorElement(t, validator));
        return this;
    }

    /**
     * 执行各个验证器中的验证逻辑
     *
     * @return
     */
    public FluentValidator doValidate() {
        if (validatorElementList.isEmpty()) {
            logger.info("Nothing to validate");
        }
        long start = System.currentTimeMillis();
        logger.info("Start to validate,validatorElementList={}", validatorElementList.toString());
        String validatorName;
        try {
            for (ValidatorElement element : validatorElementList.getList()) {
                Object target = element.getTarget();
                Validator validator = element.getValidator();
                validatorName = validator.getClass().getSimpleName();
                logger.info("{} is running", validatorName);
                validator.validate(context, target);
            }
        } catch (ValidateException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            logger.info("End to validate,time consuming {} ms", (System.currentTimeMillis() - start));
        }
        return this;
    }

    /**
     * 将键值对放入上下文
     *
     * @param key   键
     * @param value 值
     *
     * @return FluentValidator
     */
    public FluentValidator putAttribute2Context(String key, Object value) {
        if (context == null) {
            context = new ValidatorContext();
        }
        context.setAttribute(key, value);
        return this;
    }

    /**
     * 获取验证器上下文
     *
     * @return
     */
    public ValidatorContext getContext() {
        return context;
    }
}
