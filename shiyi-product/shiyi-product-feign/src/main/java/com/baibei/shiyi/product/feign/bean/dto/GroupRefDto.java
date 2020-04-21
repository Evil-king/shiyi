package com.baibei.shiyi.product.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

/**
 * @Classname GroupDto
 * @Description TODO
 * @Date 2019/7/30 11:01
 * @Created by Longer
 */
@Data
public class GroupRefDto extends PageParam {
    private Long shelfId;
    /**
     * 组id
     */
    private Long groupId;

    /**
     * 组类型（common=普通类型，hot=热门推荐，new=新品上市）
     */
    private String groupType;

    /**
     * 前端显示总条数
     */
    private int viewCount;
}
