package com.baibei.shiyi.product.rocketmq.comsume;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.product.feign.bean.message.ChangeSellCountMessage;
import com.baibei.shiyi.product.service.IProductStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: Longer
 * @date: 2019/10/12 11:32
 * @description: 修改销量消息消费
 */
@Component
@Slf4j
public class ChangeSellCountConsumerImpl implements IConsumer<List<ChangeSellCountMessage>> {
    @Autowired
    private IProductStockService productStockService;

    @Override
    public ApiResult execute(List<ChangeSellCountMessage> list) {
        return productStockService.batchChangeSellCount(list);
    }
}