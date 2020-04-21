package com.baibei.shiyi.user.feign.bean.vo;

import lombok.Data;

/**
 * @Classname RealnameInfoVo
 * @Description 用户实名信息
 * @Date 2019/11/28 17:56
 * @Created by Longer
 */
@Data
public class RealnameInfoVo {
    private String customerNo;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 身份证号
     */
    private String idcard;

    /**
     * 银行卡号
     */
    private String bankCard;

    /**
     * 银行预留手机号
     */
    private String bankMobile;

    /**
     * 总行名称
     */
    private String bankName;

    /**
     * 支行名称
     */
    private String branchName;

}
