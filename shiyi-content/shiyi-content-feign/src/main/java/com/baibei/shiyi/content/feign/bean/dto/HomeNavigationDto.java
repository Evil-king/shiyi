package com.baibei.shiyi.content.feign.bean.dto;

import lombok.Data;

import java.util.List;

@Data
public class HomeNavigationDto {
    private long id;
    private long areaId;
    private String areaCode;
    private String backImg;
    private String backColor;
    private String H5Id;
    List<HomeNavigationDetailsDto> list;
}
