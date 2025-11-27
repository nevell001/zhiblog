#!/bin/bash

echo "=== 测试MySQL数据库连接 ==="

# 测试数据库连接
echo "1. 测试数据库连接..."
mysql -u root -proot -e "SELECT 'MySQL连接成功' as status;" 2>/dev/null

if [ $? -eq 0 ]; then
    echo "✅ MySQL连接成功"
else
    echo "❌ MySQL连接失败"
    echo "请检查："
    echo "- MySQL服务是否启动"
    echo "- 用户名密码是否正确"
    exit 1
fi

# 检查数据库是否存在
echo "2. 检查数据库是否存在..."
mysql -u root -proot -e "SHOW DATABASES LIKE 'newblog';" 2>/dev/null

if [ $? -eq 0 ]; then
    echo "✅ 数据库 newblog 存在"
else
    echo "❌ 数据库 newblog 不存在"
    echo "创建数据库..."
    mysql -u root -proot -e "CREATE DATABASE newblog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
    if [ $? -eq 0 ]; then
        echo "✅ 数据库创建成功"
    else
        echo "❌ 数据库创建失败"
        exit 1
    fi
fi

# 检查表是否存在
echo "3. 检查博客相关表是否存在..."
TABLES=$(mysql -u root -proot -e "USE newblog; SHOW TABLES LIKE 'blog_%';" 2>/dev/null | wc -l)

if [ $TABLES -gt 0 ]; then
    echo "✅ 找到 $((TABLES-1)) 个博客相关表"
    echo "表列表："
    mysql -u root -proot -e "USE newblog; SHOW TABLES LIKE 'blog_%';" 2>/dev/null
else
    echo "❌ 未找到博客相关表"
    echo "需要执行初始化脚本..."
    echo "请运行：mysql -u root -proot newblog < ruoyi-vue/sql/init_database.sql"
fi

# 检查文章数据
echo "4. 检查文章数据..."
ARTICLE_COUNT=$(mysql -u root -proot -e "USE newblog; SELECT COUNT(*) FROM blog_article;" 2>/dev/null | tail -1)

if [ $? -eq 0 ] && [ "$ARTICLE_COUNT" -gt 0 ]; then
    echo "✅ 找到 $ARTICLE_COUNT 篇文章"
    echo "最新3篇文章："
    mysql -u root -proot -e "USE newblog; SELECT id, title, status, author_name FROM blog_article LIMIT 3;" 2>/dev/null
else
    echo "❌ 文章表为空或不存在"
    echo "需要插入测试数据..."
fi

echo "=== 数据库检查完成 ==="