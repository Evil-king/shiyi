package com.baibei.shiyi.cash.web.api;

import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.account.feign.bean.vo.SigningRecordVo;
import com.baibei.shiyi.account.feign.client.ISigningRecordFeign;
import com.baibei.shiyi.cash.feign.base.dto.OrderWithdrawDto;
import com.baibei.shiyi.cash.feign.base.vo.SingingFirstVo;
import com.baibei.shiyi.cash.feign.base.vo.WithdrawInfoVo;
import com.baibei.shiyi.cash.model.OrderWithdraw;
import com.baibei.shiyi.cash.service.IOrderWithdrawService;
import com.baibei.shiyi.cash.util.PropertiesVal;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.baibei.shiyi.common.tool.utils.JacksonUtil;
import com.baibei.shiyi.common.tool.utils.SFTPUtils;
import com.baibei.shiyi.pingan.feign.base.dto.ViewFileDto;
import com.baibei.shiyi.user.feign.bean.vo.RealnameInfoVo;
import com.baibei.shiyi.user.feign.client.shiyi.ICustomerFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth/api/cash")
public class ApiOrderWithdrawController {

    @Autowired
    private IOrderWithdrawService orderWithdrawService;
    @Autowired
    private ISigningRecordFeign signingRecordFeign;
    @Autowired
    private PropertiesVal propertiesVal;
    @Autowired
    private ICustomerFeign customerFeign;
    @Value("${shiyi.ftp.path}")
    private String ftpPath;

    @Autowired
    private SFTPUtils sftpUtils;
    @Autowired
    private RocketMQUtil rocketMQUtil;
    private SimpleDateFormat yyyyMMddWithLine = (SimpleDateFormat)DateUtil.yyyyMMddWithLine.get();

    /**
     * 出金申请
     * @param orderWithdrawDto
     * @return
     */
    @RequestMapping("/withdrawApplication")
    public ApiResult withdrawApplication(@RequestBody @Validated OrderWithdrawDto orderWithdrawDto){
        ApiResult apiResult;
        try{
            OrderWithdrawDto orderWithdrawDto1 = orderWithdrawService.withdrawApplication(orderWithdrawDto);
            //发送扣钱消息
            log.info("发送出金扣钱消息...");
            rocketMQUtil.sendMsg(propertiesVal.getWithdrawDetuchMoneyTxTopic(),JacksonUtil.beanToJson(orderWithdrawDto1),orderWithdrawDto1.getOrderNo());
            apiResult=ApiResult.success();
        }catch (Exception e){
            log.info("提现申请报错",e);
            apiResult=ApiResult.error(e.getMessage());
        }
        return apiResult;
    }

    /**
     * 平安用户信息(用户跳转到出金申请页面时调用的接口)
     * @param customerBaseDto
     * @return
     */
    @PostMapping("/withdrawInfo")
    public ApiResult getWithdrawInfo(@RequestBody CustomerBaseDto customerBaseDto) {
        ApiResult apiResult = new ApiResult();
        WithdrawInfoVo withdrawInfoVo = new WithdrawInfoVo();
        /*ApiResult<SigningRecordVo> signingRecordVoApiResult = signingRecordFeign.findByThirdCustId(customerBaseDto.getCustomerNo());
        if (signingRecordVoApiResult.getCode()!=200) {
            log.info("获取用户签约信息失败:",apiResult.getMsg());
        }
        SigningRecordVo signingRecordVo = signingRecordVoApiResult.getData();*/
        CustomerBaseDto custBase =  new CustomerBaseDto();
        custBase.setCustomerNo(customerBaseDto.getCustomerNo());
        ApiResult<RealnameInfoVo> realnameInfoVoApiResult = customerFeign.realnameInfo(custBase);
        if (realnameInfoVoApiResult.hasFail()||StringUtils.isEmpty(realnameInfoVoApiResult.getData())) {
            log.info("获取用户实名信息失败:",apiResult.getMsg());
            throw new ServiceException("获取用户实名信息失败");
        }
        RealnameInfoVo realnameInfoVo = realnameInfoVoApiResult.getData();
        withdrawInfoVo.setWithdrawFeeRate(propertiesVal.getRate());
        withdrawInfoVo.setWithdrawStartTime(propertiesVal.getDepositTime());
        withdrawInfoVo.setWithdrawEndTime(propertiesVal.getWithdrawTime());
        withdrawInfoVo.setWithdrawScopeTime(propertiesVal.getDepositTime()+"-"+propertiesVal.getWithdrawTime());
        withdrawInfoVo.setWithdrawFee(propertiesVal.getFee().floatValue());
        withdrawInfoVo.setBankName(realnameInfoVo.getBankName());
        withdrawInfoVo.setAcctName(realnameInfoVo.getRealname());
        withdrawInfoVo.setRelatedAcctId(realnameInfoVo.getBankCard());
        withdrawInfoVo.setBranchName(realnameInfoVo.getBranchName());
        apiResult.setData(withdrawInfoVo);
        return apiResult;
    }

    /**
     * 用户是否第一次签约。true=是；false=否
     * @param customerBaseDto
     * @return
     */
    @PostMapping("/isSingingFirst")
    public ApiResult<SingingFirstVo> isSingingFirst(@RequestBody CustomerBaseDto customerBaseDto) {
        ApiResult apiResult = new ApiResult();
        ApiResult<SigningRecordVo> signingRecordVoApiResult = signingRecordFeign.findByThirdCustId(customerBaseDto.getCustomerNo());
        SigningRecordVo signingRecordVo = signingRecordVoApiResult.getData();//用户签约信息
        SingingFirstVo singingFirstVo = new SingingFirstVo();
        if (StringUtils.isEmpty(signingRecordVo)) {
            throw new ServiceException("用户未签约");
        }
        //格式化日期
        String signingDateStr = yyyyMMddWithLine.format(signingRecordVo.getCreateTime());
        String currentDateStr = yyyyMMddWithLine.format(new Date());
        singingFirstVo.setFirstSinging(signingDateStr.equals(currentDateStr)?true:false);
        return ApiResult.success(singingFirstVo);
    }

    @PostMapping("/getFile")
    public ApiResult getFile(@RequestBody ViewFileDto viewFileDto) {
        String filePath = ftpPath+"/"+viewFileDto.getFileName();
        Boolean aBoolean = sftpUtils.PABDownload(filePath, viewFileDto.getReserve());
        if(!aBoolean){
            return ApiResult.error("下载文件失败");
        }
        return ApiResult.success();
    }

}
