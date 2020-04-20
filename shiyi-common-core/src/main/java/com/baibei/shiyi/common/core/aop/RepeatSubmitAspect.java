package com.baibei.shiyi.common.core.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baibei.component.redis.util.DistributedLock;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/5 14:44
 * @description: 防重复提交切面处理，基于Redis分布式锁实现，利用客户编号+请求参数+请求uri进行MD5加密后作为key
 */
@Aspect
@Component
@Slf4j
public class RepeatSubmitAspect {
    @Autowired
    private DistributedLock distributedLock;

    @Pointcut("@annotation(noRepeatSubmit)")
    public void pointCut(NoRepeatSubmit noRepeatSubmit) {
    }

    @Around("pointCut(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint pjp, NoRepeatSubmit noRepeatSubmit) throws Throwable {
        int lockSeconds = noRepeatSubmit.lockTime();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String uri = request.getServletPath();
        String method = request.getMethod();
        String customerNo;
        Object result;
        if ("POST".equals(method)) {
            Object[] args = pjp.getArgs();
            JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(args));
            if (jsonArray == null) {
                log.warn("接口{}无法获取客户编号", uri);
                result = pjp.proceed();
                return result;
            }
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            customerNo = jsonObject.getString("customerNo");
            if (StringUtils.isEmpty(customerNo)) {
                log.warn("接口{}无法获取客户编号", uri);
                result = pjp.proceed();
                return result;
            }
            String key = MD5Util.md5Hex(new StringBuffer(customerNo).append(JSON.toJSON(args)).append(uri).toString());
            String value = UUID.randomUUID().toString();
            boolean lockFlag = distributedLock.getLock(key, value, lockSeconds);
            if (lockFlag) {
                try {
                    result = pjp.proceed();
                } finally {
                    distributedLock.releaseLock(key, value);
                }
            } else {
                log.info("重复提交，获取锁失败，uri={}，customerNo={}", uri, customerNo);
                return ApiResult.repeatSubmit();
            }
        } else {
            log.info("暂不支持的请求方式");
            result = pjp.proceed();
        }
        return result;
    }

}