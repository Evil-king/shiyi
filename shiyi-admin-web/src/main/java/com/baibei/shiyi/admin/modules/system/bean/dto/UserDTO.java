package com.baibei.shiyi.admin.modules.system.bean.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.omg.CORBA.DATA_CONVERSION;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * @author jie
 * @date 2018-11-23
 */
@Data
public class UserDTO implements Serializable {

    private Long id;

    private String username;

    private String realname;

    private String salt;

    private String mobile;

    private String userStatus;
    private String orgType;

    @JsonIgnore
    private String password;

    private Long organizationId;


    private Date createTime;


    private Set<RoleSmallDTO> roles;


    private String position;

    private Date modifyTime;
}
