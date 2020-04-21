package com.baibei.shiyi.product.feign.bean.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

/**
 * @Classname CheckCollectDto
 * @Description 判断是否收藏dto
 * @Date 2019/9/5 11:36
 * @Created by Longer
 */
@Data
public class CheckCollectDto extends CustomerBaseDto {
    /**
     * 上架id
     */
    private Long shelfId;
}
