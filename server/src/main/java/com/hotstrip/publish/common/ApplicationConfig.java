package com.hotstrip.publish.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Data
@Component(value = "ApplicationConfig")
@ConfigurationProperties(prefix = "application")
public class ApplicationConfig {
    // 文件存储地址
    @Value("${application.baseDirectory}")
    private String baseDirectory;
}
