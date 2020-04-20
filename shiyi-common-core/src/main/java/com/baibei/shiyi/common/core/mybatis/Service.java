package com.baibei.shiyi.common.core.mybatis;

import com.baibei.shiyi.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.page.PageParam;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;

/**
 * @author: 会跳舞的机器人
 * @date: 2017/7/6 19:05
 * @description: Service层基础接口，其他Service 接口 请继承该接口
 */
public interface Service<T> {
    boolean save(T model);//持久化

    void save(List<T> models);//批量持久化

    boolean deleteById(Long id);//通过主鍵刪除

    void deleteByIds(String ids);//批量刪除 eg：ids -> “1,2,3,4”

    boolean update(T model);//更新

    boolean updateByConditionSelective(T model, Condition condition);//根据条件更新

    boolean updateNull(T model); //空值也会修改进对象中

    T findById(Integer id);//通过ID查找

    T findById(Long id);//通过ID查找

    T findBy(String fieldName, Object value); //通过Model中某个成员变量名称（非数据表中column的名称）查找,value需符合unique约束

    List<T> findByIds(String ids);//通过多个ID查找//eg：ids -> “1,2,3,4”

    List<T> findByCondition(Condition condition);//根据条件查找

    List<T> findAll();//获取所有

    int selectCountByCondition(Condition condition); //count

    MyPageInfo<T> pageList(Condition condition, PageParam pageParam); //分页

    MyPageInfo<T> pageList(Condition condition, CustomerBaseAndPageDto customerBaseAndPageDto);

    Condition buildValidCondition(Class clazz);  // 获取flag=1的condition条件

    Example.Criteria buildValidCriteria(Condition condition); // 获取flag=1的Criteria


    /**
     * 通过主键软删除
     *
     * @param id
     * @return
     */
    boolean softDeleteById(Long id);

    /**
     * 批量软删除，多个id通过逗号,隔开
     *
     * @param ids
     * @return
     */
    boolean softDeleteByIds(String ids);

    /**
     * 批量软删除,通过Set集合进行软删除
     *
     * @param ids
     * @Author uqing
     */
    void batchDelete(Iterable<String> ids);


    /**
     * 批量软删除,通过Set集合进行软删除
     *
     * @param ids
     * @Author uqing
     */
    void batchDelete2(Iterable<Long> ids);

    /**
     * 批量修改
     *
     * @param ids   批量修改的id
     * @param model 批量修改的实体类
     * @Author uqing
     * @au
     */
    void batchUpdate(Iterable<String> ids, T model);

    /**
     * 验证重复性验证
     *
     * @param fieldName
     * @param fieldValue
     * @param id         其中Id如果为null的话,则是保存的时候验证唯一性，如果Id不为null,则修改时，验证排除自己以外的,判断唯一性
     * @return
     * @Author uqing
     */
    Boolean repeatVerification(String fieldName, Object fieldValue, Long id);

    /**
     * 查询单个对象
     *
     * @param condition
     * @return
     */
    T findOneByCondition(Condition condition);
}
