package com.baibei.shiyi.order.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class SubmitApplicationDto extends CustomerBaseDto {
    @NotNull(message = "子订单编号不能为空")
    private String orderItemNo;
    private String type;//refund-退款,exchange-换货
    private String returnReasons;//退款原因
    private String refuseMoney;//退款金额
    private String refuseIntegral;//退还积分
    private String problemDescription;//问题描述
    private String photo;//上传凭证
}
