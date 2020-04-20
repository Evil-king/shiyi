package com.baibei.shiyi.account.common.vo;

import com.baibei.shiyi.content.feign.bean.vo.ConsignmentGoodsVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ConsignmentListVo {
    private BigDecimal price;

    private List<ConsignmentGoodsVo> consignmentGoodsVoList;

    private String consignmentTime;

    private BigDecimal fee;

    private BigDecimal fees;
}
