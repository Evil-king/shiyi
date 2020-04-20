package com.baibei.shiyi.account.web.api;

import com.baibei.shiyi.account.common.dto.EmpowermentDto;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.service.ICustomerBeanService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.enumeration.CustomerStatusEnum;
import com.baibei.shiyi.common.tool.utils.NumberUtil;
import com.baibei.shiyi.content.feign.base.IContentBase;
import com.baibei.shiyi.content.feign.client.ContentFeign;
import com.baibei.shiyi.trade.feign.base.ITradeDayBase;
import com.baibei.shiyi.trade.feign.client.TradeDayFeign;
import com.baibei.shiyi.user.feign.bean.vo.PABCustomerVo;
import com.baibei.shiyi.user.feign.client.shiyi.ICustomerFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hyc
 * @date: 2019/11/11 11:06
 * @description:
 */
@RestController
@RequestMapping("/auth/api/account/customerBean")
public class AuthApiCustomerBeanController {
    @Autowired
    private ICustomerBeanService customerBeanService;

    @Autowired
    private ICustomerFeign customerFeign;

    @Autowired
    private ContentFeign contentFeign;

    @Autowired
    private TradeDayFeign tradeDayFeign;


    @RequestMapping("/empowerment")
    public ApiResult empowerment(@RequestBody @Validated EmpowermentDto empowermentDto){
        if(!NumberUtil.isNumber(empowermentDto.getNumber())){
            return ApiResult.badParam("不支持输入小数或负数，请输入整数");
        }
        if(Integer.valueOf(empowermentDto.getNumber())<1){
            return ApiResult.badParam("赋能红木券数量必须大于0");
        }
        CustomerNoDto customerNoDto=new CustomerNoDto();
        customerNoDto.setCustomerNo(empowermentDto.getCustomerNo());
        ApiResult<PABCustomerVo> result = customerFeign.findCustomerNo(customerNoDto);
        if(!result.hasSuccess()){
            return result;
        }
        if(result.getData().getCustomerStatus().indexOf(CustomerStatusEnum.LIMIT_EMPOWERMENT.getCode())!=-1){
            return ApiResult.badParam("赋能失败，请联系客服");
        }
        return customerBeanService.empowerment(empowermentDto);
    }

}
