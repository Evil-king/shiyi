package com.baibei.shiyi.common.tool.utils;

import java.util.Random;

/**
 * @author: hyc
 * @date: 2019/5/28 18:19
 * @description:
 */
public class VerificationCodeUtils {
    private static String str="abcdefghigklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789";

    public  static String   getRandomCode(Integer number){
        Random r=new Random();
        String arr[]=new String [number];
        String b="";
        for(int i=0;i<number;i++)
        {
            int n=r.nextInt(62);

            arr[i]=str.substring(n,n+1);
            b+=arr[i];

        }
        return b;
    }


}
