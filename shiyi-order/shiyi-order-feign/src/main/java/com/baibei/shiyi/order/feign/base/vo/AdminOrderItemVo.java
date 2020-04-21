package com.baibei.shiyi.order.feign.base.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class AdminOrderItemVo extends BaseRowModel {
    /**
     * 订单id
     */
    private Long Id;

    /**
     * 子订单id
     */
    private long orderItemId;

    /**
     * 订单号
     */
    @ExcelProperty(index = 1, value = "主订单编号")
    private String orderNo;

    /**
     * 子订单编号
     */
    @ExcelProperty(index = 2, value = "子订单编号")
    private String orderItemNo;

    /**
     * 下单时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty(index = 3, value = "下单时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty(index = 4, value = "支付时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /**
     * 关联用户编码
     */
    @ExcelProperty(index = 5, value = "用户编码")
    private String customerNo;

    /**
     * 订单实付金额
     */
    @ExcelProperty(index = 6, value = "订单金额")
    private BigDecimal actualAmount;


    /**
     * 支付方式
     */
    @ExcelProperty(index = 7, value = "支付方式")
    private String payWay;


    /**
     * 订单状态
     */
    @ExcelProperty(index = 8, value = "订单状态")
    private String status;


    /**
     * 子订单状态
     */
    private String orderItemStatus;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 运费
     */
    private BigDecimal freightAmount;

    /**
     * 对应不同积分类型的抵扣
     */
//    private List<Map<String, Object>> integrationAmountList;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人号码
     */
    private String receiverPhone;

    /**
     * 收货人详细地址
     */
    private String receiverDetailAddress;

    /**
     * 订单取消原因
     */
    private String cancelReason;

    /**
     * 订单取消时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date cancelTime;

    /**
     * 订单商品
     */
    private List<AdminOrderProductVo> orderProductVoList;

    /**
     * 订单详情
     */
    private List<AdminOrderDetailsVo> orderDetailsVoList;

    /**
     * 退款订单信息
     */
    private List<AdminOrderRefundVo> orderRefundVoList;

    /**
     * 现金总额
     */
    private BigDecimal moneyTotalAmount;

    /**
     * 积分总额
     */
    private BigDecimal consumptionTotalAmount;

    private String shiyiIntegration;

    private String consumptionIntegration;

    private String exchangeIntegration;

    private String remark;
}
