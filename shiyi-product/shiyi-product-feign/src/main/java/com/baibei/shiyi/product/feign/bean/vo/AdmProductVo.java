package com.baibei.shiyi.product.feign.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Classname AdmProductVo
 * @Description 后台管理商品vo
 * @Date 2019/9/2 18:19
 * @Created by Longer
 */
@Data
public class AdmProductVo {
    private Long id;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 货号
     */
    private String spuNo;
    /**
     * 后台类目
     */
    private String typeTitle;
    /**
     * 品牌
     */
    private String brandTitle;
    /**
     * 类目id
     */
    private Long typeId;
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 商品主图
     */
    private String productImg;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
