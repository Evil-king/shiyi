package com.baibei.shiyi.trade.feign.base;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.DealOrderDto;
import com.baibei.shiyi.trade.feign.bean.vo.DealOrderVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/18 10:27
 * @description:
 */
public interface IAdminDealOrderBase {
    /**
     * 成交列表
     * @param dealOrderDto
     * @return
     */
    @PostMapping(value = "/shiyi/admin/trade/dealOrder/pageList")
    ApiResult<MyPageInfo<DealOrderVo>> pageList(@RequestBody DealOrderDto dealOrderDto);

    /**
     * 查询成交列表中的数据 以方便导出
     * @param dealOrderDto
     * @return
     */
    @PostMapping(value = "/shiyi/admin/trade/dealOrder/list")
    ApiResult<List<DealOrderVo>> List(DealOrderDto dealOrderDto);
}
