package com.baibei.shiyi.user.common.vo;

import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/12/4 19:58
 * @description:
 */
@Data
public class ToOpenAccountVo {
    /**
     * 总行名称
     */
    private String bankName;
    /**
     * 是否需要分行
     */
    private Integer isFlag;
    /**
     * 总行编号
     */
    private String bankTotalNode;


}
