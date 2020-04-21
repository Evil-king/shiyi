package com.baibei.shiyi.product.feign.client.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.product.feign.base.admin.IAdminParameterBase;
import com.baibei.shiyi.product.feign.bean.dto.ProTypeIdDto;
import com.baibei.shiyi.product.feign.bean.vo.ParameterListVo;
import com.baibei.shiyi.product.feign.client.hystrix.AdminParameterHystrix;
import com.baibei.shiyi.product.feign.client.hystrix.AdminProductProTypeHystrix;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/9/11 10:08
 * @description:
 */
@FeignClient(value = "shiyi-product", fallbackFactory = AdminParameterHystrix.class)
public interface AdminParameterFeign extends IAdminParameterBase {

}
