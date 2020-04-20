package com.baibei.shiyi.account.service;
import com.baibei.shiyi.account.feign.bean.dto.OperationDto;
import com.baibei.shiyi.account.feign.bean.dto.PassCardExtractOrderListDto;
import com.baibei.shiyi.account.feign.bean.vo.PassCardExtractOrderListVo;
import com.baibei.shiyi.account.model.PassCardExtractOrder;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;

import java.util.List;


/**
* @author: hyc
* @date: 2019/11/12 11:19:43
* @description: PassCardExtractOrder服务接口
*/
public interface IPassCardExtractOrderService extends Service<PassCardExtractOrder> {
    /**
     * 后台列表
     * @param passCardExtractOrderListDto
     * @return
     */
    ApiResult<MyPageInfo<PassCardExtractOrderListVo>> getPageList(PassCardExtractOrderListDto passCardExtractOrderListDto);

    /**
     * 批量操作
     * @param operationDto
     * @return
     */
    ApiResult operation(OperationDto operationDto);

    /**
     * 导出，返回一个list集合
     * @param passCardExtractOrderListDto
     * @return
     */
    ApiResult<List<PassCardExtractOrderListVo>> export(PassCardExtractOrderListDto passCardExtractOrderListDto);

    /**
     * 系统操作提取仓单
     * @return
     */
    ApiResult systemOperation();
}
