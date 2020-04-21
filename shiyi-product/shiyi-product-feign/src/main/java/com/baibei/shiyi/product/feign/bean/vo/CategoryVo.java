package com.baibei.shiyi.product.feign.bean.vo;

import lombok.Data;

/**
 * @Classname CategoryVo
 * @Description TODO
 * @Date 2019/7/30 18:12
 * @Created by Longer
 */
@Data
public class CategoryVo {
    private Long id;

    /**
     * (0标识顶层)
     */
    private long parentId;

    /**
     * 分类标题（类型名称）
     */
    private String title;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 是否是末端类目（1：是；0：否）
     */
    private Byte end;

    /**
     * 是否隐藏（show：显示；hidden：隐藏）
     */
    private String showStatus;

    private String img;

    /**
     * 上架商品Id
     */
    private Long shelfId;
}
