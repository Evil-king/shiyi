package com.baibei.shiyi.product.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.GroupCurdDto;
import com.baibei.shiyi.product.feign.bean.dto.GroupDto;
import com.baibei.shiyi.product.feign.bean.dto.GroupRefDto;
import com.baibei.shiyi.product.feign.bean.dto.ProductGroupDto;
import com.baibei.shiyi.product.feign.bean.vo.GroupProductVo;
import com.baibei.shiyi.product.feign.bean.vo.GroupVo;
import com.baibei.shiyi.product.feign.client.admin.IGroupFeign;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupHystrix implements FallbackFactory<IGroupFeign> {

    private final Logger logger = LoggerFactory.getLogger(GroupHystrix.class);

    @Override
    public IGroupFeign create(Throwable throwable) {
        return new IGroupFeign() {
            @Override
            public ApiResult<MyPageInfo<GroupVo>> pageList(GroupDto groupDto) {
                logger.info("pageList current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<GroupVo> getById(GroupCurdDto groupDto) {
                logger.info("getById current exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult deleteById(GroupCurdDto groupDto) {
                logger.info("deleteById current exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult save(GroupCurdDto groupDto) {
                logger.info("save current exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult update(GroupCurdDto groupDto) {
                logger.info("update current exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult batchDelete(GroupCurdDto groupCurdDto) {
                logger.info("batchDelete current exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult deleteProductGroupRef(ProductGroupDto productGroupDto) {
                logger.info("deleteProductGroupRef current exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<MyPageInfo<GroupProductVo>> findGroupProduct(ProductGroupDto productGroupDto) {
                logger.info("findGroupProduct current exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<MyPageInfo<GroupProductVo>> findNoExistProductGroup(ProductGroupDto productGroupDto) {
                logger.info("findNoExistProductGroup current exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<GroupVo>> findByList(GroupDto groupDto) {
                logger.info("findByList current exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<MyPageInfo<GroupProductVo>> indexGroupProduct(GroupRefDto groupRefDto) {
                logger.info("indexGroupProduct current exception is {}",throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
