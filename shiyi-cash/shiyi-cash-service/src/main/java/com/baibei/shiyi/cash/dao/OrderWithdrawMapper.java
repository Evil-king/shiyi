package com.baibei.shiyi.cash.dao;

import com.baibei.shiyi.cash.feign.base.dto.WithdrawListDto;
import com.baibei.shiyi.cash.feign.base.vo.WithdrawListVo;
import com.baibei.shiyi.cash.model.OrderWithdraw;
import com.baibei.shiyi.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OrderWithdrawMapper extends MyMapper<OrderWithdraw> {
    List<WithdrawListVo> pageList(WithdrawListDto withdrawListDto);

    BigDecimal sumAmountOfCustomer(@Param("customerNo") String customerNo, @Param("nowTime") Date nowTime);

    List<OrderWithdraw> selectPeriodOrderList(String batchNo);
}