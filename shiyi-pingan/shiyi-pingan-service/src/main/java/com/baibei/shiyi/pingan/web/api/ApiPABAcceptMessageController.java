package com.baibei.shiyi.pingan.web.api;

import com.baibei.shiyi.cash.feign.base.dto.WithdrawForBank1312Dto;
import com.baibei.shiyi.cash.feign.base.vo.WithdrawForBank1312Vo;
import com.baibei.shiyi.cash.feign.client.shiyi.IShiyiWithdrawFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.BankMessageAnalysisUtils;
import com.baibei.shiyi.pingan.bean.dto.PABMessage;
import com.baibei.shiyi.pingan.feign.base.dto.PABAcceptDto;
import com.baibei.shiyi.pingan.service.IPABAcceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 验证不同的消息报文,请求结果
 */
@RestController
@RequestMapping("/api/pingan")
public class ApiPABAcceptMessageController {

    @Autowired
    private List<IPABAcceptService> acceptService;
    @Autowired
    private IShiyiWithdrawFeign shiyiWithdrawFeign;

    /**
     * 验证平安银行消息接口的报文准确性
     *
     * @param pabMessage
     * @return
     */
    @PostMapping("/execute")
    public ApiResult<String> execute(@Validated @RequestBody PABMessage pabMessage) {
        IPABAcceptService function = acceptService.stream().filter(x -> x.getType().getIndex() == pabMessage.getTranFunc()).findFirst().orElse(null);
        if (function == null) {
            throw new ServiceException("不支持接口");
        }
        Map<String, String> retKeyDict = BankMessageAnalysisUtils.parsingTranMessageString(pabMessage.getMessage());
        PABAcceptDto acceptDto = new PABAcceptDto();
        acceptDto.setBankExternalNo(retKeyDict.get("externalNo"));
        acceptDto.setMessage(retKeyDict.get("backBodyMessages"));
        acceptDto.setTranMessage(pabMessage.getMessage());
        acceptDto.setTranFunc(pabMessage.getTranFunc().toString());
        return function.execute(acceptDto);
    }


    @PostMapping("/test1312")
    public ApiResult<String> execute() {
        WithdrawForBank1312Dto withdrawForBank1312Dto = new WithdrawForBank1312Dto();
        withdrawForBank1312Dto.setMessage("A0010301019157                0000000234000000EB001012019110816505419031426612319      999999                                                                                                    000000000000000000000000000001312010               20191108165054999999                                          000000112EB00119031426612319      9157锦绣红木&5057640885&888800120724502&test1&15000090548747&15000122507984&test1&RMB&5000.00&0.00&888800120724502&&");
        ApiResult<WithdrawForBank1312Vo> withdrawForBank1312VoApiResult = shiyiWithdrawFeign.withdrawForBank1312(withdrawForBank1312Dto);
        System.out.println(withdrawForBank1312VoApiResult);
        return ApiResult.success();
    }
}
