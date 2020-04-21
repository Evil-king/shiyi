package com.baibei.shiyi.user.web.shiyi;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.user.feign.bean.vo.PABCustomerVo;
import com.baibei.shiyi.user.feign.bean.vo.RealnameInfoVo;
import com.baibei.shiyi.user.feign.client.shiyi.ICustomerFeign;
import com.baibei.shiyi.user.model.Customer;
import com.baibei.shiyi.user.model.CustomerDetail;
import com.baibei.shiyi.user.service.ICustomerDetailService;
import com.baibei.shiyi.user.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shiyi/customer")
public class ShiyiCustomerController implements ICustomerFeign {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ICustomerDetailService customerDetailService;

    @Override
    public ApiResult update(@RequestBody PABCustomerVo pabCustomerVo) {
        Customer customer = BeanUtil.copyProperties(pabCustomerVo, Customer.class);
        customerService.update(customer);
        return ApiResult.success();
    }

    @Override
    public ApiResult<PABCustomerVo> findCustomerNo(@RequestBody CustomerNoDto customerNoDto) {
        Customer customer = customerService.findByCustomerNo(customerNoDto.getCustomerNo());
        if (customer != null) {
            CustomerDetail customerDetail = customerDetailService.findByCustomerNo(customerNoDto.getCustomerNo());
            PABCustomerVo customerVo = BeanUtil.copyProperties(customer, PABCustomerVo.class);
            if (customerDetail != null) {
                customerVo.setRealName(customerDetail.getRealname());
            }
            return ApiResult.success(customerVo);
        } else {
            return ApiResult.error("用户不存在");
        }
    }

    @Override
    public ApiResult<RealnameInfoVo> realnameInfo(@RequestBody @Validated CustomerBaseDto customerBaseDto) {
        RealnameInfoVo realnameInfoVo = customerDetailService.getRealnameInfo(customerBaseDto.getCustomerNo());
        return ApiResult.success(realnameInfoVo);
    }


}
