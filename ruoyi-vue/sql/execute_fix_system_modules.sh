#!/bin/bash

# ========================================
# 执行系统模块菜单修复脚本
# 创建时间：2025-11-13
# 描述：执行SQL脚本修复系统管理、系统监控和系统工具三个核心模块的菜单数据
# ========================================

echo "========================================"
echo "开始修复系统模块菜单数据..."
echo "========================================"

# 检查MySQL连接
if ! command -v mysql &> /dev/null; then
    echo "错误: MySQL客户端未安装"
    exit 1
fi

# 数据库连接参数
DB_HOST="localhost"
DB_PORT="3306"
DB_USER="root"
DB_PASSWORD="root"
DB_NAME="newblog"

# 执行SQL脚本
echo "正在执行修复脚本..."
mysql -h$DB_HOST -P$DB_PORT -u$DB_USER -p$DB_PASSWORD $DB_NAME < fix_system_modules_menu.sql

if [ $? -eq 0 ]; then
    echo "========================================"
    echo "系统模块菜单修复成功！"
    echo "========================================"
    echo "请刷新后台页面查看修复后的菜单。"
else
    echo "========================================"
    echo "系统模块菜单修复失败！"
    echo "========================================"
    exit 1
fi