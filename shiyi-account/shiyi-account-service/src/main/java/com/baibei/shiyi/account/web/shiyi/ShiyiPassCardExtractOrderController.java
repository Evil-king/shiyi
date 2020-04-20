package com.baibei.shiyi.account.web.shiyi;

import com.baibei.shiyi.account.feign.base.shiyi.IPassCardExtractOrderBase;
import com.baibei.shiyi.account.service.IPassCardExtractOrderService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hyc
 * @date: 2020/1/7 11:24
 * @description:
 */
@RestController
public class ShiyiPassCardExtractOrderController implements IPassCardExtractOrderBase {
    @Autowired
    private IPassCardExtractOrderService passCardExtractOrderService;
    @Override
    public ApiResult systemOperation() {
        return passCardExtractOrderService.systemOperation();
    }
}
