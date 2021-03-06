package com.baibei.shiyi.common.tool.bean;

import lombok.Data;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/24 9:43 AM
 * @description: 客户ID和编号数据参数, 针对/auth开头的接口是自动从网关转发过来的
 */
@Data
public class CustomerBaseDto {
    // 客户ID
    private Integer customerId;
    // 客户编码
    //@NotBlank(message = "客户编码不能为空")
    private String customerNo;
}
