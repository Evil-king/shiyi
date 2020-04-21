package com.baibei.shiyi.user.common.vo;

import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/9/3 10:05
 * @description:
 */
@Data
public class QrcodeVo {
    /**
     * 二维码地址
     */
    private String Qrcode;
    /**
     * 邀请信息
     */
    private String message;

    /**
     * IOS二维码地址
     */
    private String iosQrcode;

    /**
     * 安卓二维码地址
     */
    private String androidQrcode;

}
