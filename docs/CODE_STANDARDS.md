# ZhiBlog 代码规范配置指南

本指南说明如何设置和使用 ZhiBlog 项目的代码规范工具。

## 📋 目录

- [快速开始](#快速开始)
- [前端代码规范](#前端代码规范)
- [后端代码规范](#后端代码规范)
- [Git Hooks](#git-hooks)
- [CI/CD集成](#cicd集成)

## 快速开始

### 安装依赖

**前端**:
```bash
cd ruoyi-ui
npm install
```

**后端** (自动配置):
```bash
# Checkstyle 插件已在 pom.xml 中配置
mvn clean install
```

## 前端代码规范

### 工具

- **ESLint**: 代码质量检查
- **Prettier**: 代码格式化

### 配置文件

- [.eslintrc.cjs](../ruoyi-ui/.eslintrc.cjs) - ESLint 配置
- [.prettierrc.json](../ruoyi-ui/.prettierrc.json) - Prettier 配置

### 使用方法

```bash
cd ruoyi-ui

# 检查代码规范
npm run lint:check

# 自动修复代码规范问题
npm run lint

# 格式化所有代码
npm run format

# 检查代码格式
npm run format:check
```

### VS Code 集成

推荐安装以下 VS Code 扩展：

```json
{
  "recommendations": [
    "dbaeumer.vscode-eslint",
    "esbenp.prettier-vscode",
    "vue.volar"
  ]
}
```

添加到 `.vscode/settings.json`:

```json
{
  "editor.formatOnSave": true,
  "editor.defaultFormatter": "esbenp.prettier-vscode",
  "editor.codeActionsOnSave": {
    "source.fixAll.eslint": "explicit"
  },
  "[vue]": {
    "editor.defaultFormatter": "esbenp.prettier-vscode"
  },
  "[javascript]": {
    "editor.defaultFormatter": "esbenp.prettier-vscode"
  }
}
```

## 后端代码规范

### 工具

- **Checkstyle**: Java 代码规范检查

### 配置文件

- [checkstyle.xml](../checkstyle.xml) - Checkstyle 配置

### 使用方法

```bash
# 检查代码规范
mvn checkstyle:check

# 生成检查报告
mvn checkstyle:checkstyle

# 报告生成在: target/checkstyle-result.xml
```

### IDE 集成

**IntelliJ IDEA**:
1. 安装 Checkstyle-IDEA 插件
2. 导入 `checkstyle.xml` 配置文件
3. 设置为默认检查配置

**Eclipse**:
1. 安装 Checkstyle 插件
2. 导入配置文件
3. 启用实时检查

## Git Hooks

项目配置了 Git Hooks 来自动检查代码规范：

### 配置的 Hooks

1. **pre-commit**: 提交前检查代码规范
2. **commit-msg**: 检查提交信息格式

### 安装 Hooks

```bash
# 使用 husky 安装
npm install --save-dev husky

# 初始化 husky
npx husky install

# 或者手动安装
chmod +x .husky/pre-commit
chmod +x .husky/commit-msg
```

### 提交信息格式

遵循约定式提交规范：

```
type(scope): subject

body

footer
```

**类型（type）**:
- `feat`: 新功能
- `fix`: 修复bug
- `docs`: 文档
- `style`: 格式
- `refactor`: 重构
- `perf`: 性能
- `test`: 测试
- `chore`: 构建/工具

**示例**:
```
feat(blog): 添加文章分类功能

实现文章分类的CRUD操作，包括：
- 分类列表展示
- 分类添加/编辑/删除
- 分类树形结构

Closes #123
```

## CI/CD集成

### GitHub Actions

创建 `.github/workflows/code-quality.yml`:

```yaml
name: Code Quality

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]

jobs:
  frontend:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '18'
      - name: Install dependencies
        run: cd ruoyi-ui && npm install
      - name: Run ESLint
        run: cd ruoyi-ui && npm run lint:check
      - name: Check formatting
        run: cd ruoyi-ui && npm run format:check

  backend:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
      - name: Run Checkstyle
        run: mvn checkstyle:check
```

## 常见问题

### Q: ESLint 报错太多怎么办？

A: 可以运行 `npm run lint` 自动修复大部分问题。对于无法自动修复的问题，需要手动修改。

### Q: Prettier 和 ESLint 冲突？

A: Prettier 负责格式化，ESLint 负责代码质量。配置已优化避免冲突。

### Q: Checkstyle 检查太严格？

A: `checkstyle.xml` 中的规则已经过调整。如需修改，请团队讨论后调整配置。

### Q: Git Hooks 不生效？

A: 检查文件权限：
```bash
chmod +x .husky/pre-commit
chmod +x .husky/commit-msg
```

## 相关文档

- [代码审查指南](./CODE_REVIEW_GUIDE.md)
- [提交信息规范](https://www.conventionalcommits.org/)
- [Vue 风格指南](https://vuejs.org/style-guide/)
- [阿里巴巴 Java 开发规范](https://github.com/alibaba/p3c)

## 维护

- 更新依赖: 定期更新 ESLint、Prettier、Checkstyle 到最新稳定版
- 规则调整: 团队讨论后修改配置文件
- 文档同步: 配置变更时同步更新本文档

---

**版本**: v4.0.1
**最后更新**: 2026-01-10
