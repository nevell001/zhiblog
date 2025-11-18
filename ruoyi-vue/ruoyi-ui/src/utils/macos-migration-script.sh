#!/bin/bash

# 🍎 macOS 菜单系统优化迁移脚本
# 适用于 macOS 系统的自动化迁移工具

set -e  # 遇到错误立即退出

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 项目根目录 (ruoyi-vue/ruoyi-ui)
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
echo -e "${BLUE}📁 项目目录: ${PROJECT_DIR}${NC}"

# 备份目录
BACKUP_DIR="${PROJECT_DIR}/backup_$(date +%Y%m%d_%H%M%S)"
echo -e "${YELLOW}📦 备份目录: ${BACKUP_DIR}${NC}"

# 创建备份目录
mkdir -p "${BACKUP_DIR}"

# 日志函数
log_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

log_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

log_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

log_error() {
    echo -e "${RED}❌ $1${NC}"
}

# 检查文件是否存在
check_file() {
    local file_path="$1"
    if [[ -f "$file_path" ]]; then
        return 0
    else
        return 1
    fi
}

# 备份文件
backup_file() {
    local src_file="$1"
    local backup_name="$2"
    
    if check_file "$src_file"; then
        cp "$src_file" "${BACKUP_DIR}/${backup_name}"
        log_success "已备份: $backup_name"
    else
        log_warning "文件不存在，跳过备份: $src_file"
    fi
}

# 替换文件
replace_file() {
    local src_file="$1"
    local dest_file="$2"
    
    if check_file "$src_file"; then
        mv "$src_file" "$dest_file"
        log_success "已替换: $(basename "$dest_file")"
    else
        log_error "源文件不存在: $src_file"
        return 1
    fi
}

# 检查文件权限
check_permissions() {
    local file_path="$1"
    if [[ -r "$file_path" && -w "$file_path" ]]; then
        return 0
    else
        log_warning "文件权限不足: $file_path"
        chmod 644 "$file_path"
        log_info "已修复文件权限: $file_path"
    fi
}

echo -e "${GREEN}🚀 开始 macOS 菜单系统优化迁移${NC}"
echo "=================================================="

# 1. 检查项目结构
log_info "检查项目结构..."
if [[ ! -d "${PROJECT_DIR}/src" ]]; then
    log_error "未找到 src 目录，请确认项目路径正确"
    exit 1
fi
log_success "项目结构检查通过"

# 2. 备份原文件
log_info "备份原始文件..."
backup_file "${PROJECT_DIR}/src/store/modules/permission.js" "permission.js"
backup_file "${PROJECT_DIR}/src/layout/components/Sidebar/SidebarItem.vue" "SidebarItem.vue"
backup_file "${PROJECT_DIR}/src/layout/components/Sidebar/Link.vue" "Link.vue"
backup_file "${PROJECT_DIR}/src/plugins/auth.js" "auth.js"

# 3. 替换优化文件
log_info "替换优化文件..."

# 替换权限存储模块
if check_file "${PROJECT_DIR}/src/store/modules/permission-optimized.js"; then
    replace_file "${PROJECT_DIR}/src/store/modules/permission-optimized.js" "${PROJECT_DIR}/src/store/modules/permission.js"
else
    log_error "未找到优化后的权限文件"
fi

# 替换侧边栏组件
if check_file "${PROJECT_DIR}/src/layout/components/Sidebar/SidebarItem-optimized.vue"; then
    replace_file "${PROJECT_DIR}/src/layout/components/Sidebar/SidebarItem-optimized.vue" "${PROJECT_DIR}/src/layout/components/Sidebar/SidebarItem.vue"
else
    log_error "未找到优化后的侧边栏组件"
fi

# 替换链接组件
if check_file "${PROJECT_DIR}/src/layout/components/Sidebar/Link-optimized.vue"; then
    replace_file "${PROJECT_DIR}/src/layout/components/Sidebar/Link-optimized.vue" "${PROJECT_DIR}/src/layout/components/Sidebar/Link.vue"
else
    log_error "未找到优化后的链接组件"
fi

# 替换权限插件
if check_file "${PROJECT_DIR}/src/plugins/auth-optimized.js"; then
    replace_file "${PROJECT_DIR}/src/plugins/auth-optimized.js" "${PROJECT_DIR}/src/plugins/auth.js"
else
    log_error "未找到优化后的权限插件"
fi

# 4. 复制测试工具
log_info "复制测试工具..."
if check_file "${PROJECT_DIR}/src/utils/compatibilityTest.js"; then
    log_success "测试工具已存在"
else
    log_error "未找到兼容性测试工具"
fi

# 5. 检查文件权限
log_info "检查和修复文件权限..."
find "${PROJECT_DIR}/src" -name "*.js" -o -name "*.vue" | while read -r file; do
    check_permissions "$file"
done

# 6. 生成迁移报告
log_info "生成迁移报告..."
cat > "${BACKUP_DIR}/migration_report.txt" << EOF
macOS 菜单系统优化迁移报告
=============================

迁移时间: $(date)
项目目录: ${PROJECT_DIR}
备份目录: ${BACKUP_DIR}

已备份文件:
- permission.js
- SidebarItem.vue  
- Link.vue
- auth.js

已替换文件:
- permission.js (优化版本)
- SidebarItem.vue (优化版本)
- Link.vue (优化版本)
- auth.js (优化版本)

测试工具:
- compatibilityTest.js

下一步操作:
1. 重启开发服务器
2. 在浏览器控制台运行: window.runMenuCompatibilityTest()
3. 测试菜单点击功能

如需回滚，请查看 backup 目录中的原文件。
EOF

log_success "迁移报告已生成: ${BACKUP_DIR}/migration_report.txt"

# 7. macOS 特定优化
log_info "应用 macOS 特定优化..."

# 检查是否使用了 zsh
if [[ "$SHELL" == */zsh ]]; then
    log_info "检测到 zsh，添加环境变量..."
    echo "" >> ~/.zshrc
    echo "# Vue 项目开发环境变量" >> ~/.zshrc
    echo "export VUE_APP_MENU_DEBUG=true" >> ~/.zshrc
    log_success "已添加调试环境变量到 ~/.zshrc"
fi

# 检查 Node.js 版本
if command -v node &> /dev/null; then
    NODE_VERSION=$(node --version)
    log_info "Node.js 版本: $NODE_VERSION"
    
    # 检查是否满足最低版本要求
    if [[ "$NODE_VERSION" < "v14" ]]; then
        log_warning "建议升级 Node.js 到 v14 或更高版本"
    fi
else
    log_error "未安装 Node.js，请先安装 Node.js"
fi

# 8. 清理缓存
log_info "清理开发服务器缓存..."
if [[ -d "${PROJECT_DIR}/node_modules/.cache" ]]; then
    rm -rf "${PROJECT_DIR}/node_modules/.cache"
    log_success "已清理缓存目录"
fi

# 9. 创建快速启动脚本
log_info "创建快速启动脚本..."
cat > "${PROJECT_DIR}/start-dev.sh" << EOF
#!/bin/bash
# Vue 开发服务器快速启动脚本

echo "🚀 启动 Vue 开发服务器..."
cd "$(dirname "\$0")"

# 设置调试模式
export VUE_APP_MENU_DEBUG=true

# 启动开发服务器
npm run dev
EOF

chmod +x "${PROJECT_DIR}/start-dev.sh"
log_success "已创建快速启动脚本: start-dev.sh"

echo "=================================================="
log_success "🎉 macOS 菜单系统优化迁移完成！"

echo ""
echo -e "${BLUE}📋 下一步操作:${NC}"
echo "1. 重启开发服务器:"
echo "   ./start-dev.sh"
echo ""
echo "2. 在浏览器中打开应用，按 F12 打开开发者工具"
echo ""
echo "3. 在控制台中运行兼容性测试:"
echo "   window.runMenuCompatibilityTest()"
echo ""
echo "4. 测试菜单功能:"
echo "   - 点击'系统管理'菜单"
echo "   - 点击'系统监控'菜单"
echo "   - 点击'系统工具'菜单"
echo "   - 点击'博客管理'菜单"
echo "   - 点击'数据统计'菜单"
echo ""
echo -e "${YELLOW}⚠️  如遇问题，备份文件位于: ${BACKUP_DIR}${NC}"
echo -e "${GREEN}✨ 迁移报告: ${BACKUP_DIR}/migration_report.txt${NC}"

echo ""
echo -e "${BLUE}🔧 手动回滚命令:${NC}"
echo "cp ${BACKUP_DIR}/permission.js ${PROJECT_DIR}/src/store/modules/permission.js"
echo "cp ${BACKUP_DIR}/SidebarItem.vue ${PROJECT_DIR}/src/layout/components/Sidebar/SidebarItem.vue"
echo "cp ${BACKUP_DIR}/Link.vue ${PROJECT_DIR}/src/layout/components/Sidebar/Link.vue"
echo "cp ${BACKUP_DIR}/auth.js ${PROJECT_DIR}/src/plugins/auth.js"