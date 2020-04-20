package com.baibei.shiyi.admin.modules.system.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

@Data
public class RolePageDto extends PageParam {

    private String roleType;

    private Long pid;

    private String name;

}
