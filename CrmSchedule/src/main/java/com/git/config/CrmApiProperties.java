package com.git.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * CRM API 配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "crm.api")
public class CrmApiProperties {

    /**
     * API ID
     */
    private String id;

    /**
     * Secret ID
     */
    private String secretId;

    /**
     * Secret Key
     */
    private String secretKey;

    /**
     * API URL
     */
    private String url;

    /**
     * 数据类型
     */
    private String datatype;
}
