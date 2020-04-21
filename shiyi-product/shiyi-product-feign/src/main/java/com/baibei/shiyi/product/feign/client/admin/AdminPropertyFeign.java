package com.baibei.shiyi.product.feign.client.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.product.feign.base.admin.IAdminPropertyBase;
import com.baibei.shiyi.product.feign.bean.dto.ProTypeIdDto;
import com.baibei.shiyi.product.feign.bean.vo.PropertyKeyVo;
import com.baibei.shiyi.product.feign.client.hystrix.AdminPropertyHystrix;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/9/9 10:37
 * @description:
 */
@FeignClient(value = "shiyi-product", fallbackFactory = AdminPropertyHystrix.class)
public interface AdminPropertyFeign extends IAdminPropertyBase {

}
