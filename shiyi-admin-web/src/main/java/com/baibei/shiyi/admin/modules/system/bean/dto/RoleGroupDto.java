package com.baibei.shiyi.admin.modules.system.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RoleGroupDto {

    private Long id;

    @NotNull(message = "角色组名不能为空")
    private String name;

    private Long seq;

}
