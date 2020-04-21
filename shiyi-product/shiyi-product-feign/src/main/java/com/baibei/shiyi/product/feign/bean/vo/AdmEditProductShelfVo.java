package com.baibei.shiyi.product.feign.bean.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname AdmEditProductShelfVo
 * @Description 编辑上架商品（数据回显）
 * @Date 2019/9/16 15:15
 * @Created by Longer
 */
@Data
public class AdmEditProductShelfVo {
    private Long shelfId;
    private Long productId;
    private String spuNo;
    /**
     * 商品上架名称
     */
    private String shelfName;
    /**
     * 上架属性
     */
    private List<AdmProductShelfSkuVo> shelfSkuList = new ArrayList<>();
    /**
     * 划线价
     */
    private BigDecimal linePrice;
    /**
     * 上架编码
     */
    private String shelfNo;
    /**
     * 商品仓（integralmodule=积分舱，firstmodule=头等舱）
     */
    private String module;
    /**
     * 配售权
     */
    private BigDecimal plan;
    /**
     * 积分最多抵扣单位(percent=百分比，rmb=人民币)
     */
    private String unit;
    /**
     * 积分最多抵扣值
     */
    private BigDecimal maxdetuch;
    /**
     * 来源（integralmodule=积分舱，firstmodule=头等舱，upmodule=升仓，extendmodule=传承仓，sharemodule=共享仓）
     */
    private String source;
    /**
     * 分润规则
     */
    private String separetBenefit;
    /**
     * 运费模板（0=包邮）
     */
    private String freightType;
    /**
     * 运费模板类别（free=包邮；customize=自定义）
     */
    private String freightCategory;
    /**
     * 积分类型（deliveryintegral=提货积分，comsumeintegral=消费积分，yijiabao=屹家保）
     */
    private String integralType;
    /**
     * 上下架（shelf=已上架，unshelf=未上架）
     */
    private String status;

    /**
     * 商品类型（send_integral=赠送积分商品；consume_ingtegral=消费积分商品；transfer_product=交割商品）
     */
    private String shelfType;

    /**
     * 前端类目集合
     */
    private List<List<Long>> categoryIdList = new ArrayList<>();

    /**
     * 积分蕾西集合
     */
    private List<ShelfBeanVo> shelfBeanRefDtoList = new ArrayList<>();

    public String getFreightCategory(){
        if (new BigDecimal(getFreightType()).compareTo(new BigDecimal(0))==0) {
            return "free";
        }else{
            return "customize";
        }
    }

}
