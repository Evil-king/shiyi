package com.baibei.shiyi.account.feign.client;

import com.baibei.shiyi.account.feign.bean.dto.PABSigningRecordDto;
import com.baibei.shiyi.account.feign.bean.vo.PABSigningRecordVo;
import com.baibei.shiyi.account.feign.bean.vo.SigningRecordVo;
import com.baibei.shiyi.account.feign.client.hystrix.SigningRecordHystrix;
import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "${shiyi-account:shiyi-account}", path = "/shiyi/account/signingRecor", fallbackFactory = SigningRecordHystrix.class)
public interface ISigningRecordFeign {


    /**
     * 根据会员代码查询签约用户是否存在
     *
     * @param customerNo
     * @return
     */
    @PostMapping(path = "/findByThirdCustId")
    ApiResult<SigningRecordVo> findByThirdCustId(@RequestParam("customerNo") String customerNo);

    /**
     * 签约
     *
     * @param signingRecordDto
     * @return
     */
    @PostMapping(path = "/signing")
    ApiResult<PABSigningRecordVo> signingRecord(@Validated @RequestBody PABSigningRecordDto signingRecordDto);

    /**
     * 根据会员子账号查询用户是否存在
     *
     * @param custAcctId
     * @return
     */
    @PostMapping(path = "/findByCustAcctId")
    ApiResult<SigningRecordVo> findByCustAcctId(@RequestParam("custAcctId") String custAcctId);


    /**
     * 今天是否签约
     *
     * @param customerNo
     * @return
     */
    @PostMapping(path = "/isTodaySigning")
    ApiResult<Boolean> isTodaySigning(@RequestParam("customerNo") String customerNo);

    /**
     * 根据客户编号集合查询用户信息
     */
    @PostMapping(path = "/findByThirdCustIds")
    ApiResult<List<SigningRecordVo>> findByThirdCustIds(@RequestBody List<String> customerNos);

    @GetMapping("/getBalance")
    @ResponseBody
    ApiResult<String> getBalance(@RequestParam("message")  String message);

}
