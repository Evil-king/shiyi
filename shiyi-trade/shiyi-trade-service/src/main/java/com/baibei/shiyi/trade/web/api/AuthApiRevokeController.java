package com.baibei.shiyi.trade.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.biz.RevokeBiz;
import com.baibei.shiyi.trade.common.dto.BatchRevokeDto;
import com.baibei.shiyi.trade.common.dto.RevokeByDirectionDto;
import com.baibei.shiyi.trade.common.dto.RevokeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/30 18:30
 * @description:
 */
@RestController
@RequestMapping("/auth/api/trade")
public class AuthApiRevokeController {
    @Autowired
    private RevokeBiz revokeBiz;

    /**
     * 客户批量撤单
     *
     * @param dto
     * @return
     */
    @PostMapping("/batchRevoke")
    public ApiResult batchRevoke(@RequestBody @Validated BatchRevokeDto dto) {
        return revokeBiz.batchRevoke(dto);
    }

    /**
     * 根据方向撤单
     *
     * @param dto
     * @return
     */
    @PostMapping("/revokeByDirection")
    public ApiResult revokeByDirection(@RequestBody @Validated RevokeByDirectionDto dto) {
        if (StringUtils.isEmpty(dto.getCustomerNo())) {
            return ApiResult.badParam("客户编码不能为空");
        }
        return revokeBiz.revokeByDirection(dto);
    }

    /**
     * 全部撤单
     *
     * @param dto
     * @return
     */
    @PostMapping("/allRevoke")
    public ApiResult allRevoke(@RequestBody @Validated RevokeDto dto) {
        if (StringUtils.isEmpty(dto.getCustomerNo())) {
            return ApiResult.badParam("客户编码不能为空");
        }
        return revokeBiz.allRevoke(dto);
    }

    /**
     * 系统撤单
     *
     * @return
     */
    @GetMapping("/systemRevoke")
    public ApiResult systemRevoke() {
        return revokeBiz.systemRevoke();
    }
}