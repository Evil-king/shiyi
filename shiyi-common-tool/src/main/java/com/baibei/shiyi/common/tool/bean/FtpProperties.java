package com.baibei.shiyi.common.tool.bean;


import lombok.Data;

@Data
public class FtpProperties {

    private String host;

    private int port;

    private String username;

    private String password;

    private String controlEncoding;

    private String remotePath;
}
