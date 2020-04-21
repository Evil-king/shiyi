package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/9/6 14:12
 * @description:
 */
@Data
public class DeleteIdsDto {
    @NotNull(message = "需要删除的id不能为空")
    private List<Long> ids;
}
