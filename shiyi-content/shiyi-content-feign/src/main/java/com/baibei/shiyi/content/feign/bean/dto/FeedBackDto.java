package com.baibei.shiyi.content.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

@Data
public class FeedBackDto extends PageParam {
    private String userName;
    private String mobile;
}
