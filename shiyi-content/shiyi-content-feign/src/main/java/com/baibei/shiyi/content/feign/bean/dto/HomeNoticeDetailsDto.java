package com.baibei.shiyi.content.feign.bean.dto;

import lombok.Data;

@Data
public class HomeNoticeDetailsDto {
    private long id;
    private long noticeId;
    private String title;
    private String url;
    private String H5Id;
}
