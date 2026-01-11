package com.ruoyi.system;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;

/**
 * 测试应用程序配置
 * 用于为单元测试提供必要的 Spring Boot 上下文
 */
@SpringBootApplication(scanBasePackages = "com.ruoyi.system")
@TestConfiguration
public class TestApplication {
}
