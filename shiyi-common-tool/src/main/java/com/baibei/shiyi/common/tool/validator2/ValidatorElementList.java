package com.baibei.shiyi.common.tool.validator2;

import java.util.LinkedList;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/27
 * @description: 在FluentValidator内部调用使用的验证器链
 */
public class ValidatorElementList {
    /**
     * 验证器链表
     */
    private LinkedList<ValidatorElement> validatorElementLinkedList = new LinkedList<>();

    /**
     * 将验证器加入链表
     *
     * @param element
     */
    public void add(ValidatorElement element) {
        validatorElementLinkedList.add(element);
    }

    /**
     * 获取验证器链表
     *
     * @return
     */
    public LinkedList<ValidatorElement> getList() {
        return validatorElementLinkedList;
    }

    /**
     * 验证器链表是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return validatorElementLinkedList.isEmpty();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (ValidatorElement element : validatorElementLinkedList) {
            sb.append("[");
            sb.append(element.getValidator().getClass().getSimpleName());
            sb.append("]->");
        }
        return sb.toString();
    }
}
