package com.baibei.shiyi.cash.web.shiyi;


import com.baibei.shiyi.cash.feign.base.dto.SignInBackDto;
import com.baibei.shiyi.cash.feign.base.vo.SignInBackVo;
import com.baibei.shiyi.cash.feign.client.shiyi.IShiyiSigningInBackFeign;
import com.baibei.shiyi.cash.model.SignInBack;
import com.baibei.shiyi.cash.service.ISignInBackService;
import com.baibei.shiyi.cash.util.SerialNumberComponent;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.settlement.feign.client.ISettlementFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 平安签到和签退的接口
 */
@RestController
@RequestMapping("/shiyi/cash")
@Slf4j
public class ShiyiSigningInBackController implements IShiyiSigningInBackFeign {

    @Autowired
    private ISignInBackService signInBackService;

    @Autowired
    private SerialNumberComponent serialNumberComponent;


    /**
     * 签到
     *
     * @return
     */
    @Override
    public ApiResult<SignInBackVo> signIn() {
        log.info("开始签到");
        SignInBackDto signInBackDto = new SignInBackDto();
        signInBackDto.setFuncFlag("1");
        signInBackDto.setSignNo(serialNumberComponent.generateOrderNo(SignInBack.class, signInBackService, "G", "signNo"));
        signInBackDto.setSignDate(new Date());
        SignInBackVo result = signInBackService.signInOrBack(signInBackDto);
        // 更新签到的状态
      /*  CleanFlowPathDto cleanFlowPathDto = new CleanFlowPathDto();
        cleanFlowPathDto.setProjectCode(Constants.CleanFlowPathCode.SIGN_IN);
        cleanFlowPathDto.setStatus(Constants.CleanFlowPathStatus.COMPLETED);
        DateTimeFormatter nowDate = DateTimeFormatter.ofPattern("yyyyMMdd");
        cleanFlowPathDto.setBatchNo(nowDate.format(LocalDate.now()));*/
        /*settlementFeign.updateCleanFlow(cleanFlowPathDto);*/
        log.info("签到结束");
        return ApiResult.success(result);
    }

    /**
     * 签退
     *
     * @return
     */
    @Override
    public ApiResult<SignInBackVo> signBack() {
        log.info("开始签退");
        SignInBackDto signInBackDto = new SignInBackDto();
        signInBackDto.setFuncFlag("2");
        signInBackDto.setSignNo(serialNumberComponent.generateOrderNo(SignInBack.class, signInBackService, "G", "signNo"));
        signInBackDto.setSignDate(new Date());
        SignInBackVo result = signInBackService.signInOrBack(signInBackDto);
        log.info("签退结束");
        return ApiResult.success(result);
    }

    /**
     * 今天是否签到或者签退
     *
     * @param status
     * @return
     */
    @Override
    public ApiResult<Boolean> isToDaySignInBack(@RequestParam("status") String status) {
        Boolean result = signInBackService.isToDaySignInBack(status);
        return ApiResult.success(result);
    }

    /**
     * 签到或签退状态
     *
     * @return
     */
    @Override
    public ApiResult<SignInBackDto> lastSignStatus() {
        return ApiResult.success(signInBackService.lastSignStatus());
    }

}
