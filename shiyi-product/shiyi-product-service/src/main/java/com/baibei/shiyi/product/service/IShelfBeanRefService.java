package com.baibei.shiyi.product.service;
import com.baibei.shiyi.product.model.ShelfBeanRef;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
* @author: Longer
* @date: 2019/10/30 18:15:41
* @description: ShelfBeanRef服务接口
*/
public interface IShelfBeanRefService extends Service<ShelfBeanRef> {

    /**
     * 删除
     * @param shelfId
     * @param beanType
     */
    void deleteShelfBean(Long shelfId,String beanType);

    List<ShelfBeanRef> getShelfBean(Long shelfId, String beanType);
}
