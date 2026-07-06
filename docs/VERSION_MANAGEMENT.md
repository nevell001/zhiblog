# 版本管理指南

## 版本号统一管理机制

本项目采用统一的版本管理机制，确保所有地方的版本号保持一致。

### 版本号定义位置

**后端版本号**：
- **主配置文件**：`pom.xml` 中的 `<version>` 和 `<app.version>` 属性
- **当前版本**：`1.3.2`

**前端版本号**：
- **配置文件**：`ruoyi-ui/package.json` 中的 `version` 字段
- **当前版本**：`4.1.0`（前端框架版本，与后端版本独立）

### 版本号使用位置

#### 后端

1. **Maven 主配置** (`pom.xml`)
   ```xml
   <groupId>top.nevell</groupId>
   <artifactId>zhiblog</artifactId>
   <version>1.3.2</version>  <!-- 项目版本 -->
   
   <properties>
       <app.version>1.3.2</app.version>  <!-- 应用版本 -->
   </properties>
   ```

2. **应用配置** (`ruoyi-admin/src/main/resources/application.yml`)
   ```yaml
   ruoyi:
     version: @app.version@  # Maven 资源过滤会替换为实际版本号
   ```

3. **生产环境配置** (`ruoyi-admin/src/main/resources/application-prod.yml`)
   ```yaml
   ruoyi:
     version: @app.version@  # 必须使用 Maven 占位符，不能硬编码
   ```
   **重要说明**：生产环境配置文件必须使用 `@app.version@` 占位符，不能硬编码版本号，否则会在生产环境中显示错误的版本号。

4. **配置类** (`ruoyi-common/src/main/java/com/ruoyi/common/config/RuoYiConfig.java`)
   ```java
   @Value("${ruoyi.version:1.3.2}")
   private String version;
   ```

5. **API 接口** (`SysIndexController.java`)
   - 接口路径：`GET /system/version`
   - 返回数据：`{ "version": "1.3.2", "name": "RuoYi" }`

6. **子模块 parent 版本**：
   - `ruoyi-common/pom.xml`
   - `ruoyi-system/pom.xml`
   - `ruoyi-framework/pom.xml`
   - `ruoyi-quartz/pom.xml`
   - `ruoyi-generator/pom.xml`
   - `ruoyi-admin/pom.xml`
   ```xml
   <parent>
       <groupId>top.nevell</groupId>
       <artifactId>zhiblog</artifactId>
       <version>1.3.2</version>  <!-- 必须与父 POM 版本一致 -->
   </parent>
   ```

#### 前端

1. **管理后台首页** (`ruoyi-ui/src/views/admin/dashboard/index.vue`)
   ```typescript
   const version = ref('1.3.2')
   ```
   - 显示位置：系统状态卡片
   - 数据来源：后端 API `/system/version`

### 如何更新版本号

#### 更新后端版本号

**需要修改多个地方**（重要！）：

1. **修改父 POM** (`pom.xml`)
   - 第 8 行：`<version>1.3.1</version>` → `<version>1.3.2</version>`
   - 第 28 行：`<app.version>1.3.1</app.version>` → `<app.version>1.3.2</app.version>`

2. **修改所有子模块的 parent 版本**：
   - `ruoyi-common/pom.xml`
   - `ruoyi-system/pom.xml`
   - `ruoyi-framework/pom.xml`
   - `ruoyi-quartz/pom.xml`
   - `ruoyi-generator/pom.xml`
   - `ruoyi-admin/pom.xml`
   
   每个文件中的：
   ```xml
   <version>1.3.1</version>  →  <version>1.3.2</version>
   ```

3. **重新编译项目**：
   ```bash
   mvn clean install -DskipTests
   ```

4. **重启后端服务**

**自动同步**：
- Maven 构建时会自动将 `application.yml` 中的 `@app.version@` 替换为实际版本号
- 其他地方会从配置文件或 API 中读取最新版本号

#### 批量更新脚本

为了简化版本号更新，可以使用以下命令批量更新：

```bash
# 1. 修改父 POM 的版本号
sed -i 's/<version>1.3.1<\/version>/<version>1.3.2<\/version>/g' pom.xml
sed -i 's/<app.version>1.3.1<\/app.version>/<app.version>1.3.2<\/app.version>/g' pom.xml

# 2. 批量更新所有子模块的 parent 版本
sed -i 's/<version>1.3.1<\/version>/<version>1.3.2<\/version>/g' \
    ruoyi-common/pom.xml \
    ruoyi-system/pom.xml \
    ruoyi-framework/pom.xml \
    ruoyi-quartz/pom.xml \
    ruoyi-generator/pom.xml \
    ruoyi-admin/pom.xml

# 3. 重新编译
mvn clean install -DskipTests
```

#### 更新前端版本号

前端版本号独立管理，用于跟踪前端框架的更新：

1. 打开 `ruoyi-ui/package.json`
2. 修改 `version` 字段
3. 保存文件

### 版本号规范

遵循 [语义化版本](https://semver.org/lang/zh-CN/) 规范：

- **主版本号（MAJOR）**：不兼容的 API 修改
- **次版本号（MINOR）**：向下兼容的功能性新增
- **修订号（PATCH）**：向下兼容的问题修正

示例：`1.3.2`
- `1`：主版本号
- `3`：次版本号
- `2`：修订号

### 版本号检查

#### 后端版本号检查

1. **查看父 POM 配置**：
   ```bash
   grep -E "(<version>|<app.version>)" pom.xml | head -3
   ```

2. **查看子模块 parent 版本**：
   ```bash
   for module in ruoyi-common ruoyi-system ruoyi-framework ruoyi-quartz ruoyi-generator ruoyi-admin; do
       echo "=== $module ==="
       grep -A 5 "<parent>" $module/pom.xml | grep -E "(groupId|artifactId|version)"
   done
   ```

3. **查看编译后的配置**：
   ```bash
   grep "version:" ruoyi-admin/target/classes/application.yml | head -1
   ```

4. **通过 API 检查**：
   ```bash
   curl http://localhost:8080/system/version
   ```

#### 前端版本号检查

1. **查看 package.json**：
   ```bash
   grep "version" ruoyi-ui/package.json
   ```

2. **查看管理后台首页**：
   - 访问：http://localhost:3000/admin
   - 查看"系统状态"卡片中的"系统版本"

### 注意事项

1. **版本号一致性**：
   - 父 POM 的 `<version>` 和 `<app.version>` 必须一致
   - 所有子模块的 parent version 必须与父 POM 的 version 一致
   - 不要直接修改 `application.yml` 中的版本号（会被 Maven 覆盖）
   - 不要直接修改代码中的版本号（应该从配置读取）

2. **Maven 资源过滤**：
   - `ruoyi-admin/pom.xml` 已配置资源过滤
   - `application.yml` 中的 `@app.version@` 会在构建时自动替换

3. **前端获取版本号**：
   - 前端通过 API `/system/version` 获取后端版本号
   - 如果 API 调用失败，使用前端默认值

4. **版本号更新后**：
   - 必须同时修改父 POM 和所有子模块的版本号
   - 必须重新编译后端项目
   - 必须重启后端服务
   - 前端会自动获取最新版本号

5. **版本不匹配的错误**：
   - 如果父 POM 版本与子模块 parent 版本不一致，构建会失败
   - 错误信息：`Non-resolvable parent POM`
   - 解决方法：确保所有版本号一致

### 常见问题

**Q: 为什么需要同时修改父 POM 和子模块的版本号？**

A: 因为子模块通过 `<parent>` 标签引用父 POM，版本号必须一致才能正常构建。

**Q: 为什么 application.yml 中的版本号是 `@app.version@`？**

A: 这是 Maven 资源过滤的占位符，在构建时会自动替换为 `pom.xml` 中定义的实际版本号。

**Q: 为什么前端版本号和后端版本号不一样？**

A: 前端版本号（4.1.0）和后端版本号（1.3.2）是独立的，分别跟踪前端框架和后端应用的版本更新。

**Q: 如何确保所有地方的版本号一致？**

A: 需要同时修改：
1. 父 POM 的 `<version>` 和 `<app.version>`
2. 所有子模块的 parent `<version>`
3. 然后运行 `mvn clean install`

**Q: 版本号更新后需要做什么？**

A:
1. 修改 `pom.xml` 中的 `<version>` 和 `<app.version>`
2. 修改所有子模块的 parent `<version>`
3. 运行 `mvn clean install -DskipTests`
4. 重启后端服务
5. 前端会自动获取最新版本号

**Q: 构建时提示 "Non-resolvable parent POM" 怎么办？**

A: 这说明父 POM 版本与子模块 parent 版本不一致。检查并确保所有版本号一致。

---

**文档版本**: v2.0
**最后更新**: 2026-01-27
**维护者**: nevell
