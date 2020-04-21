package com.baibei.shiyi.user.feign.bean.vo;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Classname RealnameVerificationDto
 * @Description 储蓄卡验证dto
 * @Date 2019/11/28 14:24
 * @Created by Longer
 */
@Data
public class BankCardVerificationVo {
    /**
     * 卡类型（DC=储蓄卡；CC=信用卡；SCC=准贷记卡；PC=预付费卡）
     */
    private String cardType;

    /**
     * 银行
     */
    private String bank;

    private String key;

    private Boolean validated;

    private String stat;

    /**
     * 是否是储蓄卡标识（true=是；false=不是）
     */
    private boolean cashCardFlag=false;

    public boolean isCashCardFlag(){
        if ("DC".equals(getCardType())) {
            return true;
        }else{
            return false;
        }
    }
}
