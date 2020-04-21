package com.baibei.shiyi.pingan.feign.client.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.pingan.feign.base.dto.PABSendDto;
import com.baibei.shiyi.pingan.feign.base.vo.PABSendVo;
import com.baibei.shiyi.pingan.feign.client.IPABSendMessageFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PABSendMessageHystrix implements FallbackFactory<IPABSendMessageFeign> {

    @Override
    public IPABSendMessageFeign create(Throwable throwable) {
        return new IPABSendMessageFeign() {

            @Override
            public ApiResult<PABSendVo> sendMessage(PABSendDto pabSendDTO) {
                log.info("PABSendMessageHystrix is hystrix,params is {}, exception is {}", JSONObject.toJSONString(pabSendDTO), throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
