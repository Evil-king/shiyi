package com.baibei.shiyi.admin.modules.system.bean.dto;

import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/10/16 16:04
 * @description:
 */
@Data
public class OrganizationTypeDto {
    /**
     * 机构类型（platform：平台；business：事业部；organization：机构；cityAgent：市代理；areaAgent：区代理；ordinaryAgent：普通代理；branchOffice:分公司）
     */
    private String orgType;
}
