package com.myxiaowang.logistics.util.OSS;

import com.myxiaowang.logistics.config.PropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月10日 13:57:00
 */
@Component
@RequiredArgsConstructor
public class OssUtil {
    private final PropertiesConfig config;
    private final OSSClient.OSSBuilder ossBuilder;

    private volatile OSSClient ossClient;
    public  OSSClient getClient(){
        if(ossClient == null){
            synchronized (OssUtil.class){
                if(ossClient==null){
                    OSSClient.OSSBuilder ossBuilder = this.ossBuilder.setEND_POINT(config.getEND_POINT())
                            .setACCESS_KEY_ID(config.getACCESS_KEY_ID())
                            .setACCESS_KEY_SECRET(config.getACCESS_KEY_SECRET());
                    ossClient=ossBuilder.buidOSS(ossBuilder);
                }
            }
        }

        return ossClient;
    }
}
