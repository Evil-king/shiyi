package com.baibei.shiyi.trade.feign.client.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.TransferDataDto;
import com.baibei.shiyi.trade.feign.bean.dto.TransferDeleteDto;
import com.baibei.shiyi.trade.feign.bean.dto.TransferLogDto;
import com.baibei.shiyi.trade.feign.bean.dto.TransferPageListDto;
import com.baibei.shiyi.trade.feign.bean.vo.TransferDataVo;
import com.baibei.shiyi.trade.feign.bean.vo.TransferLogVo;
import com.baibei.shiyi.trade.feign.bean.vo.TransferPageListVo;
import com.baibei.shiyi.trade.feign.bean.vo.TransferTemplateVo;
import com.baibei.shiyi.trade.feign.client.admin.IAdminTransferFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AdminTransferHystrix implements FallbackFactory<IAdminTransferFeign> {
    @Override
    public IAdminTransferFeign create(Throwable throwable) {
        return new IAdminTransferFeign(){

            @Override
            public ApiResult<MyPageInfo<TransferLogVo>> pageListLog(TransferLogDto transferLogDto) {
                log.info("current pageList exception is {},params is {}", throwable, JSONObject.toJSONString(transferLogDto));
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<MyPageInfo<TransferPageListVo>> listPage(TransferPageListDto transferPageListDto) {
                log.info("current pageList exception is {},params is {}", throwable, JSONObject.toJSONString(transferPageListDto));
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<TransferPageListVo>> export(TransferPageListDto transferPageListDto) {
                log.info("current pageList exception is {},params is {}", throwable, JSONObject.toJSONString(transferPageListDto));
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<TransferDataVo> transferData(TransferDataDto transferDataDto) {
                log.info("current pageList exception is {},params is {}", throwable, JSONObject.toJSONString(transferDataDto));
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult importData(List<TransferTemplateVo> transferTemplateVo) {
                log.info("current pageList exception is {},params is {}", throwable, JSONObject.toJSONString(transferTemplateVo));
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult deleteData(TransferDeleteDto transferDeleteDto) {
                log.info("current pageList exception is {},params is {}", throwable, JSONObject.toJSONString(transferDeleteDto));
                return ApiResult.serviceFail();
            }
        };
    }
}
