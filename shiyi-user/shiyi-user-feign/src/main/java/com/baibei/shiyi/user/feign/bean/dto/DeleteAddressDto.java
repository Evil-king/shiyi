package com.baibei.shiyi.user.feign.bean.dto;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/8/12 15:17
 * @description:
 */
@Data
public class DeleteAddressDto extends CustomerNoDto {
    private Long id;
}
