package com.baibei.shiyi.account.feign.client;

import com.baibei.shiyi.account.feign.base.admin.IAdminRecordMoneyBase;
import com.baibei.shiyi.account.feign.client.hystrix.AdminRecordMoneyHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: hyc
 * @date: 2019/5/24 1:51 PM
 * @description:
 */
@FeignClient(value = "shiyi-account", fallbackFactory = AdminRecordMoneyHystrix.class)
public interface AdminRecordMoneyFeign extends IAdminRecordMoneyBase {

}
