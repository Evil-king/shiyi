package com.baibei.shiyi.product.service;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.AddCategoryDto;
import com.baibei.shiyi.product.feign.bean.dto.AdmCategoryProductDto;
import com.baibei.shiyi.product.feign.bean.dto.AdmProductNotInCategroyDto;
import com.baibei.shiyi.product.feign.bean.dto.CategoryDto;
import com.baibei.shiyi.product.feign.bean.vo.BaseIndexProductVo;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import com.baibei.shiyi.product.feign.bean.vo.CategoryProductVo;
import com.baibei.shiyi.product.feign.bean.vo.CategoryVo;
import com.baibei.shiyi.product.model.Category;
import com.baibei.shiyi.common.core.mybatis.Service;
import java.util.List;
import java.util.Map;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: Category服务接口
*/
public interface ICategoryService extends Service<Category> {

    /**
     * 获取前端首页类目的下级.
     * 商品分类
     *
     * 三级分类共有三个层级，由后台配置
     *
     * 一级分类只取前4个字
     *
     * 没有商品的分类不要展示
     *
     * 二级，三级分类最多只展示12个
     *
     * 一行展示3个，多出的换行展示
     * @param categoryDto
     * @return
     */
    MyPageInfo<List<CategoryVo>> getNextCategory(CategoryDto categoryDto);

    /**
     * 获取首页一级分类集合（一级分类只取前4个字）
     * @return
     */
    List<CategoryVo> getTheFirstIndexCategorys();



    /**
     * 获取最顶级的类目
     * @return
     */
    List<CategoryVo> getTheFirstCategorys();

    List<Category> getCategoryByPid(long pid);

    /**
     * 获取类目下的商品(分页)
     * @param categoryDto
     * @return
     */
    MyPageInfo<List<CategoryProductVo>> getIndexCategoryProducts(CategoryDto categoryDto);

    /**
     * 新增或编辑前台类目
     * @param addCategoryDto
     */
    void addOrEditCategory(AddCategoryDto addCategoryDto);

    /**
     * 删除前台类目
     * @param categoryId
     */
    void deleteCategory(Long categoryId);

    /**
     * 后台，获取前台类目的商品列表
     * @param admCategoryProductDto
     * @return
     */
    MyPageInfo<BaseShelfVo> getAdmCategoryProductList(AdmCategoryProductDto admCategoryProductDto);

    /**
     * 后台，获取不在指定类目下的商品集合
     * @param admProductNotInCategroyDto
     * @return
     */
    MyPageInfo<BaseShelfVo> getProductListNotInCategory(AdmProductNotInCategroyDto admProductNotInCategroyDto);

    /**
     * 根据id获取类目信息
     * @param categoryId
     * @return
     */
    CategoryVo getCategoryById(Long categoryId);

    /**
     * 获取单个类目
     * @param category
     * @return
     */
    Category getOneCategory(Category category);

    /**
     * 后台，获取顶级类目
     * @return
     */
    List<CategoryVo> getAdmTheFirstCategroy();

    /**
     * 后台，获取下级类目
     * @param categoryId
     * @return
     */
    List<CategoryVo> getAdmTheNextCategroy(Long categoryId);

    /**
     * 获取整个类目树
     * @return
     */
    List<Map<String, Object>> getCategoryTree();

    /**
     * 获取所有类目信息
     * @return
     */
    List<Category> getAllCategory();

    /**
     * 获取指定层级的类目数量
     * @param parentId
     * @return
     */
    int getCountByPid(Long parentId);

    /**
     * 获取层级数量
     * @param categoryId
     * @return
     */
    int getLevelCount(Long categoryId);
}
