#!/bin/bash

# 博客系统性能测试脚本
# 用于验证Redis缓存、数据库索引和分页加载优化效果

echo "🚀 博客系统性能测试开始..."

# 1. 测试Redis缓存状态
echo "📊 检查Redis缓存状态..."
docker exec redis redis-cli info stats | grep -E "(keyspace_hits|keyspace_misses|used_memory)"

# 2. 测试数据库索引使用情况
echo "📈 检查数据库索引效果..."
docker exec mysql mysql -uroot -proot newblog -e "
SELECT 
    TABLE_NAME as '表名',
    INDEX_NAME as '索引名',
    CARDINALITY as '基数',
    SUB_PART as '前缀长度',
    INDEX_TYPE as '索引类型'
FROM information_schema.STATISTICS 
WHERE TABLE_SCHEMA = 'newblog' AND TABLE_NAME LIKE 'blog_%'
ORDER BY TABLE_NAME, CARDINALITY DESC;
" | head -20

# 3. 性能基准测试 - 文章列表查询
echo "⚡ 测试文章列表查询性能..."
for i in {1..5}; do
    start_time=$(date +%s%3N)
    curl -s "http://localhost:8080/blog/article/list" > /dev/null
    end_time=$(date +%s%3N)
    query_time=$(echo "$end_time - $start_time" | bc)
    echo "第${i}次查询: ${query_time}ms"
    sleep 0.5
done

# 4. 测试热门文章缓存效果
echo "🔥 测试热门文章缓存性能..."
for i in {1..3}; do
    start_time=$(date +%s%3N)
    curl -s "http://localhost:8080/blog/article/hot" > /dev/null
    end_time=$(date +%s%3N)
    query_time=$(echo "$end_time - $start_time" | bc)
    echo "热门文章查询${i}: ${query_time}ms"
    sleep 0.5
done

# 5. 测试搜索功能
echo "🔍 测试搜索功能性能..."
start_time=$(date +%s%3N)
curl -s "http://localhost:8080/blog/article/search?keyword=Spring" > /dev/null
end_time=$(date +%s%3N)
search_time=$(echo "$end_time - $start_time" | bc)
echo "搜索查询: ${search_time}ms"

# 6. 检查前端资源加载
echo "🎨 检查前端资源加载..."
curl -s -w "DNS解析: %{time_namelookup}s\n连接时间: %{time_connect}s\n传输开始: %{time_pretransfer}s\n首字节时间: %{time_starttransfer}s\n总时间: %{time_total}s\n" "http://localhost:3000" > /dev/null

# 7. 内存使用情况
echo "💾 检查容器内存使用..."
docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.NetIO}}" ruoyi-admin mysql redis ruoyi-ui

# 8. 生成性能报告
echo "📋 性能测试完成，生成报告..."
cat > performance_report.txt << EOF
博客系统性能测试报告
=====================
测试时间: $(date)
测试环境: Docker

缓存状态:
- Redis缓存命中率: $(docker exec redis redis-cli info stats | grep keyspace_hits || echo "0")
- 内存使用: $(docker exec redis redis-cli info memory | grep used_memory_human)

数据库优化:
- 索引数量: $(docker exec mysql mysql -uroot -proot newblog -e "SELECT COUNT(*) FROM information_schema.STATISTICS WHERE TABLE_SCHEMA = 'newblog';" | tail -1)
- 缓存命中率: 待实现

API性能:
- 文章列表查询: 平均响应时间待计算
- 热门文章查询: 平均响应时间待计算  
- 搜索查询: ${search_time}ms

建议:
1. 监控Redis缓存命中率，目标 >80%
2. 定期更新数据库统计信息
3. 监控慢查询日志
4. 实现API响应时间监控

EOF

echo "✅ 性能测试完成！报告已保存至 performance_report.txt"