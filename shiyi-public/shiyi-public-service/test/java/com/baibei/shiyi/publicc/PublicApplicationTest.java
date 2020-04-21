package com.baibei.shiyi.publicc;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.baibei.shiyi.publicc.dao.SmsMapper;
import com.baibei.shiyi.publicc.model.Sms;
import com.baibei.shiyi.publicc.service.ISmsService;
import com.baibei.shiyi.publicc.sms.croe.SmsBean;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * @author hwq
 * @date 2019/05/29
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class PublicApplicationTest {

    @Autowired
    private ISmsService smsService;
    @Autowired
    private SmsMapper smsMapper;

    @Test
    public void Test01() {
        String phone = "13711449453";
        String type = "1";
        String code = "945827";
        ApiResult apiResult = smsService.validateCode(phone, code);
        log.info("apiResult{}", apiResult);

//        String phone = "13711449453";
//        String nowDay = DateUtil.get1DayByNowTime();
//        int count = smsMapper.selectByParams(phone,nowDay);
//        if(count > 1){
//            log.info("1111111");
//        }
    }

    @Test
    public void Test02() {
        String VerifyCode = getRandomNumber(6);
        SmsBean sb = new SmsBean("", "13755588979", "", "", VerifyCode, 3, 4, "");
        SmsBean bean = template(sb);
        log.info("Bean{}", JSONObject.toJSONString(bean));
    }

    public static SmsBean template(SmsBean sb) {
        try {
            String ss = "{'1':{'1':'您的验证码是XXXX','2':'您的验证码是XXXX','3':'您的验证码是XXXX'},'2':{'0':'您的验证码为:XXXX。（安全验证码，请勿将此提供他人）。','1':'验证码为XXXX，您正在进行（注册）操作，切勿将验证码泄露于他人。','2':'验证码为:XXXX，您正在进行（重置登录密码）操作，切勿将验证码泄露于他人。','3':'验证码为XXXX，您正在进行（找回密码）操作，切勿将验证码泄露于他人。','4':'验证码为XXXX，您正在进行（寄售）操作，切勿将验证码泄露于他人。','5':'验证码为XXXX，您正在进行（重置资金密码）操作，切勿将验证码泄露于他人。','6':'验证码为XXXX，您正在进行（寄售）操作，切勿将验证码泄露于他人。'},'3':{'0':'您的验证码为:XXXX。（安全验证码，请勿将此提供他人）。','1':'验证码为XXXX，您正在进行（注册）操作，切勿将验证码泄露于他人。','2':'验证码为:XXXX，您正在进行（重置登录密码）操作，切勿将验证码泄露于他人。','3':'验证码为XXXX，您正在进行（找回密码）操作，切勿将验证码泄露于他人。','4':'验证码为XXXX，您正在进行（寄售）操作，切勿将验证码泄露于他人。','5':'验证码为XXXX，您正在进行（重置资金密码）操作，切勿将验证码泄露于他人。','6':'验证码为XXXX，您正在进行（寄售）操作，切勿将验证码泄露于他人。'},'5':{'0':'【云木测试】您的验证码为:XXXX。（安全验证码，请勿将此提供他人）。','1':'【云木测试】验证码为XXXX，您正在进行（注册操作），切勿将验证码泄露于他人。','2':'【云木测试】验证码为:XXXX，您正在进行（重置登录密码操作），切勿将验证码泄露于他人。','3':'【云木测试】验证码为XXXX，您正在进行（找回密码操作），切勿将验证码泄露于他人。','4':'【云木测试】验证码为XXXX，您正在进行（寄售操作），切勿将验证码泄露于他人。','5':'【云木测试】验证码为XXXX，您正在进行（重置资金密码操作），切勿将验证码泄露于他人。','6':'【云木测试】验证码为:XXXX，您正在进行（管理后台登录），切勿将验证码泄露于他人。','7': '【云木测试】验证码为XXXX，您正在进行（支付操作），切勿将验证码泄露于他人。'},'4':{'0':'{0}行情报价已有{1}分钟没有变化，请联系行情业务相关人处理，最后一口报价时间{2}。'}}";

            String defaultSms = "您的验证码是XXXX;";

            JSONObject templateObj = JSONObject.parseObject(new String(ss.getBytes("ISO-8859-1"), "UTF-8"));
            if (templateObj != null) {
                JSONObject temp = templateObj.getJSONObject(String.valueOf(sb.getSmsType()));
                if (temp != null) {
                    String content = temp.getString(sb.getType().toString());
                    if (sb.getSmsType() == 4) {//发送短信模板
                        String contentRep = sb.getVerifyCode();
                        if (content != null && !"".equals(content) && contentRep != null && !"".equals(contentRep)) {
                            try {
                                String[] crArray = contentRep.split(",");
                                for (int i = 0; i < crArray.length; i++) {
                                    String[] crMap = crArray[i].split("=");
                                    String k = crMap[0];
                                    String v = crMap[1];
                                    content = content.replace(k, v);
                                }
                                sb.setContent(sb.getSmsSignature() + content);
                            } catch (Exception e) {
                                log.info("短信模板替换参数格式错误==>" + contentRep);
                            }
                        }
                    } else {
                        sb.setContent(sb.getSmsSignature()
                                + ((content != null && !"".equals(content)) ? content.replace(
                                "XXXX", sb.getVerifyCode()) : new String(defaultSms
                                .getBytes("ISO-8859-1"), "UTF-8")).replace("XXXX",
                                sb.getVerifyCode()));
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb;
    }

    public static String getRandomNumber(int n) {
        if (n < 1 || n > 10) {
            throw new IllegalArgumentException("cannot random " + n + " bit number");
        }
        Random ran = new Random();
        if (n == 1) {
            return String.valueOf(ran.nextInt(10));
        }
        int bitField = 0;
        char[] chs = new char[n];
        for (int i = 0; i < n; i++) {
            while (true) {
                int k = ran.nextInt(10);
                if ((bitField & (1 << k)) == 0) {
                    bitField |= 1 << k;
                    chs[i] = (char) (k + '0');
                    break;
                }
            }
        }
        return new String(chs);
    }

    public static void main(String[] args) {

    }
}
