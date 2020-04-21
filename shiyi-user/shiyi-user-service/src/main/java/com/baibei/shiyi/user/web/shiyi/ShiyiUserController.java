package com.baibei.shiyi.user.web.shiyi;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.user.feign.base.shiyi.ICustomerBase;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.bean.vo.PABCustomerVo;
import com.baibei.shiyi.user.model.Customer;
import com.baibei.shiyi.user.model.CustomerDetail;
import com.baibei.shiyi.user.service.ICustomerDetailService;
import com.baibei.shiyi.user.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: hyc
 * @date: 2019/5/28 14:41
 * @description:
 */
@Controller
public class ShiyiUserController implements ICustomerBase {
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ICustomerDetailService customerDetailService;

    @Override
    public ApiResult<CustomerVo> findUserByCustomerNo(@RequestBody @Validated CustomerNoDto customerNoDto) {
        Customer customer = customerService.findByCustomerNo(customerNoDto.getCustomerNo());
        if (customer != null) {
            CustomerVo customerVo = BeanUtil.copyProperties(customer, CustomerVo.class);
            CustomerDetail customerDetail=customerDetailService.findByCustomerNo(customerNoDto.getCustomerNo());
            customerVo.setRealName(customerDetail.getRealname());
            return ApiResult.success(customerVo);
        } else {
            throw new ServiceException("用户不存在");
        }

    }
    @Override
    public ApiResult<String> findCustomerNoByMobile(@RequestParam("mobile")String mobile) {
        return customerService.findCustomerNoByMobile(mobile);
    }

    @Override
    public ApiResult<PABCustomerVo> findByCustomerNo(@RequestParam("customerNo") String customerNo) {
        Customer customer = customerService.findByCustomerNo(customerNo);
        if (customer != null) {
            PABCustomerVo customerVo = BeanUtil.copyProperties(customer, PABCustomerVo.class);
            return ApiResult.success(customerVo);
        } else {
            return ApiResult.error("用户不存在");
        }
    }

}
