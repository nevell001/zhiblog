#!/bin/bash

# ========================================
# 验证系统模块修复结果
# 创建时间：2025-11-17
# 描述：验证系统管理、系统监控和系统工具模块是否修复成功
# ========================================

echo "========================================"
echo "验证系统模块修复结果"
echo "========================================"

# 1. 检查后端服务状态
echo "1. 检查后端服务状态..."
if ! lsof -i :8080 | grep -q "LISTEN"; then
    echo "后端服务未运行"
    exit 1
fi
echo "后端服务正在运行"

# 2. 检查前端服务状态
echo "2. 检查前端服务状态..."
if ! lsof -i :3000 | grep -q "LISTEN"; then
    echo "前端服务未运行"
    exit 1
fi
echo "前端服务正在运行"

# 3. 检查数据库菜单配置
echo "3. 检查数据库菜单配置..."
mysql -h localhost -u root -proot newblog -e "
SELECT 
    COUNT(CASE WHEN menu_id IN (1, 2, 3) AND component = 'Layout' THEN 1 END) as main_menus,
    COUNT(CASE WHEN parent_id IN (1, 2, 3) AND component LIKE 'admin/%' THEN 1 END) as sub_menus,
    COUNT(CASE WHEN parent_id IN (1, 2, 3) AND visible = '0' AND status = '0' THEN 1 END) as visible_menus
FROM sys_menu;
"

# 4. 检查组件文件是否存在
echo "4. 检查组件文件是否存在..."
components=(
    "ruoyi-ui/src/views/admin/system/user/user/index.vue"
    "ruoyi-ui/src/views/admin/system/role/role/index.vue"
    "ruoyi-ui/src/views/admin/system/menu/menu/index.vue"
    "ruoyi-ui/src/views/admin/monitor/online/index.vue"
    "ruoyi-ui/src/views/admin/monitor/druid/index.vue"
    "ruoyi-ui/src/views/admin/tool/gen/index.vue"
    "ruoyi-ui/src/views/admin/tool/swagger/index.vue"
)

missing_components=0
for component in "${components[@]}"; do
    if [ ! -f "/home/nevell/code/newblog/ruoyi-vue/$component" ]; then
        echo "缺失组件: $component"
        missing_components=$((missing_components + 1))
    fi
done

if [ $missing_components -eq 0 ]; then
    echo "所有组件文件都存在"
else
    echo "缺失 $missing_components 个组件文件"
fi

# 5. 检查API响应
echo "5. 检查API响应..."
response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/getRouters)
if [ "$response" = "401" ] || [ "$response" = "200" ]; then
    echo "API响应正常 (HTTP状态码: $response)"
else
    echo "API响应异常 (HTTP状态码: $response)"
fi

# 6. 检查前端路由配置
echo "6. 检查前端路由配置..."
if [ -f "/home/nevell/code/newblog/ruoyi-vue/ruoyi-ui/src/router/admin.js" ]; then
    echo "前端路由配置文件存在"
else
    echo "前端路由配置文件不存在"
fi

# 7. 检查权限配置
echo "7. 检查权限配置..."
role_menu_count=$(mysql -h localhost -u root -proot newblog -sN -e "SELECT COUNT(*) FROM sys_role_menu WHERE role_id = 1;")
if [ "$role_menu_count" -gt 0 ]; then
    echo "管理员角色已分配菜单权限 (共 $role_menu_count 个菜单)"
else
    echo "管理员角色未分配菜单权限"
fi

echo "========================================"
echo "验证完成！"
echo "========================================"
echo "如果以上检查都正常，请执行以下操作："
echo "1. 清除浏览器缓存"
echo "2. 重新登录后台管理系统"
echo "3. 验证系统管理、系统监控和系统工具模块是否正常显示"
echo "========================================"