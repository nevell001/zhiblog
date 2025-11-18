# 🍎 macOS 系统菜单优化指南

## 📋 系统信息
- **操作系统**: macOS
- **Shell**: zsh (默认)
- **包管理器**: Homebrew (推荐)

## 🚀 快速开始

### 方法1: 自动化脚本迁移 (推荐)

```bash
# 进入项目根目录
cd /Users/nevell/code/newblog

# 运行自动化迁移脚本
./ruoyi-vue/ruoyi-ui/src/utils/macos-migration-script.sh
```

### 方法2: 手动迁移

如果您更喜欢手动控制每个步骤：

```bash
# 1. 创建备份目录
mkdir -p backup_$(date +%Y%m%d_%H%M%S)

# 2. 备份原文件
cp ruoyi-vue/ruoyi-ui/src/store/modules/permission.js backup_$(date +%Y%m%d_%H%M%S)/
cp ruoyi-vue/ruoyi-ui/src/layout/components/Sidebar/SidebarItem.vue backup_$(date +%Y%m%d_%H%M%S)/
cp ruoyi-vue/ruoyi-ui/src/layout/components/Sidebar/Link.vue backup_$(date +%Y%m%d_%H%M%S)/
cp ruoyi-vue/ruoyi-ui/src/plugins/auth.js backup_$(date +%Y%m%d_%H%M%S)/

# 3. 替换优化文件
mv ruoyi-vue/ruoyi-ui/src/store/modules/permission-optimized.js ruoyi-vue/ruoyi-ui/src/store/modules/permission.js
mv ruoyi-vue/ruoyi-ui/src/layout/components/Sidebar/SidebarItem-optimized.vue ruoyi-vue/ruoyi-ui/src/layout/components/Sidebar/SidebarItem.vue
mv ruoyi-vue/ruoyi-ui/src/layout/components/Sidebar/Link-optimized.vue ruoyi-vue/ruoyi-ui/src/layout/components/Sidebar/Link.vue
mv ruoyi-vue/ruoyi-ui/src/plugins/auth-optimized.js ruoyi-vue/ruoyi-ui/src/plugins/auth.js
```

---

## 🔧 macOS 特定优化

### 1. 环境变量配置

```bash
# 添加到 ~/.zshrc
echo 'export VUE_APP_MENU_DEBUG=true' >> ~/.zshrc
source ~/.zshrc
```

### 2. 开发服务器启动

```bash
# 使用提供的快速启动脚本
cd /Users/nevell/code/newblog
./start-dev.sh

# 或手动启动
cd ruoyi-vue/ruoyi-ui
npm run dev
```

### 3. 浏览器开发者工具

在 macOS 上使用快捷键：
- **Chrome**: `Cmd + Option + I`
- **Safari**: `Cmd + Option + I` (需要先开启开发者菜单)
- **Firefox**: `Cmd + Option + I`

---

## 🧪 测试验证

### 1. 运行兼容性测试

在浏览器控制台中执行：

```javascript
// 运行完整测试套件
window.runMenuCompatibilityTest()
```

### 2. 手动功能测试

#### 测试清单：
- [ ] **系统管理菜单** - 点击应正常跳转到用户管理页面
- [ ] **系统监控菜单** - 点击应展开子菜单，可访问在线用户等功能
- [ ] **系统工具菜单** - 点击应展开，可访问代码生成、Swagger工具
- [ ] **博客管理菜单** - 点击应正常跳转（需要admin或editor权限）
- [ ] **数据统计菜单** - 点击应正常跳转（需要admin权限）

### 3. 权限测试

```javascript
// 检查当前用户权限状态
console.log('权限摘要:', window.auth?.getPermissionSummary())

// 测试特定权限
const result = window.auth.verifyPermission('system:user:list')
console.log('用户管理权限:', result)
```

---

## 🐛 macOS 常见问题解决

### 问题1: 文件权限错误

**症状**: `Permission denied` 错误

**解决方案**:
```bash
# 修复文件权限
chmod -R 644 ruoyi-vue/ruoyi-ui/src/
chmod -R 755 ruoyi-vue/ruoyi-ui/src/utils/

# 或者使用自动化脚本中的权限修复
find ruoyi-vue/ruoyi-ui/src -name "*.js" -o -name "*.vue" | while read file; do
    chmod 644 "$file"
done
```

### 问题2: 端口占用

**症状**: `Port 8080 is already in use`

**解决方案**:
```bash
# 查找占用端口的进程
lsof -i :8080

# 终止进程
kill -9 <PID>

# 或使用其他端口
npm run dev -- --port 3000
```

### 问题3: Node.js 版本不兼容

**检查版本**:
```bash
node --version
npm --version
```

**升级 Node.js (使用 Homebrew)**:
```bash
# 安装最新版 Node.js
brew install node

# 或安装特定版本
brew install node@18
```

### 问题4: 缓存问题

**清理缓存**:
```bash
# 清理 npm 缓存
npm cache clean --force

# 清理 Vue 缓存
rm -rf ruoyi-vue/ruoyi-ui/node_modules/.cache

# 重新安装依赖
npm install
```

---

## 🔄 回滚方案

如果迁移后出现问题，可以快速回滚：

### 自动回滚

```bash
# 找到最新的备份目录
BACKUP_DIR=$(ls -t backup_* | head -1)

# 恢复文件
cp "$BACKUP_DIR/permission.js" ruoyi-vue/ruoyi-ui/src/store/modules/permission.js
cp "$BACKUP_DIR/SidebarItem.vue" ruoyi-vue/ruoyi-ui/src/layout/components/Sidebar/SidebarItem.vue
cp "$BACKUP_DIR/Link.vue" ruoyi-vue/ruoyi-ui/src/layout/components/Sidebar/Link.vue
cp "$BACKUP_DIR/auth.js" ruoyi-vue/ruoyi-ui/src/plugins/auth.js

# 重启开发服务器
npm run dev
```

### 手动回滚

```bash
# 如果还有原文件的重命名版本
mv ruoyi-vue/ruoyi-ui/src/store/modules/permission.js ruoyi-vue/ruoyi-ui/src/store/modules/permission-optimized.js
mv ruoyi-vue/ruoyi-ui/src/store/modules/permission-old.js ruoyi-vue/ruoyi-ui/src/store/modules/permission.js
```

---

## 📊 性能监控

### macOS 系统监控工具

```bash
# 查看系统资源使用
top -o cpu

# 查看内存使用
vm_stat

# 查看磁盘使用
df -h
```

### Vue 开发服务器监控

```javascript
// 在浏览器控制台中监控性能
console.time('菜单加载测试')
// 执行菜单操作
console.timeEnd('菜单加载测试')

// 监控路由变化
window.__VUE_APP__?.router?.beforeEach((to, from, next) => {
  console.log('路由变化:', from.path, '->', to.path)
  next()
})
```

---

## 📱 macOS 快捷键参考

| 功能 | 快捷键 | 说明 |
|------|--------|------|
| 打开终端 | `Cmd + Space` → 输入 "Terminal" | Spotlight 搜索 |
| 刷新页面 | `Cmd + R` | 浏览器刷新 |
| 强制刷新 | `Cmd + Shift + R` | 清除缓存刷新 |
| 开发者工具 | `Cmd + Option + I` | Chrome/DevTools |
| 查看源码 | `Cmd + Option + U` | 浏览器查看源码 |
| 切换应用 | `Cmd + Tab` | 应用切换器 |

---

## 🎯 验证成功标准

迁移成功后，您应该看到：

### ✅ 控制台输出
```
✅ 路由配置检查通过
✅ 权限系统检查通过  
✅ 组件加载检查通过
✅ 事件绑定检查通过
✅ 菜单项状态检查通过
📊 测试完成，成功率: 100%
```

### ✅ 菜单行为
- 所有菜单项点击都有响应
- 权限不足时显示友好提示
- 页面跳转正常，无404错误
- 控制台无JavaScript错误

### ✅ 性能表现
- 菜单响应时间 < 200ms
- 页面跳转流畅
- 内存使用稳定

---

## 📞 技术支持

如果在 macOS 上遇到问题：

1. **查看系统日志**: `Console.app`
2. **检查网络连接**: `ping google.com`
3. **重启开发服务器**: `Ctrl + C` → `npm run dev`
4. **清除浏览器缓存**: `Cmd + Shift + Delete`

**祝您使用愉快！🎉**