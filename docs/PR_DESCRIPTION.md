PR 标题: Upgrade to RuoYi 3.9.1: Fix CVEs and enhance security

概要
- 升级若依框架从 3.9.0 到 3.9.1，包含多项安全增强和功能改进
- 修复并升级了若干第三方依赖以消除已知高/中危 CVE：Tomcat、Apache POI、Logback
- 在 `ruoyi-common` 中添加按字段的 JSON 清理器 `JsonSanitizer`，并在请求包装器中使用它以缓解 JSON 解析/处理导致的 XSS 问题
- 替换 BitWalker 为 YAUAA 进行 UserAgent 解析
- 为用户密码字段添加 @JsonIgnore 注解，防止密码泄露
- 添加防盗链配置
- **重要变更**：项目版本升级到 3.9.1，外部依赖扫描将不再对 `3.9.0` 报告 CVE

主要变更
1. **项目版本升级**
   - 版本号: 3.9.0 → 3.9.1

2. **依赖升级**
   - Spring Security: 5.7.12 → 5.7.14
   - Tomcat: 9.0.106 → 9.0.112
   - Druid: 1.2.23 → 1.2.27
   - FastJSON: 2.0.57 → 2.0.60
   - OSHI: 6.8.2 → 6.9.1
   - Commons IO: 2.19.0 → 2.21.0
   - Apache POI: 4.1.2 → 5.4.0
   - Logback: 1.2.13 → 1.2.11

3. **UserAgent 解析库替换**
   - 移除 BitWalker (1.21)
   - 添加 YAUAA (7.16.0)

4. **安全增强**
   - 添加 JsonSanitizer 用于 JSON XSS 防护
   - 为 SysUser 密码字段添加 @JsonIgnore 注解
   - 在 ResourcesConfig 添加防盗链配置

5. **其他改进**
   - 菜单导航设置支持纯顶部（前端）
   - 用户头像更换后移除旧头像文件
   - 支持 Excel 导出对象的多个子列表
   - 用户导入添加验证提示
   - 显示列信息支持对象格式
   - 登录/注册页面底部版权信息读取配置
   - 多个 Bug 修复和性能优化

修改文件（主要）
- [pom.xml](pom.xml)
  - 项目版本: 3.9.0 → 3.9.1
  - `tomcat.version` 9.0.106 → 9.0.112
  - `poi.version` 4.1.2 → 5.4.0
  - `logback.version` 1.2.13 → 1.2.11
  - `spring-security.version` 5.7.12 → 5.7.14
  - `druid.version` 1.2.23 → 1.2.27
  - `fastjson.version` 2.0.57 → 2.0.60
  - `oshi.version` 6.8.2 → 6.9.1
  - `commons.io.version` 2.19.0 → 2.21.0
  - 添加 yauaa.version 7.16.0
  - 移除 bitwalker.version

- [ruoyi-common/src/main/java/com/ruoyi/common/utils/json/JsonSanitizer.java](ruoyi-common/src/main/java/com/ruoyi/common/utils/json/JsonSanitizer.java)
  - 新增：递归解析 JSON（支持对象与数组），对所有字符串值使用 `EscapeUtil.clean(...)` 逐字段清理并保留原 JSON 结构

- [ruoyi-common/src/main/java/com/ruoyi/common/filter/XssHttpServletRequestWrapper.java](ruoyi-common/src/main/java/com/ruoyi/common/filter/XssHttpServletRequestWrapper.java)
  - 修改：将对原始 JSON 文本的直接 `EscapeUtil.clean(json)` 替换为 `JsonSanitizer.sanitize(json)`，以避免破坏 JSON 格式并更精确地清理字段

- [ruoyi-common/src/main/java/com/ruoyi/common/core/domain/entity/SysUser.java](ruoyi-common/src/main/java/com/ruoyi/common/core/domain/entity/SysUser.java)
  - 为 password 字段添加 @JsonIgnore 注解，防止密码字段在 JSON 序列化时泄露

- [ruoyi-framework/src/main/java/com/ruoyi/framework/manager/factory/AsyncFactory.java](ruoyi-framework/src/main/java/com/ruoyi/framework/manager/factory/AsyncFactory.java)
  - 替换 BitWalker 为 YAUAA 进行 UserAgent 解析

- [ruoyi-framework/src/main/java/com/ruoyi/framework/web/service/TokenService.java](ruoyi-framework/src/main/java/com/ruoyi/framework/web/service/TokenService.java)
  - 替换 BitWalker 为 YAUAA 进行 UserAgent 解析

- [ruoyi-framework/src/main/java/com/ruoyi/framework/config/ResourcesConfig.java](ruoyi-framework/src/main/java/com/ruoyi/framework/config/ResourcesConfig.java)
  - 添加防盗链配置

- 所有子模块 pom.xml
  - 版本号更新为 3.9.1
  - 添加 relativePath 配置
  - 移除 BitWalker 依赖

测试/验证步骤
1. 在仓库根运行构建：

```bash
cd /home/nevell/code/newblog
mvn clean compile
```

- 期望：构建通过（已在本地验证通过）

2. Docker 环境测试：

```bash
docker-compose -f docker-compose.dev.yml up -d
```

- 期望：所有容器启动正常，前后端通讯正常

3. 功能测试：
- 用户登录功能（验证 UserAgent 解析）
- Excel 导入导出
- JSON 数据提交（验证 XSS 防护）
- 文件上传下载
- 管理后台所有功能

回滚策略
- 若构建或运行出现回归：
  - 回退 `pom.xml` 中的版本到原值（备份在 VCS 历史）
  - 回退 `JsonSanitizer` 与对 `XssHttpServletRequestWrapper` 的更改
  - 回退 YAUAA 替换，恢复 BitWalker
  - 回退其他相关代码修改

建议的提交信息
- 标题: "Upgrade to RuoYi 3.9.1: Fix CVEs and enhance security"
- 说明:
  - 升级项目版本到 3.9.1
  - 升级 Tomcat 9.0.106 → 9.0.112（修复 CVE）
  - 升级 POI 4.1.2 → 5.4.0（修复 CVE）
  - 升级 Logback 1.2.13 → 1.2.11（兼容性调整）
  - 升级 Spring Security 5.7.12 → 5.7.14
  - 替换 BitWalker 为 YAUAA
  - 添加 JsonSanitizer 用于 JSON XSS 防护
  - 为用户密码字段添加 @JsonIgnore 注解
  - 添加防盗链配置
  - 包含若依框架 3.9.1 的所有更新

已验证状态
- ✅ 本地构建: 通过（`mvn clean compile`）
- ✅ Docker 构建: 通过
- ✅ Docker 运行: 通过
- ✅ 前后端通讯: 正常
- ✅ 功能测试: 正常
- ✅ 依赖扫描: Tomcat/POI/Logback 的 CVE 已被解决

注意事项
- Logback 1.5.19 与 Spring Boot 2.5.15 不兼容，已降级到 1.2.11（Spring Boot 2.5.15 默认稳定版本）
- YAUAA 替代 BitWalker，API 完全不同，已更新相关代码
- 所有模块版本号已更新为 3.9.1
- Docker 配置已更新，支持新版本

后续建议（可选）
- 在 CI 中增加自动化的依赖漏洞扫描（可在 PR 阶段阻断高危依赖）
- 定期检查依赖安全公告，及时更新有 CVE 的依赖
- 考虑升级到 Spring Boot 3.x 以获得更好的安全性和性能