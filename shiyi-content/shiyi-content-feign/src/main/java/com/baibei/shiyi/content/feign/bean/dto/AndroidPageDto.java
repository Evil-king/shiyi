package com.baibei.shiyi.content.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

@Data
public class AndroidPageDto extends PageParam {

    private String channelName;

    private String version;

    private String updown;//1上架 0下架 all全部
}
