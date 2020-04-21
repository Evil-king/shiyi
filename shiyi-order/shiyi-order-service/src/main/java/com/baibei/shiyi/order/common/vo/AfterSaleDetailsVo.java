package com.baibei.shiyi.order.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class AfterSaleDetailsVo {

    private List<AfterSaleGoodsVo> afterSaleGoodsVoList;

    private AfterSaleOrderDetailsVo afterSaleOrderDetailsVo;
}
