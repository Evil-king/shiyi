package com.baibei.shiyi.order.biz.validator;

import com.alibaba.fastjson.JSON;
import com.baibei.shiyi.account.feign.bean.dto.CheckByFundTypes;
import com.baibei.shiyi.account.feign.bean.dto.CheckFundType;
import com.baibei.shiyi.account.feign.client.AccountFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.utils.NumberUtil;
import com.baibei.shiyi.common.tool.utils.ShelfTypeUtil;
import com.baibei.shiyi.common.tool.validator2.ValidateException;
import com.baibei.shiyi.common.tool.validator2.Validator;
import com.baibei.shiyi.common.tool.validator2.ValidatorContext;
import com.baibei.shiyi.order.common.dto.OrderSubmitDto;
import com.baibei.shiyi.order.common.dto.ProductDto;
import com.baibei.shiyi.order.common.vo.KeyValue;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/30 19:39
 * @description: 余额/积分校验是否足够校验器
 */
@Slf4j
@Component
public class OrderSubmitBalanceValidator implements Validator<OrderSubmitDto> {
    @Autowired
    private AccountFeign accountFeign;

    @Override
    public void validate(ValidatorContext context, OrderSubmitDto orderSubmitDto) {
        CheckByFundTypes checkByFundTypes = new CheckByFundTypes();
        checkByFundTypes.setCustomerNo(orderSubmitDto.getCustomerNo());
        List<CheckFundType> checkFundTypeList = new ArrayList<>();
        List<BaseShelfVo> shelfVoList = context.getClazz("baseShelfVoList");
        if (CollectionUtils.isEmpty(shelfVoList)) {
            throw new SystemException("商品信息列表为空");
        }
        List<KeyValue> kvList = new ArrayList<>();
        for (BaseShelfVo shelfVo : shelfVoList) {
            for (ProductDto productDto : orderSubmitDto.getProductList()) {
                if (shelfVo.getShelfId().equals(productDto.getShelfId()) && shelfVo.getSkuId().equals(productDto.getSkuId())) {
                    KeyValue kv = new KeyValue();
                    kv.setKey(ShelfTypeUtil.getPayWay(shelfVo.getShelfType()));
                    kv.setValue(NumberUtil.roundFloor(shelfVo.getShelfPrice().multiply(productDto.getNum()), 2).toPlainString());
                    kvList.add(kv);
                }
            }
        }
        List<KeyValue> payList = KeyValue.merge(kvList);
        for (KeyValue kv : payList) {
            if (Constants.BeanType.MALLACCOUNT.equals(orderSubmitDto.getPayWay()) && kv.getKey().equals(Constants.PayWay.MONEY)) {
                CheckFundType checkFundType = new CheckFundType();
                checkFundType.setFundType(Constants.BeanType.MALLACCOUNT);
                checkFundType.setChangeAmount(new BigDecimal(kv.getValue()));
                checkFundTypeList.add(checkFundType);
            } else {
                CheckFundType checkFundType = new CheckFundType();
                checkFundType.setFundType(kv.getKey());
                checkFundType.setChangeAmount(new BigDecimal(kv.getValue()));
                checkFundTypeList.add(checkFundType);
            }
        }
        checkByFundTypes.setCheckFundTypes(checkFundTypeList);
        log.info("checkByFundTypes={}", JSON.toJSONString(checkByFundTypes));
        ApiResult apiResult = accountFeign.checkByFundType(checkByFundTypes);
        if (apiResult.hasFail()) {
            throw new ValidateException(apiResult.getMsg());
        }
    }
}