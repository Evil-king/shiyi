package com.baibei.shiyi.admin.modules.security;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author jie
 * @date 2018-11-23
 */
@Getter
@AllArgsConstructor
public class JwtUser implements UserDetails {

    @JSONField(serialize = false)
    private final Long id;

    private final String username;

    @JSONField(serialize = false)
    private final String password;

    public Long getId() {
        return id;
    }

    private final Long organizationId;

    private final String realname;

    private final String mobile;

    private final String orgType;

    private final String position;

    private final String salt;

    @JSONField(serialize = false)
    private final Collection<GrantedAuthority> authorities;

    private final String  userStatus;

    private Date createTime;


    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @JSONField(serialize = false)
    @Override
    public String getPassword() {
        return password;
    }


    public Collection getRoles() {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
