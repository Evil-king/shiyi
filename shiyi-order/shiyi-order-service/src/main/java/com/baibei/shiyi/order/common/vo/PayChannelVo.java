package com.baibei.shiyi.order.common.vo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/27 14:59
 * @description:
 */
@Data
public class PayChannelVo {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 渠道编码
     */
    private String channelCode;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 渠道图片地址
     */
    private String imgUrl;

    private Byte asDefault;
}