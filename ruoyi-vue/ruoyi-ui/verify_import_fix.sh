#!/bin/bash

# 🍎 验证博客首页模块导入修复

echo "🔍 验证模块导入修复..."

# 检查 compatibilityTest.js 文件
if [ -f "src/utils/compatibilityTest.js" ]; then
    echo "✅ compatibilityTest.js 文件存在"
    
    # 检查是否包含 exposeCompatibilityTest 导出
    if grep -q "export function exposeCompatibilityTest" src/utils/compatibilityTest.js; then
        echo "✅ exposeCompatibilityTest 函数已导出"
    else
        echo "❌ exposeCompatibilityTest 函数未找到"
        exit 1
    fi
else
    echo "❌ compatibilityTest.js 文件不存在"
    exit 1
fi

# 检查 main.js 导入语句
if grep -q "import { exposeCompatibilityTest } from './utils/compatibilityTest'" src/main.js; then
    echo "✅ main.js 导入语句正确"
else
    echo "❌ main.js 导入语句有问题"
    exit 1
fi

# 检查 menuDebug.js 文件
if [ -f "src/utils/menuDebug.js" ]; then
    echo "✅ menuDebug.js 文件存在"
    
    # 检查是否包含 exposeVueApp 导出
    if grep -q "export function exposeVueApp" src/utils/menuDebug.js; then
        echo "✅ exposeVueApp 函数已导出"
    else
        echo "❌ exposeVueApp 函数未找到"
        exit 1
    fi
else
    echo "❌ menuDebug.js 文件不存在"
    exit 1
fi

# 检查 routeDebug.js 文件
if [ -f "src/utils/routeDebug.js" ]; then
    echo "✅ routeDebug.js 文件存在"
    
    # 检查是否包含 debugRoutes 导出
    if grep -q "export function debugRoutes" src/utils/routeDebug.js; then
        echo "✅ debugRoutes 函数已导出"
    else
        echo "❌ debugRoutes 函数未找到"
        exit 1
    fi
else
    echo "❌ routeDebug.js 文件不存在"
    exit 1
fi

echo ""
echo "🎉 所有模块导入验证通过！"
echo ""
echo "📋 下一步操作："
echo "1. 访问: http://localhost:3000"
echo "2. 按 F12 打开开发者工具"
echo "3. 在控制台运行: window.runMenuCompatibilityTest()"
echo "4. 检查菜单功能是否正常"
echo ""
echo "🔧 其他调试命令："
echo "- window.debugMenu() - 菜单调试"
echo "- window.testMenuNavigation(index) - 测试导航"
echo "- debugRoutes() - 路由调试"