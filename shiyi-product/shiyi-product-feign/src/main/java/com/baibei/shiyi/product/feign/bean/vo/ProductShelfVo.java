package com.baibei.shiyi.product.feign.bean.vo;

import com.baibei.shiyi.common.tool.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProductShelfVo {
    /**
     * 商品上架Id
     */
    private Long shelfId;

    /**
     * 上架编码
     */
    private String shelfNo;

    /**
     * 商品货号
     */
    private String spuNo;

    /**
     * 商品名称
     */
    private String shelfName;

    /**
     * 商品主图
     */
    private String productImg;

    /**
     * 商品最小价格
     */
    private BigDecimal minShelfPrice;

    /**
     * 划线价
     */
    private BigDecimal linePrice;

    /**
     * 商品来源
     */
    private String source;

    /**
     * 商品Id
     */
    private Long productId;

    /**
     * 商品描述
     */
    private String productDesc;

    /**
     * 上架时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date shelfTime;

    /**
     * integralmodule=积分舱，firstmodule=头等舱，upmodule=升仓，extendmodule=传承仓，sharemodule=共享仓
     */
    private String sourceText;

    /**
     * 运费模板
     */
    private String freightType;

    /**
     * 商品分类
     */
    private List<String> categoryList;

    /**
     * 商品类型（send_integral=赠送积分商品，支付类型：余额；consume_ingtegral=消费积分商品，支付类型：金豆；transfer_product=交割商品，支付类型：红豆）
     */
    private String shelfType;

    /**
     * 赠送积分数
     */
    private BigDecimal sendIntegral;

    private BigDecimal maxdetuch;

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
        return this.sellCount.add(getCommonSellCount()==null?BigDecimal.ZERO:getCommonSellCount());
    }
}
