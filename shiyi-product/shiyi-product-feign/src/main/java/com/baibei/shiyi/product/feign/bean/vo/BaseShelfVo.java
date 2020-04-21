package com.baibei.shiyi.product.feign.bean.vo;

import com.baibei.shiyi.common.tool.constants.Constants;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname BaseShelfVo
 * @Description 上架商品vo
 * @Date 2019/7/30 11:01
 * @Created by Longer
 */
@Data
public class BaseShelfVo {
    /**
     * 商品上架id
     */
    private Long shelfId;

    /**
     * 上架编码
     */
    private String shelfNo;

    /**
     * 商品上架名称
     */
    private String shelfName;

    /**
     * 商品上架来源（integralmodule=积分舱，firstmodule=头等舱，upmodule=升仓，extendmodule=传承仓，sharemodule=共享仓）
     */
    /*private String source;

    private String sourceText;*/

    /**
     * 上架价格
     */
    private BigDecimal shelfPrice;

    /**
     * 划线价
     */
    private BigDecimal linePrice;

    /**
     * 商品仓(integralmodule=积分舱，firstmodule=头等舱）
     */
    private String module;

    /**
     * 配售权
     */
    private BigDecimal plan;

    /**
     * 积分最多抵扣
     */
    private BigDecimal maxdetuch;

    /**
     * 积分最多抵扣单位(percent=百分比，rmb=人民币)
     */
    private String unit;

    /**
     * 积分类型（deliveryintegral=提货积分，comsumeintegral=消费积分，yijiabao=屹家保）
     */
    private String integralType;

    /**
     * 上下架（shelf=已上架，unshelf=未上架）
     */
    private String status;

    /**
     *  上下架描述
     */
    private String shelfStatusText;

    /**
     * 上架时间
     */
    private String shelfTime;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String modifyTime;

    /**
     * 规格Id
     */
    private Long skuId;

    /**
     * 规格编码
     */
    private String skuNo;

    /**
     * 商品规格描述 格式：{"颜色":"红色","尺寸","30"}
     */
    private String skuProperty;

    /**
     * sku值。格式：红色,30
     */
    private String skuPropertyValsStr;

    /**
     * spu编码
     */
    private String spuNo;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品主图
     */
    private String productImg;

    /**
     * 商品描述
     */
    private String productDesc;

    /**
     * 商品品牌id
     */
    private Long brandId;

    /**
     * 商品品牌名称
     */
    private String brandTitle;

    /**
     * 商品参数
     */
    private String parameter;

    /**
     * 关联后台类目id
     */
    private Long typeId;

    /**
     * 外部链接
     */
    private String outNo;

    /**
     * 库存
     */
    private BigDecimal stock;

    /**
     * 库存单位
     */
    private String stockUnit;

    /**
     * 销量
     */
    private BigDecimal sellCount;

    /**
     * 后台类目
     */
    private String typeTitle;

    /**
     * 商品类型（send_integral=赠送积分商品，支付类型：余额；consume_ingtegral=消费积分商品，支付类型：金豆；transfer_product=交割商品，支付类型：红豆）
     */
    private String shelfType;

    /**
     * 运费
     */
    private String freightType;

    /**
     * 赠送积分数
     */
    private BigDecimal sendIntegral;

    List<ShelfBeanVo> shelfBeanVoList = new ArrayList<>();

    public BigDecimal getSendIntegral(){
        BigDecimal beanCount = new BigDecimal("0");
        for (ShelfBeanVo shelfBeanVo : shelfBeanVoList) {
            if (Constants.Unit.PERCENT.equals(shelfBeanVo.getUnit())) {
                BigDecimal value = getShelfPrice().multiply(shelfBeanVo.getValue().divide(new BigDecimal(100)));
                beanCount=beanCount.add(value);
            }
            if(Constants.Unit.BEAN.equals(shelfBeanVo.getUnit())){
                beanCount=beanCount.add(shelfBeanVo.getValue());
            }
        }
        return beanCount.compareTo(new BigDecimal(0))>0?beanCount:null;
    }
}
