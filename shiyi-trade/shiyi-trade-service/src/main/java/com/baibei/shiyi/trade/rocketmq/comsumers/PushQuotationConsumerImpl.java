package com.baibei.shiyi.trade.rocketmq.comsumers;

import com.alibaba.fastjson.JSON;
import com.baibei.component.redis.util.RedisUtil;
import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.RedisConstant;
import com.baibei.shiyi.quotation.feign.bean.dto.QuoteDto;
import com.baibei.shiyi.trade.rocketmq.message.PushQuotationMsg;
import com.baibei.shiyi.trade.utils.TradeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.MessageFormat;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/25 10:29
 * @description: 推送行情消息消费
 */
@Component
public class PushQuotationConsumerImpl implements IConsumer<PushQuotationMsg> {
    @Autowired
    private TradeUtil tradeUtil;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ApiResult execute(PushQuotationMsg msg) {
        QuoteDto quoteDto = new QuoteDto();
        quoteDto.setOperateType(msg.getOperateType());
        quoteDto.setProductTradeNo(msg.getProductTradeNo());
        quoteDto.setPrice(msg.getPrice());
        quoteDto.setDealCount(new BigDecimal(msg.getCount()));
        quoteDto.setDealtime(msg.getOccurTime().getTime());
        quoteDto.setOrderNo(msg.getOrderNo());
        redisUtil.leftPush(MessageFormat.format(RedisConstant.QUOTESERVICE_QUOTE_LIST, msg.getProductTradeNo()), JSON.toJSONString(quoteDto));
        return ApiResult.success();
    }
}