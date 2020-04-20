package com.baibei.shiyi.cash.web.shiyi;

import com.baibei.shiyi.cash.feign.base.dto.PABDepositDto;
import com.baibei.shiyi.cash.feign.base.vo.PABDepositVo;
import com.baibei.shiyi.cash.feign.client.shiyi.IShiyiDepositFeign;
import com.baibei.shiyi.cash.service.IOrderDepositService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shiyi/cash/order")
public class ShiyiDepositController implements IShiyiDepositFeign {

    @Autowired
    private IOrderDepositService orderDepositService;

    @Override
    public ApiResult<PABDepositVo> deposit(@Validated @RequestBody PABDepositDto pabDepositDto) {
        PABDepositVo pabDepositVo = orderDepositService.deposit(pabDepositDto);
        return ApiResult.success(pabDepositVo);
    }
}
