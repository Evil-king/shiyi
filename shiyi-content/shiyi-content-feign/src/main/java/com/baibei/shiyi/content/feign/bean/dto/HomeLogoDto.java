package com.baibei.shiyi.content.feign.bean.dto;

import lombok.Data;

@Data
public class HomeLogoDto {
    private long id;
    private long areaId;
    private String areaCode;
    private String logoImg;
    private String logoDisplay;
    private String hotWord;
    private String backImg;
    private String backColor;
    private String H5Id;
}
