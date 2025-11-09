<template>
  <div class="blog-container">
    <!-- 博客导航 -->
    <BlogNav />

    <!-- 博客头部 -->
    <div class="blog-header">
      <div class="header-content">
        <div class="blog-info">
          <h1 class="blog-title">{{ blogSettings.blog_name || '我的博客' }}</h1>
          <p class="blog-description">{{ blogSettings.blog_desc || '这是一个基于RuoYi-Vue的博客系统' }}</p>
          <div class="blog-stats">
            <span class="stat-item">
              <i class="el-icon-document-copy"></i>
              {{ totalArticles }} 篇文章
            </span>
            <span class="stat-item">
              <i class="el-icon-price-tag"></i>
              {{ tagCloud.length }} 个标签
            </span>
            <span class="stat-item">
              <i class="el-icon-date"></i>
              最后更新 {{ lastUpdateTime }}
            </span>
          </div>
        </div>

        <!-- 搜索框 -->
        <div class="search-container">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索文章标题或内容..."
            class="search-input"
            @keyup.enter="handleSearch"
            clearable
          >
            <template #append>
              <el-button icon="Search" @click="handleSearch" />
            </template>
          </el-input>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="blog-main">
      <div class="main-content">
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-container">
          <div class="loading-grid">
            <el-skeleton v-for="i in 6" :key="i" :loading="loading" animated class="skeleton-item">
              <template #template>
                <div class="article-item">
                  <div class="article-cover">
                    <el-skeleton-item variant="image" style="width: 100%; height: 200px;" />
                  </div>
                  <div class="article-content">
                    <el-skeleton-item variant="h3" style="width: 70%; margin-bottom: 15px;" />
                    <el-skeleton-item variant="text" style="width: 100%; margin-bottom: 10px;" />
                    <el-skeleton-item variant="text" style="width: 90%; margin-bottom: 10px;" />
                    <el-skeleton-item variant="text" style="width: 60%; margin-bottom: 15px;" />
                    <div style="display: flex; gap: 8px; margin-bottom: 15px;">
                      <el-skeleton-item variant="text" style="width: 60px; height: 24px;" />
                      <el-skeleton-item variant="text" style="width: 50px; height: 24px;" />
                    </div>
                    <el-skeleton-item variant="text" style="width: 80px; height: 20px;" />
                  </div>
                </div>
              </template>
            </el-skeleton>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-else-if="articleList.length === 0" class="empty-state">
          <div class="empty-content">
            <i class="el-icon-document-copy empty-icon"></i>
            <h3>暂无文章</h3>
            <p>还没有发布任何文章，敬请期待...</p>
          </div>
        </div>

        <!-- 文章列表 -->
        <div v-else class="article-list">
          <div v-for="article in articleList" :key="article.id" class="article-item">
            <div class="article-cover" v-if="article.coverUrl">
              <img :src="article.coverUrl" :alt="article.title" loading="lazy" />
              <div class="article-category-badge" v-if="article.categoryName">
                {{ article.categoryName }}
              </div>
            </div>
            <div class="article-content">
              <h2 class="article-title">
                <router-link :to="`/blog/article/${article.id}`" :title="article.title">
                  {{ article.title }}
                </router-link>
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
                <span class="meta-item" v-if="article.likeCount">
                  <i class="el-icon-star-off"></i>
                  {{ article.likeCount }} 点赞
                </span>
                <span class="meta-item" v-if="article.commentCount">
                  <i class="el-icon-chat-line-round"></i>
                  {{ article.commentCount }} 评论
                </span>
              </div>
              <p class="article-summary">{{ article.summary || stripHtmlTags(article.content).substring(0, 150) + '...' }}</p>
              <div class="article-tags" v-if="article.tags && article.tags.length">
                <span v-for="tag in article.tags.slice(0, 3)" :key="tag.id" class="tag-badge" :style="{ backgroundColor: tag.color || '#409EFF' }">
                  {{ tag.name }}
                </span>
              </div>
              <div class="article-footer">
                <router-link :to="`/blog/article/${article.id}`" class="read-more">
                  阅读全文 <i class="el-icon-arrow-right"></i>
                </router-link>
              </div>
            </div>
          </div>
        </div>

        <!-- 加载更多 -->
        <div v-if="articleList.length < total && !loading" class="load-more-container">
          <el-button
            type="primary"
            @click="loadMoreArticles"
            :loading="loadingMore"
            round
          >
            {{ loadingMore ? '加载中...' : '加载更多' }}
          </el-button>
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
          <h3 class="widget-title">
            <i class="el-icon-user"></i>
            关于博主
          </h3>
          <div class="about-content">
            <div class="author-avatar">
              <img :src="blogSettings.blog_avatar || 'https://via.placeholder.com/80x80/409EFF/FFFFFF?text=博主'" :alt="blogSettings.blog_author" />
            </div>
            <h4 class="author-name">{{ blogSettings.blog_author || 'nevell' }}</h4>
            <p class="about-desc">{{ blogSettings.blog_desc || '热爱技术，热爱生活，记录学习成长路上的点点滴滴' }}</p>
            <div class="social-links">
              <a href="#" class="social-link" title="GitHub">
                <i class="el-icon-github"></i>
              </a>
              <a href="#" class="social-link" title="微信">
                <i class="el-icon-chat-dot-round"></i>
              </a>
              <a href="#" class="social-link" title="邮箱">
                <i class="el-icon-message"></i>
              </a>
            </div>
          </div>
        </div>

        <!-- 分类 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <i class="el-icon-menu"></i>
            文章分类
          </h3>
          <ul class="category-list">
            <li v-for="category in categoryList" :key="category.id" class="category-item">
              <router-link :to="`/blog/category/${category.id}`" class="category-link">
                <span class="category-name">{{ category.name }}</span>
                <span class="category-count">({{ category.articleCount || 0 }})</span>
              </router-link>
            </li>
          </ul>
        </div>

        <!-- 标签云 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <i class="el-icon-collection-tag"></i>
            标签云
          </h3>
          <div class="tag-cloud">
            <router-link
              v-for="tag in tagCloud.slice(0, 15)"
              :key="tag.id"
              :to="`/blog/tag/${tag.id}`"
              class="tag-item"
              :style="{
                fontSize: getTagFontSize(tag.article_count) + 'px',
                backgroundColor: tag.color || '#409EFF',
                color: 'white',
                transform: `scale(${getTagScale(tag.article_count)})`
              }"
              :title="`${tag.name} (${tag.article_count}篇文章)`"
            >
              {{ tag.name }}
            </router-link>
          </div>
        </div>

        <!-- 热门文章 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <i class="el-icon-star-on"></i>
            热门文章
          </h3>
          <ul class="hot-article-list">
            <li v-for="(article, index) in hotArticles.slice(0, 8)" :key="article.id" class="hot-article-item">
              <span class="article-rank">{{ index + 1 }}</span>
              <router-link :to="`/blog/article/${article.id}`" class="hot-article-link" :title="article.title">
                {{ article.title }}
              </router-link>
            </li>
          </ul>
        </div>

        <!-- 文章归档 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <i class="el-icon-date"></i>
            文章归档
          </h3>
          <ul class="archive-list">
            <li v-for="archive in archiveList.slice(0, 6)" :key="archive.archive_date" class="archive-item">
              <router-link to="/blog/archive" class="archive-link">
                <span class="archive-date">{{ formatArchiveDate(archive.archive_date) }}</span>
                <span class="archive-count">({{ archive.article_count }})</span>
              </router-link>
            </li>
          </ul>
        </div>

        <!-- 最新评论 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <i class="el-icon-chat-dot-round"></i>
            最新评论
          </h3>
          <div class="recent-comments">
            <div v-if="recentComments.length === 0" class="no-comments">
              <i class="el-icon-chat-line-round"></i>
              <p>暂无评论</p>
            </div>
            <div v-for="comment in recentComments.slice(0, 5)" :key="comment.id" class="comment-item">
              <div class="comment-content">
                <p class="comment-text">{{ truncateText(comment.content, 30) }}</p>
                <div class="comment-meta">
                  <span class="comment-author">{{ comment.nickname || '匿名' }}</span>
                  <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getArticleList, getHotArticles, searchArticles, getArticleArchive } from '@/api/blog/article'
import { getCategoryList } from '@/api/blog/category'
import { getBlogSettings } from '@/api/blog/setting'
import { getTagCloud } from '@/api/blog/tag'
import BlogNav from '@/components/BlogNav.vue'

// 响应式数据
const articleList = ref([])
const categoryList = ref([])
const hotArticles = ref([])
const blogSettings = ref({})
const total = ref(0)
const totalArticles = ref(0)
const lastUpdateTime = ref('')
const searchKeyword = ref('')
const tagCloud = ref([])
const archiveList = ref([])
const recentComments = ref([])
const loading = ref(false)
const loadingMore = ref(false)

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  status: 1 // 只显示已发布的文章
})

// 获取文章列表
const loadArticleList = async (append = false) => {
  try {
    loading.value = !append
    if (append) loadingMore.value = true

    let response;
    if (searchKeyword.value) {
      // 如果有搜索关键词，则执行搜索
      response = await searchArticles(searchKeyword.value, queryParams)
    } else {
      // 否则获取所有文章
      response = await getArticleList(queryParams)
    }

    console.log('文章列表响应:', response)
    // 后端返回的数据结构：{code: 200, msg: "", total: 0, rows: [...]}
    const newArticles = response.rows || []
    if (append) {
      articleList.value = [...articleList.value, ...newArticles]
    } else {
      articleList.value = newArticles
    }
    total.value = response.total || 0
    totalArticles.value = total.value
  } catch (error) {
    console.error('获取文章列表失败:', error)
    ElMessage.error('获取文章列表失败')
  } finally {
    loading.value = false
    loadingMore.value = false
  }
}

// 获取分类列表
const loadCategoryList = async () => {
  try {
    const response = await getCategoryList({})
    console.log('分类列表响应:', response)
    // 后端返回的数据结构：{code: 200, msg: "", data: [...]}
    categoryList.value = response.data || response.rows || []
  } catch (error) {
    console.error('获取分类列表失败:', error)
  }
}

// 获取热门文章
const loadHotArticles = async () => {
  try {
    // 获取前5个热门文章
    const response = await getHotArticles({ pageNum: 1, pageSize: 5 })
    console.log('热门文章响应:', response)
    // 后端返回的数据结构：{code: 200, msg: "", total: 0, rows: [...]}
    hotArticles.value = response.rows || []
  } catch (error) {
    console.error('获取热门文章失败:', error)
  }
}

// 获取博客设置
const loadBlogSettings = async () => {
  try {
    const response = await getBlogSettings()
    console.log('博客设置响应:', response)
    // 后端返回的数据结构：{code: 200, msg: "", data: {...}}
    blogSettings.value = response.data || {}
    // 设置最后更新时间
    lastUpdateTime.value = formatDate(new Date().toISOString())
  } catch (error) {
    console.error('获取博客设置失败:', error)
  }
}

// 获取标签云
const loadTagCloud = async () => {
  try {
    const response = await getTagCloud()
    console.log('标签云响应:', response)
    // 后端返回的数据结构：{code: 200, msg: "", data: [...]}
    tagCloud.value = response.data || []
  } catch (error) {
    console.error('获取标签云失败:', error)
  }
}

// 获取文章归档
const loadArchiveData = async () => {
  try {
    const response = await getArticleArchive()
    console.log('归档数据响应:', response)
    // 后端返回的数据结构：{code: 200, msg: "", data: [...]}
    archiveList.value = (response.data || []).slice(0, 10) // 只显示前10个归档
  } catch (error) {
    console.error('获取归档数据失败:', error)
  }
}

// 搜索处理
const handleSearch = () => {
  // 重置页码为第一页
  queryParams.pageNum = 1
  loadArticleList()
}

// 清除搜索
const clearSearch = () => {
  searchKeyword.value = ''
  queryParams.pageNum = 1
  loadArticleList()
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

// 格式化归档日期
const formatArchiveDate = (dateString) => {
  if (!dateString) return ''
  const [year, month] = dateString.split('-')
  const monthNames = ['一月', '二月', '三月', '四月', '五月', '六月', 
                     '七月', '八月', '九月', '十月', '十一月', '十二月']
  return `${year}年 ${monthNames[parseInt(month) - 1]}`
}

// 根据文章数量计算标签字体大小
const getTagFontSize = (count) => {
  if (count >= 10) return 16
  if (count >= 5) return 14
  if (count >= 2) return 12
  return 11
}

// 根据标签数量计算缩放比例
const getTagScale = (count) => {
  if (count >= 10) return 1.1
  if (count >= 5) return 1.05
  if (count >= 2) return 1.0
  return 0.95
}

// 文本截断
const truncateText = (text, maxLength) => {
  if (!text) return ''
  if (text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
}

// 加载更多文章
const loadMoreArticles = () => {
  if (loadingMore.value || articleList.value.length >= total.value) return
  queryParams.pageNum++
  loadArticleList(true)
}

// 回到顶部
const scrollToTop = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 去除HTML标签
const stripHtmlTags = (html) => {
  if (!html) return ''
  return html.replace(/<[^>]*>/g, '')
}

// 组件挂载时加载数据
onMounted(() => {
  loadArticleList()
  loadCategoryList()
  loadHotArticles()
  loadBlogSettings()
  loadTagCloud()
  loadArchiveData()
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
  position: relative;
  overflow: hidden;
}

.blog-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><circle cx="20" cy="20" r="2" fill="rgba(255,255,255,0.1)"/><circle cx="80" cy="30" r="1" fill="rgba(255,255,255,0.1)"/><circle cx="40" cy="70" r="1.5" fill="rgba(255,255,255,0.1)"/><circle cx="90" cy="80" r="1" fill="rgba(255,255,255,0.1)"/></svg>');
  opacity: 0.6;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  position: relative;
  z-index: 1;
}

.blog-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.blog-title {
  font-size: 3rem;
  margin: 0;
  font-weight: 700;
  background: linear-gradient(45deg, #fff, #f0f8ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.blog-description {
  font-size: 1.3rem;
  opacity: 0.95;
  margin: 0;
  max-width: 600px;
  line-height: 1.6;
}

.blog-stats {
  display: flex;
  gap: 30px;
  justify-content: center;
  flex-wrap: wrap;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 1rem;
  opacity: 0.9;
}

.stat-item i {
  font-size: 1.1rem;
}

.search-container {
  max-width: 500px;
  margin: 20px auto 0;
}

.search-input {
  width: 100%;
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
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
  gap: 30px;
  margin-bottom: 20px;
}

.article-item {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
}

.article-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.12);
  border-color: rgba(64, 158, 255, 0.1);
}

.article-cover {
  position: relative;
  height: 200px;
  overflow: hidden;
  background: linear-gradient(45deg, #f0f0f0, #e0e0e0);
}

.article-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.article-cover:hover img {
  transform: scale(1.08);
}

.article-category-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.95), rgba(37, 117, 252, 0.95));
  color: white;
  padding: 6px 10px;
  border-radius: 6px;
  font-size: 0.75rem;
  font-weight: 600;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.article-content {
  padding: 25px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.article-title {
  margin: 0 0 15px 0;
  font-size: 1.5rem;
  line-height: 1.4;
  font-weight: 600;
}

.article-title a {
  color: #1a1a1a;
  text-decoration: none;
  transition: all 0.3s ease;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  background: linear-gradient(135deg, #1a1a1a, #333);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.article-title a:hover {
  color: #409eff;
  transform: translateX(4px);
}

.article-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
  font-size: 0.9rem;
  color: #666;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  background: rgba(102, 102, 102, 0.05);
  padding: 4px 8px;
  border-radius: 6px;
  transition: background-color 0.3s ease;
}

.meta-item:hover {
  background: rgba(64, 158, 255, 0.1);
  color: #409eff;
}

.meta-item i {
  font-size: 1rem;
  opacity: 0.8;
}

.article-summary {
  color: #555;
  line-height: 1.7;
  margin-bottom: 18px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  flex: 1;
  font-size: 0.95rem;
}

.article-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 18px;
  flex-wrap: wrap;
  margin-top: auto;
}

.tag-badge {
  color: white;
  padding: 4px 10px;
  border-radius: 14px;
  font-size: 0.8rem;
  font-weight: 500;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.tag-badge::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s ease;
}

.tag-badge:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.tag-badge:hover::before {
  left: 100%;
}

.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
  padding-top: 15px;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
}

.read-more {
  color: #409eff;
  text-decoration: none;
  font-weight: 600;
  font-size: 0.9rem;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  position: relative;
}

.read-more::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, #409eff, #337ecc);
  transition: width 0.3s ease;
}

.read-more:hover {
  color: #337ecc;
  transform: translateX(4px);
}

.read-more:hover::after {
  width: 100%;
}

.read-more i {
  transition: transform 0.3s ease;
}

.read-more:hover i {
  transform: translateX(3px);
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

.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  padding: 15px 0;
}

.tag-item {
  color: white;
  text-decoration: none;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  padding: 6px 12px;
  border-radius: 8px;
  font-size: 0.85rem;
  font-weight: 500;
  position: relative;
  overflow: hidden;
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.tag-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.6s ease;
}

.tag-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
}

.tag-item:hover::before {
  left: 100%;
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

.archive-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.archive-item {
  margin-bottom: 10px;
}

.archive-link {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  color: #333;
  text-decoration: none;
  transition: color 0.3s ease;
}

.archive-link:hover {
  color: #409eff;
}

.archive-count {
  color: #999;
  font-size: 0.9rem;
}

.loading-container {
  padding: 60px 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.loading-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
  gap: 30px;
  width: 100%;
  max-width: 1200px;
}

.skeleton-item {
  width: 100%;
}

.author-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 3px solid #409eff;
  margin-bottom: 15px;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #409eff, #337ecc);
  padding: 3px;
}

.author-avatar img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid white;
}

.author-avatar::before {
  content: '';
  position: absolute;
  top: -3px;
  left: -3px;
  right: -3px;
  bottom: -3px;
  border-radius: 50%;
  background: linear-gradient(45deg, #409eff, #764ba2, #667eea);
  z-index: -1;
  animation: rotate 3s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.author-name {
  font-size: 1.1rem;
  font-weight: 600;
  margin: 10px 0 5px 0;
  color: #333;
}

.social-links {
  display: flex;
  gap: 15px;
  justify-content: center;
  margin-top: 15px;
}

.social-link {
  color: #666;
  font-size: 1.2rem;
  transition: color 0.3s ease;
}

.social-link:hover {
  color: #409eff;
}

.article-rank {
  display: inline-block;
  width: 20px;
  height: 20px;
  background: #409eff;
  color: white;
  border-radius: 50%;
  text-align: center;
  line-height: 20px;
  font-size: 0.8rem;
  margin-right: 10px;
  flex-shrink: 0;
}

.hot-article-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.hot-article-item:last-child {
  margin-bottom: 0;
  border-bottom: none;
}

.recent-comments {
  max-height: 300px;
  overflow-y: auto;
}

.comment-item {
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-content p {
  margin: 0 0 5px 0;
  color: #666;
  font-size: 0.9rem;
  line-height: 1.4;
}

.comment-meta {
  display: flex;
  justify-content: space-between;
  font-size: 0.8rem;
  color: #999;
}

.no-comments {
  text-align: center;
  padding: 30px 0;
  color: #999;
}

.no-comments i {
  font-size: 2rem;
  margin-bottom: 10px;
  display: block;
}

.load-more-container {
  display: flex;
  justify-content: center;
  margin-top: 40px;
  padding-bottom: 20px;
}

.empty-state {
  text-align: center;
  padding: 80px 20px;
  color: #666;
}

.empty-content {
  max-width: 400px;
  margin: 0 auto;
}

.empty-icon {
  font-size: 4rem;
  color: #c0c4cc;
  margin-bottom: 20px;
  display: block;
}

.empty-content h3 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 1.5rem;
}

.empty-content p {
  margin: 0;
  font-size: 0.95rem;
  line-height: 1.5;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .blog-main {
    max-width: 100%;
    padding: 30px 20px;
    gap: 30px;
  }

  .sidebar {
    width: 260px;
  }
}

@media (max-width: 1024px) {
  .blog-main {
    gap: 25px;
  }

  .sidebar {
    width: 240px;
  }

  .article-cover {
    height: 180px;
  }
}

@media (max-width: 768px) {
  .blog-header {
    padding: 40px 0;
  }

  .blog-title {
    font-size: 2.2rem;
  }

  .blog-description {
    font-size: 1.1rem;
    padding: 0 20px;
  }

  .blog-stats {
    gap: 20px;
  }

  .blog-main {
    flex-direction: column;
    padding: 20px 15px;
    gap: 25px;
  }

  .main-content {
    order: 1;
  }

  /* 移动端侧边栏重新设计 - 使用网格布局 */
  .sidebar {
    width: 100%;
    order: 2;
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 15px;
  }

  .sidebar-widget {
    margin-bottom: 0;
    min-height: 200px;
  }

  .article-item {
    margin-bottom: 20px;
  }

  .article-content {
    padding: 20px;
  }

  .article-title {
    font-size: 1.4rem;
  }

  .article-meta {
    flex-direction: row;
    flex-wrap: wrap;
    gap: 15px;
    font-size: 0.85rem;
  }

  .article-summary {
    font-size: 0.9rem;
    line-height: 1.5;
  }

  .article-tags {
    gap: 6px;
  }

  .tag-badge {
    font-size: 0.75rem;
    padding: 3px 8px;
  }

  .article-footer {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }

  .social-links {
    gap: 20px;
  }

  .hot-article-item {
    flex-direction: row;
    align-items: center;
    gap: 8px;
    margin-bottom: 10px;
  }

  .article-rank {
    width: 24px;
    height: 24px;
    line-height: 24px;
    font-size: 0.85rem;
    margin-right: 8px;
    margin-bottom: 0;
  }

  .hot-article-link {
    font-size: 0.9rem;
    flex: 1;
  }

  .tag-cloud {
    gap: 8px;
  }

  .tag-item {
    padding: 4px 8px;
    font-size: 0.85rem;
  }
}

@media (max-width: 480px) {
  .header-content {
    padding: 0 15px;
  }

  .blog-title {
    font-size: 1.8rem;
    line-height: 1.2;
  }

  .blog-description {
    font-size: 1rem;
    line-height: 1.4;
  }

  .blog-stats {
    flex-direction: column;
    gap: 12px;
    align-items: center;
  }

  .stat-item {
    font-size: 0.9rem;
  }

  .search-container {
    margin: 20px auto 0;
    max-width: 100%;
  }

  .search-input :deep(.el-input__inner) {
    font-size: 16px; /* 防止iOS Safari缩放 */
  }

  .blog-main {
    padding: 15px 10px;
  }

  .article-item {
    border-radius: 6px;
    box-shadow: 0 1px 8px rgba(0, 0, 0, 0.08);
  }

  .article-cover {
    height: 160px;
  }

  .article-content {
    padding: 18px;
  }

  .article-title {
    font-size: 1.25rem;
    margin-bottom: 12px;
  }

  .article-summary {
    font-size: 0.9rem;
    margin-bottom: 12px;
    -webkit-line-clamp: 2;
  }

  .sidebar {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .sidebar-widget {
    padding: 15px;
    border-radius: 6px;
  }

  .widget-title {
    font-size: 1rem;
    margin-bottom: 12px;
  }

  .about-content {
    padding: 0 10px;
  }

  .author-avatar {
    width: 70px;
    height: 70px;
    margin-bottom: 12px;
  }

  .author-name {
    font-size: 1rem;
  }

  .about-desc {
    font-size: 0.85rem;
  }

  .social-links {
    gap: 18px;
  }

  .social-link {
    font-size: 1.1rem;
  }

  .category-list,
  .hot-article-list,
  .archive-list {
    font-size: 0.9rem;
  }

  .recent-comments {
    max-height: 250px;
  }

  .comment-item {
    padding: 10px 0;
  }

  .load-more-container {
    margin-top: 30px;
  }
}

/* 平板响应式优化 */
@media (min-width: 769px) and (max-width: 1024px) {
  .article-list {
    gap: 25px;
  }

  .article-item {
    display: flex;
    flex-direction: row;
    height: auto;
  }

  .article-cover {
    width: 280px;
    height: 180px;
    flex-shrink: 0;
  }

  .article-content {
    flex: 1;
    padding: 20px 25px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }

  .article-title {
    margin-bottom: 10px;
  }

  .article-meta {
    margin-bottom: 12px;
  }

  .article-summary {
    flex: 1;
    margin-bottom: 12px;
  }

  .article-tags {
    margin-bottom: 12px;
  }
}

/* 超小屏幕优化 */
@media (max-width: 360px) {
  .blog-main {
    padding: 10px 8px;
  }

  .article-content {
    padding: 15px;
  }

  .article-title {
    font-size: 1.1rem;
  }

  .sidebar-widget {
    padding: 12px;
  }

  .tag-cloud {
    gap: 6px;
  }

  .tag-item {
    padding: 3px 6px;
    font-size: 0.8rem;
  }
}
</style>