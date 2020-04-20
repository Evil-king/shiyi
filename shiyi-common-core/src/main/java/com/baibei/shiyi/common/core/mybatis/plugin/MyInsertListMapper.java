package com.baibei.shiyi.common.core.mybatis.plugin;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;

import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/12 10:13
 * @description: 批量插入，主键ID值需要手动设值，create_time，modify_time，flag表公共字段无需设值
 */
public interface MyInsertListMapper<T> {
    @Options(
            useGeneratedKeys = true,
            keyProperty = "id"
    )
    @InsertProvider(
            type = MyInsertListProvider.class,
            method = "dynamicSQL"
    )
    int myInsertList(List<T> var1);
}