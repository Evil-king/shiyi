package com.baibei.shiyi.admin.modules.security.config;

import com.alibaba.fastjson.JSON;
import com.baibei.shiyi.common.core.config.MyWebMvcConfigurer;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.validator2.ValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * admin 权限自定义控制
 */
@Configuration
@EnableWebMvc
@Slf4j
public class AdminWebMvcConfigurer implements WebMvcConfigurer {

    private MyWebMvcConfigurer myWebMvcConfigurer = new MyWebMvcConfigurer();

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        myWebMvcConfigurer.configureMessageConverters(converters);
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add((httpServletRequest, httpServletResponse, o, e) -> {
            ApiResult apiResult;
            // Hibernate Validator参数校验异常
            if (e instanceof MethodArgumentNotValidException) {
                MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
                String msg = methodArgumentNotValidException.getBindingResult().getAllErrors().get(0).getDefaultMessage();
                log.error("参数校验失败>>>{}", msg);
                apiResult = ApiResult.badParam(msg);
            } else if (e instanceof ServiceException) {
                ServiceException serviceException = (ServiceException) e;
                Integer code = serviceException.getCode() == null ? ResultEnum.BUSINESS_ERROE.getCode() : serviceException.getCode();
                log.error("业务异常,code={},msg={}", code, serviceException.getMessage());
                apiResult = new ApiResult(code, serviceException.getMessage());
            } else if (e instanceof ValidateException) {
                ValidateException validateException = (ValidateException) e;
                Integer code = validateException.getCode() == null ? ResultEnum.BUSINESS_ERROE.getCode() : validateException.getCode();
                log.error("业务异常,code={},msg={}", code, validateException.getMessage());
                apiResult = new ApiResult(code, validateException.getMessage());
            } else if (e instanceof AccessDeniedException) {
                log.error("权限异常,code={},msg={}", ResultEnum.ADMIN_SC_UNAUTHORIZED.getCode(), e.getMessage());
                apiResult = new ApiResult(ResultEnum.ADMIN_SC_UNAUTHORIZED.getCode(), ResultEnum.ADMIN_SC_UNAUTHORIZED.getMsg(), null);
            } else if (e instanceof SystemException) {
                log.error("系统异常", e);
                apiResult = ApiResult.error("系统异常");
            } else {
                apiResult = ApiResult.error();
                e.printStackTrace();
            }
            responseResult(httpServletResponse, apiResult);
            return new ModelAndView();
        });
    }

    private void responseResult(HttpServletResponse response, ApiResult result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
