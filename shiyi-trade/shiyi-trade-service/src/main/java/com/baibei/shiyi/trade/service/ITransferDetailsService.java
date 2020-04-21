package com.baibei.shiyi.trade.service;

import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.TransferPageListDto;
import com.baibei.shiyi.trade.feign.bean.vo.TransferPageListVo;
import com.baibei.shiyi.trade.feign.bean.vo.TransferTemplateVo;
import com.baibei.shiyi.trade.model.TransferDetails;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author: wenqing
 * @date: 2019/12/26 14:21:39
 * @description: TransferDetails服务接口
 */
public interface ITransferDetailsService extends Service<TransferDetails> {

    MyPageInfo<TransferPageListVo> listPage(TransferPageListDto transferPageListDto);

    ApiResult transferData(TransferTemplateVo transferTemplateVo, long transferLogId, String orderNo, BigDecimal buyFee, BigDecimal sellFee);

    List<TransferPageListVo> export(TransferPageListDto transferPageListDto);

    BigDecimal buyFee(String inCustomerNo, String price, String num, String isFee);

    BigDecimal sellFee(String outCustomerNo, String price, String num, String isFee);

    TransferDetails operatorObj(String orderNo, TransferTemplateVo transferTemplateVo, BigDecimal buyFee, BigDecimal sellFee);

    int operatorTransferDetailsDB(TransferDetails transferDetails, String status);
}
