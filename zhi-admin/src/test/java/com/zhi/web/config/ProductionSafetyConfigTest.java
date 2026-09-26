package com.zhi.web.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 生产配置防线守卫测试。
 *
 * <p>这些配置项一旦回退，表现是"开发好好的、生产才炸"（验证码不发信、上传 413、
 * 后端被直连绕过限流），所以用测试钉住。</p>
 */
class ProductionSafetyConfigTest {

    private static Map<String, Object> loadYaml(Path path) throws IOException {
        try (InputStream in = Files.newInputStream(path)) {
            return new Yaml().load(in);
        }
    }

    /** 读取经 Maven 过滤后的 classpath 配置（源码里的 @app.version@ 占位符不是合法 YAML） */
    private static Map<String, Object> loadFilteredYaml(String resource) throws IOException {
        try (InputStream in = ProductionSafetyConfigTest.class.getClassLoader().getResourceAsStream(resource)) {
            assertNotNull(in, "classpath 缺少 " + resource);
            return new Yaml().load(in);
        }
    }

    private static Path repoFile(String relative) {
        // 测试运行目录是模块目录（zhi-admin），仓库根在上一级
        Path fromModule = Path.of("..", relative);
        return Files.exists(fromModule) ? fromModule : Path.of(relative);
    }

    @Test
    @DisplayName("生产环境必须关闭验证码打印到日志（否则不发邮件且跳过 IP 限流）")
    void prodProfileDisablesDevPrintCode() throws IOException {
        Map<String, Object> yaml = loadFilteredYaml("application-prod.yml");
        Object emailCode = yaml.get("email-code");
        assertNotNull(emailCode, "application-prod.yml 必须显式声明 email-code 段");
        assertEquals(Boolean.FALSE, ((Map<?, ?>) emailCode).get("dev-print-code"),
            "生产环境 dev-print-code 必须为 false");
    }

    @Test
    @DisplayName("生产 compose 的 zhi-admin 必须通过 env_file 注入 .env")
    void prodComposeInjectsDotEnvIntoAdmin() throws IOException {
        Map<String, Object> compose = loadYaml(repoFile("docker-compose.prod.yml"));
        Map<?, ?> services = (Map<?, ?>) compose.get("services");
        assertNotNull(services);
        Map<?, ?> admin = (Map<?, ?>) services.get("zhi-admin");
        assertNotNull(admin, "docker-compose.prod.yml 缺少 zhi-admin 服务");
        assertNotNull(admin.get("env_file"),
            "zhi-admin 必须带 env_file: .env，否则 MAIL_*/EMAIL_CODE_*/上传上限等变量在生产全为空值");
    }

    @Test
    @DisplayName("生产后端不得直接暴露到公网端口（应绑定 127.0.0.1，对外走 nginx）")
    void prodComposeDoesNotPublishAdminPublicly() throws IOException {
        Map<String, Object> compose = loadYaml(repoFile("docker-compose.prod.yml"));
        Map<?, ?> services = (Map<?, ?>) compose.get("services");
        Map<?, ?> admin = (Map<?, ?>) services.get("zhi-admin");
        Object ports = admin.get("ports");
        assertNotNull(ports, "zhi-admin 应显式声明端口绑定");
        boolean loopbackOnly = ports.toString().contains("127.0.0.1:8080");
        assertTrue(loopbackOnly,
            "zhi-admin 端口应绑定 127.0.0.1:8080，否则可绕过 nginx 直连后端，限流与审计全部失效：实际 " + ports);
    }
}
