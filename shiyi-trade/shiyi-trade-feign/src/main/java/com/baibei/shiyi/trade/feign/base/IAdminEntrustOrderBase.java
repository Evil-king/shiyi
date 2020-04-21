package com.baibei.shiyi.trade.feign.base;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.CustomerEntrustOrderListDto;
import com.baibei.shiyi.trade.feign.bean.vo.CustomerEntrustOrderListVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/14 16:04
 * @description:
 */
public interface IAdminEntrustOrderBase {
    /**
     * 后台委托列表
     * @param customerEntrustOrderListDto
     * @return
     */
    @RequestMapping("/shiyi/admin/trade/entrustOrder/pageList")
    ApiResult<MyPageInfo<CustomerEntrustOrderListVo>> pageList(@RequestBody CustomerEntrustOrderListDto customerEntrustOrderListDto);

    /**
     * 后台委托列表导出查询所有list集合
     * @param customerEntrustOrderListDto
     * @return
     */
    @RequestMapping("/shiyi/admin/trade/entrustOrder/export")
    ApiResult<List<CustomerEntrustOrderListVo>> export(@RequestBody  CustomerEntrustOrderListDto customerEntrustOrderListDto);
}
