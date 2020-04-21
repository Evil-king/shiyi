package com.baibei.shiyi.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.NoUtil;
import com.baibei.shiyi.common.tool.utils.SkuPropertyUtil;
import com.baibei.shiyi.product.dao.*;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.*;
import com.baibei.shiyi.product.model.*;
import com.baibei.shiyi.product.service.*;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.github.pagehelper.PageHelper;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.persistence.Id;
import java.util.*;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: Product服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductServiceImpl extends AbstractService<Product> implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private IProductShelfService productShelfService;
    @Autowired
    private IProductSkuService productSkuService;
    @Autowired
    private IProTypeService proTypeService;
    @Autowired
    private IProductStockService productStockService;
    @Autowired
    private IProductImgService productImgService;
    @Autowired
    private  IProductContentService productContentService;
    @Autowired
    private ProductImgMapper productImgMapper;
    @Autowired
    private IBrandService brandService;
    @Autowired
    private ProductSkuMapper productSkuMapper;
    @Autowired
    private ProductContentMapper productContentMapper;
    @Autowired
    private ProductStockMapper productStockMapper;
    @Autowired
    private IPropertyKeyService propertyKeyService;
    @Autowired
    private IParameterKeyService parameterKeyService;
    @Override
    public List<ProductParamVo> getParams(ShelfRefDto shelfRefDto) {
        ProductShelf shelf = productShelfService.getShelfById(shelfRefDto);
        if (StringUtils.isEmpty(shelf)) {
            throw new ServiceException("获取不到指定上架商品");
        }
        List<ProductParamVo> productParamVoList = new ArrayList<>();
        Product product=null;
        Condition condition = new Condition(Product.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(shelf.getProductId())) {
            criteria.andEqualTo("id",shelf.getProductId());
        }
        if (!StringUtils.isEmpty(shelf.getSpuNo())) {
            criteria.andEqualTo("spuNo",shelf.getSpuNo());
        }
        List<Product> productList = productMapper.selectByCondition(condition);
        if (productList!=null&&productList.size()==1) {
            product = productList.get(0);
        }
        if (!StringUtils.isEmpty(productList)&&productList.size()>1) {
            throw new ServiceException("should select one but more");
        }
        if (!StringUtils.isEmpty(product)) {
            String parameter = product.getParameter();
            if (!StringUtils.isEmpty(parameter)) {
                JSONObject jsonObject = JSONObject.parseObject(parameter);
                Set<String> keySet = jsonObject.keySet();
                Iterator<String> keyIterator = keySet.iterator();
                while (keyIterator.hasNext()) {
                    ProductParamVo productParamVo = new ProductParamVo();
                    String key = keyIterator.next();
                    String value = jsonObject.getString(key);
                    productParamVo.setName(key);
                    productParamVo.setValue(value);
                    productParamVoList.add(productParamVo);
                }
            }
        }
        return productParamVoList;
    }

    @Override
    public MyPageInfo<AdmProductVo> pageList(AdmProductDto admProductDto) {
        PageHelper.startPage(admProductDto.getCurrentPage(), admProductDto.getPageSize());
        PageHelper.orderBy("p.create_time desc,id");
        List<AdmProductVo> productVoList = productMapper.selectList(admProductDto);
        MyPageInfo<AdmProductVo> pageInfo = new MyPageInfo<>(productVoList);
        return pageInfo;
    }

    @Override
    public void addProduct(AddProductDto addProductDto) {
        if (addProductDto.getProductImgs().size()==0) {
            throw new ServiceException("请上传商品图片");
        }
        if (StringUtils.isEmpty(addProductDto.getMainImg())) {
            throw new ServiceException("请上传商品主图");
        }
        if (addProductDto.getProductSkuDtoList().size()==0) {
            throw new ServiceException("请添加商品属性");
        }
        //根据货号查询商品
        Product etProduct = this.findBySpuNo(addProductDto.getSpuNo());
        if (!StringUtils.isEmpty(etProduct)) {
            throw new ServiceException("重复货号");
        }
        Date currentDate = new Date();
        //根据后台类目id获取库存单位信息
        ProType proType = proTypeService.findById(addProductDto.getTypeId());
        if (StringUtils.isEmpty(proType)) {
            throw new ServiceException("添加失败，未找到指定的后台类目");
        }
        //查询品牌
        Brand brand = brandService.findById(addProductDto.getBrandId());
        if (StringUtils.isEmpty(brand)||Constants.Flag.UNVALID.equals(brand.getFlag().toString())) {
            throw new ServiceException("添加失败，未找到指定的品牌");
        }

        //将属性的可编辑状态置为不可编辑状态
        for (Long proId : addProductDto.getPropIds()) {
            propertyKeyService.modifyEditFlag(proId,Constants.EditFlag.UNALLOW);
        }

        //将参数的可编辑状态置为不可编辑状态
        for (Long paramId : addProductDto.getParamIds()) {
            parameterKeyService.modifyEditFlag(paramId,Constants.EditFlag.UNALLOW);
        }

        //添加商品基础信息
        Long productId=IdWorker.getId();
        Product product = new Product();
        product.setId(productId);
        product.setSpuNo(addProductDto.getSpuNo());
        product.setProductName(addProductDto.getProductName());
        product.setProductDesc(addProductDto.getProductDesc());
        product.setTypeId(addProductDto.getTypeId());
        product.setBrandId(addProductDto.getBrandId());
        product.setBrandTitle(brand.getTitle());
        product.setProductImg(addProductDto.getMainImg());
        product.setCommonSellCount(addProductDto.getCommonSellCount());
        if (!StringUtils.isEmpty(addProductDto.getSelectedSkuList())&&addProductDto.getSelectedSkuList().size()>0) {
            product.setSelectedSku(JSONArray.toJSONString(addProductDto.getSelectedSkuList()));
        }
        if(addProductDto.getParameterList().size()>0){
            List<BaseKeyValueDto> parmList = new ArrayList<>();
            for (BaseKeyValueDto baseKeyValueDto : addProductDto.getParameterList()) {
                if (!StringUtils.isEmpty(baseKeyValueDto.getKey())&&!StringUtils.isEmpty(baseKeyValueDto.getValue())) {
                    parmList.add(baseKeyValueDto);
                }
            }
            if (parmList.size()>0) {
                String paramsJsonStr = JSONArray.toJSONString(parmList);
                String params = SkuPropertyUtil.getParams(paramsJsonStr);
                product.setParameter(params);
            }
        }
        product.setCreateTime(currentDate);
        product.setModifyTime(currentDate);
        product.setFlag(new Byte(Constants.Flag.VALID));
        productMapper.insertSelective(product);
        //添加商品图片
        for (int i = 0; i < addProductDto.getProductImgs().size(); i++) {
            ProductImg productImg = new ProductImg();
            productImg.setId(IdWorker.getId());
            productImg.setImgUrl(addProductDto.getProductImgs().get(i));
            productImg.setProductId(productId);
            productImg.setSeq(i+1);
            productImg.setHot(i==0?"1":"0");
            productImg.setCreateTime(currentDate);
            productImg.setModifyTime(currentDate);
            productImg.setFlag(new Byte(Constants.Flag.VALID));
            productImgMapper.insertSelective(productImg);
        }
        //添加图文详情
        ProductContent productContent = new ProductContent();
        productContent.setId(IdWorker.getId());
        productContent.setContent(addProductDto.getContent());
        productContent.setProductId(productId);
        productContent.setCreateTime(currentDate);
        productContent.setModifyTime(currentDate);
        productContent.setFlag(new Byte(Constants.Flag.VALID));
        productContentMapper.insertSelective(productContent);

        List<AddProductSkuDto> productSkuDtoList = addProductDto.getProductSkuDtoList();
        for (int i = 0; i < productSkuDtoList.size(); i++) {
            if(StringUtils.isEmpty(addProductDto.getProductSkuDtoList().get(i).getStock())){
                throw new ServiceException("库存不能为空");
            }
            //查询是都已有存在的skuNo
            ProductSku etSku = productSkuService.getBySkuNo(productSkuDtoList.get(i).getSkuNo());
            if (!StringUtils.isEmpty(etSku)) {
                throw new ServiceException("重复规格编码"+productSkuDtoList.get(i).getSkuNo());
            }
            ProductSku productSku = new ProductSku();
            Long skuId = IdWorker.getId();
            productSku.setId(skuId);
            productSku.setProductId(productId);
            productSku.setSpuNo(addProductDto.getSpuNo());
            productSku.setSkuNo(productSkuDtoList.get(i).getSkuNo());
            if (!StringUtils.isEmpty(addProductDto.getSelectedSkuList())&&addProductDto.getSelectedSkuList().size()>0) {
                String skuPropertyJsonStr = JSONArray.toJSONString(productSkuDtoList.get(i).getSkuPropertyList());
                String skuProperty = SkuPropertyUtil.getSkuProperty(skuPropertyJsonStr);
                productSku.setSkuProperty(skuProperty);
            }
            productSku.setSeq(i+1);
            productSku.setCreateTime(currentDate);
            productSku.setModifyTime(currentDate);
            productSku.setFlag(new Byte(Constants.Flag.VALID));
            //添加商品属性关联
            productSkuMapper.insertSelective(productSku);
            //新增库存
            InitStockDto initStockDto = new InitStockDto();
            initStockDto.setProductId(productId);
            initStockDto.setSkuId(skuId);
            initStockDto.setSpuNo(addProductDto.getSpuNo());
            initStockDto.setStock(addProductDto.getProductSkuDtoList().get(i).getStock());
            initStockDto.setUnit(proType.getStockUnit());
            initStockDto.setOperatorNo(addProductDto.getUserId());
            productStockService.initStock(initStockDto);
        }
    }

    @Override
    public void editProduct(AddProductDto addProductDto) {
        if (addProductDto.getProductImgs().size()==0) {
            throw new ServiceException("请上传商品图片");
        }
        if (StringUtils.isEmpty(addProductDto.getMainImg())) {
            throw new ServiceException("请上传商品主图");
        }
        if (addProductDto.getProductSkuDtoList().size()==0) {
            throw new ServiceException("请添加商品属性");
        }
        if (StringUtils.isEmpty(addProductDto.getProductId())) {
            throw new ServiceException("编辑失败");
        }
        Date currentDate = new Date();
        //根据后台类目id获取库存单位信息
        ProType proType = proTypeService.findById(addProductDto.getTypeId());
        if (StringUtils.isEmpty(proType)) {
            throw new ServiceException("添加失败，未找到指定的后台类目");
        }
        //查询品牌
        Brand brand = brandService.findById(addProductDto.getBrandId());
        if (StringUtils.isEmpty(brand)||Constants.Flag.UNVALID.equals(brand.getFlag().toString())) {
            throw new ServiceException("添加失败，未找到指定的品牌");
        }
        //更新商品基础信息
        Long productId=addProductDto.getProductId();
        Product product = new Product();
        product.setId(productId);
        /*product.setSpuNo(addProductDto.getSpuNo());*/
        product.setProductName(addProductDto.getProductName());
        product.setProductDesc(addProductDto.getProductDesc());
        product.setTypeId(addProductDto.getTypeId());
        product.setBrandId(addProductDto.getBrandId());
        product.setBrandTitle(brand.getTitle());
        product.setProductImg(addProductDto.getMainImg());
        product.setCommonSellCount(addProductDto.getCommonSellCount());
        if (!StringUtils.isEmpty(addProductDto.getSelectedSkuList())&&addProductDto.getSelectedSkuList().size()>0) {
            product.setSelectedSku(JSONArray.toJSONString(addProductDto.getSelectedSkuList()));
        }
        if(addProductDto.getParameterList().size()>0){
            List<BaseKeyValueDto> parmList = new ArrayList<>();
            for (BaseKeyValueDto baseKeyValueDto : addProductDto.getParameterList()) {
                if (!StringUtils.isEmpty(baseKeyValueDto.getKey())&&!StringUtils.isEmpty(baseKeyValueDto.getValue())) {
                    parmList.add(baseKeyValueDto);
                }
            }
            if (parmList.size()>0) {
                String paramsJsonStr = JSONArray.toJSONString(parmList);
                String params = SkuPropertyUtil.getParams(paramsJsonStr);
                product.setParameter(params);
            }
        }else{
            product.setParameter("");
        }
        product.setModifyTime(currentDate);
        productMapper.updateByPrimaryKeySelective(product);
        //软删除商品原有图片
        productImgService.deleteImgsByProductId(addProductDto.getProductId());
        //重新添加图片
        for (int i = 0; i < addProductDto.getProductImgs().size(); i++) {
            ProductImg productImg = new ProductImg();
            productImg.setId(IdWorker.getId());
            productImg.setImgUrl(addProductDto.getProductImgs().get(i));
            productImg.setProductId(productId);
            productImg.setSeq(i+1);
            productImg.setHot(i==0?"1":"0");
            productImg.setCreateTime(currentDate);
            productImg.setModifyTime(currentDate);
            productImg.setFlag(new Byte(Constants.Flag.VALID));
            productImgMapper.insertSelective(productImg);
        }
        //更新图文详情
        ProductContent productContent = new ProductContent();
        productContent.setContent(addProductDto.getContent());
        productContent.setProductId(productId);
        productContent.setModifyTime(currentDate);
        productContentService.modifyContent(productContent);

        //属性和库存相关
        //获取现有的sku信息
        List<ProductSku> skuList = productSkuService.getSkuList(addProductDto.getProductId());
        Map<String,String> exitSkuMap=new HashMap();//数据库已存在的sku
        for (ProductSku productSku : skuList) {
            exitSkuMap.put(productSku.getSkuNo(),productSku.getId()+"");
        }
        Map<String,String> currentSkuMap = new HashMap();//前端传过来的sku
        for (AddProductSkuDto addProductSkuDto : addProductDto.getProductSkuDtoList()) {
            currentSkuMap.put(addProductSkuDto.getSkuNo(),addProductSkuDto.getSkuNo());
        }
        //删除sku
        for (ProductSku productSku : skuList) {
           if(!currentSkuMap.containsKey(productSku.getSkuNo())){
               productSkuService.deleteSku(null,productSku.getSkuNo());
           }
        }
        for (int i = 0; i < addProductDto.getProductSkuDtoList().size(); i++) {
            if (exitSkuMap.containsKey(addProductDto.getProductSkuDtoList().get(i).getSkuNo())) {//更新
                ProductSku productSku = new ProductSku();
                if (!StringUtils.isEmpty(addProductDto.getSelectedSkuList())&&addProductDto.getSelectedSkuList().size()>0) {
                    String skuPropertyJsonStr = JSONArray.toJSONString(addProductDto.getProductSkuDtoList().get(i).getSkuPropertyList());
                    String skuProperty = SkuPropertyUtil.getSkuProperty(skuPropertyJsonStr);
                    productSku.setSkuProperty(skuProperty);
                }
                productSku.setSeq(i+1);
                productSku.setId(Long.parseLong(exitSkuMap.get(addProductDto.getProductSkuDtoList().get(i).getSkuNo())));
                //更新sku
                productSkuMapper.updateByPrimaryKeySelective(productSku);
                //查询原有库存

                ProductStock productStock = new ProductStock();
                productStock.setSkuId(Long.parseLong(exitSkuMap.get(addProductDto.getProductSkuDtoList().get(i).getSkuNo())));
                productStock.setProductId(addProductDto.getProductId());
                ProductStock stock = productStockService.getStock(productStock);
                //更新库存
                if (addProductDto.getProductSkuDtoList().get(i).getStock().compareTo(stock.getStock())!=0) {
                    ChangeStockDto changeStockDto = new ChangeStockDto();
                    changeStockDto.setProductId(addProductDto.getProductId());
                    changeStockDto.setSkuId(Long.parseLong(exitSkuMap.get(addProductDto.getProductSkuDtoList().get(i).getSkuNo())));
                    if (addProductDto.getProductSkuDtoList().get(i).getStock().compareTo(stock.getStock())>0) {
                        changeStockDto.setChangeCount(addProductDto.getProductSkuDtoList().get(i).getStock().subtract(stock.getStock()));
                        changeStockDto.setRetype(Constants.Retype.IN);
                    }else{
                        changeStockDto.setChangeCount(stock.getStock().subtract(addProductDto.getProductSkuDtoList().get(i).getStock()));
                        changeStockDto.setRetype(Constants.Retype.OUT);
                    }
                    changeStockDto.setOrderNo(NoUtil.getRamdomOrderNo());
                    changeStockDto.setChangeType(Constants.ProductStockChangeType.SYS);
                    changeStockDto.setOperatorNo(addProductDto.getUserId());
                    changeStockDto.setChangeSellCountFlag(false);//不修改销量
                    productStockService.changeStock(changeStockDto);
                }

            }else{//新增
                //查询是都已有存在的skuNo
                ProductSku etSku = productSkuService.getBySkuNo(addProductDto.getProductSkuDtoList().get(i).getSkuNo());
                if (!StringUtils.isEmpty(etSku)) {
                    throw new ServiceException("重复规格编码"+addProductDto.getProductSkuDtoList().get(i).getSkuNo());
                }
                //新增sku
                ProductSku productSku = new ProductSku();
                Long skuId = IdWorker.getId();
                productSku.setId(skuId);
                productSku.setProductId(productId);
                productSku.setSpuNo(addProductDto.getSpuNo());
                productSku.setSkuNo(addProductDto.getProductSkuDtoList().get(i).getSkuNo());
                if (!StringUtils.isEmpty(addProductDto.getSelectedSkuList())&&addProductDto.getSelectedSkuList().size()>0) {
                    String skuPropertyJsonStr = JSONArray.toJSONString(addProductDto.getProductSkuDtoList().get(i).getSkuPropertyList());
                    String skuProperty = SkuPropertyUtil.getSkuProperty(skuPropertyJsonStr);
                    productSku.setSkuProperty(skuProperty);
                }
                productSku.setSeq(i+1);
                productSku.setCreateTime(currentDate);
                productSku.setModifyTime(currentDate);
                productSku.setFlag(new Byte(Constants.Flag.VALID));
                //添加商品属性关联
                productSkuMapper.insertSelective(productSku);

                InitStockDto initStockDto = new InitStockDto();
                initStockDto.setProductId(addProductDto.getProductId());
                initStockDto.setSkuId(skuId);
                initStockDto.setSpuNo(addProductDto.getSpuNo());
                initStockDto.setStock(addProductDto.getProductSkuDtoList().get(i).getStock());
                initStockDto.setUnit(proType.getStockUnit());
                initStockDto.setOperatorNo(addProductDto.getUserId());
                productStockService.initStock(initStockDto);
            }
        }
    }

    @Override
    public void addOrEditProduct(AddProductDto addProductDto) {
        if (StringUtils.isEmpty(addProductDto.getProductId())) {
            this.addProduct(addProductDto);
        }else{
            this.editProduct(addProductDto);
        }
    }

    @Override
    public List<Product> findByTypeId(Long typeId) {
        Condition condition=new Condition(Product.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("typeId",typeId);
        return productMapper.selectByCondition(condition);
    }

    @Override
    public Product findBySpuNo(String spuNo) {
        if (StringUtils.isEmpty(spuNo)) {
            throw new ServiceException("非法查询，未指定商品货号");
        }
        Condition condition=new Condition(Product.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("spuNo",spuNo);
        criteria.andEqualTo("flag",1);
        List<Product> productList = productMapper.selectByCondition(condition);
        if (productList.size()>1) {
            throw new ServiceException("非法查询");
        }
        return productList.size()==0?null:productList.get(0);
    }

    @Override
    public AdmEditProductVo editProductPage(Long productId) {
        if (StringUtils.isEmpty(productId)) {
            throw new ServiceException("未指定商品");
        }
        AdmEditProductVo admEditProductVo = productMapper.selectEditProductInfo(productId);
        if (StringUtils.isEmpty(admEditProductVo)) {
            throw new ServiceException("未找到指定商品");
        }
        //查询商品图片
        List<ProductImg> productImgList = productImgService.getProductImgs(productId);
        for (ProductImg productImg : productImgList) {
            admEditProductVo.getProductImgs().add(productImg.getImgUrl());
        }
        //属性
        if (StringUtils.isEmpty(admEditProductVo.getSelectedSkuText())) {
            admEditProductVo.setHasSkuFlag(Constants.HasSkuFlag.NOTHAVE);
        }else{
            List<BaseKeyValueVo> selectedSkuList = JSONArray.parseArray(admEditProductVo.getSelectedSkuText(), BaseKeyValueVo.class);
            admEditProductVo.setSelectedSkuList(selectedSkuList);
            admEditProductVo.setHasSkuFlag(Constants.HasSkuFlag.HAVE);
        }
        //查询商品sku
        List<AdmEditProductSkuVo> skuStockList = productSkuService.getSkuStockList(productId);
        for (AdmEditProductSkuVo admEditProductSkuVo : skuStockList) {
            if (!StringUtils.isEmpty(admEditProductSkuVo.getSkuProperty())) {
                String skuProperty = SkuPropertyUtil.transformProperty(admEditProductSkuVo.getSkuProperty());
                List<BaseKeyValueDto> skuPropertyList = JSONArray.parseArray(skuProperty, BaseKeyValueDto.class);
                admEditProductSkuVo.setSkuPropertyList(skuPropertyList);
            }
        }
        admEditProductVo.setProductSkuDtoList(skuStockList);
        //参数
        if (!StringUtils.isEmpty(admEditProductVo.getParameterText())) {
            String params = SkuPropertyUtil.transformProperty(admEditProductVo.getParameterText());
            List<BaseKeyValueVo> parameterList = JSONArray.parseArray(params, BaseKeyValueVo.class);
            admEditProductVo.setParameterList(parameterList);
        }
        return admEditProductVo;
    }

    @Override
    public ApiResult softDeleteProduct(Long productId) {
        //查询商品是否已上架
        boolean b = productShelfService.checkShelf(productId);
        if(b){
            return ApiResult.error("商品已上架，不能删除");
        }
        if (StringUtils.isEmpty(productId)) {
            return ApiResult.error("删除失败，未指定商品id");
        }
        this.softDeleteById(productId);
        //删除商品关联的规格编码
        productSkuService.deleteSku(productId,null);
        return ApiResult.success();
    }

    @Override
    public void batchSoftDeleteProduct(List<AdmDeleteProductDto> admDeleteProductDtoList) {
        if (StringUtils.isEmpty(admDeleteProductDtoList)||admDeleteProductDtoList.size()==0) {
            throw new ServiceException("未选择商品");
        }
        for (AdmDeleteProductDto admDeleteProductDto : admDeleteProductDtoList) {
            if (!StringUtils.isEmpty(admDeleteProductDto.getProductId())) {
                this.softDeleteProduct(admDeleteProductDto.getProductId());
            }
        }
    }

    @Override
    public List<BaseProductSkuVo> getProductSkuListBySpuNo(String spuNo) {
        if (StringUtils.isEmpty(spuNo)) {
            throw new ServiceException("商品货号不能为空");
        }
        List<BaseProductSkuVo> baseProductSkuVoList=productSkuMapper.selectProductSkuList(spuNo);
        for (BaseProductSkuVo baseProductSkuVo : baseProductSkuVoList) {
            if(!StringUtils.isEmpty(baseProductSkuVo.getSkuProperty())){
                String skuProperty = SkuPropertyUtil.transformProperty(baseProductSkuVo.getSkuProperty());
                List<BaseKeyValueVo> baseKeyValueVoList = JSONArray.parseArray(skuProperty, BaseKeyValueVo.class);
                baseProductSkuVo.setSkuPropertyList(baseKeyValueVoList);
            }
        }
        return baseProductSkuVoList;
    }
}
