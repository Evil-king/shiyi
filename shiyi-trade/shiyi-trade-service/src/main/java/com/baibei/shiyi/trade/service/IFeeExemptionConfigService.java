package com.baibei.shiyi.trade.service;

import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.FeeExemptionConfigDto;
import com.baibei.shiyi.trade.feign.bean.vo.FeeExemptionConfigVo;
import com.baibei.shiyi.trade.model.FeeExemptionConfig;
import com.baibei.shiyi.common.core.mybatis.Service;


/**
 * @author: uqing
 * @date: 2019/12/20 16:18:12
 * @description: FeeExemptionConfig服务接口
 */
public interface IFeeExemptionConfigService extends Service<FeeExemptionConfig> {

    MyPageInfo<FeeExemptionConfigVo> pageList(FeeExemptionConfigDto feeExemptionConfigDto);

    void add(FeeExemptionConfigDto feeExemptionConfigDto);

    FeeExemptionConfig getByCustomerNo(String customerNo);

    boolean isExist(String customerNo);

}
