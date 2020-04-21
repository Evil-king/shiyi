package com.baibei.shiyi.product.feign.bean.vo;

import com.baibei.shiyi.common.tool.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Classname BaseIndexProductVo
 * @Description 前端展示商品信息的基础属性类
 * @Date 2019/8/13 18:11
 * @Created by Longer
 */
@Data
public class BaseIndexProductVo {
    /**
     * 商品上架id
     */
    private Long shelfId;

    /**
     * 商品上架名称
     */
    private String shelfName;

    /**
     * 商品上架来源（integralmodule=积分舱，firstmodule=头等舱，upmodule=升仓，extendmodule=传承仓，sharemodule=共享仓）
     */
    private String source;

    private String sourceText;

    /**
     * 商品上架最低价格
     */
    private BigDecimal minShelfPrice;

    /**
     * 划线价
     */
    private BigDecimal linePrice;

    /**
     * spu编码
     */
    private String spuNo;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品主图
     */
    private String productImg;

    /**
     * 商品描述
     */
    private String productDesc;

    /**
     * 上架时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date shelfTime;

    /**
     * 运费模板
     */
    private String freightType;

    /**
     * 品牌名称
     */
    private String brandTitle;

    /**
     * 上架状态（shelf=上架，unshelf=下架）
     */
    private String shelfStatus;

    /**
     * 上架状态描述
     */
    private String shelfStatusText;

    /**
     * 商品类型（send_integral=赠送积分商品；consume_ingtegral=消费积分商品；transfer_product=交割商品）
     */
    private String shelfType;

    /**
     * 赠送积分值
     */
    private BigDecimal maxdetuch;
    /**
     * 图片集合
     */
    private List imageList = new ArrayList();
    /**
     * 赠送积分数
     */
    private BigDecimal sendIntegral;

    /**
     * 基础销量
     */
    private BigDecimal commonSellCount;

    /**
     * 销量（基础销量+真实销量）
     */
    private BigDecimal sellCount;

    List<ShelfBeanVo> shelfBeanVoList = new ArrayList<>();

    public BigDecimal getSendIntegral(){
        BigDecimal beanCount = new BigDecimal("0");
        for (ShelfBeanVo shelfBeanVo : shelfBeanVoList) {
            if (Constants.Unit.PERCENT.equals(shelfBeanVo.getUnit())) {
                BigDecimal value = getMinShelfPrice().multiply(shelfBeanVo.getValue().divide(new BigDecimal(100)));
                beanCount=beanCount.add(value);
            }
            if(Constants.Unit.BEAN.equals(shelfBeanVo.getUnit())){
                beanCount=beanCount.add(shelfBeanVo.getValue());
            }
        }
        return beanCount.compareTo(new BigDecimal(0))>0?beanCount:null;
    }

    public BigDecimal getSellCount(){
        if (this.sellCount==null) {
            return BigDecimal.ZERO.add(getCommonSellCount()==null?BigDecimal.ZERO:getCommonSellCount());
        }else{
            return this.sellCount.add(getCommonSellCount()==null?BigDecimal.ZERO:getCommonSellCount());
        }
    }
}
