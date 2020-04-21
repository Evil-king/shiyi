package com.baibei.shiyi.product.service.impl;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.page.PageParam;
import com.baibei.shiyi.common.tool.page.PageUtil;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.product.dao.ProTypeMapper;
import com.baibei.shiyi.product.feign.bean.dto.AddProTypeDto;
import com.baibei.shiyi.product.feign.bean.dto.DeleteIdsDto;
import com.baibei.shiyi.product.feign.bean.dto.UpdateProTypeDto;
import com.baibei.shiyi.product.feign.bean.vo.GroupVo;
import com.baibei.shiyi.product.feign.bean.vo.ProTypeVo;
import com.baibei.shiyi.product.model.ParameterKey;
import com.baibei.shiyi.product.model.ProType;
import com.baibei.shiyi.product.model.Product;
import com.baibei.shiyi.product.model.PropertyKey;
import com.baibei.shiyi.product.service.IParameterKeyService;
import com.baibei.shiyi.product.service.IProTypeService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.product.service.IProductService;
import com.baibei.shiyi.product.service.IPropertyKeyService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.sql.rowset.serial.SerialException;
import java.util.Date;
import java.util.List;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: ProType服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ProTypeServiceImpl extends AbstractService<ProType> implements IProTypeService {

    @Autowired
    private ProTypeMapper tblProTypeMapper;

    @Autowired
    private IPropertyKeyService propertyKeyService;

    @Autowired
    private IParameterKeyService parameterKeyService;

    @Autowired
    private IProductService productService;

    @Override
    public MyPageInfo<ProTypeVo> adminList(PageParam pageParam) {
        PageHelper.startPage(pageParam.getCurrentPage(),pageParam.getPageSize());
        Condition condition=new Condition(ProType.class);
        Example.Criteria criteria=condition.createCriteria();
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        //找到所有的后台类目
        List<ProType> proTypes=tblProTypeMapper.selectByCondition(condition);
        MyPageInfo<ProType> page = new MyPageInfo<>(proTypes);
        MyPageInfo<ProTypeVo> pageVos=PageUtil.transform(page, ProTypeVo.class);
        for (int i = 0; i <pageVos.getList().size() ; i++) {
            //通过属性表根据类目id找到属性数量
            List<PropertyKey> propertyKeys=propertyKeyService.findPropertyByTypeId(proTypes.get(i).getId());
            pageVos.getList().get(i).setPropertyAmount(propertyKeys.size());
            //通过参数表根绝类目id找到参数数量
            List<ParameterKey> parameterKeys=parameterKeyService.findByTypeId(proTypes.get(i).getId());
            pageVos.getList().get(i).setParameterAmount(parameterKeys.size());
            pageVos.getList().get(i).setTypeId(proTypes.get(i).getId());
        }
        return pageVos;
    }

    @Override
    public ApiResult insertProType(AddProTypeDto addProTypeDto) {
        //先根据传入的类目名查询数据库是否存在同名
        Condition condition=new Condition(ProType.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("title",addProTypeDto.getTypeName());
        List<ProType> proTypes=tblProTypeMapper.selectByCondition(condition);
        if(proTypes.size()>0){
            return ApiResult.error("类目名称已存在");
        }
        ProType proType=new ProType();
        proType.setId(IdWorker.getId());
        proType.setTitle(addProTypeDto.getTypeName());
        proType.setStockUnit(addProTypeDto.getStockUnit());
        proType.setCreateTime(new Date());
        proType.setModifyTime(new Date());
        proType.setFlag(new Byte(Constants.Flag.VALID));
        Integer amount=tblProTypeMapper.insertSelective(proType);
        if(amount!=1){
            return ApiResult.error("插入类目失败");
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult updateProType(UpdateProTypeDto updateProTypeDto) {
        Condition condition=new Condition(ProType.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("id",updateProTypeDto.getId());
        List<ProType> proTypes=tblProTypeMapper.selectByCondition(condition);
        if(proTypes.size()>0){
            ProType proType=proTypes.get(0);
            proType.setTitle(updateProTypeDto.getTypeName());
            proType.setStockUnit(updateProTypeDto.getStockUnit());
            proType.setModifyTime(new Date());
            int flag= tblProTypeMapper.updateByConditionSelective(proType, condition);
            if(flag==1){
                return ApiResult.success();
            }else {
                return ApiResult.error("修改类目失败");
            }
        }else {
            return ApiResult.badParam("没找到相关类目");
        }
    }

    @Override
    public ApiResult deleteProType(DeleteIdsDto deleteProTypeDto) {
        boolean flag=false;
        if(deleteProTypeDto.getIds().size()==1){
            ApiResult apiResult=deleteOneProType(deleteProTypeDto.getIds().get(0));
            return apiResult;
        }else {
            //批量删除,不管个别删除用户是否失败
            for (int i = 0; i <deleteProTypeDto.getIds().size() ; i++) {
                if(StringUtils.isEmpty(deleteProTypeDto.getIds().get(i))){
                    throw  new ServiceException("批量删除类目id有误");
                }
                ApiResult apiResult=deleteOneProType(deleteProTypeDto.getIds().get(i));
                if(!apiResult.hasSuccess()){
                    flag=true;
                }
            }
            if(flag){
                ApiResult apiResult=new ApiResult();
                apiResult.setCode(ResultEnum.PRODUCT_PRO_TYPE_DELETE.getCode());
                apiResult.setMsg(ResultEnum.PRODUCT_PRO_TYPE_DELETE.getMsg());
                return apiResult;
            }else {
                return ApiResult.success();
            }
        }
    }

    @Override
    public List<ProType> findByTypeName(String typeName) {
        Condition condition=new Condition(ProType.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andLike("title",typeName);
        return tblProTypeMapper.selectByCondition(condition);
    }

    @Override
    public List<ProTypeVo> findAllAndFlag() {
        Condition condition=new Condition(ProType.class);
        Example.Criteria criteria=condition.createCriteria();
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        List<ProType> proTypes = tblProTypeMapper.selectByCondition(condition);
        List<ProTypeVo> proTypeVos = BeanUtil.copyProperties(proTypes, ProTypeVo.class);
        for (int i = 0; i <proTypeVos.size() ; i++) {
            //通过属性表根据类目id找到属性数量
            List<PropertyKey> propertyKeys=propertyKeyService.findPropertyByTypeId(proTypes.get(i).getId());
            proTypeVos.get(i).setPropertyAmount(propertyKeys.size());
            //通过参数表根绝类目id找到参数数量
            List<ParameterKey> parameterKeys=parameterKeyService.findByTypeId(proTypes.get(i).getId());
            proTypeVos.get(i).setParameterAmount(parameterKeys.size());
            proTypeVos.get(i).setTypeId(proTypes.get(i).getId());
        }
        return proTypeVos;
    }

    @Override
    public List<ProType> findByTypeId(Long typeId) {
        Condition condition=new Condition(ProType.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("id",typeId);
        return tblProTypeMapper.selectByCondition(condition);
    }

    private ApiResult deleteOneProType(Long typeId) {
        //单一删除
        List<Product> products=productService.findByTypeId(typeId);
        if(products.size()>0){
            //该类目下存在商品无法删除
            return ApiResult.badParam("该类目下还有商品不可删除该类目");
        }else {
            //不存在商品则可以进行删除
            Condition condition=new Condition(ProType.class);
            Example.Criteria criteria=buildValidCriteria(condition);
            criteria.andEqualTo("id",typeId);
            List<ProType> proTypes=tblProTypeMapper.selectByCondition(condition);
            //找到该类目
            if(proTypes.size()!=1){
                return ApiResult.badParam("该类目不存在或存在多个");
            }else {
                ProType proType=proTypes.get(0);
                proType.setFlag(new Byte(Constants.Flag.UNVALID));
                proType.setModifyTime(new Date());
                int i = tblProTypeMapper.updateByConditionSelective(proType, condition);
                if(i==1){
                    return ApiResult.success();
                }else {
                    return ApiResult.error("删除失败");
                }
            }
        }
    }
}
