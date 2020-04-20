package com.baibei.shiyi.admin.modules.system.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;


@Data
public class MenuPageDto extends PageParam {

    private String name;

    private String hidden;

    private String status;

    private String menuType;

    private Long pid;
}
