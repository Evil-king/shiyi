package com.baibei.shiyi.content.feign.bean.dto;

import lombok.Data;

@Data
public class IOSAddVersionDto {
    private String version;

    private String updown;//1上架 0下架

    private String id;
}
