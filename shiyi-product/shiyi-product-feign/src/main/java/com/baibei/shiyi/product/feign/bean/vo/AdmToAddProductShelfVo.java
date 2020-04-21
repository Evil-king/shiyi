package com.baibei.shiyi.product.feign.bean.vo;

import lombok.Data;
import org.apache.commons.collections.ArrayStack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Classname AdmToAddProductShelfVo
 * @Description 新增上架商品（数据回显）
 * @Date 2019/9/16 15:15
 * @Created by Longer
 */
@Data
public class AdmToAddProductShelfVo {
    /**
     * 商品仓列表
     */
    List<BaseKeyValueVo> moduleList = new ArrayList<>();

    /**
     * 积分类型列表
     */
    List<BaseKeyValueVo> integralTypeList = new ArrayList<>();

    /**
     * 来源列表
     */
    List<BaseKeyValueVo> sourceList = new ArrayList<>();

}
