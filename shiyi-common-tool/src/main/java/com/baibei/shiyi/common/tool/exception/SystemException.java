package com.baibei.shiyi.common.tool.exception;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/23 15:47
 * @description: 系统异常
 */
public class SystemException extends RuntimeException {

    public SystemException() {
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }
}