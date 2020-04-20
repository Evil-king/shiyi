package com.baibei.shiyi.admin.modules.system.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class RoleDto {

    private Long id;

    @NotNull(message = "角色名不能为空")
    private String name;

    private Long seq;

    @NotNull(message = "父角色id不能为空")
    private Long pid;

    private Set<Long> menuId;
}
