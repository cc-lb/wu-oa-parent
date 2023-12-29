package com.wu.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Classname WechatAccountConfig
 * @Description
 * @Date 2023/6/11 14:51
 * @Created by cc
 */



@Data
@Component
@ConfigurationProperties(prefix="wechat")
public class WechatAccountConfig {
    private String mpAppId;

    private String mpAppSecret;

}
