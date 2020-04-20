package com.baibei.shiyi.admin.modules.security.config;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        /**
         * 当用户尝试访问安全的REST资源而不提供任何凭据时，将调用此方法发送权限不足的响应
         */
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpStatus.OK.value());
        PrintWriter writer = response.getWriter();
        ApiResult apiResult = new ApiResult(ResultEnum.ADMIN_SC_UNAUTHORIZED.getCode(), ResultEnum.ADMIN_SC_UNAUTHORIZED.getMsg(),null);
        writer.write(JSONObject.toJSONString(apiResult));
    }
}
