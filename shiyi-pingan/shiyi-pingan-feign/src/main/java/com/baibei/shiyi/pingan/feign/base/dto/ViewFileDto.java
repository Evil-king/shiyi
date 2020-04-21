package com.baibei.shiyi.pingan.feign.base.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 交易网查看文件
 */
@Data
public class ViewFileDto {

    /**
     * 功能标识 1、清算失败文件 2、会员余额文件 3、出入金流水文件 4、会员开销户文件 5、对账不平记录文件
     */
    @Size(min = 1, max = 1)
    private String funcFlag;

    /**
     *
     */
    @Size(max = 32)
    @NotNull
    private String fileName;

    /**
     * 文件密码
     */
    @Size(max = 120)
    @NotNull
    private String reserve;

    private String supAcctId; // 资金汇总账号
}
