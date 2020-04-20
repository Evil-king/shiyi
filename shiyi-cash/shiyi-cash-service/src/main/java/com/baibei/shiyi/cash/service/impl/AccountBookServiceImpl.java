package com.baibei.shiyi.cash.service.impl;

import com.baibei.shiyi.cash.dao.AccountBookMapper;
import com.baibei.shiyi.cash.feign.base.dto.Apply1010PagelistDto;
import com.baibei.shiyi.cash.feign.base.vo.Apply1010PagelistVo;
import com.baibei.shiyi.cash.feign.base.vo.MemberBalanceVo;
import com.baibei.shiyi.cash.feign.base.vo.WithdrawListVo;
import com.baibei.shiyi.cash.model.AccountBook;
import com.baibei.shiyi.cash.service.IAccountBookService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.*;
import com.baibei.shiyi.pingan.feign.base.dto.PABSendDto;
import com.baibei.shiyi.pingan.feign.base.vo.PABSendVo;
import com.baibei.shiyi.pingan.feign.client.IPABSendMessageFeign;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
* @author: Longer
* @date: 2019/11/06 11:18:25
* @description: AccountBook服务实现
*/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountBookServiceImpl extends AbstractService<AccountBook> implements IAccountBookService {

    @Autowired
    private AccountBookMapper accountBookMapper;

    @Autowired
    private IPABSendMessageFeign pabSendMessageFeign;

    @Autowired
    private IAccountBookService accountBookService;

    @Override
    public void clear() {
        accountBookMapper.clear();
    }

    @Override
    public ApiResult apply1010(String selectFlag, String pageNum) {
        ApiResult apiResult = new ApiResult();
        List<MemberBalanceVo> balanceVoList = Lists.newArrayList();
        //组装数据
        HashMap<String, String> parmaKeyDict = new HashMap<String, String>();// 请求报文所需字段参数
        Map<String, String> retKeyDict;// 返回报文解析结果
        String thirdLogNo = NoUtil.getMallOrderNo();
        log.info("thirdLogNo："+thirdLogNo);
        parmaKeyDict.put("TranFunc", "1010");// 接口交易码
        parmaKeyDict.put("ThirdLogNo", thirdLogNo);

        parmaKeyDict.put("SelectFlag", selectFlag);//查询标志

        parmaKeyDict.put("PageNum", pageNum);//第几页

        parmaKeyDict.put("Reserve", "");//保留域

        String message = BankMessageSpliceUtils.getSignMessageBody_1010(parmaKeyDict);
        PABSendDto pabSendDto = new PABSendDto();
        pabSendDto.setTranFunc(1010);
        pabSendDto.setThirdLogNo(thirdLogNo);
        pabSendDto.setMessage(message);
        ApiResult<PABSendVo> pabSendVoApiResult = pabSendMessageFeign.sendMessage(pabSendDto);
        if (pabSendVoApiResult.getCode()!=ResultEnum.BUSINESS_ERROE.getCode()
                &&pabSendVoApiResult.getCode()!=ResultEnum.SUCCESS.getCode()) {
            log.info("调用1010接口失败：",pabSendVoApiResult.getMsg());
            throw new ServiceException("调用1325接口失败");
        }
        PABSendVo data = pabSendVoApiResult.getData();
        if ("000000".equals(data.getRspCode())) {
            Map<String, String> respMap = new HashMap();
            respMap.put("backBodyMessages",data.getBackBodyMessages());
            BankMessageAnalysisUtils.spiltMessage_1010(respMap);
            String array = respMap.get("Array");
            String[] arr = array.split("&");
            for (int j = 0; j < arr.length; j += 11) {
                AccountBook accountBook = new AccountBook();
                accountBook.setId(IdWorker.getId());
                accountBook.setCustAcctid(arr[j]);
                accountBook.setCustFlag(Byte.valueOf(arr[j + 1]));
                accountBook.setCustType(Byte.valueOf(arr[j + 2]));
                accountBook.setCustStatus(Byte.valueOf(arr[j + 3]));
                accountBook.setCustomerNo(arr[j + 4]);
                accountBook.setMainAcctid(arr[j + 5]);
                accountBook.setCustName(arr[j + 6]);
                accountBook.setTotalAmount(new BigDecimal(arr[j + 7]));
                accountBook.setTotalBalance(new BigDecimal(arr[j + 8]));
                accountBook.setTotalFreezeAmount(new BigDecimal(arr[j + 9]));
                accountBook.setTranDate(arr[j + 10]);
                accountBook.setFlag((byte) 1);
                accountBook.setCreateTime(new Date());
                accountBook.setModifyTime(new Date());
                MemberBalanceVo memberBalanceVo = BeanUtil.copyProperties(accountBook, MemberBalanceVo.class);
                accountBookMapper.insert(accountBook);
                balanceVoList.add(memberBalanceVo);
                apiResult.setData(balanceVoList);
            }
            if (respMap.get("LastPage").equals("1")) {
                apiResult.setCode(-901);
                return apiResult;
            }else{
                return apiResult;
            }
        } else {
            apiResult.setCode(-901);
            apiResult.setMsg(data.getRspMsg());
            return apiResult;

        }
    }

    @Override
    public MyPageInfo<Apply1010PagelistVo> apply1010Pagelist(Apply1010PagelistDto apply1010PagelistDto) {
        if (StringUtils.isEmpty(apply1010PagelistDto.getCurrentPage())) {
            throw new ServiceException("当前页不能为空");
        }
        if (StringUtils.isEmpty(apply1010PagelistDto.getPageSize())) {
            throw new ServiceException("页码不能为空");
        }
        PageHelper.startPage(apply1010PagelistDto.getCurrentPage(), apply1010PagelistDto.getPageSize());
        List<Apply1010PagelistVo> apply1010PagelistVoList = accountBookMapper.pageList(apply1010PagelistDto);
        MyPageInfo<Apply1010PagelistVo> pageInfo = new MyPageInfo<>(apply1010PagelistVoList);
        return pageInfo;
    }

    @Override
    public List<Apply1010PagelistVo> apply1010List(Apply1010PagelistDto apply1010PagelistDto) {
        List<Apply1010PagelistVo> apply1010PagelistVoList = accountBookMapper.pageList(apply1010PagelistDto);
        return apply1010PagelistVoList;
    }
}
