package com.baibei.shiyi.trade.dao;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoPageDto;
import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.trade.common.vo.HoldDetailListVo;
import com.baibei.shiyi.trade.common.vo.ListBuyVo;
import com.baibei.shiyi.trade.common.vo.MyHoldVo;
import com.baibei.shiyi.trade.common.vo.TotalMarketValueVo;
import com.baibei.shiyi.trade.feign.bean.dto.CustomerHoldPageListDto;
import com.baibei.shiyi.trade.feign.bean.vo.CustomerHoldPageListVo;
import com.baibei.shiyi.trade.model.HoldDetails;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface HoldDetailsMapper extends MyMapper<HoldDetails> {

    int countScannerByCustomerNo(String customerNo);

    List<ListBuyVo> queryByCurrent();

    List<MyHoldVo> myHoldList(String customerNo);

    List<CustomerHoldPageListVo> mypageList(CustomerHoldPageListDto customerHoldPageListDto);

    TotalMarketValueVo getTotalMarketValue(String customerNo,BigDecimal lastPrice);

    List<HoldDetailListVo> getPageList(CustomerNoPageDto customerNoDto);

    Integer myHoldRemaindCount(String customerNo);

    int updateNewPrice(BigDecimal newProductPrice);

    BigDecimal countHoldMarketValue(@Param("customerNo") String customerNo, @Param("productTradeNo") String productTradeNo,
                                    @Param("lastPrice")BigDecimal lastPrice);

    BigDecimal calculationCost(@Param("customerNo") String customerNo, @Param("productTradeNo") String productTradeNo);

}