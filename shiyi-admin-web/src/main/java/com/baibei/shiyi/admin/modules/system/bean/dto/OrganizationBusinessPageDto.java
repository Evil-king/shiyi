package com.baibei.shiyi.admin.modules.system.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;


@Data
public class OrganizationBusinessPageDto extends PageParam {


    private String orgCode;
    /**
     * 组织机构名称
     */
    private String orgName;
    /**
     * 组织机构类型，business：事业部；organization：机构，branchOffice:分公司
     */

    private String orgType;

    /**
     * 注册手机号
     */
    private String registerMobile;
}
