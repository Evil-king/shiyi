package com.baibei.shiyi.admin.modules.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MyExcelListener<E extends BaseRowModel> extends AnalysisEventListener {

    private List<E> dataList = new ArrayList<>();

    @Override
    public void invoke(Object baseRowModel, AnalysisContext context) {
        dataList.add((E) baseRowModel);
    }

    /**
     * 当方法结束时,执行批量导入
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
       log.info("当前已经全部导出");
    }

    public List<E> getDataList() {
        return dataList;
    }

    public void setDataList(List<E> dataList) {
        this.dataList = dataList;
    }
}
