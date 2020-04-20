package com.baibei.shiyi.cash.feign.base.vo;

import lombok.Data;

/**
 * @Classname AddMoneyFlagVo
 * @Description 是否需要异步加钱
 * @Date 2019/11/5 19:21
 * @Created by Longer
 */
@Data
public class AddMoneyFlagVo {
    /**
     * 是否需要加钱（true=需要；false=不需要）
     */
    private boolean addMoneyFlag;
    private Object o;
}
