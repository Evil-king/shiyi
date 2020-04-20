package com.baibei.shiyi.admin;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.content.feign.bean.vo.PublicNoticeVo;
import com.baibei.shiyi.content.feign.client.IAdminPublicNoticeFeign;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.junit4.SpringRunner;


@EnableFeignClients(basePackages = {"com.baibei.shiyi.content.feign", "com.baibei.shiyi.content.feign"})
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdminWebApplication.class)
public class PublicNoticeControllerTest {


    @Autowired
    private IAdminPublicNoticeFeign adminPublicNoticeFeign;

    @Test
    public void testPageList() {
        CustomerBaseAndPageDto customerBaseAndPageDto = new CustomerBaseAndPageDto();
        customerBaseAndPageDto.setCurrentPage(1);
        customerBaseAndPageDto.setPageSize(10);
        ApiResult<MyPageInfo<PublicNoticeVo>> pageInfoApiResult = adminPublicNoticeFeign.pageList(customerBaseAndPageDto);
        Assert.assertEquals(pageInfoApiResult.getCode().intValue(), ResultEnum.SUCCESS.getCode());
        pageInfoApiResult.getData().getList().stream().forEach(result -> log.info(JSONObject.toJSONString(result)));
    }


}
