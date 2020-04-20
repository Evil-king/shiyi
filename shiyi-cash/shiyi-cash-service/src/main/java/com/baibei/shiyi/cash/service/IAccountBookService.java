package com.baibei.shiyi.cash.service;
import com.baibei.shiyi.cash.feign.base.dto.Apply1010PagelistDto;
import com.baibei.shiyi.cash.feign.base.vo.Apply1010PagelistVo;
import com.baibei.shiyi.cash.model.AccountBook;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;

import java.util.List;


/**
* @author: Longer
* @date: 2019/11/06 11:18:25
* @description: AccountBook服务接口
*/
public interface IAccountBookService extends Service<AccountBook> {

    /**
     * 清楚整张表
     */
    void clear();

    /**
     * 请求1010接口
     * @param selectFlag
     * @param pageNum
     * @return
     */
    ApiResult apply1010(String selectFlag, String pageNum);

    MyPageInfo<Apply1010PagelistVo> apply1010Pagelist(Apply1010PagelistDto apply1010PagelistDto);

    List<Apply1010PagelistVo> apply1010List(Apply1010PagelistDto apply1010PagelistDto);

}
