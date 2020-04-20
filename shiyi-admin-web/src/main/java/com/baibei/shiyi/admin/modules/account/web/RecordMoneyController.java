package com.baibei.shiyi.admin.modules.account.web;

import com.baibei.shiyi.account.feign.bean.dto.AdminRecordDto;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.vo.AdminRecordVo;
import com.baibei.shiyi.account.feign.client.AdminRecordMoneyFeign;
import com.baibei.shiyi.admin.modules.account.bean.vo.EnumMessageVo;
import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.TradeMoneyTradeTypeEnum;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.MobileUtils;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.client.CustomerFeign;
import com.baibei.shiyi.user.feign.client.admin.AdminCustomerFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/1 17:47
 * @description:
 */
@RestController
@RequestMapping("/admin/account/recordMoney")
public class RecordMoneyController {

    @Autowired
    private AdminCustomerFeign customerFeign;

    @Autowired
    private AdminRecordMoneyFeign recordMoneyFeign;
    @Autowired
    private ExcelUtils excelUtils;

    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('ASSET_MANAGE_LOG'))")
    public ApiResult<MyPageInfo<AdminRecordVo>> pageList(@RequestBody AdminRecordDto recordDto){
        ApiResult<MyPageInfo<AdminRecordVo>> myPageInfo=recordMoneyFeign.pageList(recordDto);
        if(!myPageInfo.hasSuccess()){
            return myPageInfo;
        }
        List<String> customerNos=new ArrayList<>();
        for (int i = 0; i <myPageInfo.getData().getList().size();  i++) {
            customerNos.add(myPageInfo.getData().getList().get(i).getCustomerNo());
        }
        ApiResult<List<CustomerVo>> byCustomerNoList = customerFeign.findByCustomerNoList(customerNos);
        if(byCustomerNoList.hasSuccess()){
            for (int i = 0; i <byCustomerNoList.getData().size() ; i++) {
                if(byCustomerNoList.getData().get(i).getCustomerNo().equals(myPageInfo.getData().getList().get(i).getCustomerNo())) {
                    myPageInfo.getData().getList().get(i).setMobile(byCustomerNoList.getData().get(i).getMobile());
                    myPageInfo.getData().getList().get(i).setRecommenderId(byCustomerNoList.getData().get(i).getRecommenderId());
                }
            }
        }
        return myPageInfo;
    }
    @RequestMapping("/export")
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('ASSET_MANAGE_LOG'))")
    public void export(@RequestBody AdminRecordDto recordDto, HttpServletResponse response){
        if(StringUtils.isEmpty(recordDto.getTradeType())){
            recordDto.setTradeType(TradeMoneyTradeTypeEnum.getCode(recordDto.getTradeType()));
        }
        ApiResult<List<AdminRecordVo>> result=recordMoneyFeign.list(recordDto);
        List<String> customerNos=new ArrayList<>();
        for (int i = 0; i <result.getData().size();  i++) {
            customerNos.add(result.getData().get(i).getCustomerNo());
            if(result.getData().get(i).getRetype().equals(Constants.Retype.IN)){
                result.getData().get(i).setRetype("收入");
            }else if(result.getData().get(i).getRetype().equals(Constants.Retype.OUT)){
                result.getData().get(i).setRetype("支出");
            }else {
                result.getData().get(i).setRetype("");
            }
        }
        ApiResult<List<CustomerVo>> byCustomerNoList = customerFeign.findByCustomerNoList(customerNos);
        if(byCustomerNoList.hasSuccess()){
            for (int i = 0; i <byCustomerNoList.getData().size() ; i++) {
                if(byCustomerNoList.getData().get(i).getCustomerNo().equals(result.getData().get(i).getCustomerNo())){
                    result.getData().get(i).setMobile(byCustomerNoList.getData().get(i).getMobile());
                    result.getData().get(i).setRecommenderId(byCustomerNoList.getData().get(i).getRecommenderId());
                }
            }
        }
        excelUtils.defaultExcelExport(result.getData(),response,AdminRecordVo.class,"资金流水报表");
    }
    @RequestMapping("/getEnumMessage")
    public ApiResult<List<EnumMessageVo>> getEnumMessage(){
        List<EnumMessageVo> enumMessageVos=new ArrayList<>();
        TradeMoneyTradeTypeEnum[] customerStatusEnums=TradeMoneyTradeTypeEnum.values();
        for (int i = 0; i <customerStatusEnums.length ; i++) {
            EnumMessageVo enumMessageVo=new EnumMessageVo();
            enumMessageVo.setCode(customerStatusEnums[i].getCode());
            enumMessageVo.setMessage(customerStatusEnums[i].getMsg());
            enumMessageVos.add(enumMessageVo);
        }
        return ApiResult.success(enumMessageVos);
    }
}
