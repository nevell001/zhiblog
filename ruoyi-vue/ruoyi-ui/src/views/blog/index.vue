<template>
  <div class="blog-container">
    <!-- 博客导航 -->
    <BlogNav />
    
    <!-- 博客头部 -->
    <div class="blog-header">
      <div class="header-content">
        <h1 class="blog-title">{{ blogSettings.blog_name || '我的博客' }}</h1>
        <p class="blog-description">{{ blogSettings.blog_desc || '这是一个基于RuoYi-Vue的博客系统' }}</p>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="blog-main">
      <div class="main-content">
        <!-- 文章列表 -->
        <div class="article-list">
          <div v-for="article in articleList" :key="article.id" class="article-item">
            <div class="article-cover" v-if="article.coverUrl">
              <img :src="article.coverUrl" :alt="article.title" />
            </div>
            <div class="article-content">
              <h2 class="article-title">
                <router-link :to="`/blog/article/${article.id}`">{{ article.title }}</router-link>
              </h2>
              <div class="article-meta">
                <span class="meta-item">
                  <i class="el-icon-date"></i>
                  {{ formatDate(article.createTime) }}
                </span>
                <span class="meta-item">
                  <i class="el-icon-view"></i>
                  {{ article.viewCount || 0 }} 阅读
                </span>
                <span class="meta-item" v-if="article.categoryName">
                  <i class="el-icon-folder"></i>
                  {{ article.categoryName }}
                </span>
              </div>
              <p class="article-summary">{{ article.summary || article.content.substring(0, 150) + '...' }}</p>
              <div class="article-footer">
                <router-link :to="`/blog/article/${article.id}`" class="read-more">
                  阅读全文 <i class="el-icon-arrow-right"></i>
                </router-link>
              </div>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <div class="pagination-container" v-if="total > queryParams.pageSize">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="total"
            :page-size="queryParams.pageSize"
            :current-page="queryParams.pageNum"
            @current-change="handlePageChange"
          />
        </div>
      </div>

      <!-- 侧边栏 -->
      <div class="sidebar">
        <!-- 关于博主 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">关于博主</h3>
          <div class="about-content">
            <p>{{ blogSettings.blog_author || 'nevell' }}</p>
            <p class="about-desc">{{ blogSettings.blog_desc || '热爱技术，热爱生活' }}</p>
          </div>
        </div>

        <!-- 分类 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">文章分类</h3>
          <ul class="category-list">
            <li v-for="category in categoryList" :key="category.id" class="category-item">
              <router-link :to="`/blog/category/${category.id}`" class="category-link">
                <span class="category-name">{{ category.name }}</span>
                <span class="category-count">({{ category.articleCount || 0 }})</span>
              </router-link>
            </li>
          </ul>
        </div>

        <!-- 热门文章 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">热门文章</h3>
          <ul class="hot-article-list">
            <li v-for="article in hotArticles" :key="article.id" class="hot-article-item">
              <router-link :to="`/blog/article/${article.id}`" class="hot-article-link">
                {{ article.title }}
              </router-link>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getArticleList, getHotArticles } from '@/api/blog/article'
import { getCategoryList } from '@/api/blog/category'
import { getBlogSettings } from '@/api/blog/setting'
import BlogNav from '@/components/BlogNav.vue'

// 响应式数据
const articleList = ref([])
const categoryList = ref([])
const hotArticles = ref([])
const blogSettings = ref({})
const total = ref(0)

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  status: 1 // 只显示已发布的文章
})

// 获取文章列表
const loadArticleList = async () => {
  try {
    const response = await getArticleList(queryParams)
    articleList.value = response.rows || []
    total.value = response.total || 0
  } catch (error) {
    console.error('获取文章列表失败:', error)
  }
}

// 获取分类列表
const loadCategoryList = async () => {
  try {
    const response = await getCategoryList({})
    categoryList.value = response.rows || []
  } catch (error) {
    console.error('获取分类列表失败:', error)
  }
}

// 获取热门文章
const loadHotArticles = async () => {
  try {
    const response = await getHotArticles({ limit: 5 })
    hotArticles.value = response.data || []
  } catch (error) {
    console.error('获取热门文章失败:', error)
  }
}

// 获取博客设置
const loadBlogSettings = async () => {
  try {
    const response = await getBlogSettings()
    blogSettings.value = response.data || {}
  } catch (error) {
    console.error('获取博客设置失败:', error)
  }
}

// 分页处理
const handlePageChange = (page) => {
  queryParams.pageNum = page
  loadArticleList()
}

// 日期格式化
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

// 组件挂载时加载数据
onMounted(() => {
  loadArticleList()
  loadCategoryList()
  loadHotArticles()
  loadBlogSettings()
})
</script>

<style scoped>
.blog-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.blog-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 60px 0;
  text-align: center;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.blog-title {
  font-size: 2.5rem;
  margin-bottom: 10px;
  font-weight: 300;
}

.blog-description {
  font-size: 1.2rem;
  opacity: 0.9;
  margin: 0;
}

.blog-main {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
  display: flex;
  gap: 40px;
}

.main-content {
  flex: 1;
}

.sidebar {
  width: 300px;
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.article-item {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.article-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.article-cover {
  height: 200px;
  overflow: hidden;
}

.article-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.article-content {
  padding: 25px;
}

.article-title {
  margin: 0 0 15px 0;
  font-size: 1.5rem;
}

.article-title a {
  color: #333;
  text-decoration: none;
  transition: color 0.3s ease;
}

.article-title a:hover {
  color: #409eff;
}

.article-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
  font-size: 0.9rem;
  color: #666;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.article-summary {
  color: #666;
  line-height: 1.6;
  margin-bottom: 15px;
}

.article-footer {
  display: flex;
  justify-content: flex-end;
}

.read-more {
  color: #409eff;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.read-more:hover {
  color: #337ecc;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

.sidebar-widget {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
}

.widget-title {
  font-size: 1.2rem;
  margin: 0 0 15px 0;
  padding-bottom: 10px;
  border-bottom: 2px solid #409eff;
  color: #333;
}

.about-content {
  text-align: center;
}

.about-desc {
  color: #666;
  font-size: 0.9rem;
  margin-top: 5px;
}

.category-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.category-item {
  margin-bottom: 10px;
}

.category-link {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  color: #333;
  text-decoration: none;
  transition: color 0.3s ease;
}

.category-link:hover {
  color: #409eff;
}

.category-count {
  color: #999;
  font-size: 0.9rem;
}

.hot-article-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.hot-article-item {
  margin-bottom: 8px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.hot-article-item:last-child {
  margin-bottom: 0;
  border-bottom: none;
}

.hot-article-link {
  color: #333;
  text-decoration: none;
  font-size: 0.9rem;
  line-height: 1.4;
  transition: color 0.3s ease;
  display: block;
}

.hot-article-link:hover {
  color: #409eff;
}

@media (max-width: 768px) {
  .blog-main {
    flex-direction: column;
  }
  
  .sidebar {
    width: 100%;
  }
  
  .article-meta {
    flex-direction: column;
    gap: 5px;
  }
}
</style>