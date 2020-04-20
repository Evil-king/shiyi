package com.baibei.shiyi.admin.modules.system.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class MenuDto {

    private Long id;

    @NotNull(message = "菜单名称不能为空")
    private String name;

    private Long pid;

    /**
     * 菜单类型
     */
    private String menuType;


    private String permission;

    /**
     * 隐藏和显示
     */
    private String hidden;

    /**
     * 请求地址
     */
    private String component;

    /**
     * 排序
     */
    private Long seq;


    /**
     * 备注
     */
    private String remark;

    /**
     * 前缀
     */
    private String prefix;
}
