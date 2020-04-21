package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @Classname SumGroupProductDto
 * @Description TODO
 * @Date 2019/10/22 16:52
 * @Created by Longer
 */
@Data
public class SumGroupProductDto{
    @NotNull(message = "组id不能为空")
   private Long groupId;

}
