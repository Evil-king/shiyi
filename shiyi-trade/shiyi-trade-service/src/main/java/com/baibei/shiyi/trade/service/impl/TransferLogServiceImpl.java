package com.baibei.shiyi.trade.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.trade.dao.TransferLogMapper;
import com.baibei.shiyi.trade.feign.bean.dto.TransferDeleteDto;
import com.baibei.shiyi.trade.feign.bean.dto.TransferLogDto;
import com.baibei.shiyi.trade.feign.bean.vo.TransferLogVo;
import com.baibei.shiyi.trade.feign.bean.vo.TransferTemplateVo;
import com.baibei.shiyi.trade.model.TransferLog;
import com.baibei.shiyi.trade.service.IFeeExemptionConfigService;
import com.baibei.shiyi.trade.service.ITransferLogRecordService;
import com.baibei.shiyi.trade.service.ITransferLogService;
import com.baibei.shiyi.trade.utils.TradeUtil;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author: wenqing
 * @date: 2019/12/26 14:21:39
 * @description: TransferLog服务实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TransferLogServiceImpl extends AbstractService<TransferLog> implements ITransferLogService {

    @Autowired
    private TransferLogMapper transferLogMapper;
    @Autowired
    private IFeeExemptionConfigService feeExemptionConfigService;
    @Autowired
    private ITransferLogRecordService transferLogRecordService;
    @Autowired
    private TradeUtil tradeUtil;

    @Override
    public MyPageInfo<TransferLogVo> pageListLog(TransferLogDto transferLogDto) {
        PageHelper.startPage(transferLogDto.getCurrentPage(), transferLogDto.getPageSize());
        if (transferLogDto.getStartTime() != null && transferLogDto.getStartTime() != "") {
            transferLogDto.setStartTime(transferLogDto.getStartTime() + " 00:00:00");
        }
        if (transferLogDto.getEndTime() != null && transferLogDto.getEndTime() != "") {
            transferLogDto.setEndTime(transferLogDto.getEndTime() + " 23:59:59");
        }
        List<TransferLogVo> transferLogVos = transferLogMapper.pageListLog(transferLogDto);
        MyPageInfo<TransferLogVo> myPageInfo = new MyPageInfo<>(transferLogVos);
        return myPageInfo;
    }

    @Override
    public ApiResult importData(List<TransferTemplateVo> transferTemplateListVo) {
        log.info("importData方法入参打印,importData={}", JSONObject.toJSONString(transferTemplateListVo));
        int countNum = 0; //合计数量
        BigDecimal countAmount = new BigDecimal(0); //合计金额
        BigDecimal totalAmount = new BigDecimal(0);
        String fileName = "";
        TransferLog transferLog = new TransferLog();
        long id = IdWorker.getId();
        boolean inCustomerFlag = Boolean.FALSE;
        boolean outCustomerFlag = Boolean.FALSE;
        BigDecimal buyFee = null;
        BigDecimal sellFee = null;
        for (int i = 0; i < transferTemplateListVo.size(); i++) {
            TransferTemplateVo transferTemplateVo = transferTemplateListVo.get(i);
            fileName = transferTemplateVo.getFileName();
            //校验参数
            ApiResult validator = validator(transferTemplateVo);
            if (validator.hasFail()) {
                return validator;
            }
            inCustomerFlag = feeExemptionConfigService.isExist(transferTemplateVo.getInCustomerNo());
            outCustomerFlag = feeExemptionConfigService.isExist(transferTemplateVo.getOutCustomerNo());
            //判断是否都要收手续费
            if ("1".equals(transferTemplateVo.getIsFee())) {
                if (inCustomerFlag) { //说明在白名单中 就不用收手续
                    buyFee = new BigDecimal(0);
                } else {
                    buyFee = tradeUtil.getBuyFee(transferTemplateVo.getInCustomerNo(),
                            new BigDecimal(transferTemplateVo.getPrice()), Integer.valueOf(transferTemplateVo.getNum()));
                }
                if (outCustomerFlag) { //说明在白名单中 就不用收手续
                    sellFee = new BigDecimal(0);
                } else {
                    sellFee = tradeUtil.getSellFee(transferTemplateVo.getOutCustomerNo(),
                            new BigDecimal(transferTemplateVo.getPrice()), Integer.valueOf(transferTemplateVo.getNum()));
                }
                countNum += Integer.valueOf(transferTemplateVo.getNum());
                totalAmount = new BigDecimal(transferTemplateVo.getPrice()).multiply(new BigDecimal(transferTemplateVo.getNum()));
                countAmount = countAmount.add(totalAmount).add(buyFee).add(sellFee);
            }
            if ("0".equals(transferTemplateVo.getIsFee())) {
                countNum += Integer.valueOf(transferTemplateVo.getNum());
                totalAmount = new BigDecimal(transferTemplateVo.getPrice()).multiply(new BigDecimal(transferTemplateVo.getNum()));
                countAmount = countAmount.add(totalAmount);
            }
            transferLog.setIsfee(transferTemplateVo.getIsFee());
            transferLog.setName(transferTemplateVo.getFileName());
            transferLog.setCreator(transferTemplateVo.getCreator());
            transferLog.setModifier(transferTemplateVo.getModifier());
            //插入数据到tbl_tra_transaction_transfer_log_record表中
            transferLogRecordService.createObj(transferTemplateVo, id);
        }
        //创建tbl_tra_transaction_transfer_log表
        transferLog.setId(id);
        transferLog.setName(fileName);
        transferLog.setCountAmount(countAmount);
        transferLog.setCountNum(countNum);
        transferLog.setCreateTime(new Date());
        transferLog.setModifyTime(new Date());
        transferLog.setFlag((byte) 1);
        transferLog.setStatus("wait");
        if (transferLogMapper.insertSelective(transferLog) > 0) {
            return ApiResult.success();
        }
        return ApiResult.error();
    }

    @Override
    public void updateStatus(long id) {
        TransferLog transferLog = new TransferLog();
        transferLog.setId(id);
        transferLog.setStatus("execute");
        transferLog.setModifyTime(new Date());
        transferLogMapper.updateByPrimaryKeySelective(transferLog);
    }

    @Override
    public ApiResult deleteData(TransferDeleteDto transferDeleteDto) {
        TransferLog transferLog = new TransferLog();
        transferLog.setId(transferDeleteDto.getId());
        if (transferLogMapper.deleteByPrimaryKey(transferLog) > 0) {
            transferLogRecordService.deleteByTransferId(transferDeleteDto.getId());
            return ApiResult.success();
        }
        return ApiResult.error();
    }

    private ApiResult validator(TransferTemplateVo transferTemplateVo) {
        log.info("校验方法，transferTemplateVo={}", JSONObject.toJSONString(transferTemplateVo));
        if(StringUtils.isEmpty(transferTemplateVo.getProductTradeNo())
                ||  StringUtils.isEmpty(transferTemplateVo.getInCustomerNo())
                ||  StringUtils.isEmpty(transferTemplateVo.getOutCustomerNo())
                ||  StringUtils.isEmpty(transferTemplateVo.getPrice())
                ||  StringUtils.isEmpty(transferTemplateVo.getNum())
                ||  StringUtils.isEmpty(transferTemplateVo.getCostPrice())
                ||  StringUtils.isEmpty(transferTemplateVo.getTradeTime())
                ||  StringUtils.isEmpty(transferTemplateVo.getIsFee())
        ){
            log.info("校验非空");
            return ApiResult.error("导入失败，请检查数据后重新导入！");
        }

        if(judgeChinese(transferTemplateVo.getProductTradeNo())
                ||  judgeChinese(transferTemplateVo.getInCustomerNo())
                ||  judgeChinese(transferTemplateVo.getOutCustomerNo())
                ||  judgeChinese(transferTemplateVo.getPrice())
                ||  judgeChinese(transferTemplateVo.getNum())
                ||  judgeChinese(transferTemplateVo.getCostPrice())
                ||  judgeChinese(transferTemplateVo.getTradeTime())
                ||  judgeChinese(transferTemplateVo.getIsFee())
        ){
            log.info("校验是否含中文");
            return ApiResult.error("导入失败，请检查数据后重新导入！");
        }

        if(transferTemplateVo.getPrice().indexOf("-") > -1 || judgeTwoDecimal(transferTemplateVo.getPrice())){
            log.info("price={}",transferTemplateVo.getPrice());
            return ApiResult.error("导入失败，请检查数据后重新导入！");
        }
        if(transferTemplateVo.getNum().indexOf("-") > -1
                || transferTemplateVo.getNum().indexOf(".") > -1
                || Integer.valueOf(transferTemplateVo.getNum()) == 0){
            log.info("num={}",transferTemplateVo.getNum());
            return ApiResult.error("导入失败，请检查数据后重新导入！");
        }
        if(transferTemplateVo.getCostPrice().indexOf("-") > -1 || judgeTwoDecimal(transferTemplateVo.getCostPrice())){
            log.info("costPrice={}",transferTemplateVo.getCostPrice());
            return ApiResult.error("导入失败，请检查数据后重新导入！");
        }
        if(!("0".equals(transferTemplateVo.getIsFee()) || "1".equals(transferTemplateVo.getIsFee()))){
            log.info("isFee={}",transferTemplateVo.getIsFee());
            return ApiResult.error("导入失败，请检查数据后重新导入！");
        }
        return ApiResult.success();
    }

    private boolean judgeTwoDecimal(String str){
        Pattern pattern=Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
        Matcher match=pattern.matcher(str);
        if(!match.matches()){
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    private boolean judgeChinese(String str){
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher match=pattern.matcher(str);
        if(match.find()){
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public static void main(String[] args) {
        String str = "的撒大大";
//        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
//        Matcher match = pattern.matcher(str);
//        System.out.println(match);
//        if (!match.matches()) {
//            System.out.println("超过两位小数");
//        }
//        if (match.matches() == true) {
//            System.out.println("是两位小数");
//        }

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            System.out.println("包含中文");
        } else {
            System.out.println("不包含中文");
        }

    }
}
