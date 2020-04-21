package com.baibei.shiyi.product.service.impl;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.page.PageUtil;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.product.dao.PropertyKeyMapper;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.ProTypeVo;
import com.baibei.shiyi.product.feign.bean.vo.PropertyKeyVo;
import com.baibei.shiyi.product.model.*;
import com.baibei.shiyi.product.service.IProTypeService;
import com.baibei.shiyi.product.service.IPropertyKeyService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.product.service.IPropertyValueService;
import com.github.pagehelper.PageHelper;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author: Longer
 * @date: 2019/07/30 10:12:45
 * @description: PropertyKey服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PropertyKeyServiceImpl extends AbstractService<PropertyKey> implements IPropertyKeyService {

    @Autowired
    private PropertyKeyMapper propertyKeyMapper;
    @Autowired
    private IPropertyValueService propertyValueService;

    @Autowired
    private IProTypeService proTypeService;


    @Override
    public ApiResult addProperty(AddPropertyuDto addPropertyuDto) {
        //先从属性库查询同类目下是否存在同名属性
        Condition condition=new Condition(PropertyKey.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("typeId",addPropertyuDto.getTypeId());
        criteria.andEqualTo("title",addPropertyuDto.getPropertyName());
        List<PropertyKey> propertyKeys=propertyKeyMapper.selectByCondition(condition);
        if(propertyKeys.size()>0){
            return ApiResult.error("该类目下已存在该名称的属性");
        }
        List<ProType> proTypes=proTypeService.findByTypeId(addPropertyuDto.getTypeId());
        if(proTypes.size()<1){
            return ApiResult.error("该后台类目不存在");
        }
        PropertyKey propertyKey = new PropertyKey();
        propertyKey.setId(IdWorker.getId());
        propertyKey.setTypeId(addPropertyuDto.getTypeId());
        propertyKey.setTitle(addPropertyuDto.getPropertyName());
        propertyKey.setSeq(addPropertyuDto.getSeq());
        Date currentTime = new Date();
        propertyKey.setCreateTime(currentTime);
        propertyKey.setModifyTime(currentTime);
        propertyKey.setFlag(new Byte(Constants.Flag.VALID));
        //新增属性Key
        propertyKeyMapper.insertSelective(propertyKey);
        //新增属性value
        String propertyVals = addPropertyuDto.getPropertyValue();
        String[] vals = propertyVals.split(",");
        List<PropertyValue> propertyValueList = new ArrayList<>();
        for (int i = 0; i < vals.length; i++) {
            PropertyValue propertyValue = new PropertyValue();
            propertyValue.setId(IdWorker.getId());
            propertyValue.setKeyId(propertyKey.getId());
            propertyValue.setValue(vals[i]);
            propertyValue.setCreateTime(currentTime);
            propertyValue.setModifyTime(currentTime);
            propertyValue.setFlag(new Byte(Constants.Flag.VALID));
            propertyValueService.save(propertyValue);
        }
        return ApiResult.success();
    }

    @Override
    public List<PropertyKey> findPropertyByTypeId(Long id) {
        Condition condition = new Condition(PropertyKey.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("typeId", id);
        return propertyKeyMapper.selectByCondition(condition);
    }

    @Override
    public ApiResult<MyPageInfo<PropertyKeyVo>> propertyPageList(PropertyKeyDto propertyKeyDto) {
        PageHelper.startPage(propertyKeyDto.getCurrentPage(), propertyKeyDto.getPageSize());

        List<PropertyKeyVo> propertyKeyVos = propertyKeyMapper.findByKeyDto(propertyKeyDto);

        for (int i = 0; i < propertyKeyVos.size(); i++) {
            //通过属性id获取属性值
            List<PropertyValue> propertyValues = propertyValueService.findByKeyId(propertyKeyVos.get(i).getId());
            //属性值
            String propertyValue = "";
            for (int j = 0; j < propertyValues.size(); j++) {
                if (j == propertyValues.size() - 1) {
                    propertyValue = propertyValue + propertyValues.get(j).getValue();
                } else {
                    propertyValue = propertyValue + propertyValues.get(j).getValue() + ",";
                }
            }
            propertyKeyVos.get(i).setPropertyValue(propertyValue);
        }
        MyPageInfo<PropertyKeyVo> myPageInfo=new MyPageInfo<>(propertyKeyVos);
        return ApiResult.success(myPageInfo);
    }

    @Override
    public ApiResult updateProperty(UpdatePropertyDto updatePropertyDto) {
        Condition condition = new Condition(PropertyKey.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("id", updatePropertyDto.getId());
        List<PropertyKey> propertyKeys = propertyKeyMapper.selectByCondition(condition);
        if (propertyKeys.size() <= 0) {
            return ApiResult.badParam("找不到对应的属性");
        }
        Condition updateCondition = new Condition(PropertyKey.class);
        Example.Criteria updateCriteria = buildValidCriteria(updateCondition);
        updateCriteria.andNotEqualTo("id", updatePropertyDto.getId());
        updateCriteria.andEqualTo("typeId",updatePropertyDto.getTypeId());
        updateCriteria.andEqualTo("title",updatePropertyDto.getPropertyName());
        List<PropertyKey> updatePropertyKeys=propertyKeyMapper.selectByCondition(updateCondition);
        if(updatePropertyKeys.size()>0){
            return ApiResult.error("该类目下已存在该名称的属性");
        }
        if(!Constants.EditFlag.ALLOW.equals(propertyKeys.get(0).getEditFlag())){
            return ApiResult.badParam("该属性不支持修改");
        }
        List<ProType> proTypes=proTypeService.findByTypeId(updatePropertyDto.getTypeId());
        if(proTypes.size()<1){
            return ApiResult.error("该后台类目不存在");
        }
        PropertyKey propertyKey = propertyKeys.get(0);
        propertyKey.setTypeId(updatePropertyDto.getTypeId());
        propertyKey.setTitle(updatePropertyDto.getPropertyName());
        propertyKey.setSeq(updatePropertyDto.getSeq());
        propertyKey.setModifyTime(new Date());
        int update = propertyKeyMapper.updateByConditionSelective(propertyKey, condition);
        if(update!=1){
            return ApiResult.error("修改失败！");
        }
        //先删除该属性下的所有属性值
        propertyValueService.deleteByKeyId(propertyKey.getId());
        String[] vals = updatePropertyDto.getPropertyValue().split(",");
        for (int i = 0; i < vals.length; i++) {
            PropertyValue propertyValue = new PropertyValue();
            propertyValue.setId(IdWorker.getId());
            propertyValue.setKeyId(propertyKey.getId());
            propertyValue.setValue(vals[i]);
            propertyValue.setCreateTime(new Date());
            propertyValue.setModifyTime(new Date());
            propertyValue.setFlag(new Byte(Constants.Flag.VALID));
            propertyValueService.save(propertyValue);
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult deleteProperty(DeleteIdsDto deleteIdsDto) {
        if(deleteIdsDto.getIds().size()==1){
            ApiResult apiResult=deleteOneProperty(deleteIdsDto.getIds().get(0));
            return apiResult;
        }else {
            //批量删除,不管个别删除用户是否失败
            for (int i = 0; i <deleteIdsDto.getIds().size() ; i++) {
                deleteOneProperty(deleteIdsDto.getIds().get(i));
            }
            return ApiResult.success();
        }
    }

    @Override
    public ApiResult<List<PropertyValueVo>> getValueByKeyId(Long id) {
        List<PropertyValue> propertyValues= propertyValueService.findByKeyId(id);
        List<PropertyValueVo> propertyValueVos=BeanUtil.copyProperties(propertyValues,PropertyValueVo.class);
        for (int j = 0; j < propertyValueVos.size(); j++) {
            propertyValueVos.get(j).setPropertyValue(propertyValues.get(j).getValue());
            propertyValueVos.get(j).setPropertyId(propertyValues.get(j).getId());
        }
        return ApiResult.success(propertyValueVos);
    }

    @Override
    public ApiResult<List<PropertyKeyVo>> getPropertyList(ProTypeIdDto proTypeIdDto) {
        PropertyKeyDto propertyKeyDto=new PropertyKeyDto();
        propertyKeyDto.setTypeId(proTypeIdDto.getTypeId());
        List<PropertyKeyVo> propertyKeyVos = propertyKeyMapper.findByKeyDto(propertyKeyDto);
        for (int i = 0; i < propertyKeyVos.size(); i++) {
            //通过属性id获取属性值
            List<PropertyValue> propertyValues = propertyValueService.findByKeyId(propertyKeyVos.get(i).getId());
            //属性值
            String propertyValue = "";
            for (int j = 0; j < propertyValues.size(); j++) {
                if (j == propertyValues.size() - 1) {
                    propertyValue = propertyValue + propertyValues.get(j).getValue();
                } else {
                    propertyValue = propertyValue + propertyValues.get(j).getValue() + ",";
                }
            }
            propertyKeyVos.get(i).setPropertyValue(propertyValue);
        }
        return ApiResult.success(propertyKeyVos);
    }

    @Override
    public void modifyEditFlag(Long propId,String editFlag) {
        if (!Constants.EditFlag.ALLOW.equals(editFlag)&&!Constants.EditFlag.UNALLOW.equals(editFlag)) {
            throw new ServiceException("参数异常");
        }
        PropertyKey propertyKey = new PropertyKey();
        propertyKey.setEditFlag(editFlag);
        propertyKey.setId(propId);
        propertyKeyMapper.updateByPrimaryKeySelective(propertyKey);
    }

    private ApiResult deleteOneProperty(Long keyId) {
        Condition condition=new Condition(PropertyKey.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("id",keyId);
        List<PropertyKey> propertyKeys = propertyKeyMapper.selectByCondition(condition);
        if(propertyKeys.size()==1){
            if(Constants.EditFlag.ALLOW.equals(propertyKeys.get(0).getEditFlag())){
                PropertyKey propertyKey=propertyKeys.get(0);
                propertyKey.setFlag(new Byte(Constants.Flag.UNVALID));
                propertyKey.setModifyTime(new Date());
                int update= propertyKeyMapper.updateByConditionSelective(propertyKey, condition);
                if(update==1){
                    //删除了属性还需删除属性值，此处属性值为物理删除
                    propertyValueService.deleteByKeyId(keyId);
                    return ApiResult.success();
                }else {
                    return ApiResult.error("删除失败");
                }
            }else {
                //不可操作，直接跳过
                return ApiResult.success();
            }
        }else {
            return ApiResult.error("该属性值不存在");
        }

    }
}
