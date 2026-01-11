# 测试配置总结

## ✅ 已完成的配置

### 前端测试配置 (Vue 3 + Vitest)

#### 创建的文件：
- [ruoyi-ui/vitest.config.ts](../ruoyi-ui/vitest.config.ts) - Vitest 配置
- [ruoyi-ui/src/tests/setup.ts](../ruoyi-ui/src/tests/setup.ts) - 测试设置
- [ruoyi-ui/src/utils/validate.test.ts](../ruoyi-ui/src/utils/validate.test.ts) - 测试示例
- [ruoyi-ui/src/composables/useArticleList.test.ts](../ruoyi-ui/src/composables/useArticleList.test.ts) - Composable 测试示例

#### 更新的文件：
- [ruoyi-ui/package.json](../ruoyi-ui/package.json) - 添加测试脚本和依赖

#### 依赖包：
```json
{
  "@vue/test-utils": "^2.4.6",
  "jsdom": "^25.0.1",
  "vitest": "^2.1.8",
  "@vitest/ui": "^2.1.8",
  "@vitest/coverage-v8": "^2.1.8"
}
```

#### 使用方法：
```bash
cd ruoyi-ui

# 运行测试
npm run test

# 运行测试并生成覆盖率报告
npm run test:coverage

# 运行测试 UI
npm run test:ui
```

### 后端测试配置 (Java 17 + JUnit 5)

#### 创建的文件：
- [ruoyi-admin/src/test/resources/application-test.yml](../ruoyi-admin/src/test/resources/application-test.yml) - 测试配置

#### 更新的文件：
- [pom.xml](../pom.xml) - 添加测试依赖和 JaCoCo 插件

#### 测试依赖：
```xml
<!-- JUnit 5 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.11.4</version>
    <scope>test</scope>
</dependency>

<!-- Mockito -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.14.2</version>
    <scope>test</scope>
</dependency>

<!-- Spring Boot Test -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- H2 Database (测试用) -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

#### JaCoCo 插件配置：
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.12</version>
    <!-- 配置了 60% 的最低覆盖率要求 -->
</plugin>
```

#### 使用方法：
```bash
# 运行所有测试
mvn test

# 运行测试并生成覆盖率报告
mvn test jacoco:report

# 查看覆盖率报告
open target/site/jacoco/index.html
```

### 现有测试文件

项目中已存在一些测试文件：

```
ruoyi-system/src/test/java/com/ruoyi/system/
├── controller/BlogArticleControllerTest.java
├── service/impl/BlogArticleServiceImplTest.java
└── mapper/BlogArticleMapperTest.java
```

## 📋 测试覆盖率目标

### 前端
- 行覆盖率: ≥ 70%
- 分支覆盖率: ≥ 70%
- 函数覆盖率: ≥ 70%
- 语句覆盖率: ≥ 70%

### 后端
- 行覆盖率: ≥ 60%
- 分支覆盖率: ≥ 60%

## 🎯 快速开始

### 前端测试

1. **安装依赖**：
```bash
cd ruoyi-ui
npm install
```

2. **运行测试**：
```bash
npm run test
```

3. **查看覆盖率报告**：
```bash
npm run test:coverage
# 报告在 coverage/index.html
```

### 后端测试

1. **运行测试**：
```bash
mvn test
```

2. **查看覆盖率报告**：
```bash
mvn test jacoco:report
# 报告在 target/site/jacoco/index.html
```

## 📚 文档

详细文档请查看：
- [docs/TESTING_GUIDE.md](TESTING_GUIDE.md) - 完整测试指南

## 🔧 配置详情

### Vitest 配置要点

- 使用 jsdom 模拟浏览器环境
- 配置了路径别名 `@` 指向 `src` 目录
- 覆盖率排除：node_modules、dist、mock文件等
- 全局测试环境配置在 `src/tests/setup.ts`

### JUnit 配置要点

- 使用 JUnit 5 (Jupiter)
- Mockito 用于 Mock
- H2 内存数据库用于测试
- JaCoCo 用于代码覆盖率
- 测试配置在 `application-test.yml`

## 📝 下一步

1. **编写更多测试**：
   - 为核心业务逻辑编写单元测试
   - 为 API 端点编写集成测试
   - 为组件编写前端测试

2. **提高覆盖率**：
   - 逐步提高测试覆盖率到目标值
   - 重点关注核心业务逻辑
   - 补充边界条件测试

3. **集成 CI/CD**：
   - 在 GitHub Actions 中运行测试
   - 自动生成覆盖率报告
   - 设置覆盖率阈值

## 🐛 故障排除

### 前端测试问题

**问题**: 测试运行失败
```bash
# 清理缓存重新安装
rm -rf node_modules package-lock.json
npm install
```

### 后端测试问题

**问题**: 测试连接数据库失败
```bash
# 检查测试配置是否使用 H2
# 确保 application-test.yml 配置正确
```

**问题**: JaCoCo 报告未生成
```bash
# 先编译再运行测试
mvn clean test jacoco:report
```

## 📊 测试命令速查

### 前端

```bash
npm run test              # 运行测试
npm run test:ui           # UI 模式
npm run test:coverage     # 生成覆盖率报告
```

### 后端

```bash
mvn test                              # 运行所有测试
mvn test -Dtest=ClassName             # 运行单个测试类
mvn test -Dtest=ClassName#methodName  # 运行单个测试方法
mvn test jacoco:report                # 生成覆盖率报告
mvn clean test jacoco:report          # 清理并运行测试生成报告
```

---

**配置日期**: 2026-01-10
**版本**: v4.0.1
