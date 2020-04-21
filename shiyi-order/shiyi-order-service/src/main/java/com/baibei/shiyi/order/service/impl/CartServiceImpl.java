package com.baibei.shiyi.order.service.impl;

import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.ProductSourceEnum;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.SkuPropertyUtil;
import com.baibei.shiyi.order.common.dto.*;
import com.baibei.shiyi.order.common.vo.CartListVo;
import com.baibei.shiyi.order.dao.CartMapper;
import com.baibei.shiyi.order.feign.base.dto.ShiyiCartDto;
import com.baibei.shiyi.order.model.Cart;
import com.baibei.shiyi.order.service.ICartService;
import com.baibei.shiyi.product.feign.bean.dto.ShelfRefDto;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import com.baibei.shiyi.product.feign.bean.vo.ProductSkuVo;
import com.baibei.shiyi.product.feign.client.shiyi.ProductFeign;
import com.github.pagehelper.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.smartcardio.Card;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/31 18:16:01
 * @description: Cart服务实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CartServiceImpl extends AbstractService<Cart> implements ICartService {

    @Autowired
    private CartMapper tblOrdCartMapper;
    @Autowired
    private ProductFeign productFeign;
    // 购物车最大商品数量
    @Value("${cart.max:50}")
    private String cartMax;

    // 购物车库存告急数量
    @Value("${cart.stock:10}")
    private String cartStock;
    // 降价提示语
    @Value("${cart.compare.text}")
    private String cardCompareText;


    @Override
    public void add(CartAddDto cardAddDto) {
        // 购物车是否已存在该商品，如果存在，则增加已有商品购物车数量即可
        Cart oldCart = findByParam(cardAddDto.getCustomerNo(), cardAddDto.getShelfId(), cardAddDto.getSkuId());
        if (oldCart != null) {
            oldCart.setQuantity(oldCart.getQuantity() + cardAddDto.getQuantity());
            update(oldCart);
            return;
        }
        // 判断购物车是否满了
        if (isCartFull(cardAddDto.getCustomerNo())) {
            throw new ServiceException("购物车已满");
        }
        // 获取商品信息
        ApiResult<BaseShelfVo> apiResult = productFeign.getBaseShelfProductInfo(BeanUtil.copyProperties(cardAddDto, ShelfRefDto.class));
        if (!apiResult.hasSuccess()) {
            log.info("apiResult={}", apiResult.toString());
            throw new ServiceException("获取商品信息失败");
        }
        BaseShelfVo baseShelfVo = apiResult.getData();
        if (baseShelfVo == null) {
            throw new ServiceException("商品不存在");
        }
        Cart cart = new Cart();
        cart.setId(IdWorker.getId());
        cart.setCustomerNo(cardAddDto.getCustomerNo());
        cart.setShelfId(baseShelfVo.getShelfId());
        cart.setProductName(baseShelfVo.getShelfName());
        cart.setProductImg(baseShelfVo.getProductImg());
        cart.setQuantity(cardAddDto.getQuantity());
        cart.setSkuProperty(baseShelfVo.getSkuProperty());
        cart.setSkuId(baseShelfVo.getSkuId());
        cart.setAmount(baseShelfVo.getShelfPrice());
        save(cart);
    }

    /**
     * 购物车是否满了，只算有效商品
     *
     * @param customerNo
     * @return
     */
    private boolean isCartFull(String customerNo) {
        Integer shelfCount = 0;
        Condition condition = new Condition(Cart.class);
        condition.setOrderByClause("create_time desc,id");
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("customerNo", customerNo);
        List<Cart> cartList = findByCondition(condition);
        if (CollectionUtils.isEmpty(cartList)) {
            return false;
        }
        List<CartListVo> list = BeanUtil.copyProperties(cartList, CartListVo.class);
        BaseShelfVo baseShelfVo;
        for (CartListVo vo : list) {
            ApiResult<BaseShelfVo> apiResult = productFeign.getBaseShelfProductInfo(BeanUtil.copyProperties(vo, ShelfRefDto.class));
            if (!apiResult.hasSuccess()) {
                log.info("apiResult={}", apiResult.toString());
                throw new ServiceException("获取商品信息失败");
            }
            baseShelfVo = apiResult.getData();
            if (baseShelfVo == null) {
                continue;
            }
            if (Constants.ShelfStatus.SHELF.equals(baseShelfVo.getStatus())) {
                shelfCount = shelfCount + 1;
            }
        }
        return shelfCount >= Integer.valueOf(cartMax);
    }

    @Override
    public List<CartListVo> list(CartListDto cartListDto) {
        List<CartListVo> result = new ArrayList<>();
        Condition condition = new Condition(Cart.class);
        condition.setOrderByClause("create_time desc,id");
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("customerNo", cartListDto.getCustomerNo());
        List<Cart> cartList = findByCondition(condition);
        if (CollectionUtils.isEmpty(cartList)) {
            return result;
        }
        List<CartListVo> list = BeanUtil.copyProperties(cartList, CartListVo.class);
        BaseShelfVo baseShelfVo;
        for (CartListVo vo : list) {
            ApiResult<BaseShelfVo> apiResult = productFeign.getBaseShelfProductInfo(BeanUtil.copyProperties(vo, ShelfRefDto.class));
            if (!apiResult.hasSuccess()) {
                log.info("apiResult={}", apiResult.toString());
                throw new ServiceException("获取商品信息失败");
            }
            baseShelfVo = apiResult.getData();
            if (baseShelfVo == null) {
                continue;
            }
            vo.setSendIntegral(baseShelfVo.getSendIntegral());
            vo.setShelfType(baseShelfVo.getShelfType());
            // 降价提醒
            if (baseShelfVo.getShelfPrice().compareTo(vo.getAmount()) < 0) {
                vo.setCompareAmount(vo.getAmount().subtract(baseShelfVo.getShelfPrice()));
                vo.setCompareText(MessageFormat.format(cardCompareText, vo.getAmount().subtract(baseShelfVo.getShelfPrice())
                        ,Constants.ShelfType.SEND_INTEGRAL.equals(vo.getShelfType())?"元":"积分"));
            }
            vo.setAmount(baseShelfVo.getShelfPrice());
            vo.setStock(baseShelfVo.getStock());
            vo.setStatus(baseShelfVo.getStatus());
            vo.setSkuProperty(SkuPropertyUtil.getSkuPropertyValue(vo.getSkuProperty()));
            // 库存提醒
            if (baseShelfVo.getStock().compareTo(new BigDecimal(cartStock)) <= 0) {
                vo.setStockNotify(true);
            }
            if (cartListDto.getStatus().equals(baseShelfVo.getStatus())) {
                result.add(vo);
            }
        }
        return result;
    }

    @Override
    public void update(CartUpdateDto cartUpdateDto) {
        Condition condition = new Condition(Cart.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("id", cartUpdateDto.getId());
        Cart cart = new Cart();
        if (cartUpdateDto.getQuantity() != null) {
            cart.setQuantity(cartUpdateDto.getQuantity());
        }
        if (!StringUtils.isEmpty(cartUpdateDto.getSkuId())) {
            ApiResult<ProductSkuVo> apiResult = productFeign.getProductSkuBySkuId(cartUpdateDto.getSkuId());
            if (!apiResult.hasSuccess()) {
                log.info("apiResult={}", apiResult.toString());
                throw new ServiceException(apiResult.getMsg());
            }
            cart.setSkuProperty(apiResult.getData().getSkuProperty());
            cart.setSkuId(cartUpdateDto.getSkuId());
        }
        cart.setModifyTime(new Date());
        updateByConditionSelective(cart, condition);
    }

    @Override
    public void delete(String ids) {
        this.softDeleteByIds(ids);
    }

    @Override
    public ApiResult<List<CartListVo>> cartValidate(CartValidateDto cartValidateDto) {
        // 库存校验
        List<CartListVo> result = new ArrayList<>();
        List<ProductNoDto> list = cartValidateDto.getProductList();
        BaseShelfVo baseShelfVo;
        for (ProductNoDto productNoDto : list) {
            ApiResult<BaseShelfVo> apiResult = productFeign.getBaseShelfProductInfo(BeanUtil.copyProperties(productNoDto, ShelfRefDto.class));
            if (!apiResult.hasSuccess()) {
                return ApiResult.build(apiResult.getCode(), apiResult.getMsg(), new ArrayList<>());
            }
            baseShelfVo = apiResult.getData();
            if (baseShelfVo.getStock().compareTo(BigDecimal.valueOf(productNoDto.getNum())) < 0) {
                CartListVo vo = new CartListVo();
                vo.setProductName(baseShelfVo.getShelfName());
                vo.setProductImg(baseShelfVo.getProductImg());
                vo.setAmount(baseShelfVo.getShelfPrice());
                vo.setStock(baseShelfVo.getStock());
                vo.setStatus(baseShelfVo.getStatus());
                vo.setSkuProperty(SkuPropertyUtil.getSkuPropertyValue(baseShelfVo.getSkuProperty()));
                vo.setShelfId(baseShelfVo.getShelfId());
                vo.setSkuId(baseShelfVo.getSkuId());
                vo.setQuantity(productNoDto.getNum());
                vo.setCustomerNo(cartValidateDto.getCustomerNo());
                result.add(vo);
            }
        }
        if (!CollectionUtils.isEmpty(result)) {
            return ApiResult.build(ResultEnum.CAN_NOT_BUY.getCode(), "以下商品库存不足", result);
        }
        // 购买权限校验

        return ApiResult.success(result);
    }

    @Override
    public Cart findByParam(String customerNo, Long shelfId, Long skuId) {
        Condition condition = new Condition(Cart.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("shelfId", shelfId);
        criteria.andEqualTo("skuId", skuId);
        List<Cart> list = findByCondition(condition);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public List<Cart> findByCart(String ids) {
        Condition condition = new Condition(Cart.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        String[] cartIds = ids.split(",");
        criteria.andIn("id", Arrays.asList(cartIds));
        criteria.andEqualTo("flag", new Byte(Constants.Flag.VALID));
        return this.findByCondition(condition);
    }
}
