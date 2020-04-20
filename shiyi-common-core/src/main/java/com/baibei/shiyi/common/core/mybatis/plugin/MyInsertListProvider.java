package com.baibei.shiyi.common.core.mybatis.plugin;

import com.baibei.shiyi.common.tool.constants.DbConstant;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/12 10:45
 * @description:
 */
public class MyInsertListProvider extends MapperTemplate {

    public MyInsertListProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 批量插入，主键ID值需要手动设值，create_time，modify_time，flag表公共字段无需设值
     *
     * @param ms
     */
    public String myInsertList(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        // 拼接列
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        for (EntityColumn column : columnList) {
            if (column.getColumn().equals(DbConstant.CREATE_TIME) || column.getColumn().equals(DbConstant.MODIFY_TIME)
                    || column.getColumn().equals(DbConstant.MODIFY_TIME) || column.getColumn().equals(DbConstant.FLAG)) {
                continue;
            }
            sql.append(column.getColumn() + ",");
        }
        sql.append("</trim>");
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        for (EntityColumn column : columnList) {
            if (column.getColumn().equals(DbConstant.CREATE_TIME) || column.getColumn().equals(DbConstant.MODIFY_TIME)
                    || column.getColumn().equals(DbConstant.MODIFY_TIME) || column.getColumn().equals(DbConstant.FLAG)) {
                continue;
            }
            sql.append(column.getColumnHolder("record") + ",");
        }
        sql.append("</trim>");
        sql.append("</foreach>");
        return sql.toString();
    }
}