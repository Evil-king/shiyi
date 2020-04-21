package com.baibei.shiyi.product.service.impl;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.page.PageUtil;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.product.dao.BrandMapper;
import com.baibei.shiyi.product.feign.bean.dto.AddOrUpdateBrandDto;
import com.baibei.shiyi.product.feign.bean.dto.BrandListDto;
import com.baibei.shiyi.product.feign.bean.dto.DeleteIdsDto;
import com.baibei.shiyi.product.feign.bean.vo.BrandListVo;
import com.baibei.shiyi.product.feign.bean.vo.ProTypeVo;
import com.baibei.shiyi.product.model.Brand;
import com.baibei.shiyi.product.model.ParameterKey;
import com.baibei.shiyi.product.model.ProType;
import com.baibei.shiyi.product.service.IBrandService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: Brand服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class BrandServiceImpl extends AbstractService<Brand> implements IBrandService {

    @Autowired
    private BrandMapper tblProBrandMapper;

    @Override
    public MyPageInfo<BrandListVo> brandList(BrandListDto brandListDto) {
        PageHelper.startPage(brandListDto.getCurrentPage(),brandListDto.getPageSize());
        Condition condition=new Condition(Brand.class);
        condition.setOrderByClause("IFNULL(seq,999) asc,create_time desc");
        Example.Criteria criteria=buildValidCriteria(condition);
        if(!StringUtils.isEmpty(brandListDto.getBrandName())){
            criteria.andLike("title",brandListDto.getBrandName());
        }
        List<Brand> brands=tblProBrandMapper.selectByCondition(condition);
        MyPageInfo<Brand> page = new MyPageInfo<>(brands);
        MyPageInfo<BrandListVo> pageVos=PageUtil.transform(page, BrandListVo.class);
        for (int i = 0; i < pageVos.getList().size(); i++) {
            pageVos.getList().get(i).setBrandName(brands.get(i).getTitle());
        }
        return pageVos;
    }

    @Override
    public ApiResult addOrUpdateBrand(AddOrUpdateBrandDto addOrUpdateBrandDto) {
        if(StringUtils.isEmpty(addOrUpdateBrandDto.getId())){
            Condition checkCondtion=new Condition(Brand.class);
            Example.Criteria checkCriteria=buildValidCriteria(checkCondtion);
            checkCriteria.andEqualTo("title",addOrUpdateBrandDto.getBrandName());
            List<Brand> checkBrands=tblProBrandMapper.selectByCondition(checkCondtion);
            if(checkBrands.size()>0){
                return ApiResult.badParam("该品牌名称已存在");
            }
            //新增
            Brand brand=new Brand();
            brand.setTitle(addOrUpdateBrandDto.getBrandName());
            brand.setId(IdWorker.getId());
            brand.setCreateTime(new Date());
            brand.setModifyTime(new Date());
            brand.setLogo(addOrUpdateBrandDto.getLogo());
            brand.setSeq(addOrUpdateBrandDto.getSeq());
            brand.setFlag(new Byte(Constants.Flag.VALID));
            tblProBrandMapper.insertSelective(brand);
        }else {
            //编辑
            Condition checkCondtion=new Condition(Brand.class);
            Example.Criteria checkCriteria=buildValidCriteria(checkCondtion);
            checkCriteria.andNotEqualTo("id",addOrUpdateBrandDto.getId());
            checkCriteria.andEqualTo("title",addOrUpdateBrandDto.getBrandName());
            List<Brand> checkBrands=tblProBrandMapper.selectByCondition(checkCondtion);
            if(checkBrands.size()>0){
                return ApiResult.badParam("该品牌名称已存在");
            }
            //先查询出该id下的品牌
            Condition condition=new Condition(Brand.class);
            Example.Criteria criteria=buildValidCriteria(condition);
            criteria.andEqualTo("id",addOrUpdateBrandDto.getId());
            List<Brand> brands=tblProBrandMapper.selectByCondition(condition);
            if(brands.size()<1){
                return ApiResult.error("找不到对应的品牌");
            }
            Brand brand=brands.get(0);
            brand.setLogo(addOrUpdateBrandDto.getLogo());
            brand.setTitle(addOrUpdateBrandDto.getBrandName());
            brand.setSeq(addOrUpdateBrandDto.getSeq());
            brand.setModifyTime(new Date());
            int update = tblProBrandMapper.updateByConditionSelective(brand, condition);
            if(update!=1){
                return ApiResult.error("修改失败！");
            }
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult deleteBrand(DeleteIdsDto deleteIdsDto) {
        if(deleteIdsDto.getIds().size()==1){
            ApiResult apiResult=deleteOneBrand(deleteIdsDto.getIds().get(0));
            return apiResult;
        }else {
            //批量删除,不管个别删除用户是否失败
            for (int i = 0; i <deleteIdsDto.getIds().size() ; i++) {
                deleteOneBrand(deleteIdsDto.getIds().get(i));
            }
            return ApiResult.success();
        }
    }

    @Override
    public List<BrandListVo> getBrandList() {
        Condition condition=new Condition(Brand.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        List<Brand> brands=tblProBrandMapper.selectByCondition(condition);
        List<BrandListVo> brandListVos=BeanUtil.copyProperties(brands,BrandListVo.class);
        for (int i = 0; i < brandListVos.size(); i++) {
            brandListVos.get(i).setBrandName(brands.get(i).getTitle());
        }
        return brandListVos;
    }

    private ApiResult deleteOneBrand(Long aLong) {
        Condition condition=new Condition(Brand.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("id",aLong);
        List<Brand> brands = tblProBrandMapper.selectByCondition(condition);
        if(brands.size()==1){
            Brand brand=brands.get(0);
            brand.setFlag(new Byte(Constants.Flag.UNVALID));
            brand.setModifyTime(new Date());
            int update= tblProBrandMapper.updateByConditionSelective(brand, condition);
            if(update==1){
                return ApiResult.success();
            }else {
                return ApiResult.error("删除失败");
            }
        }
        return ApiResult.error("该品牌不存在");
    }
}
