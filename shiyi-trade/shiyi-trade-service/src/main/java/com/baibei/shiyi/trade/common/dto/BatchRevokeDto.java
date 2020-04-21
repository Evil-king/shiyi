package com.baibei.shiyi.trade.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BatchRevokeDto extends CustomerBaseDto {
    @NotBlank(message = "客户编码不能为空")
    private String customerNo;

    @NotNull(message = "委托单号不能为空")
    private List<String> entrustNoList;
}
