package com.baibei.shiyi.trade.service.impl;

import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.trade.dao.FeeExemptionConfigMapper;
import com.baibei.shiyi.trade.feign.bean.dto.FeeExemptionConfigDto;
import com.baibei.shiyi.trade.feign.bean.vo.FeeExemptionConfigVo;
import com.baibei.shiyi.trade.model.FeeExemptionConfig;
import com.baibei.shiyi.trade.service.IFeeExemptionConfigService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
 * @author: uqing
 * @date: 2019/12/20 16:18:12
 * @description: FeeExemptionConfig服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FeeExemptionConfigServiceImpl extends AbstractService<FeeExemptionConfig> implements IFeeExemptionConfigService {

    @Autowired
    private FeeExemptionConfigMapper tblTraFeeExemptionConfigMapper;

    @Override
    public MyPageInfo<FeeExemptionConfigVo> pageList(FeeExemptionConfigDto feeExemptionConfigDto) {
        if (feeExemptionConfigDto.getCurrentPage() != null && feeExemptionConfigDto.getPageSize() != null) {
            PageHelper.startPage(feeExemptionConfigDto.getCurrentPage(), feeExemptionConfigDto.getPageSize());
        }
        List<FeeExemptionConfigVo> result = tblTraFeeExemptionConfigMapper.findPageList(feeExemptionConfigDto);
        return new MyPageInfo<>(result);
    }

    @Override
    public void add(FeeExemptionConfigDto feeExemptionConfigDto) {
        FeeExemptionConfig exemptionConfig = BeanUtil.copyProperties(feeExemptionConfigDto, FeeExemptionConfig.class);
        exemptionConfig.setId(IdWorker.getId());
        exemptionConfig.setFlag(new Byte(Constants.Flag.VALID));
        exemptionConfig.setCreateTime(new Date());
        exemptionConfig.setModifyTime(new Date());
        this.save(exemptionConfig);
    }

    @Override
    public FeeExemptionConfig getByCustomerNo(String customerNo) {
        if (StringUtils.isEmpty(customerNo)) {
            throw new IllegalArgumentException("customerNo cannot be null");
        }
        Condition condition = new Condition(FeeExemptionConfig.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("customerNo", customerNo);
        return findOneByCondition(condition);
    }

    @Override
    public boolean isExist(String customerNo) {
        Condition condition = new Condition(FeeExemptionConfig.class);
        Example.Criteria criteria  = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("flag","1");
        FeeExemptionConfig oneByCondition = findOneByCondition(condition);
        return oneByCondition == null ? false : true;
    }
}
