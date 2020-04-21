package com.baibei.shiyi.publicc.sms.process;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.publicc.dao.SmsMapper;
import com.baibei.shiyi.publicc.model.Sms;
import com.baibei.shiyi.publicc.sms.PropertiesVal;
import com.baibei.shiyi.publicc.sms.SmsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Date;


/**
 * @author hwq
 * @date 2019/05/29
 */
@Slf4j
@Component
public class YXTProcess extends SmsTemplate {

    @Autowired
    private PropertiesVal propertiesVal;
    @Autowired
    private SmsMapper smsMapper;

    @Override
    protected boolean doSend(String phone, String message) {
        try {
            //组装参数请求到渠道
            String sname = propertiesVal.getUsername();//账户
            String spwd = propertiesVal.getPassword();//提交账户的密码
            String sprdid = propertiesVal.getProductNum();//产品编号
            String sdst = phone;//手机号
            String smsg = message;//短信内容(短信模版+验证码)

            StringBuffer sbf = new StringBuffer();
            sbf.append("sname=").append(sname).append("&")
                    .append("spwd=").append(spwd).append("&")
                    .append("sprdid=").append(sprdid).append("&")
                    .append("sdst=").append(sdst).append("&")
                    .append("smsg=").append(java.net.URLEncoder.encode(smsg + propertiesVal.getSign(), "utf-8"));
            log.info("sbf{}", sbf);

            String result = SMS(sbf.toString(), propertiesVal.getUrl_submit());
            log.info("result:{}", result);

            if (StringUtils.isEmpty(result)) {
                return false;
            }
            JSONObject jsonObject = JSONObject.parseObject(result);
            String state = jsonObject.getString("State");
            String MsgState = jsonObject.getString("MsgState");
            log.info("渠道返回结果：state{},MsgState{}", state, MsgState);
            if ("0".equalsIgnoreCase(state)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("e{}", e);
        }
        return false;
    }

    @Override
    protected boolean parseResultToBoolean(Boolean messageResult) {
        return messageResult;
    }

    @Override
    protected int writeToDb(String phone, String message) {
        Sms sms = new Sms();
        sms.setCode(message);
        sms.setMobile(phone);
        sms.setFlag(1);
        sms.setModifyTime(new Date());
        sms.setCreateTime(new Date());
        return smsMapper.insert(sms);
    }

    private String SMS(String postData, String postUrl) {
        try {
            //发送POST请求
            URL url = new URL(postUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Length", "" + postData.length());
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(postData);
            out.flush();
            out.close();

            //获取响应状态
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("connect failed!");
                return "";
            }
            //获取响应内容体
            String line, result = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            in.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return "";
    }
}
