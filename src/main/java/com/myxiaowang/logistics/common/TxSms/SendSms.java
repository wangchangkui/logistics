package com.myxiaowang.logistics.common.TxSms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.myxiaowang.logistics.config.PropertiesConfig;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月08日 10:21:00
 */
@Component
@RequiredArgsConstructor
public class SendSms {
   private final Logger logger = LoggerFactory.getLogger(SendSms.class);
   private final PropertiesConfig propertiesConfig;

    /**
     * 短信发送
     * @param phone 手机号
     * @param code 验证码
     * @return 发送结果
     */
   public boolean sendSms(String phone,String code){
       // 签证
       Credential credential=new Credential(propertiesConfig.getTx_Secrity_Id(),propertiesConfig.getTx_Secrity_key());
       // 请求构造
       HttpProfile httpProfile=new HttpProfile();
       httpProfile.setEndpoint("sms.tencentcloudapi.com");
       ClientProfile clientProfile = new ClientProfile();
       clientProfile.setHttpProfile(httpProfile);
       // 腾讯云配置
       SmsClient smsClient=new SmsClient(credential,"ap-guangzhou",clientProfile);
       SendSmsRequest req = new SendSmsRequest();
       // 发送手机号
       String[] phoneNumberSet1 = {"86"+phone};
       // 短信配置
       req.setPhoneNumberSet(phoneNumberSet1);
       req.setSmsSdkAppid(propertiesConfig.getTxSkdId());
       req.setSign("myxiaowang公众");
       req.setTemplateID(propertiesConfig.getTemplate_ID());
       String[] templateParamSet1 = {code};
       req.setTemplateParamSet(templateParamSet1);
       try {
           // 发送结果验证
           SendSmsResponse resp = smsClient.SendSms(req);
           String s = SendSmsResponse.toJsonString(resp);
           JSONObject jsonObject = JSON.parseObject(s);
           Object code1 = jsonObject.get("Fee");
           if(Objects.equals(code1,1)){
               return true;
           }
       } catch (TencentCloudSDKException e) {
           logger.error(e.getMessage());
       }
       return false;
   }
}
