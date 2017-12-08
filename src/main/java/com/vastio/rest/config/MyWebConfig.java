package com.vastio.rest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author 陈晓宇
 * @version 创建时间：2017年9月25日 下午5:18:06 类说明
 */
@ConfigurationProperties(prefix = "web")
@PropertySource(value = "classpath:config/my-web.properties")
@Component
@Data
public class MyWebConfig {
    private String perpUrl;
    private String pythonUrl;
}
