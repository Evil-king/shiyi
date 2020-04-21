package com.baibei.shiyi.product.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Classname AdmSelectProductShelfDto
 * @Description 商品上下架列表
 * @Date 2019/9/2 18:09
 * @Created by Longer
 */
@Data
public class AdmSelectProductShelfDto extends PageParam {
    /**
     * 货号
     */
    private String spuNo;

    /**
     * 商品上架名称
     */
    private String shelfName;

    /**
     * 后台类目id
     */
    private Long typeId;

    /**
     * 上架编码
     */
    private String shelfNo;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
