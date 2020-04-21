package com.baibei.shiyi.user.common.vo;

import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/7/30 10:08
 * @description:
 */
@Data
public class CustomerTokenVo extends CustomerVo {
    /**
     * 用户token
     */
    private String accessToken;
    /**
     * 刷新用户token的token
     */
    private String refreshToken;
}
