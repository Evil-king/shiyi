package com.baibei.shiyi.account.feign.client.hystrix;

import com.baibei.shiyi.account.feign.bean.dto.AdminRecordBeanDto;
import com.baibei.shiyi.account.feign.bean.dto.AdminRecordDto;
import com.baibei.shiyi.account.feign.bean.vo.AdminRecordVo;
import com.baibei.shiyi.account.feign.client.AdminRecordBeanFeign;
import com.baibei.shiyi.account.feign.client.AdminRecordMoneyFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/1 18:00
 * @description:
 */
@Component
@Slf4j
public class AdminRecordBeanHystrix implements FallbackFactory<AdminRecordBeanFeign> {


    @Override
    public AdminRecordBeanFeign create(Throwable cause) {
        return new AdminRecordBeanFeign() {
            @Override
            public ApiResult<MyPageInfo<AdminRecordVo>> recordPageList(AdminRecordBeanDto recordDto) {
                log.info("recordPageList fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<AdminRecordVo>> export(AdminRecordBeanDto recordDto) {
                log.info("export fallback,reason was : {}",cause);
                return ApiResult.serviceFail();
            }
        };
    }
}
