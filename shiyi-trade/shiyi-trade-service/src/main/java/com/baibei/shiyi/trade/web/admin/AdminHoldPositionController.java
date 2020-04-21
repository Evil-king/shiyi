package com.baibei.shiyi.trade.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.HoldPositionDto;
import com.baibei.shiyi.trade.feign.bean.vo.HoldPositionVo;
import com.baibei.shiyi.trade.feign.client.admin.IAdminHoldPositionFeign;
import com.baibei.shiyi.trade.model.HoldPosition;
import com.baibei.shiyi.trade.service.IHoldPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 客户持仓查询
 */
@RestController
@RequestMapping("/shiyi/admin/hold/position")
public class AdminHoldPositionController implements IAdminHoldPositionFeign {

    @Autowired
    private IHoldPositionService holdPositionService;

    /**
     * 分页查询
     *
     * @param holdPositionDto
     * @return
     */
    @Override
    public ApiResult<MyPageInfo<HoldPositionVo>> pageList(@Validated @RequestBody HoldPositionDto holdPositionDto) {
        return ApiResult.success(holdPositionService.pageList(holdPositionDto));
    }

    /**
     * 导出数据查询
     *
     * @param holdPositionDto
     * @return
     */
    @Override
    public ApiResult<List<HoldPositionVo>> exportList(@RequestBody HoldPositionDto holdPositionDto) {
        MyPageInfo<HoldPositionVo> list = holdPositionService.pageList(holdPositionDto);
        return ApiResult.success(list.getList());
    }
}
