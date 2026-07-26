package com.zhi.system;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;

/**
 * 测试应用程序配置
 * 用于为单元测试提供必要的 Spring Boot 上下文
 */
@SpringBootApplication(scanBasePackages = "com.zhi.system")
@TestConfiguration
public class TestApplication {
}
