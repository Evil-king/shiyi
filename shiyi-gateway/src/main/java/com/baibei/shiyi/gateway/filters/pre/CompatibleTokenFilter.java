package com.baibei.shiyi.gateway.filters.pre;

import com.baibei.component.redis.util.RedisUtil;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.RedisConstant;
import com.baibei.shiyi.gateway.dto.JsonRequest;
import com.baibei.shiyi.gateway.utils.FilterUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/22 16:32
 * @description: 为兼容某些接口在客户登录的情况下就需要获取到客户信息，客户不登录的情况下不用获取客户信息
 */
@Slf4j
public class CompatibleTokenFilter extends ZuulFilter {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 4;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Boolean shouldFilter = (Boolean) ctx.get("shouldFilter");
        String servletPath = ctx.getRequest().getServletPath();
        if (shouldFilter != null) {
            shouldFilter = shouldFilter && !servletPath.startsWith("/auth");
        } else {
            shouldFilter = !servletPath.startsWith("/auth");
        }
        return shouldFilter;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("CompatibleTokenFilter is running...");
        RequestContext ctx = RequestContext.getCurrentContext();
        JsonRequest jsonRequest = (JsonRequest) ctx.get("jsonRequest");
        try {
            // 没有带token则不校验
            String accessToken = jsonRequest.getAccessToken();
            if (StringUtils.isEmpty(accessToken)) {
                return null;
            }
            // 根据token获取客户编号
            String redisKey = MessageFormat.format(RedisConstant.PREFIX_TOKEN_CODE, accessToken);
            String customerNo = redisUtil.get(redisKey);
            if (StringUtils.isEmpty(customerNo)) {
                FilterUtil.terminateFilter(new ApiResult(ResultEnum.ACCESS_TOKEN_ERROE.getCode(), "令牌不存在"));
                return null;
            }
            // 获取客户token缓存信息
            String tokenKey = MessageFormat.format(RedisConstant.PREFIX_USER_TOKEN, customerNo);
            Map<String, Object> tokenMap = redisUtil.hgetAll(tokenKey);
            if (CollectionUtils.isEmpty(tokenMap)) {
                FilterUtil.terminateFilter(new ApiResult(ResultEnum.ACCESS_TOKEN_ERROE.getCode(), "令牌不存在"));
                return null;
            }
            Object accessTokenFromRedis = tokenMap.get("accessToken");
            Object accessTokenExpireTime = tokenMap.get("accessTokenExpireTime");
            if (accessTokenFromRedis == null || accessTokenExpireTime == null) {
                FilterUtil.terminateFilter(new ApiResult(ResultEnum.ACCESS_TOKEN_ERROE.getCode(), "令牌不匹配"));
                return null;
            }
            long expireTime = Long.parseLong(accessTokenExpireTime.toString());
            long now = new Date().getTime();
            if (expireTime < now) {
                FilterUtil.terminateFilter(new ApiResult(ResultEnum.ACCESS_TOKEN_ERROE.getCode(), "令牌已过期"));
                return null;
            }
            ctx.put("customerNo", customerNo);
        } catch (Exception e) {
            e.printStackTrace();
            FilterUtil.terminateFilter(ApiResult.error("网关异常"));
            return null;
        }
        return null;
    }
}