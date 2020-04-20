package com.baibei.shiyi.content.feign.bean.dto;

import lombok.Data;

@Data
public class HomeNavigationDetailsDto {
    private long id;
    private long navigationId;
    private String image;
    private String title;
    private String url;
    private String H5Id;
}
