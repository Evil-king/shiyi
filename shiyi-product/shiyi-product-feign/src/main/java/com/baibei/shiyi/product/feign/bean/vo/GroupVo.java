package com.baibei.shiyi.product.feign.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Classname GroupVo
 * @Description TODO
 * @Date 2019/7/30 11:01
 * @Created by Longer
 */
@Data
public class GroupVo {

    private Long id;

    /**
     * 组名
     */
    private String title;

    /**
     * 商品组类型（common=普通类型，hot=热门推荐，new=新品上市）
     */
    private String groupType;

    /**
     * 商品数量
     */
    private Integer productCount;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;

    /**
     * 分组商品信息
     */
    List<GroupProductVo> groupProductVoList;

}
