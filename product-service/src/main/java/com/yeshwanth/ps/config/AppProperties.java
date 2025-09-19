package com.yeshwanth.ps.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@RefreshScope
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private Map<String, String> messages = new HashMap<>();
}
