package com.baibei.shiyi.content.feign.bean.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class HelpCenterDto extends CustomerBaseDto {

    private Long Id;

    @NotNull(message = "标题不能为空")
    private String title;

    private String link;

    private String content;

    private Integer seq;

    private String hidden;

}
