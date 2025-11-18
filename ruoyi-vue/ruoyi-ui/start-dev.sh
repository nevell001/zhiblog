#!/bin/bash
# Vue 开发服务器快速启动脚本

echo "🚀 启动 Vue 开发服务器..."
cd "."

# 设置调试模式
export VUE_APP_MENU_DEBUG=true

# 启动开发服务器
npm run dev
