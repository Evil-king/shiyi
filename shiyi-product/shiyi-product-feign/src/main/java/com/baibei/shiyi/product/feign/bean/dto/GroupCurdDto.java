package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class GroupCurdDto {

    private Long id;

    /**
     * 组标题（名称）
     */
    @Length(max = 10)
    @NotNull(message = "分组标题不能为空")
    private String title;
    /**
     * 分组类型
     */
    @NotNull(message = "分组类型不能为空")
    private String groupType;

    /**
     * 组内商品
     */
    private Set<String> shelfIds;

    /**
     * 分组Ids
     */
    private Set<String> ids;
}
