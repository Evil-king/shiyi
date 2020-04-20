package com.baibei.shiyi.content.feign.bean.dto;

import lombok.Data;

@Data
public class HomeActivityDetailsDto {
    private long id;
    private long activityId;
    private String image;
    private String title;
    private String url;
    private String H5Id;
}
