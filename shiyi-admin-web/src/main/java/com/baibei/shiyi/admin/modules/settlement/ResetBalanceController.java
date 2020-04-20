package com.baibei.shiyi.admin.modules.settlement;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.HttpClientUtils;
import com.baibei.shiyi.settlement.feign.bean.dto.ResetBalanceDto;
import com.baibei.shiyi.settlement.feign.bean.vo.ResetBalanceVo;
import com.baibei.shiyi.settlement.feign.client.IAdminResetBalanceFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/5 18:27
 * @description:
 */
@RestController
@RequestMapping("/admin/settlement/resetbalance")
@Slf4j
public class ResetBalanceController {

    @Autowired
    private IAdminResetBalanceFeign resetBalanceFeign;
    @Value("${fuqing.request.url}")
    private String fuqingRequestUrl;

    @Autowired
    private ExcelUtils excelUtils;

    @PostMapping(path = "/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('CAPITAL_RESET_RECORD'))")
    public ApiResult<MyPageInfo<ResetBalanceVo>> pageList(@Validated @RequestBody ResetBalanceDto dto) {
        return resetBalanceFeign.pageList(dto);
    }

    @PostMapping(path = "/reset")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('CAPITAL_RESET_RECORD'))")
    public ApiResult reset(@RequestBody ResetBalanceDto dto) {
        return resetBalanceFeign.reset(dto);
    }

    @PostMapping(path = "/exportExcel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('CAPITAL_RESET_RECORD'))")
    public void excelExport(@RequestBody ResetBalanceDto dto, HttpServletResponse response) {
        ApiResult<List<ResetBalanceVo>> apiResult = resetBalanceFeign.exportExcel(dto);
        if (apiResult.hasFail()) {
            log.info("导出失败，apiResult={}", apiResult.toString());
            throw new ServiceException("导出失败");
        }
        excelUtils.defaultExcelExport(apiResult.getData(), response, ResetBalanceVo.class, "重置可提列表");
    }

    @PostMapping(path = "/getBalance")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('GET_BANK_BALANCE'))")
    public ApiResult getBalance(@RequestBody Map<String,String> map) {
        // 获取客户的余额
        List<Map> resultList = new ArrayList<>();
        String customers = map.get("customers");
        String[] customerArr = customers.split(",");
        for (String customerNo : customerArr) {
            try {
                //去掉空格
                customerNo = customerNo.replaceAll(" ", "");
                Map<String, Object> param = new HashMap<>();
                param.put("exchangeFundAccount", customerNo);
                log.info("查询福用户余额入参：{}", JSON.toJSONString(param));
                String res = HttpClientUtils.doPostJson(fuqingRequestUrl + "/queryAccountBalance.json", JSON.toJSONString(param));
                if (!StringUtils.isEmpty(res)) {
                    JSONObject jsonObject = JSON.parseObject(res);
                    if (jsonObject.getIntValue("code") == 200) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        Map resultMap = new HashMap();
                        resultMap.put("customerNo",customerNo);
                        //资金余额，分为单位转换成元
                        resultMap.put("accountBalance",data.getBigDecimal("accountBalance").divide(new BigDecimal(100)).toPlainString());
                        //可用余额，分为单位转换成元
                        resultMap.put("enableBalance",data.getBigDecimal("enableBalance").divide(new BigDecimal(100)).toPlainString());
                        //可取余额，分为单位转换成元
                        resultMap.put("fetchBalance",data.getBigDecimal("fetchBalance").divide(new BigDecimal(100)).toPlainString());
                        //冻结余额，分为单位转换成元
                        resultMap.put("frozenBalance",data.getBigDecimal("frozenBalance").divide(new BigDecimal(100)).toPlainString());
                        resultList.add(resultMap);
                    } else {
                        log.info("客户{}获取余额失败，res={}", res);
                    }
                }
            }catch (Exception e){
                log.info("查询福清余额报错：{}",e);
            }
        }
        return ApiResult.success(resultList);
    }

}