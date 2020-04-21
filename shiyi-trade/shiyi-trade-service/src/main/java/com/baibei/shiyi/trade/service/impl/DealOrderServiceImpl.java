package com.baibei.shiyi.trade.service.impl;

import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.trade.common.bo.DeListBo;
import com.baibei.shiyi.trade.common.dto.DealOrderQueryDTO;
import com.baibei.shiyi.trade.common.dto.MyDealOorderListDto;
import com.baibei.shiyi.trade.common.vo.DealOrderHistoryVo;
import com.baibei.shiyi.trade.common.vo.MyDealOrderListVo;
import com.baibei.shiyi.trade.dao.DealOrderMapper;
import com.baibei.shiyi.trade.feign.bean.dto.DealOrderDto;
import com.baibei.shiyi.trade.feign.bean.vo.DealOrderVo;
import com.baibei.shiyi.trade.model.DealOrder;
import com.baibei.shiyi.trade.service.IDealOrderService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author: wenqing
 * @date: 2019/11/12 19:13:49
 * @description: DealOrder服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DealOrderServiceImpl extends AbstractService<DealOrder> implements IDealOrderService {

    @Autowired
    private DealOrderMapper dealOrderMapper;


    @Override
    public void save(DeListBo bo) {
        DealOrder dealOrder = BeanUtil.copyProperties(bo, DealOrder.class);
        dealOrder.setId(IdWorker.getId());
        dealOrder.setCreateTime(new Date());
        save(dealOrder);
    }

    @Override
    public void updateStatus(String dealNo, String status) {
        Condition condition = new Condition(DealOrder.class);
        buildValidCriteria(condition).andEqualTo("dealNo", dealNo);
        DealOrder dealOrder = new DealOrder();
        dealOrder.setStatus(status);
        dealOrder.setModifyTime(new Date());
        updateByConditionSelective(dealOrder, condition);
    }

    @Override
    public MyPageInfo<MyDealOrderListVo> dealOrderList(MyDealOorderListDto myDealOorderListDto) {

        if (myDealOorderListDto.getStartTime() != null && !"".equals(myDealOorderListDto.getStartTime())) {
            String startTime = stampToDate(myDealOorderListDto.getStartTime()) + " 00:00:00";
            myDealOorderListDto.setStartTime(startTime);
        }
        if (myDealOorderListDto.getEndTime() != null && !"".equals(myDealOorderListDto.getEndTime())) {
            String endTime = stampToDate(myDealOorderListDto.getEndTime()) + " 23:59:59";
            myDealOorderListDto.setEndTime(endTime);
        }
        PageHelper.startPage(myDealOorderListDto.getCurrentPage(), myDealOorderListDto.getPageSize());
        List<MyDealOrderListVo> list = dealOrderMapper.dealOrderList(myDealOorderListDto);
        list.stream().forEach(result -> {
            if (myDealOorderListDto.getCustomerNo().equals(result.getSellCustomerNo())) {
                result.setTypeDesc("卖出");
                result.setType("sell");
            }
            if (myDealOorderListDto.getCustomerNo().equals(result.getBuyCustomerNo())) {
                result.setTypeDesc("买入");
                result.setType("buy");
            }
        });
        MyPageInfo<MyDealOrderListVo> myPageInfo = new MyPageInfo<>(list);
        return myPageInfo;
    }

    @Override
    public MyPageInfo<DealOrderVo> myPageList(DealOrderDto dealOrderDto) {
        PageHelper.startPage(dealOrderDto.getCurrentPage(), dealOrderDto.getPageSize());
        List<DealOrderVo> dealOrderVos = dealOrderMapper.myPageList(dealOrderDto);
        MyPageInfo<DealOrderVo> myPageInfo = new MyPageInfo<>(dealOrderVos);
        return myPageInfo;
    }

    @Override
    public List<DealOrderVo> List(DealOrderDto dealOrderDto) {
        List<DealOrderVo> dealOrderVos = dealOrderMapper.myPageList(dealOrderDto);
        return dealOrderVos;
    }

    @Override
    public MyPageInfo<DealOrderHistoryVo> customerHistoryList(DealOrderQueryDTO dealOrderQueryDTO) {
        PageHelper.startPage(dealOrderQueryDTO.getCurrentPage(), dealOrderQueryDTO.getPageSize());
        List<DealOrderHistoryVo> list = dealOrderMapper.customerHistoryList(dealOrderQueryDTO);
        MyPageInfo<DealOrderHistoryVo> myPageInfo = new MyPageInfo<>(list);
        return myPageInfo;
    }

    protected String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
