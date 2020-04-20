package com.baibei.shiyi.admin.modules.security;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author jie
 * @date 2018-11-30
 */
@Getter
@Setter
public class AuthorizationUser {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String code;

    @Override
    public String toString() {
        return "{username=" + username  + ", password= ******}";
    }
}
