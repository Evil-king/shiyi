package com.baibei.shiyi.account.feign.client.admin;

import com.baibei.shiyi.account.feign.base.admin.IAdminCustomerBeanBase;
import com.baibei.shiyi.account.feign.base.admin.IAdminPassCardExtractOrderBase;
import com.baibei.shiyi.account.feign.client.hystrix.AdminCustomerBeanHystrix;
import com.baibei.shiyi.account.feign.client.hystrix.AdminPassCardExtractOrderHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: hyc
 * @date: 2019/5/24 1:51 PM
 * @description:
 */
@FeignClient(value = "shiyi-account", fallbackFactory = AdminPassCardExtractOrderHystrix.class)
public interface AdminPassCardExtractOrderFeign extends IAdminPassCardExtractOrderBase {

}
