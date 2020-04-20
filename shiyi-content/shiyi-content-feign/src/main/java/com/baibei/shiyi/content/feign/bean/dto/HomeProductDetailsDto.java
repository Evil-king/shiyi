package com.baibei.shiyi.content.feign.bean.dto;

import lombok.Data;

@Data
public class HomeProductDetailsDto {
    private long id;
    private long homeProductId;
    private long groupId;
    private String commoditySource;
    private int displayCount;
    private String displayStyle;
    private String menuName;
    private String H5Id;
}
