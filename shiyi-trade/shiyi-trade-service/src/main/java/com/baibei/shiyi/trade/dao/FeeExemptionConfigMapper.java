package com.baibei.shiyi.trade.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.trade.feign.bean.dto.FeeExemptionConfigDto;
import com.baibei.shiyi.trade.feign.bean.vo.FeeExemptionConfigVo;
import com.baibei.shiyi.trade.model.FeeExemptionConfig;

import java.util.List;

public interface FeeExemptionConfigMapper extends MyMapper<FeeExemptionConfig> {

    List<FeeExemptionConfigVo> findPageList(FeeExemptionConfigDto feeExemptionConfigDto);
}