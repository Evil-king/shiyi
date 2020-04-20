package com.baibei.shiyi.cash.rocketmq.comsume;

import com.baibei.component.redis.util.RedisUtil;
import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.shiyi.cash.feign.base.dto.Apply1010Dto;
import com.baibei.shiyi.cash.service.IAccountBookService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.constants.RedisConstant;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Longer
 * @date: 2019/11/1 10:41
 * @description: 台账
 */
@Component
@Slf4j
public class Apply1010ConsumerImpl implements IConsumer<String> {
    @Autowired
    private IAccountBookService accountBookService;
    @Autowired
    private RedisUtil redisUtil;
    private SimpleDateFormat sf = (SimpleDateFormat)DateUtil.yyyyMMddWithLine.get();
    private SimpleDateFormat sf2 = (SimpleDateFormat)DateUtil.yyyyMMddHHmmssWithLine.get();

    @Override
    public ApiResult execute(String msg) {
        long start = System.currentTimeMillis();
        boolean allSuccess=true;
        log.info("开始台账...");
        accountBookService.clear();//清空表
        ApiResult apiResult = new ApiResult();
        Apply1010Dto apply1010Dto = new Apply1010Dto();
        apply1010Dto.setSelectFlag("1");//查询全部台账信息
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            try{
                apiResult = accountBookService.apply1010(apply1010Dto.getSelectFlag(), String.valueOf(i));
                if (-901 == apiResult.getCode()) {
                    apiResult.setCode(200);
                    apiResult.setMsg("查询成功!");
                    break;
                }
            }catch (Exception e){
                allSuccess=false;
                log.error("台账报错：",e);
            }
        }
        if(allSuccess){
            //更新台账状态（缓存）doing=处理中；finished=已完成；fail=失败
            redisUtil.set(RedisConstant.APPLY1010_STATUS,Constants.Apply1010Status.FINISHED);
        }else{
            redisUtil.set(RedisConstant.APPLY1010_STATUS,Constants.Apply1010Status.FAIL);
        }
        String format = sf.format(new Date());
        try {
            //设置超时时间
            Date expiredTime = sf2.parse(format + " 23:59:59");
            redisUtil.expireAt(RedisConstant.APPLY1010_STATUS,expiredTime);
        } catch (ParseException e) {
            log.error("设置台账状态超时时间报错",e);
            e.printStackTrace();
        }
        log.info("台账耗时{}ms", (System.currentTimeMillis() - start));
        return apiResult;
    }
}