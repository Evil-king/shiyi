package com.baibei.shiyi.product.feign.bean.vo;

import com.baibei.shiyi.common.tool.constants.Constants;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProductCollectVo {

    private Long id;
    /**
     * 上架商品Id
     */
    private Long shelfId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品仓integralmodule=积分舱，firstmodule=头等舱，upmodule=升仓，extendmodule=传承仓，sharemodule=共享仓
     */
    private String source;

    /**
     * 商品主图
     */
    private String productImg;

    /**
     * 商品价格
     */
    private BigDecimal productPrice;

    /**
     * 收藏时间
     */
    private Date createTime;


    /**
     * 商品仓（integralmodule=积分舱，firstmodule=头等舱）
     */
    private String sourceText;

    /**
     * 商品类型
     */
    private String shelfType;

    /**
     * 赠送积分数
     */
    private BigDecimal sendIntegral;

    List<ShelfBeanVo> shelfBeanVoList = new ArrayList<>();

    /**
     * 这个方法是获取赠送积分分数使用,不能删除
     * @return
     */
    public BigDecimal getSendIntegral(){
        BigDecimal beanCount = new BigDecimal("0");
        for (ShelfBeanVo shelfBeanVo : shelfBeanVoList) {
            if (Constants.Unit.PERCENT.equals(shelfBeanVo.getUnit())) {
                BigDecimal value = getProductPrice().multiply(shelfBeanVo.getValue().divide(new BigDecimal(100)));
                beanCount=beanCount.add(value);
            }
            if(Constants.Unit.BEAN.equals(shelfBeanVo.getUnit())){
                beanCount=beanCount.add(shelfBeanVo.getValue());
            }
        }
        return beanCount.compareTo(new BigDecimal(0))>0?beanCount:null;
    }

}
