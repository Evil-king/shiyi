package com.baibei.shiyi.content.feign.bean.dto;

import lombok.Data;

import java.util.List;

@Data
public class HomeNoticeDto {
    private long id;
    private long areaId;
    private String areaCode;
    private String backColor;
    private String backImg;
    private String textColor;
    private String image;
    private String scrollingSpeed;
    private String H5Id;
    List<HomeNoticeDetailsDto> list;
}
