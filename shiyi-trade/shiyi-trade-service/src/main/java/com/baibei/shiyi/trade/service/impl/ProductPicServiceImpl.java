package com.baibei.shiyi.trade.service.impl;

import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.trade.common.dto.TradeProductPicDto;
import com.baibei.shiyi.trade.common.vo.TradeProductSlideListVo;
import com.baibei.shiyi.trade.common.vo.TradeProductSlideVo;
import com.baibei.shiyi.trade.dao.ProductPicMapper;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductAddDto;
import com.baibei.shiyi.trade.model.ProductPic;
import com.baibei.shiyi.trade.service.IProductPicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
 * @author: wenqing
 * @date: 2019/12/20 16:06:04
 * @description: ProductPic服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductPicServiceImpl extends AbstractService<ProductPic> implements IProductPicService {

    @Autowired
    private ProductPicMapper tblTraProductPicMapper;

    @Override
    public ApiResult createObj(TradeProductAddDto tradeProductAddDto) {

        for (int i = 0; i < tradeProductAddDto.getSlideUrl().size(); i++) {
            TradeProductAddDto.ImageDta imageDta = tradeProductAddDto.getSlideUrl().get(i);
            ProductPic productPic = new ProductPic();
            productPic.setId(IdWorker.getId());
            productPic.setProductTradeNo(tradeProductAddDto.getProductTradeNo());
            productPic.setUrl(imageDta.getUrl());
            productPic.setType(Constants.TradeProductPicType.SLIDE);
            productPic.setCreateTime(new Date());
            productPic.setModifyTime(new Date());
            productPic.setFlag((byte) 1);
            if (tblTraProductPicMapper.insertSelective(productPic) < 1) {
                return ApiResult.error();
            }
        }
        for (int i = 0; i < tradeProductAddDto.getDetailsUrl().size(); i++) {
            TradeProductAddDto.ImageDta imageDta = tradeProductAddDto.getDetailsUrl().get(i);
            ProductPic productPic = new ProductPic();
            productPic.setId(IdWorker.getId());
            productPic.setProductTradeNo(tradeProductAddDto.getProductTradeNo());
            productPic.setUrl(imageDta.getUrl());
            productPic.setType(Constants.TradeProductPicType.DETAILS);
            productPic.setCreateTime(new Date());
            productPic.setModifyTime(new Date());
            productPic.setFlag((byte) 1);
            if (tblTraProductPicMapper.insertSelective(productPic) < 1) {
                return ApiResult.error();
            }
        }

        return ApiResult.success();
    }

    @Override
    public List<ProductPic> selectByTradeNo(String productTradeNo) {
        Condition condition = new Condition(ProductPic.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("productTradeNo", productTradeNo);
        criteria.andEqualTo("flag",1);
        List<ProductPic> productPics = tblTraProductPicMapper.selectByCondition(condition);
        return productPics;
    }


    @Override
    public ApiResult<TradeProductSlideListVo> getTradeProductPic(TradeProductPicDto productPicDto) {
        TradeProductSlideListVo tradeProductSlideListVo = new TradeProductSlideListVo();
        List<TradeProductSlideVo> productPics = null;
        if(Constants.TradeProductPicType.SLIDE.equals(productPicDto.getType())){
            productPics = tblTraProductPicMapper.selectPic(productPicDto.getType(),productPicDto.getProductTradeNo());
        }
        if(Constants.TradeProductPicType.DETAILS.equals(productPicDto.getType())){
            productPics = tblTraProductPicMapper.selectPic(productPicDto.getType(),productPicDto.getProductTradeNo());
        }
        productPics.stream().forEach(x ->{
            tradeProductSlideListVo.setProductName(x.getProductName());
        });
        tradeProductSlideListVo.setProductSlideVoList(productPics);
        return ApiResult.success(tradeProductSlideListVo);
    }

    @Override
    public ApiResult updateByTradeProductNo(TradeProductAddDto tradeProductAddDto) {
        //先删
        tblTraProductPicMapper.deleteByTradeProductNo(tradeProductAddDto.getProductTradeNo());
        ApiResult result = createObj(tradeProductAddDto);
        return result;
    }
}
