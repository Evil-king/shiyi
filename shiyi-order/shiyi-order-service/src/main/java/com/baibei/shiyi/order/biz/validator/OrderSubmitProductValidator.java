package com.baibei.shiyi.order.biz.validator;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.validator2.ValidateException;
import com.baibei.shiyi.common.tool.validator2.Validator;
import com.baibei.shiyi.common.tool.validator2.ValidatorContext;
import com.baibei.shiyi.order.common.dto.OrderSubmitDto;
import com.baibei.shiyi.order.common.dto.ProductDto;
import com.baibei.shiyi.product.feign.bean.dto.ShelfRefDto;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import com.baibei.shiyi.product.feign.client.shiyi.ProductFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/30 15:34
 * @description: 商品状态以及库存校验
 */
@Component
@Slf4j
public class OrderSubmitProductValidator implements Validator<OrderSubmitDto> {
    @Autowired
    private ProductFeign productFeign;

    @Override
    public void validate(ValidatorContext context, OrderSubmitDto orderSubmitDto) {
        List<ShelfRefDto> shelfProductDtoList = BeanUtil.copyProperties(orderSubmitDto.getProductList(), ShelfRefDto.class);
        ApiResult<List<BaseShelfVo>> productApiResult = productFeign.getBatchShelfProductInfo(shelfProductDtoList);
        if (productApiResult.hasFail() || productApiResult.getData() == null) {
            log.info("获取商品信息失败，apiResult={}", productApiResult.toString());
            throw new SystemException("获取商品信息失败");
        }
        List<BaseShelfVo> baseShelfVoList = productApiResult.getData();
        for (BaseShelfVo item : baseShelfVoList) {
            if (Constants.ShelfStatus.UNSHELF.equals(item.getStatus())) {
                log.info("商品已下架，skuId={},shelfId={}", item.getSkuId(), item.getShelfId());
                throw new ValidateException("存在已下架的商品");
            }
            for (ProductDto productDto : orderSubmitDto.getProductList()) {
                if (item.getShelfId().equals(productDto.getShelfId()) && item.getSkuId().equals(productDto.getSkuId())) {
                    if (item.getStock().compareTo(productDto.getNum()) < 0) {
                        log.info("商品库存不足，skuId={}，shelfId={}，stock={}，num={}", item.getSkuId(), item.getShelfId(), item.getStock(), productDto.getNum());
                        throw new ValidateException("下单失败，库存不足");
                    }
                }
            }
        }
        context.setAttribute("baseShelfVoList", baseShelfVoList);
    }
}