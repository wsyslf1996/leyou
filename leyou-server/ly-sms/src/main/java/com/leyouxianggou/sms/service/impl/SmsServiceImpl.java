package com.leyouxianggou.sms.service.impl;

import com.leyouxianggou.common.utils.JsonUtils;
import com.leyouxianggou.common.utils.NumberUtils;
import com.leyouxianggou.sms.config.SmsProperties;
import com.leyouxianggou.sms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

@Service
@Slf4j
@EnableConfigurationProperties(SmsProperties.class)
public class SmsServiceImpl implements SmsService {

    // 短信验证码的key存入Redis的前缀
    private static final String PREFIX = "user:phone:verify:";
    // 验证码长度
    private static final int DEFAULT_VERIFY_CODE_LENGTH = 6;
    // 短信发送频率 60s
    private static final long SMS_MIN_SEND_INTERVEL = 60 * 1000;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SmsProperties smsProperties;

    private void sendMessage(String accessKeyId,
                            String accessSecret,
                            String signName,
                            String templateCode,
                            String phoneNum,
                            Map<String,Object> params){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        // 短信服务中心远程节点Id
        request.putQueryParameter("RegionId", "cn-hangzhou");
        // 手机号码
        request.putQueryParameter("PhoneNumbers", phoneNum);
        // 签名
        request.putQueryParameter("SignName", signName);
        // 模板编号
        request.putQueryParameter("TemplateCode", templateCode);
        // 模板中的参数，JSON格式
        request.putQueryParameter("TemplateParam", JsonUtils.serialize(params));
        try {
            // 短信发送
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            log.error("[短信服务]：用户{}短信发送失败！",phoneNum);
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public void sendVerifyMessage(String phoneNum,String verifyCode){
        String key = PREFIX + phoneNum;
        BoundHashOperations<String, Object, Object> map = redisTemplate.boundHashOps(key);

        // 限流，防止恶意请求无线点短信
        if(map!=null && map.size() > 0){
            String time = map.get("time").toString();
            if(System.currentTimeMillis() - Long.valueOf(time) < SMS_MIN_SEND_INTERVEL){
                log.error("[短信服务]:短信发送频率过快，手机号码:{}",phoneNum);
                return ;
            }
        }

        // TODO 给手机发送验证码
        Map<String, Object> params = new HashMap<>();
        params.put("code",verifyCode);
        sendMessage(smsProperties.getVerifyCodeTemplate(),phoneNum,params);
        // 打印输出手机号验证码
//        System.out.println(phoneNum+"的验证码为:"+verifyCode);

        // 将验证码存入Redis服务器
        map.put("code",verifyCode);
        map.put("time",String.valueOf(System.currentTimeMillis()));
        // 设置超时时长
        redisTemplate.expire(key,5,TimeUnit.MINUTES);
    }

    private void sendMessage(String templateCode,String phoneNum,Map<String,Object> params){
        sendMessage(smsProperties.getAccessKeyId(), smsProperties.getAccessKeySecret(), smsProperties.getSignName(),templateCode,phoneNum,params);
    }

    public void sendVerifyMessage(String phoneNum){
        // 生成验证码
        String verifyCode = NumberUtils.generateCode(DEFAULT_VERIFY_CODE_LENGTH);
        sendVerifyMessage(phoneNum,verifyCode);
    }
}
