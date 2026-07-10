<template>
  <BlogLayout>
    <div class="archive-page">
      <!-- 页面头部 -->
      <header class="page-header">
        <div class="header-bg"></div>
        <div class="header-content">
          <div class="header-icon">
            <el-icon :size="48">
              <Calendar />
            </el-icon>
          </div>
          <h1 class="page-title">文章归档</h1>
          <p class="page-description">
            共
            <span class="highlight-number">{{ totalArticles }}</span>
            篇文章， 记录了
            <span class="highlight-number">{{ archiveList.length }}</span>
            个月的写作历程
          </p>
          <div class="back-button">
            <router-link to="/" class="back-link">
              <el-button type="default" plain size="large" round>
                <el-icon><ArrowLeft /></el-icon>
                返回首页
              </el-button>
            </router-link>
          </div>
        </div>
      </header>

      <!-- 归档内容 -->
      <main class="archive-main">
        <div
          v-loading="loading"
          element-loading-text="加载归档中..."
          element-loading-background="rgba(255, 255, 255, 0.9)"
          class="archive-content"
        >
          <!-- 空状态 -->
          <div v-if="archiveList.length === 0 && !loading" class="no-data">
            <div class="empty-icon">
              <el-icon :size="80">
                <Calendar />
              </el-icon>
            </div>
            <h3>暂无归档数据</h3>
            <p>还没有发布任何文章，快去创作吧！</p>
            <router-link to="/" class="go-home-link">
              <el-button type="primary" round>返回首页</el-button>
            </router-link>
          </div>

          <!-- 归档列表 -->
          <div v-else class="timeline-container">
            <div class="timeline-line"></div>

            <div
              v-for="(archive, index) in archiveList"
              :key="archive.archive_date"
              class="timeline-item"
              :style="{ animationDelay: `${index * 0.1}s` }"
            >
              <!-- 时间轴节点 -->
              <div class="timeline-dot"></div>

              <!-- 归档卡片 -->
              <div class="archive-card">
                <!-- 归档头部 -->
                <div class="archive-header" @click="toggleArchive(archive.archive_date)">
                  <div class="archive-date-wrapper">
                    <div class="archive-year">
                      {{ archive.archive_date.split('-')[0] }}
                    </div>
                    <div class="archive-month">
                      {{ formatMonth(archive.archive_date.split('-')[1]) }}
                    </div>
                  </div>

                  <div class="archive-meta">
                    <span class="archive-count">
                      <el-icon><Document /></el-icon>
                      {{ archive.article_count }} 篇文章
                    </span>
                    <el-icon
                      :size="20"
                      class="toggle-icon"
                      :class="{ expanded: expandedArchives.includes(archive.archive_date) }"
                    >
                      <ArrowDown />
                    </el-icon>
                  </div>
                </div>

                <!-- 文章列表 -->
                <transition
                  name="expand"
                  @enter="onEnter"
                  @after-enter="onAfterEnter"
                  @leave="onLeave"
                >
                  <div
                    v-show="expandedArchives.includes(archive.archive_date)"
                    class="archive-articles"
                  >
                    <!-- 加载状态 -->
                    <div v-if="loadingArticles[archive.archive_date]" class="loading-articles">
                      <el-icon :size="32" class="is-loading">
                        <Loading />
                      </el-icon>
                      <span>加载文章中...</span>
                    </div>

                    <!-- 文章列表 -->
                    <div
                      v-else-if="
                        articlesByArchive[archive.archive_date] &&
                        articlesByArchive[archive.archive_date].length > 0
                      "
                      class="article-list"
                    >
                      <div
                        v-for="(article, articleIndex) in articlesByArchive[archive.archive_date]"
                        :key="article.id"
                        class="article-item"
                        :style="{ animationDelay: `${articleIndex * 0.05}s` }"
                      >
                        <!-- 文章封面 -->
                        <div v-if="article.coverUrl || article.coverImage" class="article-cover">
                          <img
                            :src="article.coverUrl || article.coverImage"
                            :alt="article.title"
                            loading="lazy"
                          />
                          <div class="cover-overlay"></div>
                        </div>

                        <!-- 文章信息 -->
                        <div class="article-info">
                          <router-link
                            :to="{ name: 'PublicBlogArticleDetail', params: { id: article.id } }"
                            class="article-title"
                          >
                            {{ article.title }}
                          </router-link>

                          <p class="article-summary">
                            {{
                              article.summary ||
                              stripHtmlTags(article.content).substring(0, 150) + '...'
                            }}
                          </p>

                          <div class="article-meta">
                            <span class="meta-item">
                              <el-icon :size="14"><Calendar /></el-icon>
                              {{ formatDate(article.createTime) }}
                            </span>
                            <span class="meta-item">
                              <el-icon :size="14"><View /></el-icon>
                              {{ article.viewCount || 0 }}
                            </span>
                            <span class="meta-item">
                              <el-icon :size="14"><ChatDotRound /></el-icon>
                              {{ article.commentCount || 0 }}
                            </span>
                            <span v-if="article.likeCount" class="meta-item">
                              <el-icon :size="14"><Star /></el-icon>
                              {{ article.likeCount }}
                            </span>
                          </div>
                        </div>
                      </div>
                    </div>

                    <!-- 无文章状态 -->
                    <div v-else class="no-articles">
                      <el-icon :size="48">
                        <DocumentDelete />
                      </el-icon>
                      <p>该月份暂无文章</p>
                    </div>
                  </div>
                </transition>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  </BlogLayout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import BlogLayout from '@/components/BlogLayout.vue'
import { getArticleArchive, getArticlesByArchive } from '@/api/blog/article'
import {
  Calendar,
  ArrowDown,
  Loading,
  View,
  ChatDotRound,
  ArrowLeft,
  Document,
  Star,
  DocumentDelete
} from '@element-plus/icons-vue'

// 响应式数据
const archiveList = ref<any[]>([])
const articlesByArchive = ref<Record<string, any[]>>({})
const loadingArticles = ref<Record<string, boolean>>({})
const expandedArchives = ref<string[]>([])
const totalArticles = ref(0)
const loading = ref(false)

// 获取归档数据
const loadArchiveData = async () => {
  try {
    loading.value = true
    const response = await getArticleArchive()
    archiveList.value = response.data || []

    // 计算总文章数
    totalArticles.value = archiveList.value.reduce(
      (total, item) => total + (item.article_count || 0),
      0
    )

    // 默认展开第一个归档
    if (archiveList.value.length > 0) {
      expandedArchives.value = [archiveList.value[0].archive_date]
      await loadArticlesByArchive(archiveList.value[0].archive_date)
    }
  } catch (error) {
    console.error('获取归档数据失败:', error)
  } finally {
    loading.value = false
  }
}

// 根据归档月份获取文章列表
const loadArticlesByArchive = async (archiveDate: string) => {
  try {
    loadingArticles.value[archiveDate] = true
    const [year, month] = archiveDate.split('-')
    const response = await getArticlesByArchive(Number(year), Number(month), {
      pageNum: 1,
      pageSize: 100
    })

    if (response && response.code === 200) {
      articlesByArchive.value[archiveDate] = response.rows || response.data || []
    }
  } catch (error) {
    console.error('获取归档文章失败:', error)
    articlesByArchive.value[archiveDate] = []
  } finally {
    loadingArticles.value[archiveDate] = false
  }
}

// 切换归档展开/收起
const toggleArchive = async (archiveDate: string) => {
  const index = expandedArchives.value.indexOf(archiveDate)

  if (index > -1) {
    // 收起
    expandedArchives.value.splice(index, 1)
  } else {
    // 展开
    expandedArchives.value.push(archiveDate)

    // 如果还没有加载文章，则加载
    if (!articlesByArchive.value[archiveDate]) {
      await loadArticlesByArchive(archiveDate)
    }
  }
}

// 格式化月份
const formatMonth = (monthStr: string) => {
  const monthNames = [
    '一月',
    '二月',
    '三月',
    '四月',
    '五月',
    '六月',
    '七月',
    '八月',
    '九月',
    '十月',
    '十一月',
    '十二月'
  ]
  return monthNames[parseInt(monthStr) - 1]
}

// 格式化日期
const formatDate = (dateString: string) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

// 去除HTML标签
const stripHtmlTags = (html: string) => {
  if (!html) return ''
  return html.replace(/<[^>]*>/g, '')
}

// 动画钩子
const onEnter = (el: Element) => {
  const element = el as HTMLElement
  element.style.height = '0'
  element.style.opacity = '0'
}

const onAfterEnter = (el: Element) => {
  const element = el as HTMLElement
  element.style.transition = 'all 0.3s ease'
  element.style.height = element.scrollHeight + 'px'
  element.style.opacity = '1'
}

const onLeave = (el: Element) => {
  const element = el as HTMLElement
  element.style.height = element.scrollHeight + 'px'
  element.style.opacity = '1'

  // 强制重绘
  element.offsetHeight

  element.style.transition = 'all 0.3s ease'
  element.style.height = '0'
  element.style.opacity = '0'
}

// 组件挂载时加载数据
onMounted(() => {
  loadArchiveData()
})
</script>

<style scoped>
/* 页面容器 */
.archive-page {
  padding-top: 64px;
  min-height: 100vh;
  background: #fafbfc;
  padding-bottom: 0;
}

/* 页面头部 */
.page-header {
  position: relative;
  padding: 100px 20px 80px;
  text-align: center;
  color: white;
  overflow: hidden;
  background: linear-gradient(135deg, #4a7bff 0%, #6b8cff 100%);
}

/* 页面头部底部波浪过渡 */
.page-header::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  right: 0;
  height: 40px;
  background: #fafbfc;
  border-radius: 40px 40px 0 0;
}

.header-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background:
    radial-gradient(circle at 20% 50%, rgba(255, 255, 255, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 80% 80%, rgba(255, 255, 255, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 40% 20%, rgba(255, 255, 255, 0.05) 0%, transparent 30%);
  animation: float 20s ease-in-out infinite;
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(5deg);
  }
}

.header-content {
  position: relative;
  z-index: 1;
  max-width: 900px;
  margin: 0 auto;
}

.header-icon {
  margin-bottom: 20px;
  animation: bounce 2s ease-in-out infinite;
}

@keyframes bounce {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.page-title {
  font-size: 3rem;
  margin-bottom: 16px;
  font-weight: 800;
  letter-spacing: -1px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
  animation: slideDown 0.8s ease;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.page-description {
  font-size: 1.2rem;
  opacity: 0.95;
  margin-bottom: 30px;
  font-weight: 300;
  animation: slideDown 0.8s ease 0.2s both;
}

.highlight-number {
  font-size: 1.4rem;
  font-weight: 700;
  color: #ffd700;
  text-shadow: 0 0 10px rgba(255, 215, 0, 0.5);
}

.back-button {
  animation: slideDown 0.8s ease 0.4s both;
}

.back-link {
  text-decoration: none;
}

/* 主要内容区域 */
.archive-main {
  max-width: 900px;
  margin: 0 auto;
  padding: 40px 20px 60px;
  position: relative;
  z-index: 2;
}

.archive-content {
  background: white;
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  overflow: hidden;
  min-height: 400px;
}

/* 空状态 */
.no-data {
  text-align: center;
  padding: 80px 20px;
  color: #999;
}

.empty-icon {
  margin-bottom: 20px;
  color: #ddd;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0%,
  100% {
    opacity: 0.5;
    transform: scale(1);
  }
  50% {
    opacity: 1;
    transform: scale(1.05);
  }
}

.no-data h3 {
  font-size: 1.5rem;
  color: #666;
  margin-bottom: 10px;
}

.no-data p {
  font-size: 1rem;
  color: #999;
  margin-bottom: 30px;
}

.go-home-link {
  text-decoration: none;
}

/* 时间轴容器 */
.timeline-container {
  position: relative;
  padding: 40px 30px;
}

.timeline-line {
  position: absolute;
  left: 50px;
  top: 0;
  bottom: 0;
  width: 3px;
  background: linear-gradient(180deg, #4a7bff 0%, #6b8cff 100%);
  border-radius: 2px;
}

/* 时间轴项 */
.timeline-item {
  position: relative;
  margin-bottom: 30px;
  padding-left: 80px;
  opacity: 0;
  animation: fadeInRight 0.6s ease forwards;
}

@keyframes fadeInRight {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.timeline-item:last-child {
  margin-bottom: 0;
}

.timeline-dot {
  position: absolute;
  left: 35px;
  top: 25px;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: white;
  border: 4px solid #4a7bff;
  box-shadow: 0 0 0 4px rgba(74, 123, 255, 0.2);
  z-index: 1;
  transition: all 0.3s ease;
}

.timeline-item:hover .timeline-dot {
  background: #4a7bff;
  box-shadow: 0 0 0 8px rgba(74, 123, 255, 0.3);
  transform: scale(1.2);
}

/* 归档卡片 */
.archive-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.3s ease;
}

.archive-card:hover {
  box-shadow: 0 8px 30px rgba(74, 123, 255, 0.2);
}

/* 归档头部 */
.archive-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 25px;
  background: linear-gradient(135deg, #f8f9ff 0%, #f5f5ff 100%);
  cursor: pointer;
  user-select: none;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.archive-header:hover {
  background: linear-gradient(135deg, #e8e9ff 0%, #e5e5ff 100%);
  border-color: #4a7bff;
}

.archive-date-wrapper {
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.archive-year {
  font-size: 2rem;
  font-weight: 800;
  background: linear-gradient(135deg, #4a7bff 0%, #6b8cff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.archive-month {
  font-size: 1.2rem;
  font-weight: 600;
  color: #666;
}

.archive-meta {
  display: flex;
  align-items: center;
  gap: 15px;
}

.archive-count {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 16px;
  background: white;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 600;
  color: #4a7bff;
  box-shadow: 0 2px 8px rgba(74, 123, 255, 0.15);
}

.toggle-icon {
  transition: transform 0.3s ease;
  color: #4a7bff;
}

.toggle-icon.expanded {
  transform: rotate(180deg);
}

/* 文章列表容器 */
.archive-articles {
  overflow: hidden;
  background: #fafafa;
}

/* 加载状态 */
.loading-articles {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 50px;
  color: #999;
  font-size: 1rem;
}

/* 文章列表 */
.article-list {
  padding: 20px 25px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 文章项 */
.article-item {
  display: flex;
  gap: 20px;
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border: 2px solid transparent;
  opacity: 0;
  animation: fadeInUp 0.5s ease forwards;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.article-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 30px rgba(74, 123, 255, 0.15);
  border-color: #4a7bff;
}

/* 文章封面 */
.article-cover {
  width: 200px;
  height: 130px;
  flex-shrink: 0;
  border-radius: 12px;
  overflow: hidden;
  position: relative;
}

.article-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.article-item:hover .article-cover img {
  transform: scale(1.1);
}

.cover-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(74, 123, 255, 0.3) 0%, rgba(107, 140, 255, 0.3) 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.article-item:hover .cover-overlay {
  opacity: 1;
}

/* 文章信息 */
.article-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  min-width: 0;
}

.article-title {
  font-size: 1.15rem;
  font-weight: 700;
  color: #333;
  text-decoration: none;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-bottom: 10px;
  transition: color 0.3s ease;
}

.article-title:hover {
  color: #4a7bff;
}

.article-summary {
  font-size: 0.9rem;
  color: #666;
  line-height: 1.6;
  margin: 0 0 14px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.article-meta {
  display: flex;
  gap: 18px;
  font-size: 0.85rem;
  color: #999;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  transition: color 0.3s ease;
}

.article-item:hover .meta-item {
  color: #4a7bff;
}

/* 无文章状态 */
.no-articles {
  text-align: center;
  padding: 40px;
  color: #999;
}

.no-articles .el-icon {
  color: #ddd;
  margin-bottom: 12px;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .page-title {
    font-size: 2.5rem;
  }

  .archive-main {
    max-width: 700px;
  }
}

@media (max-width: 768px) {
  .page-header {
    padding: 80px 20px 50px;
  }

  .page-title {
    font-size: 2rem;
  }

  .page-description {
    font-size: 1rem;
  }

  .archive-main {
    margin-top: -30px;
  }

  .archive-content {
    border-radius: 16px;
  }

  .timeline-container {
    padding: 30px 20px;
  }

  .timeline-line {
    left: 30px;
  }

  .timeline-item {
    padding-left: 60px;
  }

  .timeline-dot {
    left: 22px;
    width: 14px;
    height: 14px;
  }

  .archive-header {
    padding: 16px 20px;
  }

  .archive-year {
    font-size: 1.6rem;
  }

  .archive-month {
    font-size: 1rem;
  }

  .article-item {
    flex-direction: column;
  }

  .article-cover {
    width: 100%;
    height: 160px;
  }

  .article-meta {
    gap: 12px;
  }
}

@media (max-width: 480px) {
  .page-header {
    padding: 60px 15px 40px;
  }

  .page-title {
    font-size: 1.6rem;
  }

  .page-description {
    font-size: 0.9rem;
  }

  .timeline-container {
    padding: 20px 15px;
  }

  .timeline-line {
    left: 20px;
  }

  .timeline-item {
    padding-left: 50px;
  }

  .timeline-dot {
    left: 13px;
    width: 12px;
    height: 12px;
  }

  .archive-header {
    padding: 14px 16px;
  }

  .archive-year {
    font-size: 1.4rem;
  }

  .archive-month {
    font-size: 0.9rem;
  }

  .archive-count {
    font-size: 0.8rem;
    padding: 4px 12px;
  }

  .article-list {
    padding: 15px 16px;
  }

  .article-item {
    padding: 16px;
  }

  .article-title {
    font-size: 1rem;
  }

  .article-summary {
    font-size: 0.85rem;
  }

  .article-meta {
    font-size: 0.8rem;
    gap: 10px;
  }
}

/* 深色主题 */
html.dark .archive-page {
  background: #1e1e2e;
}

html.dark .page-header {
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
}

html.dark .page-header::after {
  background: #1e1e2e;
}

html.dark .archive-content {
  background: #252535;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

html.dark .no-data {
  color: #999;
}

html.dark .empty-icon {
  color: #444;
}

html.dark .no-data h3 {
  color: #e0e0e0;
}

html.dark .timeline-line {
  background: linear-gradient(180deg, #4a7bff 0%, #6b8cff 100%);
}

html.dark .timeline-dot {
  background: #1e1e2e;
  border-color: #4a7bff;
}

html.dark .timeline-item:hover .timeline-dot {
  background: #4a7bff;
}

html.dark .archive-card {
  background: #252535;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

html.dark .archive-card:hover {
  box-shadow: 0 8px 30px rgba(74, 123, 255, 0.2);
}

html.dark .archive-header {
  background: linear-gradient(135deg, #2a2a3e 0%, #2e2e45 100%);
}

html.dark .archive-header:hover {
  background: linear-gradient(135deg, #35354a 0%, #3a3a55 100%);
  border-color: #4a7bff;
}

html.dark .archive-month {
  color: #b0b0b0;
}

html.dark .archive-count {
  background: #2a2a3e;
  color: #4a7bff;
}

html.dark .archive-articles {
  background: #1a1a2e;
}

html.dark .article-item {
  background: #252535;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

html.dark .article-item:hover {
  border-color: #4a7bff;
}

html.dark .article-title {
  color: #e0e0e0;
}

html.dark .article-title:hover {
  color: #4a7bff;
}

html.dark .article-summary {
  color: #b0b0b0;
}

html.dark .article-meta {
  color: #999;
}

html.dark .article-item:hover .meta-item {
  color: #4a7bff;
}

html.dark .no-articles {
  color: #999;
}

html.dark .no-articles .el-icon {
  color: #444;
}

/* 展开动画 */
.expand-enter-active,
.expand-leave-active {
  transition: all 0.3s ease;
  overflow: hidden;
}

.expand-enter-from,
.expand-leave-to {
  height: 0 !important;
  opacity: 0;
}
</style>
