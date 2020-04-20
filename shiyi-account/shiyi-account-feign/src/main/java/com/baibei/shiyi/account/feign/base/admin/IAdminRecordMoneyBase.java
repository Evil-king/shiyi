package com.baibei.shiyi.account.feign.base.admin;

import com.baibei.shiyi.account.feign.bean.dto.AdminRecordDto;
import com.baibei.shiyi.account.feign.bean.vo.AdminRecordVo;
import com.baibei.shiyi.account.feign.bean.vo.CustomerBeanVo;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/1 17:58
 * @description:
 */
public interface IAdminRecordMoneyBase {
    /**
     * 资金流水报表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/shiyi/admin/account/recordMoney/pageList", method = RequestMethod.POST)
    @ResponseBody
    ApiResult<MyPageInfo<AdminRecordVo>> pageList(@RequestBody AdminRecordDto recordDto);
    /**
     * 资金流水报表（无分页）
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/shiyi/admin/account/recordMoeny/List", method = RequestMethod.POST)
    @ResponseBody
    ApiResult<List<AdminRecordVo>> list(@RequestBody AdminRecordDto recordDto);
}
