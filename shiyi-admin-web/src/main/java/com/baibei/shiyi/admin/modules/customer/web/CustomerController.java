package com.baibei.shiyi.admin.modules.customer.web;

import com.baibei.shiyi.account.feign.bean.vo.CustomerBeanVo;
import com.baibei.shiyi.account.feign.client.AdminCustomerBeanFeign;
import com.baibei.shiyi.account.feign.client.ISigningRecordFeign;
import com.baibei.shiyi.admin.modules.account.bean.vo.EnumMessageVo;
import com.baibei.shiyi.admin.modules.security.utils.SecurityUtils;
import com.baibei.shiyi.admin.modules.system.bean.vo.OrganizationBusinessVo;
import com.baibei.shiyi.admin.modules.system.model.Organization;
import com.baibei.shiyi.admin.modules.system.service.IOrganizationService;
import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.CustomerStatusEnum;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.IDCardUtils;
import com.baibei.shiyi.common.tool.utils.MobileUtils;
import com.baibei.shiyi.user.feign.bean.dto.AdminCustomerAccountDto;
import com.baibei.shiyi.user.feign.bean.dto.ChangeRecommendDto;
import com.baibei.shiyi.user.feign.bean.dto.ChangeStatusDto;
import com.baibei.shiyi.user.feign.bean.dto.CustomerListDto;
import com.baibei.shiyi.user.feign.bean.vo.AdminCustomerBalanceVo;
import com.baibei.shiyi.user.feign.bean.vo.CustomerListVo;
import com.baibei.shiyi.user.feign.client.admin.AdminCustomerFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/1 15:25
 * @description:
 */
@RestController
@RequestMapping("/admin/customer")
public class CustomerController {
    @Autowired
    private AdminCustomerFeign adminCustomerFeign;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private AdminCustomerBeanFeign customerBeanFeign;

    @Autowired
    private ISigningRecordFeign signingRecordFeign;


    @Autowired
    private ExcelUtils excelUtils;

    @RequestMapping("/getCustomerAccount")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('ASSET_MANAGE_BALANCE'))")
    public ApiResult<MyPageInfo<AdminCustomerBalanceVo>> getCustomer(@RequestBody AdminCustomerAccountDto adminCustomerAccountDto){
        ApiResult<MyPageInfo<AdminCustomerBalanceVo>> adminCustomerBalanceVoMyPageInfo=adminCustomerFeign.getCustomerPageList(adminCustomerAccountDto);
        for (int i = 0; i < adminCustomerBalanceVoMyPageInfo.getData().getList().size(); i++) {
            Organization organization=organizationService.findByOrgCode(adminCustomerBalanceVoMyPageInfo.getData().getList().get(i).getOrgId());
            if(organization!=null){
                List<OrganizationBusinessVo> organizations=organizationService.findAllFather(organization.getId()+"");
                for (int j = 0; j <organizations.size() ; j++) {
                    //普代机构代码
                    if(organizations.get(j).getOrgType().equals(Constants.OrganizationType.ORDINARYAGENT)){
                        adminCustomerBalanceVoMyPageInfo.getData().getList().get(i).setOrdinaryAgent(organizations.get(j).getOrgCode());
                    }
                    //区代机构代码
                    if(organizations.get(j).getOrgType().equals(Constants.OrganizationType.AREAAGENT)){
                        adminCustomerBalanceVoMyPageInfo.getData().getList().get(i).setAreaAgent(organizations.get(j).getOrgCode());
                    }
                    //市代机构代码
                    if(organizations.get(j).getOrgType().equals(Constants.OrganizationType.CITYAGENT)){
                        adminCustomerBalanceVoMyPageInfo.getData().getList().get(i).setCityAgent(organizations.get(j).getOrgCode());
                    }
                    //机构代码
                    if(organizations.get(j).getOrgType().equals(Constants.OrganizationType.ORGANIZATION)){
                        adminCustomerBalanceVoMyPageInfo.getData().getList().get(i).setOrganization(organizations.get(j).getOrgCode());
                    }
                    //事业部代码
                    if(organizations.get(j).getOrgType().equals(Constants.OrganizationType.BUSINESS)){
                        adminCustomerBalanceVoMyPageInfo.getData().getList().get(i).setBusiness(organizations.get(j).getOrgCode());
                    }
                }
            }
            AdminCustomerBalanceVo adminCustomerBalanceVo = adminCustomerBalanceVoMyPageInfo.getData().getList().get(i);
            ApiResult<CustomerBeanVo> customerBeanVo = customerBeanFeign.getBeanBalance(adminCustomerBalanceVoMyPageInfo.getData().getList().get(i).getCustomerNo());
            adminCustomerBalanceVo.setConsumptionBalance(customerBeanVo.getData().getConsumptionBalance());
            adminCustomerBalanceVo.setExchangeBalance(customerBeanVo.getData().getExchangeBalance());
            adminCustomerBalanceVo.setShiyiBalance(customerBeanVo.getData().getShiyiBalance());
            adminCustomerBalanceVo.setMoneyBalance(customerBeanVo.getData().getMoneyBalance());
            adminCustomerBalanceVo.setWithdrawBalance(customerBeanVo.getData().getWithdrawBalance());
            adminCustomerBalanceVo.setMallBalance(customerBeanVo.getData().getMallAccountBalance());
        }
        return adminCustomerBalanceVoMyPageInfo;
    }
    @RequestMapping("/exportList")
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('ASSET_MANAGE_BALANCE'))")
    public void exportList(@RequestBody AdminCustomerAccountDto adminCustomerAccountDto, HttpServletResponse response) {
        ApiResult<List<AdminCustomerBalanceVo>> adminCustomerBalanceVoMyPageInfo=adminCustomerFeign.getCustomerList(adminCustomerAccountDto);
        for (int i = 0; i < adminCustomerBalanceVoMyPageInfo.getData().size(); i++) {
        Organization organization=organizationService.findByOrgCode(adminCustomerBalanceVoMyPageInfo.getData().get(i).getOrgId());
        if(organization!=null) {
            List<OrganizationBusinessVo> organizations = organizationService.findAllFather(organization.getId() + "");
                for (int j = 0; j < organizations.size(); j++) {
                    //普代机构代码
                    if (organizations.get(j).getOrgType().equals(Constants.OrganizationType.ORDINARYAGENT)) {
                        adminCustomerBalanceVoMyPageInfo.getData().get(i).setOrdinaryAgent(organizations.get(j).getOrgCode());
                    }
                    //区代机构代码
                    if (organizations.get(j).getOrgType().equals(Constants.OrganizationType.AREAAGENT)) {
                        adminCustomerBalanceVoMyPageInfo.getData().get(i).setAreaAgent(organizations.get(j).getOrgCode());
                    }
                    //市代机构代码
                    if (organizations.get(j).getOrgType().equals(Constants.OrganizationType.CITYAGENT)) {
                        adminCustomerBalanceVoMyPageInfo.getData().get(i).setCityAgent(organizations.get(j).getOrgCode());
                    }
                    //机构代码
                    if (organizations.get(j).getOrgType().equals(Constants.OrganizationType.ORGANIZATION)) {
                        adminCustomerBalanceVoMyPageInfo.getData().get(i).setOrganization(organizations.get(j).getOrgCode());
                    }
                    //事业部代码
                    if (organizations.get(j).getOrgType().equals(Constants.OrganizationType.BUSINESS)) {
                        adminCustomerBalanceVoMyPageInfo.getData().get(i).setBusiness(organizations.get(j).getOrgCode());
                    }
                }
                AdminCustomerBalanceVo adminCustomerBalanceVo = adminCustomerBalanceVoMyPageInfo.getData().get(i);
                ApiResult<CustomerBeanVo> customerBeanVo = customerBeanFeign.getBeanBalance(adminCustomerBalanceVoMyPageInfo.getData().get(i).getCustomerNo());
                adminCustomerBalanceVo.setConsumptionBalance(customerBeanVo.getData().getConsumptionBalance());
                adminCustomerBalanceVo.setExchangeBalance(customerBeanVo.getData().getExchangeBalance());
                adminCustomerBalanceVo.setShiyiBalance(customerBeanVo.getData().getShiyiBalance());
        }
        }
        excelUtils.defaultExcelExport(adminCustomerBalanceVoMyPageInfo.getData(), response, AdminCustomerBalanceVo.class, "用户资产余额表");
    }
    @RequestMapping("/getAllCustomerPageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('USER_MANAGE_LIST'))")
    ApiResult<MyPageInfo<CustomerListVo>> getAllCustomerList(@RequestBody @Validated CustomerListDto customerListDto){
        ApiResult<MyPageInfo<CustomerListVo>> mypageInfo = adminCustomerFeign.getAllCustomerList(customerListDto);
        if(!mypageInfo.hasSuccess()){
            return mypageInfo;
        }
        List<String> customerNos=new ArrayList<>();
        for (int i = 0; i <mypageInfo.getData().getList().size() ; i++) {
            CustomerListVo customerListVo = mypageInfo.getData().getList().get(i);
            Organization organization=organizationService.findByOrgCode(customerListVo.getOrgId());
            if(organization!=null){
                List<OrganizationBusinessVo> organizations=organizationService.findAllFather(organization.getId()+"");
                for (int j = 0; j <organizations.size() ; j++) {
                    //普代机构代码
                    if(organizations.get(j).getOrgType().equals(Constants.OrganizationType.ORDINARYAGENT)){
                        customerListVo.setOrdinaryAgent(organizations.get(j).getOrgCode());
                    }
                    //区代机构代码
                    if(organizations.get(j).getOrgType().equals(Constants.OrganizationType.AREAAGENT)){
                        customerListVo.setAreaAgent(organizations.get(j).getOrgCode());
                    }
                    //市代机构代码
                    if(organizations.get(j).getOrgType().equals(Constants.OrganizationType.CITYAGENT)){
                        customerListVo.setCityAgent(organizations.get(j).getOrgCode());
                    }
                    //机构代码
                    if(organizations.get(j).getOrgType().equals(Constants.OrganizationType.ORGANIZATION)){
                        customerListVo.setOrganization(organizations.get(j).getOrgCode());
                    }
                    //事业部代码
                    if(organizations.get(j).getOrgType().equals(Constants.OrganizationType.BUSINESS)){
                        customerListVo.setBusiness(organizations.get(j).getOrgCode());
                    }
                }
            }
            customerNos.add(customerListVo.getCustomerNo());
            customerListVo.setRecommenderId("0".equals(customerListVo.getRecommenderId())?null:customerListVo.getRecommenderId());
            customerListVo.setIndirectRecommendId("0".equals(customerListVo.getIndirectRecommendId())?null:customerListVo.getIndirectRecommendId());
            if(!StringUtils.isEmpty(customerListVo.getOrdinaryAgent())){
                customerListVo.setOrgId(customerListVo.getOrdinaryAgent());
            }else if(!StringUtils.isEmpty(customerListVo.getAreaAgent())){
                customerListVo.setOrgId(customerListVo.getAreaAgent());
            }else if(!StringUtils.isEmpty(customerListVo.getCityAgent())){
                customerListVo.setOrgId(customerListVo.getCityAgent());
            }else{
                customerListVo.setOrgId(null);
            }
            if(!StringUtils.isEmpty(mypageInfo.getData().getList().get(i).getIdCard())){
                customerListVo.setAge(IDCardUtils.IdNOToAge(mypageInfo.getData().getList().get(i).getIdCard()));
                customerListVo.setSex(IDCardUtils.getSex(mypageInfo.getData().getList().get(i).getIdCard()));
            }
        }

        return mypageInfo;
    }
    @RequestMapping("/export")
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('USER_MANAGE_LIST'))")
    public void export(@RequestBody CustomerListDto customerListDto,HttpServletResponse response){
        ApiResult<List<CustomerListVo>> mypageInfo = adminCustomerFeign.export(customerListDto);
        List<String> customerNos=new ArrayList<>();
        for (int i = 0; i <mypageInfo.getData().size() ; i++) {
            CustomerListVo customerListVo = mypageInfo.getData().get(i);
            Organization organization=organizationService.findByOrgCode(customerListVo.getOrgId());
            if(organization!=null) {
                List<OrganizationBusinessVo> organizations = organizationService.findAllFather(organization.getId() + "");
                for (int j = 0; j < organizations.size(); j++) {
                    //普代机构代码
                    if (organizations.get(j).getOrgType().equals(Constants.OrganizationType.ORDINARYAGENT)) {
                        customerListVo.setOrdinaryAgent(organizations.get(j).getOrgCode());
                    }
                    //区代机构代码
                    if (organizations.get(j).getOrgType().equals(Constants.OrganizationType.AREAAGENT)) {
                        customerListVo.setAreaAgent(organizations.get(j).getOrgCode());
                    }
                    //市代机构代码
                    if (organizations.get(j).getOrgType().equals(Constants.OrganizationType.CITYAGENT)) {
                        customerListVo.setCityAgent(organizations.get(j).getOrgCode());
                    }
                    //机构代码
                    if (organizations.get(j).getOrgType().equals(Constants.OrganizationType.ORGANIZATION)) {
                        customerListVo.setOrganization(organizations.get(j).getOrgCode());
                    }
                    //事业部代码
                    if (organizations.get(j).getOrgType().equals(Constants.OrganizationType.BUSINESS)) {
                        customerListVo.setBusiness(organizations.get(j).getOrgCode());
                    }
                }
            }
            customerNos.add(customerListVo.getCustomerNo());
            customerListVo.setRecommenderId("0".equals(customerListVo.getRecommenderId())?null:customerListVo.getRecommenderId());
            customerListVo.setIndirectRecommendId("0".equals(customerListVo.getIndirectRecommendId())?null:customerListVo.getIndirectRecommendId());
            if(!StringUtils.isEmpty(customerListVo.getOrdinaryAgent())){
                customerListVo.setOrgId(customerListVo.getOrdinaryAgent());
            }else if(!StringUtils.isEmpty(customerListVo.getAreaAgent())){
                customerListVo.setOrgId(customerListVo.getAreaAgent());
            }else if(!StringUtils.isEmpty(customerListVo.getCityAgent())){
                customerListVo.setOrgId(customerListVo.getCityAgent());
            }else{
                customerListVo.setOrgId(null);
            }
            if(!StringUtils.isEmpty(mypageInfo.getData().get(i).getIdCard())){
                customerListVo.setAge(IDCardUtils.IdNOToAge(mypageInfo.getData().get(i).getIdCard()));
                customerListVo.setSex(IDCardUtils.getSex(mypageInfo.getData().get(i).getIdCard()));

            }
        }
        excelUtils.defaultExcelExport(mypageInfo.getData(),response,CustomerListVo.class,"用户管理列表");
    }


    @RequestMapping("/changeStatus")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('USER_MANAGE_LIST'))")
    public ApiResult changeStatus(@RequestBody @Validated ChangeStatusDto changeStatusDto){
        return adminCustomerFeign.changeStatus(changeStatusDto);
    }

    @RequestMapping("/getEnumMessage")
    public ApiResult<List<EnumMessageVo>> getEnumMessage(){
            List<EnumMessageVo> enumMessageVos=new ArrayList<>();
            CustomerStatusEnum[] customerStatusEnums=CustomerStatusEnum.values();
            for (int i = 0; i <customerStatusEnums.length ; i++) {
                EnumMessageVo enumMessageVo=new EnumMessageVo();
                enumMessageVo.setCode(customerStatusEnums[i].getCode());
                enumMessageVo.setMessage(customerStatusEnums[i].getMsg());
                enumMessageVos.add(enumMessageVo);
            }
            return ApiResult.success(enumMessageVos);
    }
    @RequestMapping("/changeRecommend")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('USER_MANAGE_LIST'))")
    ApiResult changeRecommend(@RequestBody @Validated ChangeRecommendDto changeRecommendDto){
        changeRecommendDto.setOperatorId(SecurityUtils.getUserId());
        return adminCustomerFeign.changeRecommend(changeRecommendDto);
    }
}
