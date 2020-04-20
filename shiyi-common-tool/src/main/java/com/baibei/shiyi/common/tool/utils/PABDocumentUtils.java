package com.baibei.shiyi.common.tool.utils;

import com.sdb.util.Base64;
import com.sdb.util.SignUtil;


import java.security.SecureRandom;
import java.util.Random;


public class PABDocumentUtils {


    private final static String ALG = "DesEde/CBC/PKCS5Padding"; // 加密的方式


    /**
     * @param filePath 文件在本地的路径,包含文件名
     * @return 文件加密
     * @throws Exception
     */
    public static String FileEncrypt(String filePath) throws Exception {
        Random random = new SecureRandom();
        byte[] bkey = new byte[24];
        random.nextBytes(bkey);
        // stop 1 生成文件加密的密码
        String password = new String(Base64.encode(bkey));

        // stop 2 生成加密的文件
        String srcFile = filePath;
        String zipFile = srcFile + ".zip";
        String encFile = srcFile + ".enc";
        // stop 3 加压
        SignUtil.compress(srcFile, zipFile);
        // stop 4 加密
        SignUtil.encrypt(zipFile, encFile, bkey, ALG, "DesEde", null);
        return password;
    }

    /**
     * @param filePath 文件在本地的路径,包含文件名
     * @param password 文件解密需要的密码
     * @return
     */
    public static void FileDecryption(String filePath, String password) throws Exception {
        String desFile = filePath;
        String srcFile = filePath + ".enc";
        String zipFile = filePath + ".zip";
        // stop 1解密需要加密的密码，如果没有key则解密失败
        byte[] bkey = Base64.decode(password.getBytes());
        // stop 2 解密
        SignUtil.decrypt(srcFile, zipFile, bkey, ALG, "DesEde", null);
        // stop 3 解压
        SignUtil.uncompress(zipFile, desFile);
    }

}
