package com.baibei.shiyi.product.service.impl;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.page.PageParam;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.product.dao.ParameterKeyMapper;
import com.baibei.shiyi.product.feign.bean.dto.AddParameterDto;
import com.baibei.shiyi.product.feign.bean.dto.DeleteIdsDto;
import com.baibei.shiyi.product.feign.bean.dto.ParameterListDto;
import com.baibei.shiyi.product.feign.bean.dto.UpdateParameterDto;
import com.baibei.shiyi.product.feign.bean.vo.ParameterListVo;
import com.baibei.shiyi.product.model.ParameterKey;
import com.baibei.shiyi.product.model.ParameterValue;
import com.baibei.shiyi.product.model.ProType;
import com.baibei.shiyi.product.service.IParameterKeyService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.product.service.IParameterValueService;
import com.baibei.shiyi.product.service.IProTypeService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: ParameterKey服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ParameterKeyServiceImpl extends AbstractService<ParameterKey> implements IParameterKeyService {

    @Autowired
    private ParameterKeyMapper tblProParameterKeyMapper;

    @Autowired
    private IProTypeService proTypeService;

    @Autowired
    private IParameterValueService parameterValueService;

    @Override
    public List<ParameterKey> findByTypeId(Long id) {
        Condition condition=new Condition(ParameterKey.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("typeId",id);
        return tblProParameterKeyMapper.selectByCondition(condition);
    }

    @Override
    public MyPageInfo<ParameterListVo> parameterPageList(ParameterListDto parameterListDto) {
        PageHelper.startPage(parameterListDto.getCurrentPage(),parameterListDto.getPageSize());
        List<ParameterListVo> parameterListVos=tblProParameterKeyMapper.findByListDto(parameterListDto);
        for (int i = 0; i <parameterListVos.size() ; i++) {
            List<ParameterValue> parameterValues = parameterValueService.findByKeyId(parameterListVos.get(i).getId());
            //属性值
            String parameterValue = "";
            for (int j = 0; j < parameterValues.size(); j++) {
                if (j == parameterValues.size() - 1) {
                    parameterValue = parameterValue + parameterValues.get(j).getValue();
                } else {
                    parameterValue = parameterValue + parameterValues.get(j).getValue() + ",";
                }
                parameterListVos.get(i).setParameterValue(parameterValue);
            }
        }
        MyPageInfo<ParameterListVo> pageInfo=new MyPageInfo<>(parameterListVos);
        return pageInfo;
    }

    @Override
    public ApiResult addParameter(AddParameterDto addParameterDto) {
        //判断类型是否存在
        if(Constants.parameterKeyType.all.indexOf(addParameterDto.getParameterType())==-1){
            return ApiResult.badParam("不存在该参数类型");
        }
        //先从属性库查询同类目下是否存在同名属性
        Condition condition=new Condition(ParameterKey.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("typeId",addParameterDto.getTypeId());
        criteria.andEqualTo("title",addParameterDto.getParameterName());
        List<ParameterKey> parameterKeys=tblProParameterKeyMapper.selectByCondition(condition);
        if(parameterKeys.size()>0){
            return ApiResult.error("该类目下已存在该名称的参数");
        }
        List<ProType> proTypes=proTypeService.findByTypeId(addParameterDto.getTypeId());
        if(proTypes.size()<0){
            return ApiResult.error("该后台类目不存在");
        }
        ParameterKey parameterKey=BeanUtil.copyProperties(addParameterDto,ParameterKey.class);
        parameterKey.setTitle(addParameterDto.getParameterName());
        parameterKey.setCreateTime(new Date());
        parameterKey.setModifyTime(new Date());
        parameterKey.setId(IdWorker.getId());
        parameterKey.setFlag(new Byte(Constants.Flag.VALID));
        parameterKey.setType(addParameterDto.getParameterType());
        tblProParameterKeyMapper.insertSelective(parameterKey);
        String[] parameterValues=addParameterDto.getParameterValue().split(",");
        for (int i = 0; i <parameterValues.length ; i++) {
            ParameterValue parameterValue=new ParameterValue();
            parameterValue.setKeyId(parameterKey.getId());
            parameterValue.setValue(parameterValues[i]);
            parameterValue.setCreateTime(new Date());
            parameterValue.setModifyTime(new Date());
            parameterValue.setId(IdWorker.getId());
            parameterValue.setFlag(new Byte(Constants.Flag.VALID));
            parameterValueService.insertSelective(parameterValue);
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult updateParameter(UpdateParameterDto updateParameterDto) {
        //判断类型是否存在
        if(Constants.parameterKeyType.all.indexOf(updateParameterDto.getParameterType())==-1){
            return ApiResult.badParam("不存在该参数类型");
        }

        Condition condition=new Condition(ParameterKey.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("id",updateParameterDto.getId());
        List<ParameterKey> parameterKeys=tblProParameterKeyMapper.selectByCondition(condition);
        if(parameterKeys.size()<1){
            return ApiResult.error("该类目下不存在该参数");
        }
        if(!Constants.EditFlag.ALLOW.equals(parameterKeys.get(0).getEditFlag())){
            return ApiResult.badParam("该参数不支持修改");
        }
        //从参数库查询同类目下是否存在同名属性
        Condition updateCondition = new Condition(ParameterKey.class);
        Example.Criteria updateCriteria = buildValidCriteria(updateCondition);
        updateCriteria.andNotEqualTo("id", updateParameterDto.getId());
        updateCriteria.andEqualTo("typeId",updateParameterDto.getTypeId());
        updateCriteria.andEqualTo("title",updateParameterDto.getParameterName());
        List<ParameterKey> updatePropertyKeys=tblProParameterKeyMapper.selectByCondition(updateCondition);
        if(updatePropertyKeys.size()>0){
            return ApiResult.error("该类目下已存在该名称的参数");
        }
        List<ProType> proTypes=proTypeService.findByTypeId(updateParameterDto.getTypeId());
        if(proTypes.size()<1){
            return ApiResult.error("该后台类目不存在");
        }
        ParameterKey parameterKey=parameterKeys.get(0);
        parameterKey.setTypeId(updateParameterDto.getTypeId());
        parameterKey.setType(updateParameterDto.getParameterType());
        parameterKey.setTitle(updateParameterDto.getParameterName());
        parameterKey.setSeq(updateParameterDto.getSeq());
        parameterKey.setModifyTime(new Date());
        int update = tblProParameterKeyMapper.updateByCondition(parameterKey, condition);
        if(update!=1){
            return ApiResult.error("修改失败！");
        }
        //删除该参数下所有参数值
        parameterValueService.deleteByKeyId(updateParameterDto.getId());
        String[] parameterValues=updateParameterDto.getParameterValue().split(",");
        for (int i = 0; i <parameterValues.length ; i++) {
            ParameterValue parameterValue=new ParameterValue();
            parameterValue.setKeyId(parameterKey.getId());
            parameterValue.setValue(parameterValues[i]);
            parameterValue.setCreateTime(new Date());
            parameterValue.setModifyTime(new Date());
            parameterValue.setId(IdWorker.getId());
            parameterValue.setFlag(new Byte(Constants.Flag.VALID));
            parameterValueService.insertSelective(parameterValue);
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult deleteParameter(DeleteIdsDto deleteIdsDto) {
        if(deleteIdsDto.getIds().size()==1){
            ApiResult apiResult=deleteOneParameter(deleteIdsDto.getIds().get(0));
            return apiResult;
        }else {
            //批量删除,不管个别删除用户是否失败
            for (int i = 0; i <deleteIdsDto.getIds().size() ; i++) {
                deleteOneParameter(deleteIdsDto.getIds().get(i));
            }
            return ApiResult.success();
        }
    }

    @Override
    public List<ParameterListVo> list(Long typeId) {
        ParameterListDto parameterListDto=new ParameterListDto();
        parameterListDto.setTypeId(typeId);
        List<ParameterListVo> parameterListVos=tblProParameterKeyMapper.findByListDto(parameterListDto);
        for (int i = 0; i <parameterListVos.size() ; i++) {
            List<ParameterValue> parameterValues = parameterValueService.findByKeyId(parameterListVos.get(i).getId());
            //属性值
            String parameterValue = "";
            for (int j = 0; j < parameterValues.size(); j++) {
                if (j == parameterValues.size() - 1) {
                    parameterValue = parameterValue + parameterValues.get(j).getValue();
                } else {
                    parameterValue = parameterValue + parameterValues.get(j).getValue() + ",";
                }
                parameterListVos.get(i).setParameterValue(parameterValue);
            }
        }
        return parameterListVos;
    }

    @Override
    public void modifyEditFlag(Long paramId, String editFlag) {
        if (!Constants.EditFlag.ALLOW.equals(editFlag)&&!Constants.EditFlag.UNALLOW.equals(editFlag)) {
            throw new ServiceException("参数异常");
        }
        ParameterKey parameterKey = new ParameterKey();
        parameterKey.setId(paramId);
        parameterKey.setEditFlag(editFlag);
        parameterKey.setModifyTime(new Date());
        tblProParameterKeyMapper.updateByPrimaryKeySelective(parameterKey);
    }

    private ApiResult deleteOneParameter(Long aLong) {
        Condition condition=new Condition(ParameterKey.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("id",aLong);
        List<ParameterKey> parameterKeys = tblProParameterKeyMapper.selectByCondition(condition);
        if(parameterKeys.size()==1){
            if(Constants.EditFlag.ALLOW.equals(parameterKeys.get(0).getEditFlag())){
                ParameterKey parameterKey=parameterKeys.get(0);
                parameterKey.setFlag(new Byte(Constants.Flag.UNVALID));
                parameterKey.setModifyTime(new Date());
                int update= tblProParameterKeyMapper.updateByConditionSelective(parameterKey, condition);
                if(update==1){
                    //删除了属性还需删除属性值，此处属性值为物理删除
                    parameterValueService.deleteByKeyId(aLong);
                    return ApiResult.success();
                }else {
                    return ApiResult.error("删除失败");
                }
            }else {
                //不支持修改
                return ApiResult.success();
            }
        }
        return ApiResult.error("该属性值不存在");
    }
}
