package com.baibei.shiyi.trade.service.impl;

import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.ProductTradeStatusEnum;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.quotation.feign.bean.vo.QuoteVo;
import com.baibei.shiyi.quotation.feign.service.ICommonQuoteService;
import com.baibei.shiyi.trade.common.vo.TradeIndexVo;
import com.baibei.shiyi.trade.dao.ProductMapper;
import com.baibei.shiyi.trade.feign.bean.dto.ProductDto;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductAddDto;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductDto;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductLookDto;
import com.baibei.shiyi.trade.feign.bean.vo.TradeProductListVo;
import com.baibei.shiyi.trade.feign.bean.vo.TradeProductVo;
import com.baibei.shiyi.trade.model.Product;
import com.baibei.shiyi.trade.model.ProductPic;
import com.baibei.shiyi.trade.service.IHoldPositionService;
import com.baibei.shiyi.trade.service.IProductPicService;
import com.baibei.shiyi.trade.service.IProductService;
import com.baibei.shiyi.trade.service.ITradeDayService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/26 10:36:53
 * @description: Product服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class ProductServiceImpl extends AbstractService<Product> implements IProductService {

    @Autowired
    private IProductPicService productPicService;

    @Autowired
    private ITradeDayService tradeDayService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ICommonQuoteService commonQuoteService;

    @Autowired
    private IHoldPositionService holdPositionService;

    @Override
    public Product findEffective(String productTradeNo) {
        return productMapper.findEffective(productTradeNo);
    }

    @Override
    public boolean isTrading(String productTradeNo) {
        Product product = findEffective(productTradeNo);
        if (product == null) {
            log.error("交易商品不存在，productTradeNo={}", productTradeNo);
            throw new SystemException("交易商品不存在");
        }
        return !StringUtils.isEmpty(product.getTradeStatus())
                && ProductTradeStatusEnum.TRADING.getCode().equals(product.getTradeStatus());
    }

    @Override
    public MyPageInfo<TradeProductListVo> listPage(TradeProductDto tradeProductDto) {
        PageHelper.startPage(tradeProductDto.getCurrentPage(), tradeProductDto.getPageSize());
        List<TradeProductListVo> tradeProductVos = productMapper.listPage(tradeProductDto);
        MyPageInfo<TradeProductListVo> myPageInfo = new MyPageInfo<>(tradeProductVos);
        return myPageInfo;
    }

    @Override
    public ApiResult add(TradeProductAddDto tradeProductAddDto) {
        //查询商品编号是否存在
        Condition condition = new Condition(Product.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("productTradeNo", tradeProductAddDto.getProductTradeNo());
        criteria.andEqualTo("flag",1);
        List<Product> productList = productMapper.selectByCondition(condition);
        if (!CollectionUtils.isEmpty(productList)) {
            return ApiResult.error("交易商品编号已经存在");
        }
        //创建交易商品
        Product product = createProduct(tradeProductAddDto);
        if (productMapper.insertSelective(product) < 1) {
            return ApiResult.error("创建交易商品失败");
        }
        //创建交易商品图片
        ApiResult apiResult = productPicService.createObj(tradeProductAddDto);
        if (apiResult.hasFail()) {
            return ApiResult.error("创建交易商品图片失败");
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult editOperator(TradeProductAddDto tradeProductAddDto) {
        //根据id查询商品
        List<Product> productList = productMapper.selectByIds(String.valueOf(tradeProductAddDto.getId()));
        if (CollectionUtils.isEmpty(productList)) {
            throw new SystemException("该商品不存在");
        }
        Product product = productList.get(0);
        ApiResult result = editProduct(tradeProductAddDto,product);
        if (result.hasFail()) {
            return ApiResult.error();
        }
        //更新图片
        ApiResult apiResult = productPicService.updateByTradeProductNo(tradeProductAddDto);
        if (apiResult.hasFail()) {
            return ApiResult.error();
        }
        return ApiResult.success();
    }


    @Override
    public ApiResult<TradeProductVo> look(TradeProductLookDto tradeProductLookDto) {
        //根据id查询商品
        Condition condition = new Condition(Product.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("flag", 1);
        criteria.andEqualTo("id", tradeProductLookDto.getId());
        List<Product> productList = productMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(productList)) {
            throw new SystemException("该商品不存在");
        }
        Product product = productList.get(0);
        //组装vo数据
        return ApiResult.success(operatorObj(product));
    }

    @Override
    public ApiResult modifyStatus() {
        //查询商品状态为wait的商品信息
        List<Product> productList = productMapper.modifyStatus();
        productList.stream().forEach(product -> {
            String nowTime = DateUtil.yyyyMMddWithLine.get().format(new Date());
            String marketTime = DateUtil.yyyyMMddWithLine.get().format(product.getMarketTime());
            log.info("nowTime={},marketTime={}",nowTime,marketTime);
            if (DateUtil.yyyyMMddWithLine.get().format(new Date())
                    .compareTo(DateUtil.yyyyMMddWithLine.get().format(product.getMarketTime())) > -1) {
                product.setTradeStatus(Constants.TradeProductStatus.TRADING);
                product.setModifyTime(new Date());
                productMapper.updateByPrimaryKeySelective(product);
            }
        });
        return ApiResult.success();
    }

    @Override
    public List<Product> listTradeingProduct(ProductDto dto) {
        Condition condition = new Condition(Product.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("tradeStatus", Constants.TradeProductStatus.TRADING);
        if (!StringUtils.isEmpty(dto.getProductTradeNo())) {
            criteria.andLike("productTradeNo", "%" + dto.getProductTradeNo() + "%");
        }
        if (!StringUtils.isEmpty(dto.getProductName())) {
            criteria.andLike("productName", "%" + dto.getProductName() + "%");
        }
        return findByCondition(condition);
    }

    @Override
    public ApiResult<List<TradeIndexVo>> getIndex() {
        List<TradeIndexVo> indexVoList = Lists.newArrayList();
        List<Product> productList = findEffectiveList();
        productList.stream().forEach(product -> {
            Integer canSellCount = holdPositionService.sumCanSellCount(product.getProductTradeNo());
            QuoteVo quoteVo =  commonQuoteService.getQuoteInfo(product.getProductTradeNo());
            TradeIndexVo tradeIndexVo = new TradeIndexVo()
                    .setProductTradeNo(quoteVo.getProductTradeNo())
                    .setDealCount(quoteVo.getDealCount())
                    .setLastPrice(quoteVo.getLastPrice())
                    .setProductName(product.getProductName())
                    .setBuyCount(quoteVo.getBuyCount())
                    .setBuyPrice(quoteVo.getBuyPrice())
                    .setDealAmount(quoteVo.getDealAmount())
                    .setHighPrice(quoteVo.getHighPrice())
                    .setLastPrice(quoteVo.getLastPrice())
                    .setLowPrice(quoteVo.getLowPrice())
                    .setOpenPrice(quoteVo.getOpenPrice())
                    .setSellCount(quoteVo.getSellCount())
                    .setSellPrice(quoteVo.getSellPrice())
                    .setUpdown(quoteVo.getUpdown())
                    .setUpdownRate(quoteVo.getUpdownRate())
                    .setTotalCanSell(String.valueOf(canSellCount));
            indexVoList.add(tradeIndexVo);
        });
        return ApiResult.success(indexVoList);
    }

    private List<Product> findEffectiveList() {
        return productMapper.findEffectiveList();
    }

    private ApiResult editProduct(TradeProductAddDto tradeProductAddDto,Product product){
        product.setId(tradeProductAddDto.getId());
        product.setUnit(tradeProductAddDto.getUnit());
        product.setModifier(tradeProductAddDto.getModifier());
        product.setCreator(tradeProductAddDto.getCreator());
        if(Constants.TradeProductStatus.WAIT.equals(product.getTradeStatus())){
            String marketTimeStr = tradeProductAddDto.getMarketTime() + " 00:00:00";
            product.setMarketTime(DateUtil.strToDate(marketTimeStr));//上市时间
        }
        product.setMaxTrade(tradeProductAddDto.getMaxTrade());
        product.setMinTrade(tradeProductAddDto.getMinTrade());
        List<TradeProductAddDto.ImageDta> icon = tradeProductAddDto.getIcon();
        product.setProductIcon(icon.get(0).getUrl());
        if (tradeDayService.isClose()) {
            product.setHighestQuotedPrice(tradeProductAddDto.getHighestQuotedPrice());
            product.setLowestQuotedPrice(tradeProductAddDto.getLowestQuotedPrice());
        }
        product.setModifyTime(new Date());
        if(productMapper.updateByPrimaryKeySelective(product) > 0){
            return ApiResult.success();
        }
        return ApiResult.error();
    }


    private Product createProduct(TradeProductAddDto tradeProductAddDto) {
        //获取下一个交易日
        Date nextTradeDay = tradeDayService.getAddNTradeDay(1);
        Product product = new Product();
        product.setId(IdWorker.getId());
        product.setCreateTime(new Date());
        product.setModifyTime(new Date());
        product.setFlag((byte) 1);
        product.setHighestQuotedPrice(tradeProductAddDto.getHighestQuotedPrice());
        product.setLowestQuotedPrice(tradeProductAddDto.getLowestQuotedPrice());
        List<TradeProductAddDto.ImageDta> icon = tradeProductAddDto.getIcon();
        product.setProductIcon(icon.get(0).getUrl());
        product.setProductName(tradeProductAddDto.getProductName());
        product.setProductTradeNo(tradeProductAddDto.getProductTradeNo());
        product.setIssuePrice(tradeProductAddDto.getIssuePrice());
        product.setExchangePrice(tradeProductAddDto.getExchangePrice());
        product.setMinTrade(tradeProductAddDto.getMinTrade());
        product.setMaxTrade(tradeProductAddDto.getMaxTrade());
        product.setTradeStatus(Constants.TradeProductStatus.WAIT);
        product.setCreator(tradeProductAddDto.getCreator());
        product.setModifier(tradeProductAddDto.getModifier());
        product.setUnit(tradeProductAddDto.getUnit());
        if (Boolean.FALSE.equals(tradeDayService.isTradeDay(new Date()))) { //如果当前日期不是交易日 则用下一个交易日为上市时间
            product.setMarketTime(nextTradeDay);
        } else {
            String marketTimeStr = tradeProductAddDto.getMarketTime() + " 00:00:00";
            product.setMarketTime(DateUtil.strToDate(marketTimeStr));
        }
        if (nextTradeDay == null) {
            throw new SystemException("获取不到下一个交易日");
        }
        return product;
    }

    private TradeProductVo operatorObj(Product product) {

        List<ProductPic> productPic = productPicService.selectByTradeNo(product.getProductTradeNo());
        List<TradeProductVo.ImageDta> icon = new ArrayList<>();
        List<TradeProductVo.ImageDta> slideUrlList = new ArrayList<>();
        List<TradeProductVo.ImageDta> detailsUrlList = new ArrayList<>();

        Date marketTime = product.getMarketTime();
        String marketTimeStr = DateUtil.yyyyMMddWithLine.get().format(marketTime);

        TradeProductVo.ImageDta iconUrl = new TradeProductVo.ImageDta().setUrl(product.getProductIcon());
        icon.add(iconUrl);

        productPic.stream().forEach(productPic1 -> {
            if (Constants.TradeProductPicType.SLIDE.equals(productPic1.getType())) {
                TradeProductVo.ImageDta slideUrl = new TradeProductVo.ImageDta();
                slideUrl.setUrl(productPic1.getUrl());
                slideUrlList.add(slideUrl);
            }
            if (Constants.TradeProductPicType.DETAILS.equals(productPic1.getType())) {
                TradeProductVo.ImageDta detailsUrl = new TradeProductVo.ImageDta();
                detailsUrl.setUrl(productPic1.getUrl());
                detailsUrlList.add(detailsUrl);
            }
        });

        TradeProductVo tradeProductVo = new TradeProductVo()
                .setId(product.getId())
                .setProductTradeNo(product.getProductTradeNo())
                .setMaxTrade(product.getMaxTrade())
                .setMinTrade(product.getMinTrade())
                .setProductName(product.getProductName())
                .setExchangePrice(product.getExchangePrice())
                .setHighestQuotedPrice(product.getHighestQuotedPrice())
                .setLowestQuotedPrice(product.getLowestQuotedPrice())
                .setIssuePrice(product.getIssuePrice())
                .setMarketTime(marketTimeStr)
                .setUnit(product.getUnit())
                .setIcon(icon)
                .setStatus(product.getTradeStatus())
                .setDetailsUrl(detailsUrlList)
                .setSlideUrl(slideUrlList);
        //判断是否闭式
        if (tradeDayService.isClose()) {
            tradeProductVo.setTradeStatus("close");
        } else {
            tradeProductVo.setTradeStatus("open");
        }
        return tradeProductVo;
    }
}
