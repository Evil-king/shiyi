package com.baibei.shiyi.pingan.feign.client;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.pingan.feign.base.dto.PABSendDto;
import com.baibei.shiyi.pingan.feign.base.vo.PABSendVo;
import com.baibei.shiyi.pingan.feign.client.hystrix.PABSendMessageHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${shiyi-order:shiyi-pingan}", path = "/shiyi/pingan", fallbackFactory = PABSendMessageHystrix.class)
public interface IPABSendMessageFeign {

    /**
     * 发送消息
     *
     * @param pabSendDTO
     * @return
     */
    @PostMapping("/sendMessage")
    ApiResult<PABSendVo> sendMessage(@Validated @RequestBody PABSendDto pabSendDTO);
}
