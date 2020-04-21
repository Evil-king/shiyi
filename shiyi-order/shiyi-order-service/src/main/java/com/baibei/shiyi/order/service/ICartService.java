package com.baibei.shiyi.order.service;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.order.common.dto.CartAddDto;
import com.baibei.shiyi.order.common.dto.CartListDto;
import com.baibei.shiyi.order.common.dto.CartUpdateDto;
import com.baibei.shiyi.order.common.dto.CartValidateDto;
import com.baibei.shiyi.order.common.vo.CartListVo;
import com.baibei.shiyi.order.feign.base.dto.ShiyiCartDto;
import com.baibei.shiyi.order.model.Cart;
import com.baibei.shiyi.common.core.mybatis.Service;

import javax.smartcardio.Card;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/31 18:16:01
 * @description: Cart服务接口
 */
public interface ICartService extends Service<Cart> {

    /**
     * 加入购物车
     *
     * @param cardAddDto
     */
    void add(CartAddDto cardAddDto);

    /**
     * 购物车列表
     *
     * @return
     */
    List<CartListVo> list(CartListDto cartListDto);


    /**
     * 更新
     *
     * @param cartUpdateDto
     */
    void update(CartUpdateDto cartUpdateDto);

    /**
     * 删除
     *
     * @param ids
     */
    void delete(String ids);

    /**
     * 购物车结算校验
     *
     * @param cartValidateDto
     * @return
     */
    ApiResult<List<CartListVo>> cartValidate(CartValidateDto cartValidateDto);


    /**
     * 查询客户加入指定购物车商品
     *
     * @param customerNo
     * @param shelfId
     * @param skuId
     * @return
     */
    Cart findByParam(String customerNo, Long shelfId, Long skuId);


    /**
     * 查询购物车
     *
     * @return
     */
    List<Cart> findByCart(String ids);
}
