package com.baibei.shiyi.content.feign.bean.dto;

import lombok.Data;

@Data
public class HomeBannerDetailsDto {
    private long id;
    private long bannerId;
    private String image;
    private String title;
    private String url;
    private String H5Id;
}
