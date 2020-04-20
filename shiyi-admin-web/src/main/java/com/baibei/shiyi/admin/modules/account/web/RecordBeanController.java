package com.baibei.shiyi.admin.modules.account.web;

import com.baibei.shiyi.account.feign.bean.dto.AdminRecordBeanDto;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.vo.AdminRecordVo;
import com.baibei.shiyi.account.feign.client.AdminRecordBeanFeign;
import com.baibei.shiyi.admin.modules.account.bean.dto.RecordBeanDto;
import com.baibei.shiyi.admin.modules.account.bean.vo.EnumMessageVo;
import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.RecordBeanTradeTypeEnum;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.MobileUtils;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.client.CustomerFeign;
import com.baibei.shiyi.user.feign.client.admin.AdminCustomerFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/1 19:59
 * @description:
 */
@RestController
@Slf4j
@RequestMapping("/admin/account/recordBean")
public class RecordBeanController {
    @Autowired
    private AdminRecordBeanFeign adminRecordBeanFeign;

    @Autowired
    private AdminCustomerFeign customerFeign;

    @Autowired
    private ExcelUtils excelUtils;

    /**
     * 消费积分流水
     * @param recordDto
     * @return
     */
    @RequestMapping("/comsuptionPageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('ASSET_MANAGE_CONSUME'))")
    public ApiResult<MyPageInfo<AdminRecordVo>> comsuptionPageList(@RequestBody AdminRecordBeanDto recordDto){
        ApiResult<MyPageInfo<AdminRecordVo>> myPageInfo=adminRecordBeanFeign.recordPageList(recordDto);
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

    /**
     * 兑换积分流水
     * @param recordDto
     * @return
     */
    @RequestMapping("/exchangePageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('ASSET_MANAGE_EXCHANGE'))")
    public ApiResult<MyPageInfo<AdminRecordVo>> exchangePageList(@RequestBody AdminRecordBeanDto recordDto){
        ApiResult<MyPageInfo<AdminRecordVo>> myPageInfo=adminRecordBeanFeign.recordPageList(recordDto);
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

    /**
     * 屹家无忧积分流水
     * @param recordDto
     * @return
     */
    @RequestMapping("/shiyiPageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('ASSET_MANAGE_CAREFREE'))")
    public ApiResult<MyPageInfo<AdminRecordVo>> shiyiPageList(@RequestBody AdminRecordBeanDto recordDto){
        ApiResult<MyPageInfo<AdminRecordVo>> myPageInfo=adminRecordBeanFeign.recordPageList(recordDto);
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

    /**
     * 商城账户流水表
     * @param recordDto
     * @return
     */
    @RequestMapping("/mallAccountPageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('ASSET_MANAGE_SHOP_ACCOUNT'))")
    public ApiResult<MyPageInfo<AdminRecordVo>> mallAccountPageList(@RequestBody AdminRecordBeanDto recordDto){
        ApiResult<MyPageInfo<AdminRecordVo>> myPageInfo=adminRecordBeanFeign.recordPageList(recordDto);
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


    /**
     * 消费积分流水导出
     * @param recordDto
     * @param response
     */
    @RequestMapping("/comsuptionExport")
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('ASSET_MANAGE_CONSUME'))")
    public void  comsuptionExport(@RequestBody AdminRecordBeanDto recordDto, HttpServletResponse response){
        String beanType=recordDto.getBeanType();
        if(beanType.equals(Constants.BeanType.CONSUMPTION)){
            beanType="消费积分";
        }else if(beanType.equals(Constants.BeanType.EXCHANGE)){
            beanType="兑换积分";
        }else if(beanType.equals(Constants.BeanType.SHIYI)){
            beanType="世屹无忧";
        }else if(beanType.equals(Constants.BeanType.MALLACCOUNT)){
            beanType="商城账户";
        }
        if(StringUtils.isEmpty(recordDto.getTradeType())){
            recordDto.setTradeType(RecordBeanTradeTypeEnum.getCode(recordDto.getTradeType()));
        }
        ApiResult<List<AdminRecordVo>> recordVos=adminRecordBeanFeign.export(recordDto);
        List<String> customerNos=new ArrayList<>();
        for (int i = 0; i <recordVos.getData().size();  i++) {
            customerNos.add(recordVos.getData().get(i).getCustomerNo());
            if(recordVos.getData().get(i).getRetype().equals(Constants.Retype.IN)){
                recordVos.getData().get(i).setRetype("收入");
            }else if(recordVos.getData().get(i).getRetype().equals(Constants.Retype.OUT)){
                recordVos.getData().get(i).setRetype("支出");
            }else {
                recordVos.getData().get(i).setRetype("");
            }
        }
        ApiResult<List<CustomerVo>> byCustomerNoList = customerFeign.findByCustomerNoList(customerNos);
        if(byCustomerNoList.hasSuccess()){
            for (int i = 0; i <byCustomerNoList.getData().size() ; i++) {
                if(byCustomerNoList.getData().get(i).getCustomerNo().equals(recordVos.getData().get(i).getCustomerNo())){
                    recordVos.getData().get(i).setMobile(byCustomerNoList.getData().get(i).getMobile());
                    recordVos.getData().get(i).setRecommenderId(byCustomerNoList.getData().get(i).getRecommenderId());
                }
            }
        }
        excelUtils.defaultExcelExport(recordVos.getData(),response,AdminRecordVo.class,beanType+"流水报表");
    }

    /**
     * 兑换积分流水导出
     * @param recordDto
     * @param response
     */
    @RequestMapping("/exchangeExport")
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('ASSET_MANAGE_EXCHANGE'))")
    public void  exchangeExport(@RequestBody AdminRecordBeanDto recordDto, HttpServletResponse response){
        String beanType=recordDto.getBeanType();
        if(beanType.equals(Constants.BeanType.CONSUMPTION)){
            beanType="消费积分";
        }else if(beanType.equals(Constants.BeanType.EXCHANGE)){
            beanType="兑换积分";
        }else if(beanType.equals(Constants.BeanType.SHIYI)){
            beanType="世屹无忧";
        }else if(beanType.equals(Constants.BeanType.MALLACCOUNT)){
            beanType="商城账户";
        }
        if(StringUtils.isEmpty(recordDto.getTradeType())){
            recordDto.setTradeType(RecordBeanTradeTypeEnum.getCode(recordDto.getTradeType()));
        }
        ApiResult<List<AdminRecordVo>> recordVos=adminRecordBeanFeign.export(recordDto);
        List<String> customerNos=new ArrayList<>();
        for (int i = 0; i <recordVos.getData().size();  i++) {
            customerNos.add(recordVos.getData().get(i).getCustomerNo());
            if(recordVos.getData().get(i).getRetype().equals(Constants.Retype.IN)){
                recordVos.getData().get(i).setRetype("收入");
            }else if(recordVos.getData().get(i).getRetype().equals(Constants.Retype.OUT)){
                recordVos.getData().get(i).setRetype("支出");
            }else {
                recordVos.getData().get(i).setRetype("");
            }
        }
        ApiResult<List<CustomerVo>> byCustomerNoList = customerFeign.findByCustomerNoList(customerNos);
        if(byCustomerNoList.hasSuccess()){
            for (int i = 0; i <byCustomerNoList.getData().size() ; i++) {
                if(byCustomerNoList.getData().get(i).getCustomerNo().equals(recordVos.getData().get(i).getCustomerNo())){
                    recordVos.getData().get(i).setMobile(byCustomerNoList.getData().get(i).getMobile());
                    recordVos.getData().get(i).setRecommenderId(byCustomerNoList.getData().get(i).getRecommenderId());
                }
            }
        }
        excelUtils.defaultExcelExport(recordVos.getData(),response,AdminRecordVo.class,beanType+"流水报表");
    }


    /**
     * 屹家无忧流水导出
     * @param recordDto
     * @param response
     */
    @RequestMapping("/shiyiExport")
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('ASSET_MANAGE_CAREFREE'))")
    public void  shiyiExport(@RequestBody AdminRecordBeanDto recordDto, HttpServletResponse response){
        String beanType=recordDto.getBeanType();
        if(beanType.equals(Constants.BeanType.CONSUMPTION)){
            beanType="消费积分";
        }else if(beanType.equals(Constants.BeanType.EXCHANGE)){
            beanType="兑换积分";
        }else if(beanType.equals(Constants.BeanType.SHIYI)){
            beanType="世屹无忧";
        }else if(beanType.equals(Constants.BeanType.MALLACCOUNT)){
            beanType="商城账户";
        }
        if(StringUtils.isEmpty(recordDto.getTradeType())){
            recordDto.setTradeType(RecordBeanTradeTypeEnum.getCode(recordDto.getTradeType()));
        }
        ApiResult<List<AdminRecordVo>> recordVos=adminRecordBeanFeign.export(recordDto);
        List<String> customerNos=new ArrayList<>();
        for (int i = 0; i <recordVos.getData().size();  i++) {
            customerNos.add(recordVos.getData().get(i).getCustomerNo());
            if(recordVos.getData().get(i).getRetype().equals(Constants.Retype.IN)){
                recordVos.getData().get(i).setRetype("收入");
            }else if(recordVos.getData().get(i).getRetype().equals(Constants.Retype.OUT)){
                recordVos.getData().get(i).setRetype("支出");
            }else {
                recordVos.getData().get(i).setRetype("");
            }
        }
        ApiResult<List<CustomerVo>> byCustomerNoList = customerFeign.findByCustomerNoList(customerNos);
        if(byCustomerNoList.hasSuccess()){
            for (int i = 0; i <byCustomerNoList.getData().size() ; i++) {
                if(byCustomerNoList.getData().get(i).getCustomerNo().equals(recordVos.getData().get(i).getCustomerNo())){
                    recordVos.getData().get(i).setMobile(byCustomerNoList.getData().get(i).getMobile());
                    recordVos.getData().get(i).setRecommenderId(byCustomerNoList.getData().get(i).getRecommenderId());
                }
            }
        }
        excelUtils.defaultExcelExport(recordVos.getData(),response,AdminRecordVo.class,beanType+"流水报表");
    }


    /**
     * 商城账户流水导出
     * @param recordDto
     * @param response
     */
    @RequestMapping("/mallAccountExport")
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('ASSET_MANAGE_SHOP_ACCOUNT'))")
    public void  mallAccountExport(@RequestBody AdminRecordBeanDto recordDto, HttpServletResponse response){
        String beanType=recordDto.getBeanType();
        if(beanType.equals(Constants.BeanType.CONSUMPTION)){
            beanType="消费积分";
        }else if(beanType.equals(Constants.BeanType.EXCHANGE)){
            beanType="兑换积分";
        }else if(beanType.equals(Constants.BeanType.SHIYI)){
            beanType="世屹无忧";
        }else if(beanType.equals(Constants.BeanType.MALLACCOUNT)){
            beanType="商城账户";
        }
        if(StringUtils.isEmpty(recordDto.getTradeType())){
            recordDto.setTradeType(RecordBeanTradeTypeEnum.getCode(recordDto.getTradeType()));
        }
        ApiResult<List<AdminRecordVo>> recordVos=adminRecordBeanFeign.export(recordDto);
        List<String> customerNos=new ArrayList<>();
        for (int i = 0; i <recordVos.getData().size();  i++) {
            customerNos.add(recordVos.getData().get(i).getCustomerNo());
            if(recordVos.getData().get(i).getRetype().equals(Constants.Retype.IN)){
                recordVos.getData().get(i).setRetype("收入");
            }else if(recordVos.getData().get(i).getRetype().equals(Constants.Retype.OUT)){
                recordVos.getData().get(i).setRetype("支出");
            }else {
                recordVos.getData().get(i).setRetype("");
            }
        }
        ApiResult<List<CustomerVo>> byCustomerNoList = customerFeign.findByCustomerNoList(customerNos);
        if(byCustomerNoList.hasSuccess()){
            for (int i = 0; i <byCustomerNoList.getData().size() ; i++) {
                if(byCustomerNoList.getData().get(i).getCustomerNo().equals(recordVos.getData().get(i).getCustomerNo())){
                    recordVos.getData().get(i).setMobile(byCustomerNoList.getData().get(i).getMobile());
                    recordVos.getData().get(i).setRecommenderId(byCustomerNoList.getData().get(i).getRecommenderId());
                }
            }
        }
        excelUtils.defaultExcelExport(recordVos.getData(),response,AdminRecordVo.class,beanType+"流水报表");
    }


    @RequestMapping("/getEnumMessage")
    public ApiResult<List<EnumMessageVo>> getEnumMessage(@RequestBody RecordBeanDto recordBeanDto){
        List<EnumMessageVo> enumMessageVos=new ArrayList<>();
        RecordBeanTradeTypeEnum[] customerStatusEnums=RecordBeanTradeTypeEnum.values();
        for (int i = 0; i <customerStatusEnums.length ; i++) {
            EnumMessageVo enumMessageVo=new EnumMessageVo();
            if(customerStatusEnums[i].getCode().indexOf(recordBeanDto.getBeanType())!=-1){
                enumMessageVo.setCode(customerStatusEnums[i].getCode());
                enumMessageVo.setMessage(customerStatusEnums[i].getMsg());
                enumMessageVos.add(enumMessageVo);
            }
        }
        return ApiResult.success(enumMessageVos);
    }
}
