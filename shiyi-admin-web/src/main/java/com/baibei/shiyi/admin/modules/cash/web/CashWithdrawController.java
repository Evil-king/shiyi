package com.baibei.shiyi.admin.modules.cash.web;

import com.baibei.component.redis.util.RedisUtil;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.admin.modules.security.utils.SecurityUtils;
import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.cash.feign.base.dto.Apply1010PagelistDto;
import com.baibei.shiyi.cash.feign.base.dto.AuditOrderDto;
import com.baibei.shiyi.cash.feign.base.dto.WithdrawListDto;
import com.baibei.shiyi.cash.feign.base.vo.Apply1010PagelistVo;
import com.baibei.shiyi.cash.feign.base.vo.WithdrawListVo;
import com.baibei.shiyi.cash.feign.client.admin.IAdminWithdrawFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.constants.RedisConstant;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author: Longer
 * @date: 2019/11/08 13:55
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/admin/withdraw")
public class CashWithdrawController {
    @Autowired
    private IAdminWithdrawFeign adminWithdrawFeign;
    @Autowired
    private RocketMQUtil rocketMQUtil;

    @Value("${rocketmq.apply1010.topics}")
    private String apply1010Topic;//台账主题
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ExcelUtils excelUtils;
    private SimpleDateFormat sf = (SimpleDateFormat)DateUtil.yyyyMMddWithLine.get();
    private SimpleDateFormat sf2 = (SimpleDateFormat)DateUtil.yyyyMMddHHmmssWithLine.get();
    /**
     * 出金审核
     * @param auditOrderDto
     * @return
     */
    @PostMapping("/auditWithdraw")
    @ResponseBody
    public ApiResult auditWithdraw(@RequestBody @Validated AuditOrderDto auditOrderDto){
        //TODO 获取登录用户信息
        String username = SecurityUtils.getUsername();
        auditOrderDto.setReviewer(username);
        ApiResult apiResult = adminWithdrawFeign.auditWithdraw(auditOrderDto);
        return apiResult;
    }

    /**
     * 出金订单列表
     * @param withdrawListDto
     * @return
     */
    @PostMapping("/pagelist")
    @ResponseBody
    public ApiResult<MyPageInfo<WithdrawListVo>> pagelist(@RequestBody @Validated WithdrawListDto withdrawListDto){
        ApiResult<MyPageInfo<WithdrawListVo>> pagelist = adminWithdrawFeign.pagelist(withdrawListDto);
        return pagelist;
    }

    /**
     * 导出出金列表
     * @param withdrawListDto
     * @param response
     */
    @PostMapping("/exportWithdraw")
    @ResponseBody
    public void export(@RequestBody @Validated WithdrawListDto withdrawListDto, HttpServletResponse response){
        ApiResult<List<WithdrawListVo>> withdrawList = adminWithdrawFeign.getWithdrawList(withdrawListDto);
        excelUtils.defaultExcelExport(withdrawList.getData(),response,WithdrawListVo.class,"提现列表");
    }

    /**
     * 台账
     * @return
     */
    @PostMapping("/apply1010")
    @ResponseBody
    public ApiResult apply1010(){
        //发送台账异步消息
        log.info("发送台账消息....");
        String uuid = UUID.randomUUID().toString();
        rocketMQUtil.sendMsg(apply1010Topic,uuid,uuid);
        //更新台账状态（缓存）doing=处理中；finished=已完成
        redisUtil.set(RedisConstant.APPLY1010_STATUS,Constants.Apply1010Status.DOING);
        String format = sf.format(new Date());
        try {
            //设置超时时间
            Date expiredTime = sf2.parse(format + " 23:59:59");
            redisUtil.expireAt(RedisConstant.APPLY1010_STATUS,expiredTime);
        } catch (ParseException e) {
            log.error("设置台账状态超时时间报错",e);
            e.printStackTrace();
        }
        return ApiResult.success();
    }

    /**
     * 获取台账状态
     * @return
     */
    @PostMapping("/aplly1010Status")
    @ResponseBody
    public ApiResult getAplly1010Status(){
        ApiResult apiResult;
        String status = redisUtil.get(RedisConstant.APPLY1010_STATUS);
        if (StringUtils.isEmpty(status)) {
            apiResult=ApiResult.success(Constants.Apply1010Status.UNDO);
        }else{
            apiResult=ApiResult.success(status);
        }
        return apiResult;
    }

    /**
     * 台账分页列表
     * @param apply1010PagelistDto
     * @return
     */
    @PostMapping("/apply1010Pagelist")
    @ResponseBody
    ApiResult<MyPageInfo<Apply1010PagelistVo>> apply1010Pagelist(@RequestBody @Validated Apply1010PagelistDto apply1010PagelistDto){
        ApiResult<MyPageInfo<Apply1010PagelistVo>> myPageInfoApiResult = adminWithdrawFeign.apply1010Pagelist(apply1010PagelistDto);
        return myPageInfoApiResult;
    }

    /**
     * 导出台账列表
     * @param withdrawListDto
     * @param response
     */
    @RequestMapping("/export1010")
    @ResponseBody
    public void export(@RequestBody @Validated Apply1010PagelistDto apply1010PagelistDto, HttpServletResponse response){
        ApiResult<List<Apply1010PagelistVo>> listApiResult = adminWithdrawFeign.apply1010List(apply1010PagelistDto);
        excelUtils.defaultExcelExport(listApiResult.getData(),response,Apply1010PagelistVo.class,"台账列表");
    }
}
