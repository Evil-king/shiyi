package com.baibei.shiyi.account.feign.client;

import com.baibei.shiyi.account.feign.base.shiyi.IAccountBase;
import com.baibei.shiyi.account.feign.client.hystrix.AccountHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: hyc
 * @date: 2019/5/24 1:51 PM
 * @description:
 */
@FeignClient(value = "shiyi-account", fallbackFactory = AccountHystrix.class)
public interface AccountFeign extends IAccountBase {

}
