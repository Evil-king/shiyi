package com.baibei.shiyi.content.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class AdminHelpCenterDto {

    private Long Id;

    @NotNull(message = "标题不能为空")
    private String title;

    private String link;

    private String content;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 是否隐藏(1:隐藏,0:显示)
     */
    private String hidden;

    Set<String> ids;

    /**
     * 批量类型delete:删除 update:修改
     */
    private String batchType;

}
