#!/bin/bash

echo "🚀 博客系统快速完善脚本"
echo "================================"

# 检查MySQL连接
echo "📊 检查数据库连接..."
if mysql -u root -proot -e "USE ry_vue; SELECT 1;" 2>/dev/null; then
    echo "✅ 数据库连接正常"
    
    # 导入示例数据
    echo "📝 导入博客示例数据..."
    mysql -u root ry_vue < sql/blog_sample_data.sql
    echo "✅ 示例数据导入完成"
    
    # 检查数据
    echo "📈 数据统计:"
    mysql -u root -e "
    USE ry_vue;
    SELECT 
        (SELECT COUNT(*) FROM blog_article) as '文章数量',
        (SELECT COUNT(*) FROM blog_category) as '分类数量',
        (SELECT COUNT(*) FROM blog_tag) as '标签数量',
        (SELECT COUNT(*) FROM blog_friend_link) as '友链数量';
    "
else
    echo "❌ 数据库连接失败，请检查MySQL配置"
fi

# 检查前端依赖
echo "🎨 检查前端项目..."
cd ruoyi-ui

# 安装富文本编辑器 (可选)
echo "📝 是否安装富文本编辑器? (y/n)"
read -r install_editor
if [ "$install_editor" = "y" ]; then
    echo "安装 TinyMCE 编辑器..."
    npm install @tinymce/tinymce-vue
    echo "✅ 富文本编辑器安装完成"
fi

# 检查项目运行状态
echo "🔍 检查项目运行状态..."
if curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
    echo "✅ 后端服务运行正常 (http://localhost:8080)"
else
    echo "⚠️  后端服务未运行，请启动: cd ruoyi-admin && mvn spring-boot:run"
fi

if curl -s http://localhost:3000 > /dev/null 2>&1; then
    echo "✅ 前端服务运行正常 (http://localhost:3000)"
else
    echo "⚠️  前端服务未运行，请启动: cd ruoyi-ui && npm run dev"
fi

echo ""
echo "🎉 博客系统检查完成！"
echo "📖 查看完善建议: cat improve_blog_system.md"
echo "🌐 访问地址: http://localhost:3000"
echo "================================"