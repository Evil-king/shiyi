package com.baibei.shiyi.cash.service.impl;

import com.baibei.shiyi.cash.dao.FuqingAccountDepositMapper;
import com.baibei.shiyi.cash.feign.base.dto.DepositWithDrawDto;
import com.baibei.shiyi.cash.feign.base.vo.DepositWithdrawVo;
import com.baibei.shiyi.cash.service.IDepositWithdrawService;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositWithdrawServiceImpl implements IDepositWithdrawService {

    @Autowired
    private FuqingAccountDepositMapper fuqingAccountDepositMapper;

    @Override
    public MyPageInfo<DepositWithdrawVo> pageList(DepositWithDrawDto depositWithDrawDto) {
        if (depositWithDrawDto.getCurrentPage() != null && depositWithDrawDto.getPageSize() != null) {
            PageHelper.startPage(depositWithDrawDto.getCurrentPage(), depositWithDrawDto.getPageSize());
        }
        List<DepositWithdrawVo> depositWithdrawVoList = fuqingAccountDepositMapper.pageList(depositWithDrawDto);
        return new MyPageInfo<>(depositWithdrawVoList);
    }
}
