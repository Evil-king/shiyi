package com.baibei.shiyi.content.feign.bean.dto;

import lombok.Data;

import java.util.List;

@Data
public class HomeActivityDto {
    private long id;
    private long areaId;
    private String areaCode;
    private String backColor;
    private String backImg;
    private String H5Id;
    List<HomeActivityDetailsDto> list;
}
