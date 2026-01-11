<template>
  <div class="tag-container">
    <!-- 博客导航 -->
    <BlogNav />

    <!-- 标签头部 -->
    <div class="tag-header" :style="{ backgroundColor: tagColor }">
      <div class="header-content">
        <div class="tag-info">
          <div class="tag-icon-large">
            <i class="el-icon-price-tag"></i>
          </div>
          <h1 class="tag-title">
            {{ tagName || '文章标签' }}
          </h1>
          <p class="tag-description">
            {{ tagDescription || `浏览标签"${tagName}"下的所有文章` }}
          </p>
          <div class="tag-stats">
            <span class="stat-item">
              <i class="el-icon-document-copy"></i>
              {{ total }} 篇文章
            </span>
          </div>
        </div>

        <!-- 返回按钮 -->
        <div class="back-button">
          <router-link to="/" class="back-link">
            <el-button type="default" plain>
              <i class="el-icon-arrow-left"></i>
              返回首页
            </el-button>
          </router-link>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="tag-main">
      <div class="main-content">
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-container">
          <div class="loading-grid">
            <el-skeleton v-for="i in 6" :key="i" :loading="loading" animated class="skeleton-item">
              <template #template>
                <div class="article-item">
                  <div class="article-cover">
                    <el-skeleton-item variant="image" style="width: 100%; height: 200px" />
                  </div>
                  <div class="article-content">
                    <el-skeleton-item variant="h3" style="width: 70%; margin-bottom: 15px" />
                    <el-skeleton-item variant="text" style="width: 100%; margin-bottom: 10px" />
                    <el-skeleton-item variant="text" style="width: 90%; margin-bottom: 10px" />
                    <el-skeleton-item variant="text" style="width: 60%; margin-bottom: 15px" />
                    <div style="display: flex; gap: 8px; margin-bottom: 15px">
                      <el-skeleton-item variant="text" style="width: 60px; height: 24px" />
                      <el-skeleton-item variant="text" style="width: 50px; height: 24px" />
                    </div>
                    <el-skeleton-item variant="text" style="width: 80px; height: 20px" />
                  </div>
                </div>
              </template>
            </el-skeleton>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-else-if="articleList.length === 0" class="empty-state">
          <div class="empty-content">
            <i class="el-icon-price-tag empty-icon"></i>
            <h3>暂无文章</h3>
            <p>该标签下还没有文章，敬请期待...</p>
            <router-link to="/" class="back-home-btn">
              <el-button type="primary">返回首页</el-button>
            </router-link>
          </div>
        </div>

        <!-- 文章列表 -->
        <div v-else class="article-list">
          <div v-for="article in articleList" :key="article.id" class="article-item">
            <div v-if="article.coverUrl" class="article-cover">
              <img :src="article.coverUrl" :alt="article.title" loading="lazy" />
              <div v-if="article.categoryName" class="article-category-badge">
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
                <span v-if="article.likeCount" class="meta-item">
                  <i class="el-icon-star-off"></i>
                  {{ article.likeCount }} 点赞
                </span>
                <span v-if="article.commentCount" class="meta-item">
                  <i class="el-icon-chat-line-round"></i>
                  {{ article.commentCount }} 评论
                </span>
              </div>
              <p class="article-summary">
                {{ article.summary || stripHtmlTags(article.content).substring(0, 150) + '...' }}
              </p>
              <div v-if="article.tags && article.tags.length" class="article-tags">
                <span
                  v-for="tag in article.tags.slice(0, 3)"
                  :key="tag.id"
                  class="tag-badge"
                  :style="{ backgroundColor: tag.color || '#409EFF' }"
                >
                  {{ tag.name }}
                </span>
              </div>
              <div class="article-footer">
                <router-link :to="`/blog/article/${article.id}`" class="read-more">
                  阅读全文
                  <i class="el-icon-arrow-right"></i>
                </router-link>
              </div>
            </div>
          </div>
        </div>

        <!-- 加载更多 -->
        <div v-if="articleList.length < total && !loading" class="load-more-container">
          <el-button type="primary" :loading="loadingMore" round @click="loadMoreArticles">
            {{ loadingMore ? '加载中...' : '加载更多' }}
          </el-button>
        </div>

        <!-- 分页 -->
        <div v-if="total > queryParams.pageSize" class="pagination-container">
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
        <!-- 关于这个标签 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <i class="el-icon-price-tag"></i>
            关于这个标签
          </h3>
          <div class="tag-about">
            <div class="tag-icon" :style="{ backgroundColor: tagColor }">
              <i class="el-icon-price-tag"></i>
            </div>
            <h4 class="tag-name">
              {{ tagName || '未命名标签' }}
            </h4>
            <p class="tag-desc">
              {{ tagDescription || '暂无描述' }}
            </p>
            <div class="tag-meta">
              <span class="meta-item">
                <i class="el-icon-document-copy"></i>
                {{ total }} 篇文章
              </span>
              <span class="meta-item">
                <i class="el-icon-date"></i>
                创建时间 {{ formatDate(tagCreateTime) }}
              </span>
            </div>
          </div>
        </div>

        <!-- 相关标签 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <i class="el-icon-share"></i>
            相关标签
          </h3>
          <div class="related-tags">
            <router-link
              v-for="tag in relatedTags.slice(0, 12)"
              :key="tag.id"
              :to="`/blog/tag/${tag.id}`"
              class="related-tag-item"
              :class="{ active: tag.id === currentTagId }"
              :style="{
                backgroundColor: tag.color || '#409EFF',
                fontSize: getTagFontSize(tag.article_count) + 'px',
                transform: `scale(${getTagScale(tag.article_count)})`
              }"
            >
              {{ tag.name }}
              <span class="tag-count">({{ tag.article_count }})</span>
            </router-link>
          </div>
        </div>

        <!-- 热门标签 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <i class="el-icon-star-on"></i>
            热门标签
          </h3>
          <div class="popular-tags">
            <div v-for="tag in popularTags.slice(0, 10)" :key="tag.id" class="popular-tag-item">
              <div class="tag-rank">#{{ popularTags.indexOf(tag) + 1 }}</div>
              <router-link
                :to="`/blog/tag/${tag.id}`"
                class="tag-link"
                :style="{ color: tag.color || '#409EFF' }"
              >
                {{ tag.name }}
              </router-link>
              <div class="tag-article-count">{{ tag.article_count }}篇</div>
            </div>
          </div>
        </div>

        <!-- 最新文章 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <i class="el-icon-star-on"></i>
            最新文章
          </h3>
          <ul class="recent-articles">
            <li
              v-for="article in recentArticles.slice(0, 8)"
              :key="article.id"
              class="article-item"
            >
              <router-link
                :to="`/blog/article/${article.id}`"
                class="article-link"
                :title="article.title"
              >
                <span class="article-date">{{ formatDate(article.createTime, 'MM-dd') }}</span>
                <span class="article-title">{{ article.title }}</span>
              </router-link>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'
import BlogNav from '@/components/BlogNav.vue'
import { getArticlesByTag, getTagDetail } from '@/api/blog/tag'
import { getTagCloud } from '@/api/blog/tag'
import { getArticleList } from '@/api/blog/article'

const route = useRoute()

// 响应式数据
const articleList = ref([])
const tagName = ref('')
const tagDescription = ref('')
const tagColor = ref('#409EFF')
const tagCreateTime = ref('')
const total = ref(0)
const loading = ref(false)
const loadingMore = ref(false)
const currentTagId = ref(null)
const relatedTags = ref([])
const popularTags = ref([])
const recentArticles = ref([])

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  tagId: null,
  status: 1 // 只显示已发布的文章
})

// 获取标签文章列表
const loadTagArticles = async (append = false) => {
  try {
    loading.value = !append
    if (append) loadingMore.value = true

    const response = await getArticlesByTag(queryParams.tagId, queryParams)
    console.log('标签文章列表完整响应:', JSON.stringify(response, null, 2))
    console.log('查询参数:', queryParams.tagId, queryParams)

    // 处理不同的响应格式
    let newArticles = []
    let totalCount = 0

    if (response && response.rows && Array.isArray(response.rows)) {
      // 标准的 TableDataInfo 格式
      newArticles = response.rows
      totalCount = response.total || 0
    } else if (response && Array.isArray(response)) {
      // 直接返回数组格式
      newArticles = response
      totalCount = response.length
    } else if (response && response.data && Array.isArray(response.data)) {
      // 包装在 data 字段中的格式
      newArticles = response.data
      totalCount = response.total || response.data.length
    } else {
      console.warn('未知的响应格式:', response)
      newArticles = []
      totalCount = 0
    }

    console.log('处理后的文章数量:', newArticles.length)
    console.log('处理后的总数:', totalCount)
    console.log('第一篇文章内容:', newArticles[0] ? '有内容' : '无内容')
    if (append) {
      articleList.value = [...articleList.value, ...newArticles]
    } else {
      articleList.value = newArticles
    }

    // 如果是首次加载，更新总数（避免分页时覆盖标签详情中的总数）
    if (!append && totalCount > 0) {
      total.value = totalCount
    }

    console.log('文章列表长度:', articleList.value.length, '总数:', total.value)
  } catch (error) {
    console.error('获取标签文章失败:', error)
    ElMessage.error('获取文章列表失败')
  } finally {
    loading.value = false
    loadingMore.value = false
  }
}

// 获取标签详情
const loadTagDetail = async () => {
  try {
    const response = await getTagDetail(queryParams.tagId)
    console.log('标签详情响应:', response)
    const tag = response.data || response

    tagName.value = tag.name || ''
    tagDescription.value = tag.description || ''
    tagColor.value = tag.color || '#409EFF'
    tagCreateTime.value = tag.createTime || new Date().toISOString()

    // 更新页面显示的文章数量
    if (tag.articleCount !== undefined && tag.articleCount !== null) {
      total.value = tag.articleCount
      console.log('标签文章数量:', tag.articleCount)
    }
  } catch (error) {
    console.error('获取标签详情失败:', error)
  }
}

// 获取相关标签
const loadRelatedTags = async () => {
  try {
    const response = await getTagCloud()
    console.log('相关标签响应:', response)
    const tags = response.data || []
    relatedTags.value = tags.filter(tag => tag.id !== currentTagId.value).slice(0, 12)
  } catch (error) {
    console.error('获取相关标签失败:', error)
  }
}

// 获取热门标签
const loadPopularTags = async () => {
  try {
    const response = await getTagCloud()
    console.log('热门标签响应:', response)
    popularTags.value = (response.data || []).slice(0, 10)
  } catch (error) {
    console.error('获取热门标签失败:', error)
  }
}

// 获取最新文章
const loadRecentArticles = async () => {
  try {
    const response = await getArticleList({ pageNum: 1, pageSize: 8, status: 1 })
    console.log('最新文章响应:', response)
    recentArticles.value = response.rows || []
  } catch (error) {
    console.error('获取最新文章失败:', error)
  }
}

// 加载更多文章
const loadMoreArticles = () => {
  if (loadingMore.value || articleList.value.length >= total.value) return
  queryParams.pageNum++
  loadTagArticles(true)
}

// 分页处理
const handlePageChange = page => {
  queryParams.pageNum = page
  loadTagArticles()
}

// 日期格式化
const formatDate = (dateString, format = 'full') => {
  if (!dateString) return ''
  const date = new Date(dateString)

  if (format === 'MM-dd') {
    return date.toLocaleDateString('zh-CN', {
      month: '2-digit',
      day: '2-digit'
    })
  }

  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

// 根据文章数量计算标签字体大小
const getTagFontSize = count => {
  if (count >= 10) return 16
  if (count >= 5) return 14
  if (count >= 2) return 12
  return 11
}

// 根据标签数量计算缩放比例
const getTagScale = count => {
  if (count >= 10) return 1.1
  if (count >= 5) return 1.05
  if (count >= 2) return 1.0
  return 0.95
}

// 去除HTML标签
const stripHtmlTags = html => {
  if (!html) return ''
  return html.replace(/<[^>]*>/g, '')
}

// 监听路由变化
watch(
  () => route.params.id,
  newId => {
    if (newId) {
      currentTagId.value = parseInt(newId)
      queryParams.tagId = parseInt(newId)
      queryParams.pageNum = 1
      loadTagDetail()
      loadTagArticles()
      loadRelatedTags()
    }
  },
  { immediate: true }
)

// 组件挂载时加载数据
onMounted(() => {
  loadPopularTags()
  loadRecentArticles()
})
</script>

<style scoped>
.tag-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.tag-header {
  color: white;
  padding: 60px 0;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.tag-header::before {
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
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 40px;
}

.tag-info {
  flex: 1;
  text-align: left;
}

.tag-icon-large {
  font-size: 4rem;
  margin-bottom: 20px;
  opacity: 0.9;
}

.tag-title {
  font-size: 3rem;
  margin: 0 0 20px 0;
  font-weight: 700;
  background: linear-gradient(45deg, #fff, #f0f8ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.tag-description {
  font-size: 1.3rem;
  opacity: 0.95;
  margin: 0 0 20px 0;
  max-width: 600px;
  line-height: 1.6;
}

.tag-stats {
  display: flex;
  gap: 20px;
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

.back-button {
  flex-shrink: 0;
}

.back-link {
  text-decoration: none;
}

.tag-main {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
  display: flex;
  gap: 40px;
}

.main-content {
  flex: 1;
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

.load-more-container {
  display: flex;
  justify-content: center;
  margin-top: 40px;
  padding-bottom: 20px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

.sidebar {
  width: 300px;
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

.tag-about {
  text-align: center;
}

.tag-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.5rem;
  margin-bottom: 15px;
  margin: 0 auto 15px;
}

.tag-about .tag-name {
  font-size: 1.2rem;
  font-weight: 600;
  margin: 10px 0 8px 0;
  color: #333;
}

.tag-about .tag-desc {
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 15px;
  line-height: 1.5;
}

.tag-meta {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.tag-meta .meta-item {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  font-size: 0.85rem;
  color: #666;
  background: rgba(102, 102, 102, 0.05);
  padding: 6px 12px;
  border-radius: 6px;
}

.related-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  padding: 15px 0;
}

.related-tag-item {
  color: white;
  text-decoration: none;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  padding: 8px 12px;
  border-radius: 8px;
  font-size: 0.85rem;
  font-weight: 500;
  position: relative;
  overflow: hidden;
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  min-height: 50px;
}

.related-tag-item:hover,
.related-tag-item.active {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
}

.tag-count {
  font-size: 0.75rem;
  opacity: 0.8;
}

.popular-tags {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.popular-tag-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.popular-tag-item:last-child {
  border-bottom: none;
}

.tag-rank {
  font-weight: bold;
  color: #409eff;
  min-width: 24px;
  text-align: center;
}

.tag-link {
  flex: 1;
  text-decoration: none;
  font-weight: 500;
  transition: opacity 0.3s ease;
}

.tag-link:hover {
  opacity: 0.8;
}

.tag-article-count {
  font-size: 0.8rem;
  color: #999;
}

.recent-articles {
  list-style: none;
  padding: 0;
  margin: 0;
}

.recent-articles .article-item {
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.recent-articles .article-item:last-child {
  margin-bottom: 0;
  border-bottom: none;
}

.article-link {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #333;
  text-decoration: none;
  font-size: 0.9rem;
  line-height: 1.4;
  transition: color 0.3s ease;
}

.article-link:hover {
  color: #409eff;
}

.article-date {
  font-size: 0.8rem;
  color: #999;
  min-width: 40px;
}

.article-link .article-title {
  flex: 1;
  margin: 0;
  font-size: 0.9rem;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
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

.back-home-btn {
  margin-top: 20px;
  text-decoration: none;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .tag-main {
    max-width: 100%;
    padding: 30px 20px;
    gap: 30px;
  }

  .sidebar {
    width: 260px;
  }
}

@media (max-width: 1024px) {
  .tag-main {
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
  .tag-header {
    padding: 40px 0;
  }

  .header-content {
    flex-direction: column;
    gap: 30px;
    text-align: center;
  }

  .tag-info {
    text-align: center;
  }

  .tag-icon-large {
    font-size: 3rem;
  }

  .tag-title {
    font-size: 2.2rem;
  }

  .tag-description {
    font-size: 1.1rem;
    padding: 0 20px;
  }

  .tag-stats {
    justify-content: center;
  }

  .tag-main {
    flex-direction: column;
    padding: 20px 15px;
    gap: 25px;
  }

  .main-content {
    order: 1;
  }

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

  .related-tags {
    gap: 8px;
  }

  .related-tag-item {
    padding: 6px 10px;
    font-size: 0.8rem;
    min-height: 45px;
  }

  .popular-tag-item {
    padding: 6px 0;
  }
}

@media (max-width: 480px) {
  .header-content {
    padding: 0 15px;
  }

  .tag-icon-large {
    font-size: 2.5rem;
  }

  .tag-title {
    font-size: 1.8rem;
    line-height: 1.2;
  }

  .tag-description {
    font-size: 1rem;
    line-height: 1.4;
  }

  .tag-stats {
    flex-direction: column;
    gap: 12px;
    align-items: center;
  }

  .stat-item {
    font-size: 0.9rem;
  }

  .tag-main {
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

  .tag-about {
    padding: 0 10px;
  }

  .tag-icon {
    width: 50px;
    height: 50px;
    font-size: 1.2rem;
  }

  .tag-about .tag-name {
    font-size: 1rem;
  }

  .tag-about .tag-desc {
    font-size: 0.85rem;
  }
}
</style>
