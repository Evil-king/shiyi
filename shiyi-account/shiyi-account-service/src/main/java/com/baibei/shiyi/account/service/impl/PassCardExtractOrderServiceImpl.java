package com.baibei.shiyi.account.service.impl;

import com.baibei.shiyi.account.dao.PassCardExtractOrderMapper;
import com.baibei.shiyi.account.feign.bean.dto.ChangeAmountDto;
import com.baibei.shiyi.account.feign.bean.dto.OperationDto;
import com.baibei.shiyi.account.feign.bean.dto.PassCardExtractOrderListDto;
import com.baibei.shiyi.account.feign.bean.vo.PassCardExtractOrderListVo;
import com.baibei.shiyi.account.model.PassCardExtractOrder;
import com.baibei.shiyi.account.service.IPassCardExtractOrderService;
import com.baibei.shiyi.account.service.IPassCardService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.HoldDetailResourceEnum;
import com.baibei.shiyi.common.tool.enumeration.RecordPassCardEnum;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.content.feign.client.ContentFeign;
import com.baibei.shiyi.trade.feign.bean.dto.ChangeHoldPositionDto;
import com.baibei.shiyi.trade.feign.client.shiyi.TradeFeign;
import com.baibei.shiyi.user.feign.bean.vo.RealnameInfoVo;
import com.baibei.shiyi.user.feign.client.CustomerDetailFeign;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author: hyc
 * @date: 2019/11/12 11:19:43
 * @description: PassCardExtractOrder服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PassCardExtractOrderServiceImpl extends AbstractService<PassCardExtractOrder> implements IPassCardExtractOrderService {

    @Autowired
    private PassCardExtractOrderMapper tblPassCardExtractOrderMapper;

    @Autowired
    private IPassCardService passCardService;

    @Autowired
    private TradeFeign tradeFeign;

    @Autowired
    private ContentFeign contentFeign;

    @Autowired
    private CustomerDetailFeign customerDetailFeign;

    @Override
    public ApiResult<MyPageInfo<PassCardExtractOrderListVo>> getPageList(PassCardExtractOrderListDto passCardExtractOrderListDto) {
        PageHelper.startPage(passCardExtractOrderListDto.getCurrentPage(), passCardExtractOrderListDto.getPageSize());
        List<PassCardExtractOrderListVo> passCardExtractOrderListVos = tblPassCardExtractOrderMapper.getPageList(passCardExtractOrderListDto);
        MyPageInfo<PassCardExtractOrderListVo> myPageInfo = new MyPageInfo<>(passCardExtractOrderListVos);
        return ApiResult.success(myPageInfo);
    }

    @Override
    public ApiResult operation(OperationDto operationDto) {
        if (operationDto.getStatus().equals(Constants.ExtractOrderStatus.SUCCESS)) {
            //此时为批量通过,此时则需要一个一个将仓单处理
            List<ChangeHoldPositionDto> changeHoldPositionDtoList = new ArrayList<>();
            for (int i = 0; i < operationDto.getIds().size(); i++) {
                PassCardExtractOrder order = tblPassCardExtractOrderMapper.selectByPrimaryKey(operationDto.getIds().get(i));
                if (Constants.ExtractOrderStatus.WAIT.equals(order.getStatus())) {
                    //先处理订单，再处理仓单
                    //订单置为成功
                    order.setStatus(Constants.ExtractOrderStatus.SUCCESS);
                    order.setOperatorName(operationDto.getOperatorName());
                    order.setModifyTime(new Date());
                    tblPassCardExtractOrderMapper.updateByPrimaryKeySelective(order);
                    ApiResult<RealnameInfoVo> customer = customerDetailFeign.findRealNameByCustomerNo(order.getCustomerNo());
                    if(!customer.hasSuccess()){
                        throw new ServiceException(customer.getMsg());
                    }
                    ApiResult<RealnameInfoVo> extractCustomer = customerDetailFeign.findRealNameByCustomerNo(order.getExtractCustomerNo());
                    if(!extractCustomer.hasSuccess()){
                        throw new ServiceException(extractCustomer.getMsg());
                    }
                    //操作持仓
                    ChangeHoldPositionDto inChangeHoldPositionDto = ChangeHoldPositionDto.builder().count(order.getAmount()).customerNo(order.getCustomerNo())
                            .productTradeNo(order.getProductTradeNo()).resource(HoldDetailResourceEnum.EXCHANGE.getCode()).reType(Constants.Retype.IN).price(BigDecimal.ZERO).resourceNo(order.getOrderNo()).customerName(customer.getData().getRealname()).build();
                    ChangeHoldPositionDto outChangeHoldPositionDto = ChangeHoldPositionDto.builder().count(order.getAmount()).customerNo(order.getExtractCustomerNo())
                            .productTradeNo(order.getProductTradeNo()).resource(HoldDetailResourceEnum.EXCHANGE.getCode()).reType(Constants.Retype.OUT).price(BigDecimal.ZERO).resourceNo(order.getOrderNo()).customerName(extractCustomer.getData().getRealname()).build();
                    changeHoldPositionDtoList.add(inChangeHoldPositionDto);
                    changeHoldPositionDtoList.add(outChangeHoldPositionDto);
                }
            }
            if (changeHoldPositionDtoList.size() > 0) {
                ApiResult apiResult = tradeFeign.changeHoldPosition(changeHoldPositionDtoList);
                if (apiResult.hasFail()) {
                    throw new ServiceException(apiResult.getMsg());
                }
            }
            return ApiResult.success();
        } else if (operationDto.getStatus().equals(Constants.ExtractOrderStatus.FAIL)) {
            //此时为批量驳回，将订单中的通证一个个退回
            for (int i = 0; i < operationDto.getIds().size(); i++) {
                PassCardExtractOrder order = tblPassCardExtractOrderMapper.selectByPrimaryKey(operationDto.getIds().get(i));
                if (Constants.ExtractOrderStatus.WAIT.equals(order.getStatus())) {
                    //如果状态是等待状态才进行操作
                    ChangeAmountDto changeAmountDto = new ChangeAmountDto();
                    changeAmountDto.setOrderNo(order.getOrderNo());
                    changeAmountDto.setReType(Constants.Retype.IN);
                    changeAmountDto.setTradeType(RecordPassCardEnum.PASS_CARD_BACK.getCode());
                    changeAmountDto.setChangeAmount(order.getTotalPrice());
                    changeAmountDto.setCustomerNo(order.getCustomerNo());
                    //先退回本金
                    passCardService.changeAmount(changeAmountDto);
                    changeAmountDto.setChangeAmount(order.getServiceCharge());
                    changeAmountDto.setTradeType(RecordPassCardEnum.SERVICE_CHARGE_BACK.getCode());
                    //再退回手续费
                    passCardService.changeAmount(changeAmountDto);
                    order.setStatus(Constants.ExtractOrderStatus.FAIL);
                    order.setOperatorName(operationDto.getOperatorName());
                    order.setModifyTime(new Date());
                    tblPassCardExtractOrderMapper.updateByPrimaryKeySelective(order);
                }
            }
            return ApiResult.success();
        } else {
            return ApiResult.badParam("状态错误，请联系管理员");
        }
    }

    @Override
    public ApiResult<List<PassCardExtractOrderListVo>> export(PassCardExtractOrderListDto passCardExtractOrderListDto) {
        return ApiResult.success(tblPassCardExtractOrderMapper.getPageList(passCardExtractOrderListDto));
    }

    @Override
    public ApiResult systemOperation() {
        ApiResult<Integer> auditQuantity = contentFeign.getAuditQuantity();
        if (!auditQuantity.hasSuccess()) {
            return ApiResult.error();
        }
        //找到所有需要自动审核的订单id
        List<Long> longs = tblPassCardExtractOrderMapper.findNotAuditOrder(auditQuantity.getData());
        OperationDto operationDto = new OperationDto();
        operationDto.setIds(longs);
        operationDto.setStatus(Constants.ExtractOrderStatus.SUCCESS);
        operationDto.setOperatorName("system");
        return operation(operationDto);
    }

}
