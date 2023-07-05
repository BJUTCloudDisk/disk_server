package com.bjut.bjut_clouddisk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="disk")
@Data
@Component
public class DiskConfig {
    private String diskRootPath;
    private String IP;
    private String port;
}
