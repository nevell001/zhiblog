#!/bin/bash

# ========================================
# 最终验证修复结果
# 创建时间：2025-11-17
# 描述：最终验证系统模块修复结果
# ========================================

echo "========================================"
echo "最终验证修复结果"
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

# 3. 检查数据库菜单路径
echo "3. 检查数据库菜单路径..."
mysql -h localhost -u root -proot newblog -e "
SELECT 
    '系统管理' as module_name,
    COUNT(CASE WHEN menu_id = 1 AND path = '/admin/system' THEN 1 END) as main_menu_ok,
    COUNT(CASE WHEN parent_id = 1 AND path LIKE '/admin/system/%' THEN 1 END) as sub_menus_ok
FROM sys_menu
UNION ALL
SELECT 
    '系统监控' as module_name,
    COUNT(CASE WHEN menu_id = 2 AND path = '/admin/monitor' THEN 1 END) as main_menu_ok,
    COUNT(CASE WHEN parent_id = 2 AND path LIKE '/admin/monitor/%' THEN 1 END) as sub_menus_ok
FROM sys_menu
UNION ALL
SELECT 
    '系统工具' as module_name,
    COUNT(CASE WHEN menu_id = 3 AND path = '/admin/tool' THEN 1 END) as main_menu_ok,
    COUNT(CASE WHEN parent_id = 3 AND path LIKE '/admin/tool/%' THEN 1 END) as sub_menus_ok
FROM sys_menu;
"

# 4. 检查外链菜单是否已隐藏
echo "4. 检查外链菜单是否已隐藏..."
external_link_visible=$(mysql -h localhost -u root -proot newblog -sN -e "SELECT visible FROM sys_menu WHERE menu_id = 4;")
if [ "$external_link_visible" = "1" ]; then
    echo "外链菜单已隐藏"
else
    echo "警告：外链菜单未隐藏"
fi

# 5. 检查前端路由处理逻辑
echo "5. 检查前端路由处理逻辑..."
if grep -q "route.path && !route.path.startsWith('/') && !route.path.startsWith('http')" /home/nevell/code/newblog/ruoyi-vue/ruoyi-ui/src/store/modules/permission.js; then
    echo "前端路由路径修复逻辑已应用"
else
    echo "警告：前端路由路径修复逻辑未应用"
fi

# 6. 检查API响应
echo "6. 检查API响应..."
response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/getRouters)
if [ "$response" = "401" ] || [ "$response" = "200" ]; then
    echo "API响应正常 (HTTP状态码: $response)"
else
    echo "API响应异常 (HTTP状态码: $response)"
fi

echo "========================================"
echo "最终验证完成！"
echo "========================================"
echo "如果以上检查都正常，请执行以下操作："
echo "1. 清除浏览器缓存 (Ctrl+Shift+R)"
echo "2. 重新登录后台管理系统"
echo "3. 验证系统管理、系统监控和系统工具模块是否正常显示"
echo "4. 点击各个子菜单，确认不再出现404错误"
echo "========================================"