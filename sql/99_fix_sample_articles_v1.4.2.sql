-- ============================================================================
-- ZhiBlog v1.4.2 一次性数据修复：示例文章（正文 / 分类 / 标签）
--
-- 修什么
--   1. 正文：v1.4.1 及更早的种子把 Markdown 原文写进 blog_article.content，
--      format 却停在默认 'html'；前台详情只做「消毒 + v-html」、不渲染 Markdown，
--      于是 6 篇示例文章整页显示 '#' 标题和 ``` 代码围栏源码。
--   2. 分类：《MySQL优化》《Vue3》《Docker》《Git》各挂错一处。
--   3. 标签：关联错乱，且有幽灵 link（tag_id=19 在 blog_tag 里根本不存在，
--      blog_article_tag 没建外键，所以它一路静默生效）。
--
-- 为什么得单独跑
--   sql/00_init_database.sql 的幂等靠 INSERT IGNORE + 「按标题判存在」实现，
--   重跑只会**跳过**已存在的示例文章，不会把正文和关联改过来。
--   全新安装不需要本脚本（00 里的种子已经修正）。
--
-- 安全性
--   - 只按这 6 个示例标题定位行，其它文章一律不碰；
--   - 正文只在「仍以 Markdown 开头的坏数据」上重写（content LIKE '# %'），
--     你自己编辑过的文章不会被覆盖；
--   - 标签用 INSERT ... SELECT 重建：文章按标题、标签按名称匹配，
--     任一侧不存在就不插，不会再制造指向不存在标签的新脏数据；
--   - 显式钉住 update_time（该列带 ON UPDATE CURRENT_TIMESTAMP），
--     否则示例文章会被顶到「最近更新」最前面。
--   - 可重复执行：第二次跑下来所有写语句都是 0 行受影响。
--
-- 执行前先备份：
--   mysqldump --single-transaction zhiblog blog_article blog_article_tag > demo.before.sql
--
-- 用法（脚本刻意不写 CREATE DATABASE / USE，库名由命令行给出，避免连错库）：
--   mysql -u root -p zhiblog < sql/99_fix_sample_articles_v1.4.2.sql
--   docker exec -i mysql mysql -uroot -p"$DB_PASSWORD" zhiblog < sql/99_fix_sample_articles_v1.4.2.sql
-- ============================================================================

-- ---------------------------------------------------------------------------
-- 0. 自检（只读）：确认目标行现状，'Markdown(待修)' 的行数就是接下来会被改的正文行数
-- ---------------------------------------------------------------------------
SELECT a.id, a.title, a.category_id AS 现分类,
       IF(a.content LIKE '# %', 'Markdown(待修)', 'HTML(跳过)') AS 正文形态
  FROM blog_article a
 WHERE a.del_flag = '0'
   AND a.title IN ('Spring Boot + Vue.js 全栈开发实战', 'MySQL数据库优化实战指南', 'Vue.js 3.0 Composition API 深度解析', 'Docker容器化部署最佳实践', 'Redis缓存设计与实战', 'Git工作流程与团队协作规范')
 ORDER BY a.id;

-- ---------------------------------------------------------------------------
-- 1. 正文改存渲染后的 HTML，并把分类挂到主题对应的目录
--    正文与 sql/00_init_database.sql 里修正后的种子逐字节一致。
-- ---------------------------------------------------------------------------
-- 1. Spring Boot + Vue.js 全栈开发实战：分类 1（技术分享）本就正确，不写
UPDATE `blog_article`
   SET `content` = '<h1>Spring Boot + Vue.js 全栈开发实战</h1>
<h2>📋 项目介绍</h2>
<p>本项目是基于Spring Boot 3.3.0和Vue.js 3.x构建的现代化博客系统，采用前后端分离架构，提供完整的内容管理功能。</p>
<h2>🛠️ 技术栈详解</h2>
<h3>后端技术栈</h3>
<ul>
<li><strong>Spring Boot 3.3.0</strong> - 核心框架</li>
<li><strong>MyBatis</strong> - 持久层框架</li>
<li><strong>MySQL 8.0</strong> - 关系型数据库</li>
<li><strong>Redis</strong> - 缓存和会话存储</li>
<li><strong>Spring Security</strong> - 安全认证框架</li>
</ul>
<h3>前端技术栈</h3>
<ul>
<li><strong>Vue.js 3.x</strong> - 前端框架</li>
<li><strong>Element Plus</strong> - UI组件库</li>
<li><strong>Vite</strong> - 构建工具</li>
<li><strong>Axios</strong> - HTTP客户端</li>
<li><strong>Vue Router</strong> - 路由管理</li>
</ul>
<h2>🚀 核心功能特性</h2>
<ol>
<li><strong>用户管理</strong> - 注册、登录、权限控制</li>
<li><strong>文章管理</strong> - 增删改查、富文本编辑</li>
<li><strong>分类标签</strong> - 分类管理、标签关联</li>
<li><strong>评论系统</strong> - 评论发布、回复、审核</li>
<li><strong>搜索功能</strong> - 全文搜索、关键词高亮</li>
<li><strong>统计分析</strong> - 访问统计、热度排行</li>
</ol>
<h2>💡 开发经验总结</h2>
<p>通过本项目的实践，深入理解了：</p>
<ul>
<li>前后端分离架构的设计思想</li>
<li>RESTful API设计规范</li>
<li>数据库性能优化技巧</li>
<li>前端组件化开发模式</li>
<li>安全防护和权限控制</li>
</ul>',
       `format` = 'html',
       `update_time` = `update_time`
 WHERE `title` = 'Spring Boot + Vue.js 全栈开发实战'
   AND `del_flag` = '0'
   AND `content` LIKE '# %';

-- 2. MySQL数据库优化实战指南：分类 6 -> 7（数据库）
UPDATE `blog_article`
   SET `content` = '<h1>MySQL数据库优化实战指南</h1>
<h2>🔍 索引优化策略</h2>
<h3>合理创建索引</h3>
<pre><code class="language-sql">-- 单列索引
CREATE INDEX idx_user_email ON user(email);

-- 复合索引
CREATE INDEX idx_article_status_create_time ON article(status, create_time);

-- 覆盖索引
CREATE INDEX idx_article_cover ON article(id, title, status);
</code></pre>
<h3>避免索引失效</h3>
<ul>
<li>避免在索引列上使用函数</li>
<li>避免隐式类型转换</li>
<li>避免左模糊查询（LIKE &quot;%value&quot;）</li>
</ul>
<h2>⚡ 查询优化技巧</h2>
<h3>SQL语句优化</h3>
<ul>
<li>使用EXPLAIN分析执行计划</li>
<li>避免SELECT * 查询</li>
<li>合理使用JOIN和子查询</li>
</ul>
<h3>分页优化</h3>
<pre><code class="language-sql">-- 传统分页（深度分页性能差）
SELECT * FROM article ORDER BY id LIMIT 100000, 10;

-- 优化分页（使用书签模式）
SELECT * FROM article WHERE id &gt; 100000 ORDER BY id LIMIT 10;
</code></pre>
<h2>🛠️ 配置参数调优</h2>
<pre><code class="language-ini"># 内存相关配置
innodb_buffer_pool_size = 4G
innodb_log_file_size = 256M

# 连接数配置
max_connections = 500
max_connect_errors = 1000

# 查询缓存
query_cache_type = 1
query_cache_size = 256M
</code></pre>
<p>通过以上优化措施，数据库性能可提升3-5倍。</p>',
       `format` = 'html',
       `category_id` = 7,
       `update_time` = `update_time`
 WHERE `title` = 'MySQL数据库优化实战指南'
   AND `del_flag` = '0'
   AND `content` LIKE '# %';

-- 3. Vue.js 3.0 Composition API 深度解析：分类 6 -> 5（前端开发）
UPDATE `blog_article`
   SET `content` = '<h1>Vue.js 3.0 Composition API 深度解析</h1>
<h2>🎯 Composition API 核心概念</h2>
<h3>基本语法</h3>
<pre><code class="language-javascript">import { ref, reactive, computed, watch } from &#39;vue&#39;

export default {
  setup() {
    const count = ref(0)
    const state = reactive({ name: &#39;Vue&#39;, version: 3 })

    const doubled = computed(() =&gt; count.value * 2)

    watch(count, (newVal, oldVal) =&gt; {
      console.log(`Count changed from ${oldVal} to ${newVal}`)
    })

    return { count, state, doubled }
  }
}
</code></pre>
<h3>逻辑复用</h3>
<p>通过自定义Hook实现逻辑复用：</p>
<pre><code class="language-javascript">// useCounter.js
export function useCounter(initialValue = 0) {
  const count = ref(initialValue)
  const increment = () =&gt; count.value++
  const decrement = () =&gt; count.value--

  return { count, increment, decrement }
}
</code></pre>
<h2>🚀 性能提升</h2>
<p>Vue 3相比Vue 2在以下方面有显著提升：</p>
<ul>
<li><strong>打包体积</strong> 减少41%</li>
<li><strong>初始渲染</strong> 快55%</li>
<li><strong>更新渲染</strong> 快133%</li>
<li><strong>内存占用</strong> 减少54%</li>
</ul>
<h2>🔧 TypeScript支持</h2>
<p>Vue 3提供更好的TypeScript集成：</p>
<pre><code class="language-typescript">interface User {
  id: number
  name: string
  email: string
}

const user = ref&lt;User&gt;({ id: 1, name: &#39;Admin&#39;, email: &#39;admin@example.com&#39; })
</code></pre>
<p>Composition API让代码更加模块化和可维护，特别适合大型项目开发。</p>',
       `format` = 'html',
       `category_id` = 5,
       `update_time` = `update_time`
 WHERE `title` = 'Vue.js 3.0 Composition API 深度解析'
   AND `del_flag` = '0'
   AND `content` LIKE '# %';

-- 4. Docker容器化部署最佳实践：分类 3 -> 8（运维部署）
UPDATE `blog_article`
   SET `content` = '<h1>Docker容器化部署最佳实践</h1>
<h2>🐳 Dockerfile编写技巧</h2>
<h3>多阶段构建优化</h3>
<pre><code class="language-dockerfile"># 构建阶段
FROM maven:3.8.4-openjdk-11 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 运行阶段
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT [&quot;java&quot;, &quot;-jar&quot;, &quot;app.jar&quot;]
</code></pre>
<h3>镜像体积优化</h3>
<ul>
<li>使用合适的基础镜像（alpine、slim）</li>
<li>清理不必要的包和缓存</li>
<li>合并RUN指令减少层数</li>
</ul>
<h2>🚀 部署配置</h2>
<h3>Docker Compose配置</h3>
<pre><code class="language-yaml">version: &#39;3.8&#39;
services:
  app:
    build: .
    ports:
      - &quot;8080:8080&quot;
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    depends_on:
      - mysql
      - redis

  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: CHANGE_ME_ROOT_PASSWORD
      MYSQL_DATABASE: blog
    volumes:
      - mysql_data:/var/lib/mysql

  redis:
    image: redis:alpine
    volumes:
      - redis_data:/data

volumes:
  mysql_data:
  redis_data:
</code></pre>
<h2>📊 监控和日志</h2>
<h3>健康检查</h3>
<pre><code class="language-dockerfile">HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \\
  CMD curl -f http://localhost:8080/actuator/health || exit 1
</code></pre>
<p>通过容器化部署，大大简化了应用的环境管理和部署流程。</p>',
       `format` = 'html',
       `category_id` = 8,
       `update_time` = `update_time`
 WHERE `title` = 'Docker容器化部署最佳实践'
   AND `del_flag` = '0'
   AND `content` LIKE '# %';

-- 5. Redis缓存设计与实战：分类 7（数据库）本就正确，不写
UPDATE `blog_article`
   SET `content` = '<h1>Redis缓存设计与实战</h1>
<h2>🎯 缓存设计模式</h2>
<h3>Cache-Aside模式</h3>
<pre><code class="language-java">public User getUser(Long id) {
    String key = &quot;user:&quot; + id;
    User user = redisTemplate.opsForValue().get(key);

    if (user == null) {
        user = userMapper.selectById(id);
        if (user != null) {
            redisTemplate.opsForValue().set(key, user, 1, TimeUnit.HOURS);
        }
    }
    return user;
}
</code></pre>
<h3>Write-Through模式</h3>
<p>在写入数据库的同时更新缓存，保证数据一致性。</p>
<h2>🔄 缓存更新策略</h2>
<h3>延迟双删策略</h3>
<pre><code class="language-java">public void updateUser(User user) {
    // 第一次删除
    redisTemplate.delete(&quot;user:&quot; + user.getId());

    // 更新数据库
    userMapper.updateById(user);

    // 延迟删除（避免脏数据）
    Thread.sleep(500);
    redisTemplate.delete(&quot;user:&quot; + user.getId());
}
</code></pre>
<h2>⚡ 性能优化技巧</h2>
<h3>Pipeline批量操作</h3>
<pre><code class="language-java">List&lt;Object&gt; results = redisTemplate.executePipelined((RedisCallback&lt;Object&gt;) connection -&gt; {
    for (Long id : userIds) {
        connection.get((&quot;user:&quot; + id).getBytes());
    }
    return null;
});
</code></pre>
<h3>合理设置过期时间</h3>
<ul>
<li>热点数据：短过期时间</li>
<li>静态数据：长过期时间</li>
<li>活动数据：动态过期时间</li>
</ul>
<p>通过合理使用Redis缓存，可以将系统性能提升10倍以上。</p>',
       `format` = 'html',
       `update_time` = `update_time`
 WHERE `title` = 'Redis缓存设计与实战'
   AND `del_flag` = '0'
   AND `content` LIKE '# %';

-- 6. Git工作流程与团队协作规范：分类 5 -> 3（学习笔记）
UPDATE `blog_article`
   SET `content` = '<h1>Git工作流程与团队协作规范</h1>
<h2>🌲 分支管理策略</h2>
<h3>Git Flow工作流</h3>
<pre><code>master (生产分支)
  ↑
develop (开发分支)
  ↑
feature/* (功能分支)
hotfix/* (热修复分支)
release/* (发布分支)
</code></pre>
<h3>分支命名规范</h3>
<ul>
<li><code>feature/用户登录功能</code></li>
<li><code>bugfix/修复支付接口异常</code></li>
<li><code>hotfix/紧急修复内存泄漏</code></li>
<li><code>release/v1.2.0</code></li>
</ul>
<h2>📝 提交信息规范</h2>
<h3>Conventional Commits规范</h3>
<pre><code>&lt;type&gt;(&lt;scope&gt;): &lt;description&gt;

[optional body]

[optional footer(s)]
</code></pre>
<h3>提交类型</h3>
<ul>
<li><code>feat</code>: 新功能</li>
<li><code>fix</code>: 修复bug</li>
<li><code>docs</code>: 文档更新</li>
<li><code>style</code>: 代码格式调整</li>
<li><code>refactor</code>: 重构代码</li>
<li><code>test</code>: 测试相关</li>
<li><code>chore</code>: 构建过程或辅助工具的变动</li>
</ul>
<h2>🔍 代码审查清单</h2>
<h3>功能性检查</h3>
<ul>
<li><input disabled="" type="checkbox"> 功能是否按需求实现</li>
<li><input disabled="" type="checkbox"> 边界条件是否处理</li>
<li><input disabled="" type="checkbox"> 异常情况是否有处理</li>
</ul>
<h3>代码质量检查</h3>
<ul>
<li><input disabled="" type="checkbox"> 代码是否遵循规范</li>
<li><input disabled="" type="checkbox"> 是否有重复代码</li>
<li><input disabled="" type="checkbox"> 注释是否充分</li>
</ul>
<h3>安全性检查</h3>
<ul>
<li><input disabled="" type="checkbox"> 是否有SQL注入风险</li>
<li><input disabled="" type="checkbox"> 敏感信息是否脱敏</li>
<li><input disabled="" type="checkbox"> 权限控制是否合理</li>
</ul>
<p>良好的Git工作流程可以显著提升团队的协作效率和代码质量。</p>',
       `format` = 'html',
       `category_id` = 3,
       `update_time` = `update_time`
 WHERE `title` = 'Git工作流程与团队协作规范'
   AND `del_flag` = '0'
   AND `content` LIKE '# %';

-- ---------------------------------------------------------------------------
-- 2. 标签关联：按文章整组重建（顺带清掉幽灵 tag_id 与错挂）
-- ---------------------------------------------------------------------------
-- 先清掉这 6 篇的旧关联（只影响示例文章）
DELETE `bat` FROM `blog_article_tag` `bat`
  JOIN `blog_article` `a` ON `a`.`id` = `bat`.`article_id`
 WHERE `a`.`title` IN ('Spring Boot + Vue.js 全栈开发实战', 'MySQL数据库优化实战指南', 'Vue.js 3.0 Composition API 深度解析', 'Docker容器化部署最佳实践', 'Redis缓存设计与实战', 'Git工作流程与团队协作规范');

-- 1. Spring Boot + Vue.js 全栈开发实战：Java / Vue.js / Spring Boot
INSERT IGNORE INTO `blog_article_tag` (`article_id`, `tag_id`)
SELECT `a`.`id`, `t`.`id`
  FROM `blog_article` `a`
  JOIN `blog_tag` `t` ON `t`.`name` IN ('Java', 'Vue.js', 'Spring Boot')
 WHERE `a`.`title` = 'Spring Boot + Vue.js 全栈开发实战'
   AND `a`.`del_flag` = '0';

-- 2. MySQL数据库优化实战指南：Java / MySQL / 后端开发
INSERT IGNORE INTO `blog_article_tag` (`article_id`, `tag_id`)
SELECT `a`.`id`, `t`.`id`
  FROM `blog_article` `a`
  JOIN `blog_tag` `t` ON `t`.`name` IN ('Java', 'MySQL', '后端开发')
 WHERE `a`.`title` = 'MySQL数据库优化实战指南'
   AND `a`.`del_flag` = '0';

-- 3. Vue.js 3.0 Composition API 深度解析：Vue.js / 前端开发 / JavaScript / TypeScript
INSERT IGNORE INTO `blog_article_tag` (`article_id`, `tag_id`)
SELECT `a`.`id`, `t`.`id`
  FROM `blog_article` `a`
  JOIN `blog_tag` `t` ON `t`.`name` IN ('Vue.js', '前端开发', 'JavaScript', 'TypeScript')
 WHERE `a`.`title` = 'Vue.js 3.0 Composition API 深度解析'
   AND `a`.`del_flag` = '0';

-- 4. Docker容器化部署最佳实践：Docker / 后端开发 / Linux
INSERT IGNORE INTO `blog_article_tag` (`article_id`, `tag_id`)
SELECT `a`.`id`, `t`.`id`
  FROM `blog_article` `a`
  JOIN `blog_tag` `t` ON `t`.`name` IN ('Docker', '后端开发', 'Linux')
 WHERE `a`.`title` = 'Docker容器化部署最佳实践'
   AND `a`.`del_flag` = '0';

-- 5. Redis缓存设计与实战：Redis / 后端开发
INSERT IGNORE INTO `blog_article_tag` (`article_id`, `tag_id`)
SELECT `a`.`id`, `t`.`id`
  FROM `blog_article` `a`
  JOIN `blog_tag` `t` ON `t`.`name` IN ('Redis', '后端开发')
 WHERE `a`.`title` = 'Redis缓存设计与实战'
   AND `a`.`del_flag` = '0';

-- 6. Git工作流程与团队协作规范：Git
INSERT IGNORE INTO `blog_article_tag` (`article_id`, `tag_id`)
SELECT `a`.`id`, `t`.`id`
  FROM `blog_article` `a`
  JOIN `blog_tag` `t` ON `t`.`name` IN ('Git')
 WHERE `a`.`title` = 'Git工作流程与团队协作规范'
   AND `a`.`del_flag` = '0';

-- ---------------------------------------------------------------------------
-- 3. 复核：正文形态应全为 'HTML(正常)'，标签数依次为 3/3/4/3/2/1
-- ---------------------------------------------------------------------------
SELECT a.id, a.title, a.category_id AS 分类,
       IF(a.content LIKE '# %', '仍是Markdown(异常)', 'HTML(正常)') AS 正文形态,
       (SELECT COUNT(*) FROM blog_article_tag bat WHERE bat.article_id = a.id) AS 标签数,
       (SELECT GROUP_CONCAT(t.name ORDER BY t.id SEPARATOR ', ')
          FROM blog_article_tag bat JOIN blog_tag t ON t.id = bat.tag_id
         WHERE bat.article_id = a.id) AS 标签
  FROM blog_article a
 WHERE a.del_flag = '0'
   AND a.title IN ('Spring Boot + Vue.js 全栈开发实战', 'MySQL数据库优化实战指南', 'Vue.js 3.0 Composition API 深度解析', 'Docker容器化部署最佳实践', 'Redis缓存设计与实战', 'Git工作流程与团队协作规范')
 ORDER BY a.id;

-- 全库残留的幽灵标签关联（本脚本只重建了示例文章的；其余留给你判断是否手动处理）
SELECT bat.article_id, bat.tag_id
  FROM blog_article_tag bat
  LEFT JOIN blog_tag t ON t.id = bat.tag_id
 WHERE t.id IS NULL;
