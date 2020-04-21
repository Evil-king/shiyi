package com.baibei.shiyi.trade.common.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/8 15:44
 * @description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DealOrderBo {
    private String buyer;
    private BigDecimal buyActualAmount;
    private String seller;
    private BigDecimal sellActualAmount;
    private String dealNo;
    private Date dealTime;
}