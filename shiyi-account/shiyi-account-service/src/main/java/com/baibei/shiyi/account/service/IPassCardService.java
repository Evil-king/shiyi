package com.baibei.shiyi.account.service;
import com.baibei.shiyi.account.common.dto.ExtractProductDto;
import com.baibei.shiyi.account.feign.bean.dto.ChangeAmountDto;
import com.baibei.shiyi.account.model.PassCard;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.content.feign.bean.vo.ExtractProductVo;


/**
* @author: hyc
* @date: 2019/11/11 10:32:40
* @description: PassCard服务接口
*/
public interface IPassCardService extends Service<PassCard> {
    /**
     * 修改通证数量
     * @return
     */
    ApiResult changeAmount(ChangeAmountDto changeAmountDto);

    PassCard findByCustomerNo(String customerNo);

    /**
     * 提取仓单
     * @param extractProductDto
     * @param data
     * @return
     */
    ApiResult extractProduct(ExtractProductDto extractProductDto, ExtractProductVo data);
}
