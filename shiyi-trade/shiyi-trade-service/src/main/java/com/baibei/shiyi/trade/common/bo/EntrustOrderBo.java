package com.baibei.shiyi.trade.common.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/13 14:39
 * @description:
 */
@Data
@Builder
public class EntrustOrderBo {
    private String entrustNo;
    private Date entrustTime;
}