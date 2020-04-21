package com.baibei.shiyi.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.CollectionUtils;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.SkuPropertyUtil;
import com.baibei.shiyi.product.dao.ShelfBeanRefMapper;
import com.baibei.shiyi.product.feign.bean.vo.ProductShelfVo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.*;
import com.baibei.shiyi.product.dao.ProductShelfMapper;
import com.baibei.shiyi.product.model.*;
import com.baibei.shiyi.product.service.*;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.nio.charset.CoderResult;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author: Longer
 * @date: 2019/07/30 10:12:45
 * @description: ProductShelf服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductShelfServiceImpl extends AbstractService<ProductShelf> implements IProductShelfService {

    @Autowired
    private ProductShelfMapper productShelfMapper;
    @Autowired
    private IProductImgService productImgService;
    @Autowired
    private IShelfCategoryRefService shelfCategoryRefService;
    @Autowired
    private IProductShelfRefService productShelfRefService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IShelfBeanRefService shelfBeanRefService;
    @Autowired
    private ShelfBeanRefMapper shelfBeanRefMapper;
    @Value("${shelf.editpage.module.list}")
    private String moduleListJsonStr;//商品仓列表
    @Value("${shelf.editpage.integraltype.list}")
    private String integralTypeListJsonStr;//积分类型列表
    @Value("${shelf.editpage.source.list}")
    private String sourceListJsonStr;//来源列表


    @Override
    public BaseShelfVo getBaseShelfProductInfo(ShelfRefDto shelfRefDto) {
        if (StringUtils.isEmpty(shelfRefDto.getSkuId())) {
            throw new ServiceException("未指定skuId");
        }
        if (StringUtils.isEmpty(shelfRefDto.getShelfId())) {
            throw new ServiceException("未指定上架id");
        }
        List<BaseShelfVo> baseShelfVoList = productShelfMapper.selectBaseShelfProductInfo(shelfRefDto);
        for (BaseShelfVo baseShelfVo : baseShelfVoList) {
            //获取商品积分列表
            List<ShelfBeanRef> shelfBeanList = shelfBeanRefService.getShelfBean(baseShelfVo.getShelfId(), null);
            for (ShelfBeanRef shelfBeanRef : shelfBeanList) {
                ShelfBeanVo shelfBeanVo = new ShelfBeanVo();
                shelfBeanVo.setBeanType(shelfBeanRef.getBeanType());
                shelfBeanVo.setShelfId(shelfBeanRef.getShelfId());
                shelfBeanVo.setUnit(shelfBeanRef.getUnit());
                shelfBeanVo.setValue(shelfBeanRef.getValue());
                baseShelfVo.getShelfBeanVoList().add(shelfBeanVo);
            }
        }
        return baseShelfVoList.size() > 0 ? baseShelfVoList.get(0) : null;
    }

    @Override
    public MyPageInfo<GroupProductVo> findProductGroupList(ProductGroupDto productGroupDto) {
        PageHelper.startPage(productGroupDto.getCurrentPage(), productGroupDto.getPageSize());
        List<GroupProductVo> productVoList = productShelfMapper.findGroupProduct(productGroupDto);
        MyPageInfo<GroupProductVo> pageInfo = new MyPageInfo<>(productVoList);
        return pageInfo;
    }

    @Override
    public List<GroupProductVo> findByProductGroup(ProductGroupDto productGroupDto) {
        return productShelfMapper.findGroupProduct(productGroupDto);
    }

    @Override
    public MyPageInfo<GroupProductVo> findNoExistProductGroup(ProductGroupDto productGroupDto) {
        PageHelper.startPage(productGroupDto.getCurrentPage(), productGroupDto.getPageSize());
        List<GroupProductVo> productVoList = productShelfMapper.findNoExistProductGroup(productGroupDto);
        productVoList.stream().forEach(result -> {
            if (result.getStatus().equals(Constants.ProductShelfStatus.SHELF)) {
                result.setStatusTxt("上架");
            }
            if (result.getStatus().equals(Constants.ProductShelfStatus.UNSHELF)) {
                result.setStatusTxt("下架");
            }
            if (!StringUtils.isEmpty(result.getSource())) {
                result.setSourceText(Constants.SourceType.getMapTypeText().get(result.getSource()));
            }
        });
        MyPageInfo<GroupProductVo> pageInf = new MyPageInfo<>(productVoList);
        return pageInf;
    }

    @Override
    public BuyProductVo getBuyProductInfo(ShelfRefDto shelfRefDto) {
        BuyProductVo buyProductVo = new BuyProductVo();
        List<MultiSku> multiSkuList = new ArrayList<>();
        List<BaseShelfVo> baseShelfVoList = productShelfMapper.selectBaseShelfProductInfo(shelfRefDto);
        if (baseShelfVoList.size() == 0) {
            throw new ServiceException("商品信息不完整");
        }
        for (BaseShelfVo baseShelfVo : baseShelfVoList) {
            //获取商品积分列表
            List<ShelfBeanRef> shelfBeanList = shelfBeanRefService.getShelfBean(baseShelfVo.getShelfId(), null);
            for (ShelfBeanRef shelfBeanRef : shelfBeanList) {
                ShelfBeanVo shelfBeanVo = new ShelfBeanVo();
                shelfBeanVo.setBeanType(shelfBeanRef.getBeanType());
                shelfBeanVo.setShelfId(shelfBeanRef.getShelfId());
                shelfBeanVo.setUnit(shelfBeanRef.getUnit());
                shelfBeanVo.setValue(shelfBeanRef.getValue());
                baseShelfVo.getShelfBeanVoList().add(shelfBeanVo);
            }

            //sku
            String skuProperty = baseShelfVo.getSkuProperty();
            if (!StringUtils.isEmpty(skuProperty)) {
                JSONObject skuDesObj = JSONObject.parseObject(skuProperty);
                Collection<Object> values = skuDesObj.values();
                Iterator<Object> valIterator = values.iterator();
                String skuPropertyStr = "";
                while (valIterator.hasNext()) {
                    if ("".equals(skuPropertyStr)) {
                        skuPropertyStr = valIterator.next().toString();
                    } else {
                        skuPropertyStr = skuPropertyStr + ";" + valIterator.next().toString();
                    }
                }
                baseShelfVo.setSkuPropertyValsStr(skuPropertyStr);
            }
        }
        BaseShelfVo baseShelfVo = baseShelfVoList.get(0);
        String skuProperty = baseShelfVo.getSkuProperty();
        if (!StringUtils.isEmpty(skuProperty)) {
            JSONObject skuDesObj = JSONObject.parseObject(skuProperty);
            //获取所有的key。如[颜色，大小，尺寸]
            Set<String> keySet = skuDesObj.keySet();
            Iterator<String> keyIterator = keySet.iterator();
            List<String> valList = new ArrayList<>();//去重
            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                String value = "";
                for (BaseShelfVo shelfVo : baseShelfVoList) {
                    String skuP = shelfVo.getSkuProperty();
                    JSONObject skuPObj = JSONObject.parseObject(skuP);
                    if (!valList.contains(skuPObj.getString(key))) {
                        if ("".equals(value)) {
                            value = skuPObj.getString(key);
                        } else {
                            value = value + ";" + skuPObj.getString(key);
                        }
                        valList.add(skuPObj.getString(key));
                    }
                }
                MultiSku multiSku = new MultiSku();
                multiSku.setKey(key);
                multiSku.setValue(value);
                multiSkuList.add(multiSku);
            }
        }
        buyProductVo.setBaseShelfVoList(baseShelfVoList);
        buyProductVo.setMultiSkuList(multiSkuList);
        return buyProductVo;
    }

    @Override
    public BaseIndexProductVo getIndexProductView(ShelfRefDto shelfRefDto) {
        ProductShelf productShelf = getShelfById(shelfRefDto);
        if (productShelf.getStatus().equals(Constants.ShelfStatus.UNSHELF)) {
            throw new ServiceException(ResultEnum.PRODUCT_UNSHELF.getMsg(),ResultEnum.PRODUCT_UNSHELF.getCode());
        }
        BaseIndexProductVo baseIndexProductVo = productShelfMapper.selectBaseIndexProductInfo(shelfRefDto);
        //获取商品积分列表
        List<ShelfBeanRef> shelfBeanList = shelfBeanRefService.getShelfBean(baseIndexProductVo.getShelfId(), null);
        for (ShelfBeanRef shelfBeanRef : shelfBeanList) {
            ShelfBeanVo shelfBeanVo = new ShelfBeanVo();
            shelfBeanVo.setBeanType(shelfBeanRef.getBeanType());
            shelfBeanVo.setShelfId(shelfBeanRef.getShelfId());
            shelfBeanVo.setUnit(shelfBeanRef.getUnit());
            shelfBeanVo.setValue(shelfBeanRef.getValue());
            baseIndexProductVo.getShelfBeanVoList().add(shelfBeanVo);
        }
        if (StringUtils.isEmpty(baseIndexProductVo)) {
            throw new ServiceException("查询不到该商品详情");
        }
        //查询图片
        List<ProductImg> productImgs = productImgService.getProductImgs(baseIndexProductVo.getProductId());
        baseIndexProductVo.setImageList(productImgs);
        return baseIndexProductVo;
    }

    @Override
    public ProductShelf getShelfById(ShelfRefDto shelfRefDto) {
        if (StringUtils.isEmpty(shelfRefDto.getShelfId())) {
            throw new ServiceException("未指定上架id");
        }
        ProductShelf productShelf = new ProductShelf();
        productShelf.setId(shelfRefDto.getShelfId());
        return productShelfMapper.selectByPrimaryKey(productShelf);
    }

    @Override
    public Long countLatestProduct() {
        PageHelper.startPage(1, 50);
        return this.productShelfMapper.countLatestProduct();
    }

    @Override
    public List<GroupProductVo> findLastShelfTimeByProduct(ProductGroupDto productGroupDto) {
        PageHelper.startPage(productGroupDto.getCurrentPage(), productGroupDto.getPageSize());
        List<GroupProductVo> result = this.productShelfMapper.findLastByShelfTime(productGroupDto);
        return result;
    }

    @Override
    public List<GroupProductVo> findLastSellCountByProduct(ProductGroupDto productGroupDto) {
        PageHelper.startPage(productGroupDto.getCurrentPage(), productGroupDto.getPageSize());
        List<GroupProductVo> result = this.productShelfMapper.findLastSellCountByProduct(productGroupDto);
        return result;
    }

    @Override
    public void shelfProduct(AdmProductShelfDto admProductShelfDto) {
        if (Constants.ShelfType.SEND_INTEGRAL.equals(admProductShelfDto.getShelfType())
                &&CollectionUtils.isEmpty(admProductShelfDto.getShelfBeanRefDtoList())) {
            throw new ServiceException("赠送积分不能为空");
        }
        if (admProductShelfDto.getCategories().size() == 0) {
            throw new ServiceException("未指定前台类目");
        }
        if (admProductShelfDto.getShelfSkuList().size() == 0) {
            throw new ServiceException("未指定上架的属性规格");
        }
        if (Constants.Unit.PERCENT.equals(admProductShelfDto.getUnit())&&admProductShelfDto.getMaxdetuch().compareTo(new BigDecimal(100))>0) {
            throw new ServiceException("抵扣值不能大于100%");
        }
        //tbl_pro_product_shelf
        ProductShelf productShelf = new ProductShelf();
        productShelf.setId(admProductShelfDto.getShelfId());
        if (StringUtils.isEmpty(admProductShelfDto.getShelfId())) {
            productShelf.setId(IdWorker.getId());
            productShelf.setShelfNo(admProductShelfDto.getShelfNo());
            productShelf.setSpuNo(admProductShelfDto.getSpuNo());
            productShelf.setProductId(admProductShelfDto.getProductId());
            productShelf.setCreateTime(new Date());
        }
        productShelf.setShelfType(admProductShelfDto.getShelfType());
        productShelf.setProductShelfName(admProductShelfDto.getShelfName());
        productShelf.setLinePrice(admProductShelfDto.getLinePrice());
        /*productShelf.setUnit(admProductShelfDto.getUnit());
        productShelf.setMaxdetuch(admProductShelfDto.getMaxdetuch());*/
        /*productShelf.setIntegralType(admProductShelfDto.getIntegralType());*/
        productShelf.setFreightType(admProductShelfDto.getFreightType().toPlainString());
        productShelf.setSeparetBenefit(admProductShelfDto.getSeparetBenefit());
        productShelf.setStatus(admProductShelfDto.getStatus());
        if (Constants.ProductShelfStatus.SHELF.equals(admProductShelfDto.getStatus())) {
            productShelf.setShelfTime(new Date());
        }
        productShelf.setModifyTime(new Date());
        productShelf.setFlag(new Byte(Constants.Flag.VALID));
        if (StringUtils.isEmpty(admProductShelfDto.getShelfId())) {//新增
            productShelfMapper.insertSelective(productShelf);
        } else {//编辑
            productShelfMapper.updateById(productShelf);
        }

        //tbl_pro_shelf_bean_ref
        if (!StringUtils.isEmpty(admProductShelfDto.getShelfId())) {
            //删除原来的积分信息
            shelfBeanRefService.deleteShelfBean(admProductShelfDto.getShelfId(),null);
        }
        List<ShelfBeanRef> addShelfBeanList = new ArrayList<>();
        if (Constants.ShelfType.SEND_INTEGRAL.equals(admProductShelfDto.getShelfType())) {
            for (ShelfBeanRefDto shelfBeanRefDto : admProductShelfDto.getShelfBeanRefDtoList()) {
                ShelfBeanRef shelfBeanRef = new ShelfBeanRef();
                shelfBeanRef.setId(IdWorker.getId());
                shelfBeanRef.setBeanType(shelfBeanRefDto.getBeanType());
                shelfBeanRef.setShelfId(productShelf.getId());
                shelfBeanRef.setUnit(shelfBeanRefDto.getUnit());
                shelfBeanRef.setValue(shelfBeanRefDto.getValue());
                shelfBeanRef.setCreateTime(new Date());
                shelfBeanRef.setModifyTime(new Date());
                shelfBeanRef.setFlag(new Byte(Constants.Flag.VALID));
                addShelfBeanList.add(shelfBeanRef);
            }
        }
        for (ShelfBeanRef shelfBeanRef : addShelfBeanList) {
            shelfBeanRefMapper.insert(shelfBeanRef);
        }

        //tbl_pro_shelf_categroy_ref
        if (!StringUtils.isEmpty(admProductShelfDto.getShelfId())) {
            //删除上架商品和前台类目的关联关系
            shelfCategoryRefService.deleteCategoryProduct(null, admProductShelfDto.getShelfId());
        }
        List<ShelfCategoryRef> shelfCategoryRefList = new ArrayList<>();
        for (AddCategoryProduct addCategoryProduct : admProductShelfDto.getCategories()) {
            ShelfCategoryRef shelfCategoryRef = new ShelfCategoryRef();
            shelfCategoryRef.setId(IdWorker.getId());
            shelfCategoryRef.setShelfId(productShelf.getId());
            shelfCategoryRef.setCategoryId(addCategoryProduct.getCategoryId());
            shelfCategoryRefList.add(shelfCategoryRef);
        }
        shelfCategoryRefService.save(shelfCategoryRefList);

        //tbl_pro_product_shelf_ref
        if (!StringUtils.isEmpty(admProductShelfDto.getShelfId())) {
            //删除上架商品和sku的关联关系
            productShelfRefService.deleteProductShelf(admProductShelfDto.getShelfId(), null);
        }
        List<ProductShelfRef> productShelfRefList = new ArrayList<>();
        for (AdmProductShelfSkuDto admProductShelfSkuDto : admProductShelfDto.getShelfSkuList()) {
            if (!StringUtils.isEmpty(admProductShelfDto.getLinePrice()) &&
                    admProductShelfDto.getLinePrice().compareTo(admProductShelfSkuDto.getShelfPrice()) < 0) {
                throw new ServiceException("划线价不能小于上架价");
            }
            ProductShelfRef productShelfRef = new ProductShelfRef();
            productShelfRef.setId(IdWorker.getId());
            productShelfRef.setShelfId(productShelf.getId());
            productShelfRef.setSkuId(admProductShelfSkuDto.getSkuId());
            productShelfRef.setShelfPrice(admProductShelfSkuDto.getShelfPrice());
            productShelfRefList.add(productShelfRef);
        }
        productShelfRefService.save(productShelfRefList);
    }

    @Override
    public AdmEditProductShelfVo admEditShelfProductPage(Long shelfId) {
        AdmEditProductShelfVo admEditProductShelfVo = new AdmEditProductShelfVo();
        if (StringUtils.isEmpty(shelfId)) {
            throw new ServiceException("未指定商品");
        }
        admEditProductShelfVo.setShelfId(shelfId);
        ShelfRefDto shelfRefDto = new ShelfRefDto();
        shelfRefDto.setShelfId(shelfId);
        ProductShelf productShelf = this.getShelfById(shelfRefDto);
        if (StringUtils.isEmpty(productShelf)) {
            throw new ServiceException("找不到指定商品");
        }
        admEditProductShelfVo.setShelfType(productShelf.getShelfType());
        admEditProductShelfVo.setProductId(productShelf.getProductId());
        admEditProductShelfVo.setSpuNo(productShelf.getSpuNo());
        admEditProductShelfVo.setShelfName(productShelf.getProductShelfName());
        admEditProductShelfVo.setLinePrice(productShelf.getLinePrice());
        admEditProductShelfVo.setShelfNo(productShelf.getShelfNo());
        admEditProductShelfVo.setModule(productShelf.getModule());
        admEditProductShelfVo.setPlan(productShelf.getPlan());
        admEditProductShelfVo.setUnit(productShelf.getUnit());
        admEditProductShelfVo.setMaxdetuch(productShelf.getMaxdetuch());
        admEditProductShelfVo.setSource(productShelf.getSource());
        admEditProductShelfVo.setSeparetBenefit(productShelf.getSeparetBenefit());
        admEditProductShelfVo.setIntegralType(productShelf.getIntegralType());
        admEditProductShelfVo.setFreightType(productShelf.getFreightType());
        admEditProductShelfVo.setStatus(productShelf.getStatus());
        //获取商品积分列表
        List<ShelfBeanRef> shelfBeanList = shelfBeanRefService.getShelfBean(shelfId, null);
        for (ShelfBeanRef shelfBeanRef : shelfBeanList) {
            ShelfBeanVo shelfBeanVo = new ShelfBeanVo();
            shelfBeanVo.setBeanType(shelfBeanRef.getBeanType());
            shelfBeanVo.setShelfId(shelfBeanRef.getShelfId());
            shelfBeanVo.setUnit(shelfBeanRef.getUnit());
            shelfBeanVo.setValue(shelfBeanRef.getValue());
            admEditProductShelfVo.getShelfBeanRefDtoList().add(shelfBeanVo);
        }
        //获取上架sku列表
        List<ProductShelfRef> shelfSkuList = productShelfRefService.getShelfSkus(shelfId, null);
        for (ProductShelfRef productShelfRef : shelfSkuList) {
            AdmProductShelfSkuVo admProductShelfSkuVo = new AdmProductShelfSkuVo();
            admProductShelfSkuVo.setSkuId(productShelfRef.getSkuId());
            admProductShelfSkuVo.setShelfPrice(productShelfRef.getShelfPrice());
            admEditProductShelfVo.getShelfSkuList().add(admProductShelfSkuVo);
        }
        //获取已选的前端类目
        List<CategoryVo> categoryVoList = shelfCategoryRefService.findByCategory(shelfId);
        for (CategoryVo categoryVo : categoryVoList) {
            List<Long> revCategoryList = new ArrayList<>();
            List<Long> categoryList = new ArrayList<>();
            List<Long> categoryIdList = getCategoryIdList(categoryVo, categoryList);
            for (int i = categoryIdList.size() - 1; i >= 0; i--) {
                revCategoryList.add(categoryIdList.get(i));//列表倒序
            }
            admEditProductShelfVo.getCategoryIdList().add(revCategoryList);
        }
        return admEditProductShelfVo;
    }

    public List<Long> getCategoryIdList(CategoryVo categoryVo, List<Long> categoryList) {
        categoryList.add(categoryVo.getId());
        if (categoryVo.getParentId() == 0) {
            return categoryList;
        } else {
            CategoryVo cv = categoryService.getCategoryById(categoryVo.getParentId());
            if (StringUtils.isEmpty(cv)) {
                throw new ServiceException("已选前台类目数据异常");
            }
            return getCategoryIdList(cv, categoryList);
        }
    }

    @Override
    public AdmToAddProductShelfVo admToAddProductShelfPage() {
        AdmToAddProductShelfVo admToAddProductShelfVo = new AdmToAddProductShelfVo();
        if (!StringUtils.isEmpty(moduleListJsonStr)) {
            String moduleListStr = SkuPropertyUtil.transformProperty(moduleListJsonStr);
            List<BaseKeyValueVo> baseKeyValueVoList = JSONArray.parseArray(moduleListStr, BaseKeyValueVo.class);
            admToAddProductShelfVo.setModuleList(baseKeyValueVoList);
        }
        if (!StringUtils.isEmpty(integralTypeListJsonStr)) {
            String integralTypeListStr = SkuPropertyUtil.transformProperty(integralTypeListJsonStr);
            List<BaseKeyValueVo> baseKeyValueVoList = JSONArray.parseArray(integralTypeListStr, BaseKeyValueVo.class);
            admToAddProductShelfVo.setIntegralTypeList(baseKeyValueVoList);
        }
        if (!StringUtils.isEmpty(sourceListJsonStr)) {
            String sourceListStr = SkuPropertyUtil.transformProperty(sourceListJsonStr);
            List<BaseKeyValueVo> baseKeyValueVoList = JSONArray.parseArray(sourceListStr, BaseKeyValueVo.class);
            admToAddProductShelfVo.setSourceList(baseKeyValueVoList);
        }
        return admToAddProductShelfVo;
    }

    @Override
    public List<AdmProductShelfSkuVo> getAdmShelfSkuInfoList(Long shelfId) {
        List<AdmProductShelfSkuVo> admProductShelfSkuVoList = productShelfMapper.selectAdmShelfSkuInfoList(shelfId);
        for (AdmProductShelfSkuVo admProductShelfSkuVo : admProductShelfSkuVoList) {
            String skuProperty = SkuPropertyUtil.transformProperty(admProductShelfSkuVo.getSkuProperty());
            List<BaseKeyValueVo> baseKeyValueVoList = JSONArray.parseArray(skuProperty, BaseKeyValueVo.class);
            admProductShelfSkuVo.setSkuPropertyList(baseKeyValueVoList);
        }
        return admProductShelfSkuVoList;
    }

    @Override
    public MyPageInfo<BaseShelfVo> getProductShelfListForPage(AdmSelectProductShelfDto admSelectProductShelfDto) {
        PageHelper.startPage(admSelectProductShelfDto.getCurrentPage(), admSelectProductShelfDto.getPageSize());
        List<BaseShelfVo> baseShelfVoList = productShelfMapper.selectProductShelfList(admSelectProductShelfDto);
        MyPageInfo<BaseShelfVo> pageInfo = new MyPageInfo<>(baseShelfVoList);
        return pageInfo;
    }

    /**
     * 1、查询所有商品
     * 2、查询商品的的分类
     * 3、这个api,暂时暂停,因为es暂时无法提供
     *
     * @return
     */
    @Override
    public List<ProductShelfVo> findProductShelf() {
        ProductShelfDto productShelfDto = new ProductShelfDto();
        List<ProductShelfVo> productShelfList = productShelfMapper.findProductShelf(productShelfDto);
        Map<Long, List<CategoryVo>> categoryGroupMap =
                shelfCategoryRefService.findByCategory(null).stream().collect(Collectors.groupingBy(CategoryVo::getShelfId));
        productShelfList.stream().forEach(result -> {
            List<CategoryVo> categoryVos = categoryGroupMap.get(result.getShelfId());
            if (categoryVos != null) {
                List<String> categoryList = categoryVos.stream().map(categoryVo -> categoryVo.getTitle()).collect(Collectors.toList());
                result.setCategoryList(categoryList);
            }
        });
        return productShelfList;
    }

    /**
     * 根据商品名字进行搜索
     *
     * @param productShelfDto
     * @return
     */
    @Override
    public MyPageInfo<ProductShelfVo> searchProduct(ProductShelfDto productShelfDto) {
        PageHelper.startPage(productShelfDto.getCurrentPage(), productShelfDto.getPageSize());
        List<ProductShelfVo> productShelfVoList = this.productShelfMapper.findProductShelf(productShelfDto);
        for (ProductShelfVo productShelfVo : productShelfVoList) {
            //获取商品积分列表
            List<ShelfBeanRef> shelfBeanList = shelfBeanRefService.getShelfBean(productShelfVo.getShelfId(), null);
            for (ShelfBeanRef shelfBeanRef : shelfBeanList) {
                ShelfBeanVo shelfBeanVo = new ShelfBeanVo();
                shelfBeanVo.setBeanType(shelfBeanRef.getBeanType());
                shelfBeanVo.setShelfId(shelfBeanRef.getShelfId());
                shelfBeanVo.setUnit(shelfBeanRef.getUnit());
                shelfBeanVo.setValue(shelfBeanRef.getValue());
                productShelfVo.getShelfBeanVoList().add(shelfBeanVo);
            }
        }
        productShelfVoList.stream().forEach(result -> {
            List<CategoryVo> categoryVos = shelfCategoryRefService.findByCategory(result.getShelfId());
            if (!categoryVos.isEmpty()) {
                result.setCategoryList(categoryVos.stream().map(categoryVo -> categoryVo.getTitle()).collect(Collectors.toList()));
            }
            if (!StringUtils.isEmpty(result.getSource())) {
                result.setSourceText(Constants.SourceType.getMapTypeText().get(result.getSource()));
            }
        });
        MyPageInfo<ProductShelfVo> productShelfMyPageInfo = new MyPageInfo(productShelfVoList);

        return productShelfMyPageInfo;
    }

    @Override
    public List<String> searchShelfName(ProductShelfDto productShelfDto) {
        PageHelper.startPage(productShelfDto.getCurrentPage(), productShelfDto.getPageSize());
        List<ProductShelf> productShelfList = productShelfMapper.findByShelfName(productShelfDto);
        List<String> shelfName = productShelfList.stream().map(result -> result.getProductShelfName()).collect(Collectors.toList());
        return shelfName;
    }

    @Override
    public ApiResult shelf(ShelfProductDto shelfProductDto) {
        if (StringUtils.isEmpty(shelfProductDto.getShelfId())) {
            throw new ServiceException("未指定商品");
        }
        if (StringUtils.isEmpty(shelfProductDto.getStatus())) {
            throw new ServiceException("未指定状态");
        }
        if (!Constants.ShelfStatus.SHELF.equals(shelfProductDto.getStatus()) &&
                !Constants.ShelfStatus.UNSHELF.equals(shelfProductDto.getStatus())) {
            throw new ServiceException("参数错误");
        }
        ProductShelf productShelf = new ProductShelf();
        productShelf.setId(shelfProductDto.getShelfId());
        productShelf.setStatus(shelfProductDto.getStatus());
        productShelf.setModifyTime(new Date());
        productShelfMapper.updateByPrimaryKeySelective(productShelf);
        return ApiResult.success();
    }

    @Override
    public ApiResult batchShelf(List<ShelfProductDto> shelfProductDtos) {
        for (ShelfProductDto shelfProductDto : shelfProductDtos) {
            this.shelf(shelfProductDto);
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult batchSoftDelete(List<BatchDeleteShelfDto> batchDeleteShelfDtos) {
        for (BatchDeleteShelfDto batchDeleteShelfDto : batchDeleteShelfDtos) {
            if (StringUtils.isEmpty(batchDeleteShelfDto.getShelfId())) {
                throw new ServiceException("未指定商品");
            }
            this.softDeleteById(batchDeleteShelfDto.getShelfId());
        }
        return ApiResult.success();
    }

    @Override
    public boolean checkShelf(Long productId) {
        if (StringUtils.isEmpty(productId)) {
            throw new ServiceException("参数异常");
        }
        Condition condition = new Condition(ProductShelf.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("productId", productId);
        criteria.andEqualTo("status", Constants.ProductShelfStatus.SHELF);
        criteria.andEqualTo("flag", Constants.Flag.VALID);
        int i = productShelfMapper.selectCountByCondition(condition);
        return i > 0 ? true : false;
    }


    public static void main(String[] args) {
        /*String a="[{\"颜色\":\"红色\"},{\"尺码\":\"25\"}]";
        JSONArray jsonArray = JSONArray.parseArray(a);
        Set<String> keys = jsonArray.getJSONObject(0).keySet();
        Iterator<String> iterator = keys.iterator();
        String key = iterator.next();
        System.out.println(key);
        System.out.println("aaa");*/

        String b = "{\"颜色\":\"红色\",\"尺码\":\"25\"}";
        JSONObject jsonObject = JSONObject.parseObject(b);
        Set<String> keys = jsonObject.keySet();
        System.out.println(keys);
        Collection<Object> values = jsonObject.values();
        System.out.println(values);
        Iterator<String> iterator = keys.iterator();


        String c = "{\"颜色\":\"黑色\",\"尺码\":\"27\"}";
        JSONObject jsonObject2 = JSONObject.parseObject(c);

        String arr = "[{\"name\":\"颜色\",\"value\":\"红,黑\"},{\"name\":\"尺寸\",\"value\":\"35,36\"}]";
        JSONArray jsonArray = JSONArray.parseArray(arr);
        List<Map> mapList = new ArrayList<>();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = jsonObject.getString(key) + "," + jsonObject2.getString(key);
            System.out.println(key + ":" + value);
            Map map = new HashMap();
            map.put("name", key);
            map.put("value", value);
            mapList.add(map);
        }
        System.out.println(mapList);
    }
}
