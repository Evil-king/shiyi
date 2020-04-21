package com.baibei.shiyi.product.service.impl;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.NoUtil;
import com.baibei.shiyi.common.tool.utils.NumberUtil;
import com.baibei.shiyi.product.dao.ProductStockMapper;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.message.ChangeSellCountMessage;
import com.baibei.shiyi.product.model.ProductShelf;
import com.baibei.shiyi.product.model.ProductStock;
import com.baibei.shiyi.product.model.StockRecord;
import com.baibei.shiyi.product.service.IProductShelfService;
import com.baibei.shiyi.product.service.IProductStockService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.product.service.IStockRecordService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author: Longer
 * @date: 2019/07/30 10:12:45
 * @description: ProductStock服务实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductStockServiceImpl extends AbstractService<ProductStock> implements IProductStockService {

    @Autowired
    private ProductStockMapper productStockMapper;
    @Autowired
    private IProductShelfService productShelfService;
    @Autowired
    private IStockRecordService stockRecordService;


    @Override
    public ApiResult changeStock(ChangeStockDto changeStockDto) {
        log.info("修改库存订单号："+changeStockDto.getOrderNo()+"，修改数量："+changeStockDto.getChangeCount()+"，修改类型："+changeStockDto.getRetype());
        //参数校验
        validateParam(changeStockDto);
        //step1.扣库存（乐观锁）
        ProductShelf productShelf = new ProductShelf();
        if(!StringUtils.isEmpty(changeStockDto.getShelfId())){
            ShelfRefDto shelfRefDto = new ShelfRefDto();
            shelfRefDto.setShelfId(changeStockDto.getShelfId());
            productShelf = productShelfService.getShelfById(shelfRefDto);
            if (StringUtils.isEmpty(productShelf)) {
                throw new ServiceException("未找到指定上架商品");
            }
            changeStockDto.setProductId(productShelf.getProductId());
        }
        ProductStock productStock = new ProductStock();
        productStock.setSkuId(changeStockDto.getSkuId());
        productStock.setProductId(changeStockDto.getProductId());
        ProductStock stock = this.getStock(productStock);
        if (StringUtils.isEmpty(stock)) {
            throw new ServiceException("获取不到指定库存信息");
        }
        if (Constants.Retype.OUT.equals(changeStockDto.getRetype())&&//扣减库存
                stock.getStock().compareTo(changeStockDto.getChangeCount()) < 0) {
            throw new ServiceException("库存不足");
        }
        //更改库存
        BigDecimal changeCount = BigDecimal.ZERO;//更改数量。
        BigDecimal sellCount = BigDecimal.ZERO;
        if(Constants.Retype.OUT.equals(changeStockDto.getRetype())){//扣减库存
            changeCount=new BigDecimal(0).subtract(changeStockDto.getChangeCount());//负数
            if(changeStockDto.isChangeSellCountFlag()){
                sellCount=changeStockDto.getChangeCount();//正数
            }
        }
        if(Constants.Retype.IN.equals(changeStockDto.getRetype())){//新增库存
            changeCount=changeStockDto.getChangeCount();
            if(changeStockDto.isChangeSellCountFlag()){
                sellCount=new BigDecimal(0).subtract(changeStockDto.getChangeCount());//负数
            }
        }
        int i = productStockMapper.changeStock(changeStockDto.getProductId(),changeStockDto.getSkuId(),changeCount,sellCount);
        if (i == 0) {
            throw new ServiceException("库存不足");
        }
        //step2.增加扣库存流水
        StockRecord stockRecord = new StockRecord();
        stockRecord.setId(IdWorker.getId());
        stockRecord.setRecordNo(IdWorker.get32UUID());
        stockRecord.setOrderNo(changeStockDto.getOrderNo());
        stockRecord.setStockId(stock.getId());
        stockRecord.setProductId(changeStockDto.getProductId());
        stockRecord.setSkuId(changeStockDto.getSkuId());
        stockRecord.setOperatorNo(changeStockDto.getOperatorNo());
        stockRecord.setChangeType(changeStockDto.getChangeType());
        stockRecord.setRetype(changeStockDto.getRetype());
        stockRecord.setBeforeCount(stock.getStock());
        stockRecord.setChangeCount(changeCount);
        stockRecord.setAfterCount(stock.getStock().add(changeCount));
        stockRecord.setRemark(changeStockDto.getRemark());
        stockRecordService.save(stockRecord);
        return ApiResult.success();
    }

    @Override
    public ApiResult batchChangeStock(List<ChangeStockDto> changeStockDtoList) {
        for (ChangeStockDto changeStockDto : changeStockDtoList) {
            this.changeStock(changeStockDto);
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult initStock(InitStockDto initStockDto) {
        //新增库存
        ProductStock productStock = BeanUtil.copyProperties(initStockDto, ProductStock.class);
        productStock.setId(IdWorker.getId());
        productStockMapper.insertSelective(productStock);
        //新增流水
        StockRecord stockRecord = new StockRecord();
        stockRecord.setId(IdWorker.getId());
        stockRecord.setRecordNo(IdWorker.get32UUID());
        stockRecord.setOrderNo(NoUtil.getRamdomOrderNo());
        stockRecord.setStockId(productStock.getId());
        stockRecord.setProductId(productStock.getProductId());
        stockRecord.setSkuId(productStock.getSkuId());
        stockRecord.setOperatorNo(initStockDto.getOperatorNo());
        stockRecord.setChangeType(Constants.ProductStockChangeType.INIT);
        stockRecord.setRetype(Constants.Retype.IN);
        stockRecord.setBeforeCount(new BigDecimal(0));
        stockRecord.setChangeCount(initStockDto.getStock());
        stockRecord.setAfterCount(initStockDto.getStock());
        stockRecord.setRemark(initStockDto.getRemark());
        stockRecordService.save(stockRecord);
        return ApiResult.success();
    }

    @Override
    public ApiResult changeSellCount(ChangeSellCountMessage changeSellCountMessage) {
        if (StringUtils.isEmpty(changeSellCountMessage.getShelfId())) {
            throw new ServiceException("未指定商品");
        }
        if (StringUtils.isEmpty(changeSellCountMessage.getSkuId())) {
            throw new ServiceException("规格ID不能为空");
        }
        if (StringUtils.isEmpty(changeSellCountMessage.getRetype())) {
            throw new ServiceException("未指定方向");
        }
        if (StringUtils.isEmpty(changeSellCountMessage.getChangeAmount())||changeSellCountMessage.getChangeAmount()
                .compareTo(new BigDecimal(0))<=0) {
            throw new ServiceException("修改销量数必须大于0");
        }
        ShelfRefDto shelfRefDto = new ShelfRefDto();
        shelfRefDto.setShelfId(changeSellCountMessage.getShelfId());
        ProductShelf productShelf = productShelfService.getShelfById(shelfRefDto);
        if (StringUtils.isEmpty(productShelf)) {
            throw new ServiceException("未找到指定上架商品");
        }
        if (changeSellCountMessage.getRetype().equals(Constants.Retype.OUT)) {//减
            changeSellCountMessage.setChangeAmount(new BigDecimal(0).subtract(changeSellCountMessage.getChangeAmount()));
        }
        int i = productStockMapper.changeSellCount(productShelf.getProductId(), changeSellCountMessage.getSkuId(), changeSellCountMessage.getChangeAmount());
        return ApiResult.success();
    }

    @Override
    public ApiResult batchChangeSellCount(List<ChangeSellCountMessage> changeSellCountMessageList) {
        for (ChangeSellCountMessage changeSellCountMessage : changeSellCountMessageList) {
            changeSellCount(changeSellCountMessage);
        }
        return ApiResult.success();
    }

    @Override
    public ProductStock getStock(ProductStock productStock) {
        Condition condition = new Condition(ProductStock.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(productStock.getProductId())) {
            criteria.andEqualTo("productId", productStock.getProductId());
        }
        if (!StringUtils.isEmpty(productStock.getSkuId())) {
            criteria.andEqualTo("skuId", productStock.getSkuId());
        }
        criteria.andEqualTo("flag", Constants.Flag.VALID);
        List<ProductStock> productStockList = productStockMapper.selectByCondition(condition);
        if (productStockList.size() > 1) {
            throw new ServiceException("stock should select one but more");
        }
        return productStockList.size() == 0 ? null : productStockList.get(0);
    }

    @Override
    public Long countHotProduct() {
        PageHelper.startPage(1, 50);
        Long count = this.productStockMapper.countHotProduct();
        return count;
    }

    @Override
    public void softDeleteByProductId(Long productId) {
        if (StringUtils.isEmpty(productId)) {
            throw new ServiceException("库存删除失败，未指定商品id");
        }
        productStockMapper.softDelete(productId);
    }


    public void validateParam(ChangeStockDto changeStockDto) {
        if (StringUtils.isEmpty(changeStockDto.getShelfId())&&StringUtils.isEmpty(changeStockDto.getProductId())) {
            throw new ServiceException("商品id不能为空");
        }
        if (StringUtils.isEmpty(changeStockDto.getSkuId())) {
            throw new ServiceException("未指定skuId");
        }
        if (StringUtils.isEmpty(changeStockDto.getOrderNo())) {
            throw new ServiceException("未指定订单号");
        }
        if (changeStockDto.getChangeCount().compareTo(new BigDecimal(0)) <= 0) {
            throw new ServiceException("更改数量应大于0");
        }
    }


}
