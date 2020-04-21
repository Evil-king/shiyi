package com.baibei.shiyi.trade.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.baibei.shiyi.trade.feign.bean.dto.AdminTradeConfigDto;
import com.baibei.shiyi.trade.feign.bean.vo.TradeDayVo;
import com.baibei.shiyi.trade.feign.service.TradeTimeService;
import com.baibei.shiyi.trade.model.TradeConfig;
import com.baibei.shiyi.trade.service.ITradeConfigService;
import com.baibei.shiyi.trade.service.ITradeDayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 全局交易参数配置
 */
@Slf4j
@RestController
@RequestMapping("/api/trade/config")
public class ApiTradeConfigController {

    @Autowired
    private ITradeConfigService tradeConfigService;
    @Autowired
    private ITradeDayService tradeDayService;
    @Autowired
    private TradeTimeService tradeTimeService;


    @PostMapping("/get")
    public ApiResult<AdminTradeConfigDto> get() {
        TradeConfig tradeConfig = tradeConfigService.getConfig();
        AdminTradeConfigDto adminTradeConfigDto = BeanUtil.copyProperties(tradeConfig, AdminTradeConfigDto.class);
        boolean tradeTime = tradeDayService.isTradeTime();
        adminTradeConfigDto.setTradeTimeFlag(tradeTime?"1":"0");
        List<TradeDayVo> tradeDayConfigList = tradeTimeService.getTradeDayConfig();
        String morningStart = "";
        String morningEnd = "";
        String afternoonEnd="";
        String afternoonStart="";
        for (TradeDayVo tradeDayVo : tradeDayConfigList) {
            if (tradeDayVo.getPeriod().equals("morning")) {
                morningStart=tradeDayVo.getStartTime();
                morningEnd=tradeDayVo.getEndTime();
            }else{
                afternoonStart=tradeDayVo.getStartTime();
                afternoonEnd=tradeDayVo.getEndTime();
            }
        }
        String day = DateUtil.yyyyMMddWithLine.get().format(new Date());
        try {
            Date morningStartTime = DateUtil.yyyyMMddHHmmssWithLine.get().parse(day+" "+morningStart);
            Date morningEndTime = DateUtil.yyyyMMddHHmmssWithLine.get().parse(day+" "+morningEnd);
            Date afternoonStartTime = DateUtil.yyyyMMddHHmmssWithLine.get().parse(day+" "+afternoonStart);
            Date afternoonEndTime = DateUtil.yyyyMMddHHmmssWithLine.get().parse(day+" "+afternoonEnd);
            adminTradeConfigDto.setMorningStartTime(morningStartTime.getTime());
            adminTradeConfigDto.setMorningEndTime(morningEndTime.getTime());
            adminTradeConfigDto.setAfternoonStartTime(afternoonStartTime.getTime());
            adminTradeConfigDto.setAfternoonEndTime(afternoonEndTime.getTime());
        } catch (ParseException e) {
            log.info("时间转化报错：{}",e);
        }
        return ApiResult.success(adminTradeConfigDto);
    }
}
