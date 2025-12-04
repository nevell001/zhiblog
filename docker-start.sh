#!/bin/bash

# Docker Compose 启动脚本
# 设置时区环境变量以避免警告

export TZ=Asia/Shanghai
export LANG=C.UTF-8
export LC_ALL=C.UTF-8

echo "🚀 启动 Docker Compose 服务..."
echo "📍 时区设置: $TZ"

if [ "$1" = "down" ]; then
    echo "🛑 停止并删除容器..."
    docker compose down
    echo "✅ 服务已停止"
elif [ "$1" = "restart" ]; then
    echo "🔄 重启服务..."
    docker compose down
    docker compose up -d
    echo "✅ 服务已重启"
elif [ "$1" = "logs" ]; then
    echo "📋 查看服务日志..."
    docker compose logs -f
else
    echo "🔧 构建并启动服务..."
    docker compose up -d --build
    echo "✅ 服务已启动"
    echo ""
    echo "📊 服务状态:"
    docker compose ps
    echo ""
    echo "🔗 访问地址:"
    echo "  前端: http://localhost:3000"
    echo "  后端: http://localhost:8080"
fi