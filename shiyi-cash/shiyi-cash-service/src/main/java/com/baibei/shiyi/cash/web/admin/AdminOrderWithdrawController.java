package com.baibei.shiyi.cash.web.admin;

import com.baibei.component.redis.util.RedisUtil;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.account.feign.bean.dto.ChangeAmountDto;
import com.baibei.shiyi.cash.feign.base.dto.*;
import com.baibei.shiyi.cash.feign.base.vo.AddMoneyFlagVo;
import com.baibei.shiyi.cash.feign.base.vo.Apply1010PagelistVo;
import com.baibei.shiyi.cash.feign.base.vo.WithDrawDepositDiffVo;
import com.baibei.shiyi.cash.feign.base.vo.WithdrawListVo;
import com.baibei.shiyi.cash.feign.client.admin.IAdminWithdrawFeign;
import com.baibei.shiyi.cash.model.OrderWithdraw;
import com.baibei.shiyi.cash.service.IAccountBookService;
import com.baibei.shiyi.cash.service.IOrderWithdrawService;
import com.baibei.shiyi.cash.service.IWithDrawDepositDiffService;
import com.baibei.shiyi.cash.util.PropertiesVal;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.constants.RedisConstant;
import com.baibei.shiyi.common.tool.enumeration.TradeMoneyTradeTypeEnum;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.baibei.shiyi.common.tool.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/withdraw")
public class AdminOrderWithdrawController implements IAdminWithdrawFeign {

    @Autowired
    private IOrderWithdrawService orderWithdrawService;
    @Autowired
    private IWithDrawDepositDiffService withDrawDepositDiffService;
    @Autowired
    private PropertiesVal propertiesVal;
    @Autowired
    private RocketMQUtil rocketMQUtil;
    @Autowired
    private IAccountBookService accountBookService;
    @Value("100000")
    private int exportLimit;//导出限制条数

    @Autowired
    private RedisUtil redisUtil;
    private SimpleDateFormat sf = (SimpleDateFormat)DateUtil.yyyyMMddWithLine.get();
    private SimpleDateFormat sf2 = (SimpleDateFormat)DateUtil.yyyyMMddHHmmssWithLine.get();



    /**
     * 审核出金
     * @param auditOrderDto
     * @return
     */
    @Override
    public ApiResult auditWithdraw(@RequestBody @Validated AuditOrderDto auditOrderDto){
        ApiResult<AddMoneyFlagVo> booleanApiResult = orderWithdrawService.auditOrder(auditOrderDto);
        AddMoneyFlagVo addMoneyFlagVo = booleanApiResult.getData();
        OrderWithdraw orderWithdraw=(OrderWithdraw)addMoneyFlagVo.getO();
        if(addMoneyFlagVo.isAddMoneyFlag()){
            //异步加钱
            ChangeAmountDto changeAmountDto = new ChangeAmountDto();
            changeAmountDto.setCustomerNo(orderWithdraw.getCustomerNo());
            changeAmountDto.setOrderNo(orderWithdraw.getOrderNo());
            changeAmountDto.setChangeAmount(orderWithdraw.getOrderamt().add(orderWithdraw.getHandelFee()));
            changeAmountDto.setReType(Constants.Retype.IN);
            changeAmountDto.setTradeType(TradeMoneyTradeTypeEnum.WITHDRAW_BACK.getCode());
            log.info("审核出金，发送加本金消息：",changeAmountDto.toString());
            //发送加钱消息
            rocketMQUtil.sendMsg(propertiesVal.getWithdrawAddMoneyTopic(),
                     JacksonUtil.beanToJson(changeAmountDto), orderWithdraw.getOrderNo());

        }
        return ApiResult.success();
    }

    /**
     * 出金订单列表
     * @param withdrawListDto
     * @return
     */
    @Override
    public ApiResult<MyPageInfo<WithdrawListVo>> pagelist(@RequestBody @Validated WithdrawListDto withdrawListDto){
        MyPageInfo<WithdrawListVo> withdrawListVoMyPageInfo = orderWithdrawService.pageList(withdrawListDto);
        return ApiResult.success(withdrawListVoMyPageInfo);
    }

    @Override
    public ApiResult<List<WithdrawListVo>> getWithdrawList(@RequestBody @Validated WithdrawListDto withdrawListDto) {
        List<WithdrawListVo> withdrawListVoList = orderWithdrawService.getWithdrawList(withdrawListDto);
        return ApiResult.success(withdrawListVoList);
    }

    @Override
    public ApiResult apply1010() {
        long start = System.currentTimeMillis();
        log.info("开始台账...");
        accountBookService.clear();//清空表
        ApiResult apiResult = new ApiResult();
        Apply1010Dto apply1010Dto = new Apply1010Dto();
        apply1010Dto.setSelectFlag("1");//查询全部台账信息
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            apiResult = accountBookService.apply1010(apply1010Dto.getSelectFlag(), String.valueOf(i));
            if (-901 == apiResult.getCode()) {
                apiResult.setCode(200);
                apiResult.setMsg("查询成功!");
                break;
            }
        }
        //更新台账状态（缓存）doing=处理中；finished=已完成
        redisUtil.set(RedisConstant.APPLY1010_STATUS,Constants.Apply1010Status.FINISHED);
        String format = sf.format(new Date());
        try {
            //设置超时时间
            Date expiredTime = sf2.parse(format + " 23:59:59");
            redisUtil.expireAt(RedisConstant.APPLY1010_STATUS,expiredTime);
        } catch (ParseException e) {
            log.error("设置台账状态超时时间报错",e);
            e.printStackTrace();
        }
        log.info("台账耗时{}ms", (System.currentTimeMillis() - start));
        return ApiResult.success();
    }

    @Override
    public ApiResult apply10102(@Validated @RequestBody Apply1010Dto apply1010Dto) {
        long start = System.currentTimeMillis();
        log.info("开始台账...selectFlag="+apply1010Dto.getSelectFlag());
        ApiResult apiResult = new ApiResult();
        accountBookService.clear();//清空表
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            apiResult = accountBookService.apply1010(apply1010Dto.getSelectFlag(), String.valueOf(i));
            if (-901 == apiResult.getCode()) {
                apiResult.setCode(200);
                apiResult.setMsg("查询成功!");
                break;
            }
        }
        log.info("台账耗时{}ms", (System.currentTimeMillis() - start));
        return apiResult;
    }

    @Override
    public ApiResult<MyPageInfo<Apply1010PagelistVo>> apply1010Pagelist(@RequestBody @Validated Apply1010PagelistDto apply1010PagelistDto) {
        MyPageInfo<Apply1010PagelistVo> apply1010PagelistVoMyPageInfo = accountBookService.apply1010Pagelist(apply1010PagelistDto);
        return ApiResult.success(apply1010PagelistVoMyPageInfo);
    }

    @Override
    public ApiResult<List<Apply1010PagelistVo>> apply1010List(@RequestBody @Validated Apply1010PagelistDto apply1010PagelistDto) {
        List<Apply1010PagelistVo> apply1010PagelistVoList = accountBookService.apply1010List(apply1010PagelistDto);
        return ApiResult.success(apply1010PagelistVoList);
    }

    @Override
    public ApiResult<MyPageInfo<WithDrawDepositDiffVo>> withDrawDepositDiffPageList(@RequestBody WithDrawDepositDiffDto withDrawDepositDiffDto) {
        return ApiResult.success(withDrawDepositDiffService.pageList(withDrawDepositDiffDto));
    }

    @Override
    public ApiResult<List<WithDrawDepositDiffVo>> withDrawDepositDiffExcelExport(@RequestBody WithDrawDepositDiffDto withDrawDepositDiffDto) {
        withDrawDepositDiffDto.setExportLimit(exportLimit);
        List<WithDrawDepositDiffVo> withDrawDepositDiffVos = withDrawDepositDiffService.WithDrawDepositDiffVoList(withDrawDepositDiffDto);
        for (int i = 0; i <withDrawDepositDiffVos.size() ; i++) {
            if("withdraw".equals(withDrawDepositDiffVos.get(i).getType())){
                withDrawDepositDiffVos.get(i).setType("出金");
            }else if("deposit".equals(withDrawDepositDiffVos.get(i).getType())){
                withDrawDepositDiffVos.get(i).setType("入金");
            }

            if("1".equals(withDrawDepositDiffVos.get(i).getHengjiaStatus())){
                withDrawDepositDiffVos.get(i).setHengjiaStatus("出金申请中");
            }else if("2".equals(withDrawDepositDiffVos.get(i).getHengjiaStatus())){
                withDrawDepositDiffVos.get(i).setHengjiaStatus("出金审核通过");
            }else if("3".equals(withDrawDepositDiffVos.get(i).getHengjiaStatus())){
                withDrawDepositDiffVos.get(i).setHengjiaStatus("出金处理中");
            }else if("4".equals(withDrawDepositDiffVos.get(i).getHengjiaStatus())){
                withDrawDepositDiffVos.get(i).setHengjiaStatus("出金成功");
            }else if("5".equals(withDrawDepositDiffVos.get(i).getHengjiaStatus())){
                withDrawDepositDiffVos.get(i).setHengjiaStatus("出金失败");
            }else if("6".equals(withDrawDepositDiffVos.get(i).getHengjiaStatus())){
                withDrawDepositDiffVos.get(i).setHengjiaStatus("出金审核失败");
            }else if("success".equals(withDrawDepositDiffVos.get(i).getHengjiaStatus())){
                withDrawDepositDiffVos.get(i).setHengjiaStatus("入金成功");
            }else if("fail".equals(withDrawDepositDiffVos.get(i).getHengjiaStatus())){
                withDrawDepositDiffVos.get(i).setHengjiaStatus("入金失败");
            }
            if("success".equals(withDrawDepositDiffVos.get(i).getBankStatus())){
                withDrawDepositDiffVos.get(i).setBankStatus("成功");
            }

            if("long_diff".equals(withDrawDepositDiffVos.get(i).getDiffType())){
                withDrawDepositDiffVos.get(i).setDiffType("长款差错");
            }else if("short_diff".equals(withDrawDepositDiffVos.get(i).getDiffType())){
                withDrawDepositDiffVos.get(i).setDiffType("短款差错");
            }else if("amount_diff".equals(withDrawDepositDiffVos.get(i).getDiffType())){
                withDrawDepositDiffVos.get(i).setDiffType("金额不一致");
            }else if("status_diff".equals(withDrawDepositDiffVos.get(i).getDiffType())){
                withDrawDepositDiffVos.get(i).setDiffType("状态不一致");
            }else if("amount_status_diff".equals(withDrawDepositDiffVos.get(i).getDiffType())){
                withDrawDepositDiffVos.get(i).setDiffType("金额和状态不一致");
            }

            if("wait".equals(withDrawDepositDiffVos.get(i).getStatus())){
                withDrawDepositDiffVos.get(i).setStatus("待处理");
            }else if("deal".equals(withDrawDepositDiffVos.get(i).getStatus())){
                withDrawDepositDiffVos.get(i).setStatus("已处理");
            }
        }
        return ApiResult.success(withDrawDepositDiffVos);
    }

}
