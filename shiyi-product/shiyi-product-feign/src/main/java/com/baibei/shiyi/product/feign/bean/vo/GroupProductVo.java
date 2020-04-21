package com.baibei.shiyi.product.feign.bean.vo;

import com.baibei.shiyi.common.tool.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Classname GroupProductVo
 * @Description 组商品vo
 * @Date 2019/7/30 11:01
 * @Created by Longer
 */
@Data
public class GroupProductVo extends BaseIndexProductVo{

    /**
     * 上架状态
     */
    private String status;

    /**
     * 上架中文描述
     */
    private String statusTxt;

    /**
     * 后台类目名称
     */
    private String typeTitle;

    /**
     * 上架编码
     */
    private String shelfNo;

}
