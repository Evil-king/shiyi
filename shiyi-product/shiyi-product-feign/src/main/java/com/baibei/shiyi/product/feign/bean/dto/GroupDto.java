package com.baibei.shiyi.product.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * @Classname GroupDto
 * @Description TODO
 * @Date 2019/7/30 11:01
 * @Created by Longer
 */
@Data
public class GroupDto extends PageParam {

    /**
     * 查询字段
     */
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;


}
