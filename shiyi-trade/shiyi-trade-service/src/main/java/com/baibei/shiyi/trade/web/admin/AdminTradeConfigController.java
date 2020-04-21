package com.baibei.shiyi.trade.web.admin;

import com.baibei.component.redis.util.RedisUtil;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.constants.RedisConstant;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.MapUtil;
import com.baibei.shiyi.trade.feign.bean.dto.AdminTradeConfigDto;
import com.baibei.shiyi.trade.feign.client.admin.IAdminTradeConfigFeign;
import com.baibei.shiyi.trade.model.TradeConfig;
import com.baibei.shiyi.trade.service.ITradeConfigService;
import com.baibei.shiyi.trade.service.ITradeDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 交易全局参数配置
 */
@RestController
@RequestMapping("/shiyi/admin/trade/config")
public class AdminTradeConfigController implements IAdminTradeConfigFeign {

    @Autowired
    private ITradeConfigService tradeConfigService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ITradeDayService tradeDayService;

    @Override
    public ApiResult save(@RequestBody AdminTradeConfigDto adminTradeConfigDto) {
        TradeConfig tradeConfig;
        if (!tradeDayService.isClose()) {
            return ApiResult.error("当前没有闭市，无法编辑");
        }
        List<TradeConfig> tradeConfigList = this.tradeConfigService.findAll();
        if (tradeConfigList.size() == 0) {
            tradeConfig = BeanUtil.copyProperties(adminTradeConfigDto, TradeConfig.class);
            tradeConfig.setId(IdWorker.getId());
            tradeConfig.setCreateTime(new Date());
            tradeConfig.setFlag(new Byte(Constants.Flag.VALID));
            tradeConfig.setModifyTime(new Date());
            tradeConfigService.save(tradeConfig);
        } else {
            TradeConfig config = tradeConfigList.get(0);
            tradeConfig = BeanUtil.copyProperties(adminTradeConfigDto, TradeConfig.class);
            tradeConfig.setModifyTime(new Date());
            tradeConfig.setId(config.getId());
            tradeConfigService.update(tradeConfig);
        }
        redisUtil.hsetAll(RedisConstant.TRADE_CONFIG, MapUtil.objectToMap(tradeConfig));
        return ApiResult.success();
    }

    @Override
    public ApiResult<AdminTradeConfigDto> getConfig() {
        TradeConfig tradeConfig = tradeConfigService.find();
        if (tradeConfig == null) {
            return ApiResult.success(new AdminTradeConfigDto());
        }
        AdminTradeConfigDto adminTradeConfigDto = BeanUtil.copyProperties(tradeConfig, AdminTradeConfigDto.class);
        return ApiResult.success(adminTradeConfigDto);
    }
}
