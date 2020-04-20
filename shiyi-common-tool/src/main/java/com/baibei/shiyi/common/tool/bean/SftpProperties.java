package com.baibei.shiyi.common.tool.bean;

import lombok.Data;

@Data
public class SftpProperties {

    private String username;

    private String password;

    private String privateKey;

    private String host;

    private int port;

    private String filePath;
}
