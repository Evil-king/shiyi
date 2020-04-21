package com.baibei.shiyi.trade.common.vo;


import com.baibei.shiyi.common.tool.page.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 提货订单数据展示
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PickOrderVo extends PageParam {

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品编号
     */
    private String spuNo;

    /**
     * 审核状态
     */
    private String reviewStatus;

    /**
     * 提货订单
     */
    private String pickNo;

    /**
     * 提货数量
     */
    private Integer pickNumber;

    /**
     * 成本价
     */
    private BigDecimal costPrice;

    /**
     * 提货价
     */
    private BigDecimal pickUpPrice;


    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reviewTime;

    /**
     * 创建时间(申请时间)
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 提货子订单
     */
    private List<PickOrderChildVo> pickOrderChildVoList;

}
