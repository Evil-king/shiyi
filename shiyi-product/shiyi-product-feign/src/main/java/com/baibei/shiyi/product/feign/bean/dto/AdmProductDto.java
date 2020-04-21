package com.baibei.shiyi.product.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Classname AdmProductDto
 * @Description TODO
 * @Date 2019/9/2 18:09
 * @Created by 后台管理商品dto
 */
@Data
public class AdmProductDto extends PageParam {
    /**
     * 货号
     */
    private String spuNo;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 后台类目id
     */
    private Long typeId;

    /**
     * 品牌
     */
    private Long brandId;

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
