package com.baibei.shiyi.publicc.feign.client;

import com.baibei.shiyi.publicc.feign.base.IPublicBase;
import com.baibei.shiyi.publicc.feign.client.hystrix.SmsHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author hwq
 * @date 2019/05/24
 */
@FeignClient(value = "shiyi-public",fallbackFactory = SmsHystrix.class)
public interface SmsFeign extends IPublicBase {
}
