package com.baibei.shiyi.account.web.shiyi;

import com.baibei.shiyi.account.feign.bean.dto.PABSigningRecordDto;
import com.baibei.shiyi.account.feign.bean.vo.PABSigningRecordVo;
import com.baibei.shiyi.account.feign.bean.vo.SigningRecordVo;
import com.baibei.shiyi.account.feign.client.ISigningRecordFeign;
import com.baibei.shiyi.account.model.SigningRecord;
import com.baibei.shiyi.account.service.ISigningRecordService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shiyi/account/signingRecor")
public class ShiyiSigningRecordController implements ISigningRecordFeign {

    @Autowired
    private ISigningRecordService signingRecordService;


    // 查询会员
    public ApiResult<SigningRecordVo> findByThirdCustId(@RequestParam("customerNo") String customerNo) {
        SigningRecord signingRecord = signingRecordService.findByThirdCustId(customerNo);
        SigningRecordVo signingRecordVo = BeanUtil.copyProperties(signingRecord, SigningRecordVo.class);
        return ApiResult.success(signingRecordVo);
    }

    /**
     * 签约
     *
     * @param signingRecordDto
     * @return
     */
    @Override
    public ApiResult<PABSigningRecordVo> signingRecord(@Validated @RequestBody PABSigningRecordDto signingRecordDto) {
        PABSigningRecordVo result = signingRecordService.signingRecord(signingRecordDto);
        return ApiResult.success(result);
    }

    @Override
    public ApiResult<SigningRecordVo> findByCustAcctId(@RequestParam("custAcctId") String custAcctId) {
        SigningRecord signingRecord = signingRecordService.findByOneCustAcctId(custAcctId);
        SigningRecordVo signingRecordVo = BeanUtil.copyProperties(signingRecord, SigningRecordVo.class);
        return ApiResult.success(signingRecordVo);
    }

    @Override
    public ApiResult<Boolean> isTodaySigning(@RequestParam("customerNo") String customerNo) {
        Boolean result = signingRecordService.isTodaySigning(customerNo);
        return ApiResult.success(result);
    }

    @Override
    public ApiResult<List<SigningRecordVo>> findByThirdCustIds(@RequestBody List<String> customerNos) {
        List<SigningRecord> signingRecords = signingRecordService.findByThirdCustIdList(customerNos);
        List<SigningRecordVo> signingRecordVoList = BeanUtil.copyProperties(signingRecords, SigningRecordVo.class);
        return ApiResult.success(signingRecordVoList);
    }

    @Override
    public ApiResult<String> getBalance(@RequestParam("message") String message) {
        return ApiResult.success(signingRecordService.getBalance(message));
    }


}
