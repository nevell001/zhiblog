# 代码规范配置总结

## ✅ 已完成的配置

### 前端代码规范 (Vue 3)

#### 创建的文件：
- [.eslintrc.cjs](../ruoyi-ui/.eslintrc.cjs) - ESLint 配置
- [.prettierrc.json](../ruoyi-ui/.prettierrc.json) - Prettier 配置
- [.prettierignore](../ruoyi-ui/.prettierignore) - Prettier 忽略文件
- [.eslintignore](../ruoyi-ui/.eslintignore) - ESLint 忽略文件

#### 更新的文件：
- [ruoyi-ui/package.json](../ruoyi-ui/package.json) - 添加了 lint 和 format 脚本

#### 使用方法：
```bash
cd ruoyi-ui

# 安装依赖（包含 ESLint 和 Prettier）
npm install

# 检查代码规范
npm run lint:check

# 自动修复代码规范问题
npm run lint

# 格式化代码
npm run format
```

### 后端代码规范 (Java)

#### 创建的文件：
- [checkstyle.xml](../checkstyle.xml) - Checkstyle 配置

#### 更新的文件：
- [pom.xml](../pom.xml) - 添加了 maven-checkstyle-plugin

#### 使用方法：
```bash
# 检查代码规范
mvn checkstyle:check

# 生成检查报告
mvn checkstyle:checkstyle
```

### Git Hooks

#### 创建的文件：
- [.husky/pre-commit](../.husky/pre-commit) - 提交前检查
- [.husky/commit-msg](../.husky/commit-msg) - 提交信息格式检查

#### 功能：
- 提交前自动运行代码规范检查
- 检查提交信息格式（约定式提交）

### 文档

#### 创建的文档：
- [docs/CODE_REVIEW_GUIDE.md](CODE_REVIEW_GUIDE.md) - 代码审查指南
- [docs/CODE_STANDARDS.md](CODE_STANDARDS.md) - 代码规范配置指南

### VS Code 配置

#### 创建的文件：
- [.vscode/extensions.json](../.vscode/extensions.json) - 推荐扩展
- [.vscode/settings.json](../.vscode/settings.json) - 编辑器设置

## 📋 下一步操作

### 1. 安装前端依赖
```bash
cd ruoyi-ui
npm install
```

### 2. 安装 Git Hooks（可选）
```bash
npm install --save-dev husky
npx husky install
```

### 3. 安装 VS Code 扩展
打开 VS Code，会提示安装推荐扩展：
- ESLint
- Prettier
- Vue Language Features (Volar)
- Java Extension Pack

### 4. 测试配置
```bash
# 测试前端检查
cd ruoyi-ui
npm run lint:check
npm run format:check

# 测试后端检查
cd ..
mvn checkstyle:check
```

## 🎯 代码规范要点

### 前端 (Vue 3)

**ESLint 规则**：
- Vue 3 推荐规则
- 无 console（生产环境）
- 单引号、无分号
- 2 空格缩进
- 最大行宽 100

**Prettier 配置**：
- 单引号
- 无分号
- 2 空格缩进
- 行宽 100
- LF 换行符

### 后端 (Java)

**Checkstyle 规则**：
- 基于 Google Java Style
- 最大行宽 120
- 方法最大 150 行
- 参数最多 7 个
- 圈复杂度最大 15

### 提交信息格式

```
type(scope): subject

body

footer
```

**类型**：
- feat: 新功能
- fix: 修复bug
- docs: 文档
- style: 格式
- refactor: 重构
- perf: 性能
- test: 测试
- chore: 构建

## 📚 相关文档

- [代码审查指南](./CODE_REVIEW_GUIDE.md)
- [代码规范配置指南](./CODE_STANDARDS.md)
- [CLAUDE.md](../CLAUDE.md) - 项目总体文档

## 🔧 故障排除

### ESLint 不工作
```bash
cd ruoyi-ui
rm -rf node_modules package-lock.json
npm install
```

### Git Hooks 不生效
```bash
chmod +x .husky/pre-commit
chmod +x .husky/commit-msg
```

### Checkstyle 检查失败
```bash
# 查看详细错误
mvn checkstyle:checkstyle
# 报告在 target/checkstyle-result.xml
```

---

**版本**: v4.0.1
**配置日期**: 2026-01-10
