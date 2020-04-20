package com.baibei.shiyi.content.feign.bean.dto;

import lombok.Data;

import java.util.List;

@Data
public class HomeProductDto {
    private long id;
    private long areaId;
    private String areaCode;
    private String backColor;
    private String backImg;
    private String listStyle;
    private String H5Id;
    List<HomeProductDetailsDto> list;
}
