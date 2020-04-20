package com.baibei.shiyi.admin.modules.system.bean.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MenuVo {

    private Long id;

    private String name;

    private String iFrame;

    private String component;

    private Long pid;

    private Long seq;

    private String icon;

    private String path;

    private String prefix;

    /**
     * 菜单类型
     */
    private String menuType;

    /**
     * 菜单类型文字
     */
    private String menuTypeText;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 隐藏和显示
     */
    private String hidden;

    /**
     * 子菜单
     */
    private List<MenuVo> childList;

    /**
     * 备注
     */
    private String remark;

    /**
     * 用在构建菜单下面的按钮使用
     */
    private List<Map<String, Object>> buttonList;
}
