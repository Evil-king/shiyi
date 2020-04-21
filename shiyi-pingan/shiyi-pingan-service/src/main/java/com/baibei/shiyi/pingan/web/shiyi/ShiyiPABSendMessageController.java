package com.baibei.shiyi.pingan.web.shiyi;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.pingan.feign.client.IPABSendMessageFeign;
import com.baibei.shiyi.pingan.feign.base.dto.PABSendDto;
import com.baibei.shiyi.pingan.feign.base.vo.PABSendVo;
import com.baibei.shiyi.pingan.service.IPABSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shiyi/pingan")
public class ShiyiPABSendMessageController implements IPABSendMessageFeign {

    @Autowired
    private IPABSendService sendService;


    /**
     * 发送请求
     *
     * @param pabSendDTO
     * @return
     */
    @Override
    public ApiResult<PABSendVo> sendMessage(@Validated @RequestBody PABSendDto pabSendDTO) {
        return sendService.sendMessage(pabSendDTO);
    }

}
