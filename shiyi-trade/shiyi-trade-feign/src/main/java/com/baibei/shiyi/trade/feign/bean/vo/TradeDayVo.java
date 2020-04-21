package com.baibei.shiyi.trade.feign.bean.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/7 9:49
 * @description:
 */
@Data
public class TradeDayVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 交易日日期
     */
    private Date tradeDay;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * morning=上午，afternoon=下午
     */
    private String period;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 是否删除(1:正常，0:删除)
     */
    private Byte flag;
}