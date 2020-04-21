package com.baibei.shiyi.product.feign.bean.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname BuyProductVo
 * @Description 购买商品vo
 * @Date 2019/8/13 15:51
 * @Created by Longer
 */
@Data
public class BuyProductVo {

    List<BaseShelfVo> baseShelfVoList = new ArrayList<>();

    /**
     * 多维sku
     */
    List<MultiSku> multiSkuList = new ArrayList<>();
}
