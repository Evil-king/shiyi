package com.baibei.shiyi.product.service.impl;

import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.product.dao.ShelfCategoryRefMapper;
import com.baibei.shiyi.product.feign.bean.dto.AddCategoryDto;
import com.baibei.shiyi.product.feign.bean.dto.AdmCategoryProductDto;
import com.baibei.shiyi.product.feign.bean.dto.AdmProductNotInCategroyDto;
import com.baibei.shiyi.product.feign.bean.dto.CategoryDto;
import com.baibei.shiyi.product.feign.bean.vo.*;
import com.baibei.shiyi.product.dao.CategoryMapper;
import com.baibei.shiyi.product.model.Category;
import com.baibei.shiyi.product.model.ShelfBeanRef;
import com.baibei.shiyi.product.model.ShelfCategoryRef;
import com.baibei.shiyi.product.service.ICategoryService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.product.service.IShelfBeanRefService;
import com.baibei.shiyi.product.service.IShelfCategoryRefService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.*;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: Category服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class CategoryServiceImpl extends AbstractService<Category> implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Value("${category.cutCount}")
    private Integer cutCount;//截取类目名称长度
    @Value("${category.maxCount}")
    private Integer maxCount;//前端显示类目最大数量
    @Value("${category.top.count}")
    private Integer topCount;//顶级类目最大数量指定。超过该数量，不允许再新增
    @Value("${category.children.count}")
    private Integer childrenCount;//子级类目最大数量指定。超过该数量，不允许再新增
    @Value("${category.level.count}")
    private Integer levelCount;//指定层级最大数量，超过该数量，不允许往下再新增子级
    @Value("${category.title.length}")
    private int titleLengthLimit;//类目名称最大长度限制
    @Autowired
    private IShelfCategoryRefService shelfCategoryRefService;
    @Autowired
    private ShelfCategoryRefMapper shelfCategroyRefMapper;
    @Autowired
    private IShelfBeanRefService shelfBeanRefService;


    @Override
    public MyPageInfo<List<CategoryVo>> getNextCategory(CategoryDto categoryDto) {
        //根据Id查询类目信息
        List<CategoryVo> categoryVoList = new ArrayList<>();
        CategoryVo categoryVo = new CategoryVo();
        Category category = categoryMapper.selectByPrimaryKey(categoryDto.getId());
        if (StringUtils.isEmpty(category)) {
            throw new ServiceException("找不到指定类目");
        }
        if(Constants.CategoryEnd.NOTEND.equals(category.getEnd().toString())){//不是末端类目，则查询下一级类目
            PageHelper.startPage(categoryDto.getCurrentPage(), categoryDto.getPageSize());
            if (!StringUtils.isEmpty(maxCount)) {
                categoryDto.setMaxCount(maxCount);
            }
            categoryDto.setParentId(categoryDto.getId());
            categoryVoList = categoryMapper.selectIndexCategorysByPid(categoryDto);
        }
        MyPageInfo<List<CategoryVo>> myPageInfo= new MyPageInfo(categoryVoList);
        return myPageInfo;
    }

    @Override
    public List<CategoryVo> getTheFirstIndexCategorys() {
        CategoryDto categoryDto = new CategoryDto();
        if(cutCount!=null){
            categoryDto.setCutCount(cutCount);
        }
        categoryDto.setParentId(0);
        List<CategoryVo> categoryVoList = categoryMapper.selectIndexCategorysByPid(categoryDto);
        List<CategoryVo> returnList = new ArrayList<>();
        for (CategoryVo categoryVo : categoryVoList) {
            if(categoryVo.getEnd().toString().equals(Constants.CategoryEnd.ISEND)){
                //查询该类目下是否有商品，若没有，则不返回给前端
                boolean b = shelfCategoryRefService.checkHasProduct(categoryVo.getId());
                if(b){
                    returnList.add(categoryVo);
                }
            }else{
                returnList.add(categoryVo);
            }
        }
        return returnList;
    }


    @Override
    public List<CategoryVo> getTheFirstCategorys() {

        return categoryMapper.selectFirstCategorys();
    }

    @Override
    public List<Category> getCategoryByPid(long pid) {
        Condition condition = new Condition(Category.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("parentId",pid);
        List<Category> categories = categoryMapper.selectByCondition(condition);
        return categories;
    }

    @Override
    public MyPageInfo<List<CategoryProductVo>> getIndexCategoryProducts(CategoryDto categoryDto) {
        if(StringUtils.isEmpty(categoryDto.getId())){
            throw new ServiceException("未指定类目");
        }
        PageHelper.startPage(categoryDto.getCurrentPage(), categoryDto.getPageSize());
        List<CategoryProductVo> categoryProductVos = categoryMapper.selectIndexCategoryProducts(categoryDto);
        for (CategoryProductVo categoryProductVo : categoryProductVos) {
            List<ShelfBeanRef> shelfBeanList = shelfBeanRefService.getShelfBean(categoryProductVo.getShelfId(), null);
            for (ShelfBeanRef shelfBeanRef : shelfBeanList) {
                ShelfBeanVo shelfBeanVo = new ShelfBeanVo();
                shelfBeanVo.setShelfId(shelfBeanRef.getShelfId());
                shelfBeanVo.setBeanType(shelfBeanRef.getBeanType());
                shelfBeanVo.setUnit(shelfBeanRef.getUnit());
                shelfBeanVo.setValue(shelfBeanRef.getValue());
                categoryProductVo.getShelfBeanVoList().add(shelfBeanVo);
            }
        }
        MyPageInfo<List<CategoryProductVo>> myPageInfo= new MyPageInfo(categoryProductVos);
        return myPageInfo;
    }

    @Override
    public void addOrEditCategory(AddCategoryDto addCategoryDto) {
        if (addCategoryDto.getTitle().length()>titleLengthLimit) {
            throw new ServiceException("类目名称只能填写"+titleLengthLimit+"个字");
        }
        if(addCategoryDto.getParentId()!=0&&StringUtils.isEmpty(addCategoryDto.getImg())){
            throw new ServiceException("类目图片不能为空");
        }
        Category category = new Category();
        category.setParentId(addCategoryDto.getParentId());
        category.setTitle(addCategoryDto.getTitle());
        category.setEnd(new Byte(addCategoryDto.getEnd()));
        if (!StringUtils.isEmpty(addCategoryDto.getImg())) {
            category.setImg(addCategoryDto.getImg());
        }
        category.setShowStatus(addCategoryDto.getShowStatus());
        if (!StringUtils.isEmpty(addCategoryDto.getSeq())) {
            category.setSeq(addCategoryDto.getSeq());
        }

        if(addCategoryDto.getParentId()!=0){
            CategoryVo categoryVo = this.getCategoryById(addCategoryDto.getParentId());
            if (!StringUtils.isEmpty(categoryVo)) {
                if(categoryVo.getEnd().toString().equals("1")){
                    throw new ServiceException("不能在末级类目下添加类目");
                }
            }else{
                throw new ServiceException("未找到上级类目");
            }
            if (categoryVo.getEnd().equals(Constants.CategoryEnd.ISEND)) {
                throw new ServiceException("末级类目下不能添加子级类目");
            }
        }

        Category ct=new Category();
        ct.setParentId(addCategoryDto.getParentId());
        ct.setTitle(addCategoryDto.getTitle());
        Category oneCategory = this.getOneCategory(ct);
        if (StringUtils.isEmpty(addCategoryDto.getCategoryId())) {//新增类目
            if (!StringUtils.isEmpty(oneCategory)) {
                throw new ServiceException("同级存在同名情况");
            }
            category.setId(IdWorker.getId());
            if(addCategoryDto.getParentId()==0){//新增顶级类目
                //查看顶级类目的数量。超过指定数量不允许添加
                int countByPid = this.getCountByPid(addCategoryDto.getParentId());
                if (countByPid>=topCount) {
                    throw new ServiceException("顶级类目，最多创建"+topCount+"个");
                }
            }else{
                int countByPid = this.getCountByPid(addCategoryDto.getParentId());
                if (countByPid>=childrenCount) {
                    throw new ServiceException("子级类目，最多创建"+childrenCount+"个");
                }
            }
            int levelCount = this.getLevelCount(addCategoryDto.getParentId());
            if(levelCount>this.levelCount){
                throw new ServiceException("类目树最多"+this.levelCount+"层");
            }
            categoryMapper.insertSelective(category);
        }else{//编辑类目
            if (!StringUtils.isEmpty(oneCategory)&&!addCategoryDto.getCategoryId().equals(oneCategory.getId())) {
                throw new ServiceException("同级存在同名情况");
            }
            if(Constants.CategoryEnd.ISEND.equals(addCategoryDto.getEnd())){
                int countByPid = this.getCountByPid(addCategoryDto.getCategoryId());
                if(countByPid>0){
                    throw new ServiceException("该类目下有子类目，不能指定其为末级类目");
                }
            }else{
                boolean b = shelfCategoryRefService.hasProductsInCategory(addCategoryDto.getCategoryId());
                if(b){
                    throw new ServiceException("该类目下已挂载商品，不能将其设置为非末级类目");
                }
            }
            category.setId(addCategoryDto.getCategoryId());
            category.setModifyTime(new Date());
            categoryMapper.updateByPrimaryKeySelective(category);
        }
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (StringUtils.isEmpty(categoryId)) {
            throw new ServiceException("删除失败，未指定类目");
        }
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (StringUtils.isEmpty(category)) {
            throw new ServiceException("删除失败，不存在指定类目");
        }
        List<Category> categoryList = this.getCategoryByPid(categoryId);
        if(categoryList.size()>0){
            throw new ServiceException("删除失败，不是底层类目");
        }
        boolean result = shelfCategoryRefService.hasProductsInCategory(categoryId);
        if(result){
            throw new ServiceException("删除失败，该类目下存在商品");
        }
        this.deleteById(categoryId);
    }

    @Override
    public MyPageInfo<BaseShelfVo> getAdmCategoryProductList(AdmCategoryProductDto admCategoryProductDto) {
        if(StringUtils.isEmpty(admCategoryProductDto.getCategoryId())){
            throw new ServiceException("未指定类目");
        }
        PageHelper.startPage(admCategoryProductDto.getCurrentPage(), admCategoryProductDto.getPageSize());
        List<BaseShelfVo> baseShelfVoList = categoryMapper.selectAdmCategoryProductList(admCategoryProductDto.getCategoryId());
        MyPageInfo<BaseShelfVo> myPageInfo= new MyPageInfo(baseShelfVoList);
        return myPageInfo;
    }

    @Override
    public MyPageInfo<BaseShelfVo> getProductListNotInCategory(AdmProductNotInCategroyDto admProductNotInCategroyDto) {
        PageHelper.startPage(admProductNotInCategroyDto.getCurrentPage(), admProductNotInCategroyDto.getPageSize());
        List<BaseShelfVo> baseShelfVoList = categoryMapper.selectProductListNotInCategory(admProductNotInCategroyDto);
        MyPageInfo<BaseShelfVo> myPageInfo= new MyPageInfo(baseShelfVoList);
        return myPageInfo;
    }

    @Override
    public CategoryVo getCategoryById(Long categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        categoryVo.setId(category.getId());
        categoryVo.setParentId(category.getParentId());
        categoryVo.setTitle(category.getTitle());
        categoryVo.setImg(category.getImg());
        categoryVo.setEnd(category.getEnd());
        categoryVo.setSeq(category.getSeq());
        categoryVo.setShowStatus(category.getShowStatus());
        return categoryVo;
    }

    @Override
    public Category getOneCategory(Category category) {
        Condition condition = new Condition(Category.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(category.getId())) {
            criteria.andEqualTo("id",category.getId());
        }
        if (!StringUtils.isEmpty(category.getParentId())) {
            criteria.andEqualTo("parentId",category.getParentId());
        }
        if (!StringUtils.isEmpty(category.getTitle())) {
            criteria.andEqualTo("title",category.getTitle());
        }
        List<Category> categories = categoryMapper.selectByCondition(condition);
        return categories.size()==0?null:categories.get(0);
    }

    @Override
    public List<CategoryVo> getAdmTheFirstCategroy() {
        Condition condition = new Condition(Category.class);
        condition.setOrderByClause("seq is null,seq asc");
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        criteria.andEqualTo("parentId",0);
        List<Category> categoryList = categoryMapper.selectByCondition(condition);
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categoryList) {
            CategoryVo categoryVo = new CategoryVo();
            categoryVo.setId(category.getId());
            categoryVo.setParentId(category.getParentId());
            categoryVo.setTitle(category.getTitle());
            categoryVo.setImg(category.getImg());
            categoryVo.setEnd(category.getEnd());
            categoryVo.setSeq(category.getSeq());
            categoryVo.setShowStatus(category.getShowStatus());
            categoryVoList.add(categoryVo);
        }
        return categoryVoList;
    }

    @Override
    public List<CategoryVo> getAdmTheNextCategroy(Long categoryId) {
        Condition condition = new Condition(Category.class);
        condition.setOrderByClause("seq is null,seq asc");
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        criteria.andEqualTo("parentId",categoryId);
        List<Category> categoryList = categoryMapper.selectByCondition(condition);
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categoryList) {
            CategoryVo categoryVo = new CategoryVo();
            categoryVo.setId(category.getId());
            categoryVo.setParentId(category.getParentId());
            categoryVo.setTitle(category.getTitle());
            categoryVo.setImg(category.getImg());
            categoryVo.setEnd(category.getEnd());
            categoryVo.setSeq(category.getSeq());
            categoryVo.setShowStatus(category.getShowStatus());
            categoryVoList.add(categoryVo);
        }
        return categoryVoList;
    }

    @Override
    public List<Map<String, Object>> getCategoryTree() {
        //获取所有类目信息
        List<Category> allCategory = this.getAllCategory();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        for (Category category : allCategory) {
            Map<String, Object> mapArr = new HashMap<String, Object>();
            if (category.getParentId()==0) {//顶层节点
                mapArr.put("categoryId",category.getId());
                mapArr.put("parentId",category.getParentId());
                mapArr.put("title",category.getTitle());
                mapArr.put("seq",category.getSeq());
                mapArr.put("end",category.getEnd());
                mapArr.put("img",category.getImg());
                mapArr.put("showStatus",category.getShowStatus());
                mapArr.put("children",menuChild(category.getId(), allCategory));
                returnList.add(mapArr);
            }
        }
        return returnList;
    }

    /**
     * 递归获取孩子节点
     * @param id 节点id
     * @param categoryList 所有类目集合
     * @return
     */
    private List<Map<String, Object>> menuChild(Long id, List<Category> categoryList){
        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
        for (Category category : categoryList) {
            Map<String, Object> childArray = new HashMap<String, Object>();
            if (id.equals(category.getParentId())) {
                childArray.put("categoryId",category.getId());
                childArray.put("parentId",category.getParentId());
                childArray.put("title",category.getTitle());
                childArray.put("seq",category.getSeq());
                childArray.put("end",category.getEnd());
                childArray.put("img",category.getImg());
                childArray.put("showStatus",category.getShowStatus());
                childArray.put("children", menuChild(category.getId(), categoryList));
                lists.add(childArray);
            }
        }
        return lists;
    }

    @Override
    public List<Category> getAllCategory() {
        Condition condition = buildValidCondition(Category.class);
        List<Category> categoryList = categoryMapper.selectByCondition(condition);
        return categoryList;
    }

    @Override
    public int getCountByPid(Long parentId) {
        Condition condition = new Condition(Category.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("parentId",parentId);
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        return categoryMapper.selectCountByCondition(condition);
    }

    @Override
    public int getLevelCount(Long categoryId) {
        int levelCount = levelCount(categoryId, 1);
        return levelCount;
    }

    public int levelCount(Long categoryId,int levelCount){
        if(categoryId==0){
            return levelCount;
        }
        CategoryVo categoryVo = this.getCategoryById(categoryId);
        if (StringUtils.isEmpty(categoryVo)) {
            throw new ServiceException("新增类目失败，参数异常");
        }
        return levelCount(categoryVo.getParentId(),levelCount+1);
    }
}
