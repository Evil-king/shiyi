package com.baibei.shiyi.admin.modules.system.bean.vo;

import lombok.Data;

import java.util.List;

@Data
public class RoleVo {

    private Long id;

    private String name;

    private Long seq;

    private String roleType;

    private Long pid;

    private List<Long> menuId;

    private List<RoleVo> childList;

}
