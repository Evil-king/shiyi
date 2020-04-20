package com.baibei.shiyi.cash.feign.base.vo;

import com.baibei.shiyi.cash.feign.base.dto.ExchCashExchangeDetail;
import com.baibei.shiyi.common.tool.api.BasePageQuery;

import java.util.List;

/**
 * @Classname ExchCashExchangeDetailQuery
 * @Description 查询出入金
 * @Date 2019/12/4 14:54
 * @Created by Longer
 */

public class ExchCashExchangeDetailQuery extends BasePageQuery {
    private List<ExchCashExchangeDetail> items;// 查询结果集
    private ExchCashExchangeDetail item;//非查询结果集
    private ExchCashExchangeDetail query;
    public List<ExchCashExchangeDetail> getItems() {
        return items;
    }
    public void setItems(List<ExchCashExchangeDetail> items) {
        this.items = items;
    }
    public ExchCashExchangeDetail getItem() {
        return item;
    }
    public void setItem(ExchCashExchangeDetail item) {
        this.item = item;
    }
    public ExchCashExchangeDetail getQuery() {
        return query;
    }
    public void setQuery(ExchCashExchangeDetail query) {
        this.query = query;
    }


}