<template>
  <div class="blog-home-container">
    <!-- 博客导航 -->
    <BlogNav />

    <!-- 博客头部 -->
    <header class="blog-header">
      <div class="header-bg"></div>
      <div class="header-content">
        <h1 class="blog-title">
          {{ blogSettings.blog_name || '我的博客' }}
        </h1>
        <p class="blog-subtitle">
          {{ blogSettings.blog_desc || '欢迎来到我的博客' }}
        </p>

        <!-- 搜索栏 -->
        <div
          v-if="isFeatureEnabled('search_enabled')"
          class="search-container"
        >
          <el-input
            v-model="searchKeyword"
            placeholder="搜索文章..."
            size="large"
            class="search-input"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
            <template #append>
              <el-button
                :icon="Search"
                @click="handleSearch"
              />
            </template>
          </el-input>
        </div>

        <div class="header-stats">
          <div class="stat-item">
            <span class="stat-number">{{ total }}</span>
            <span class="stat-label">篇文章</span>
          </div>
          <div class="stat-item">
            <span class="stat-number">{{ categories.length }}</span>
            <span class="stat-label">个分类</span>
          </div>
          <div class="stat-item">
            <span class="stat-number">{{ tags.length }}</span>
            <span class="stat-label">个标签</span>
          </div>
        </div>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <main class="blog-main">
      <div class="content-wrapper">
        <!-- 文章列表 -->
        <section class="articles-section">
          <div class="section-header">
            <h2>最新文章</h2>
            <div class="header-line"></div>
          </div>

          <div
            v-loading="loading"
            class="articles-list"
          >
            <div
              v-for="(article, index) in articles"
              :key="article.id"
              class="article-card"
              :style="{ animationDelay: `${index * 0.1}s` }"
              @click="goToArticle(article.id)"
            >
              <div
                v-if="article.coverUrl"
                class="article-image"
              >
                <img
                  :src="article.coverUrl"
                  :alt="article.title"
                  loading="lazy"
                />
                <div class="image-overlay"></div>
              </div>
              <div class="article-content">
                <div class="article-meta-top">
                  <span
                    v-if="article.categoryName"
                    class="article-category"
                  >
                    <el-icon><FolderOpened /></el-icon>
                    {{ article.categoryName }}
                  </span>
                  <span class="article-date">{{ formatDate(article.createTime) }}</span>
                </div>
                <h3 class="article-title">
                  {{ article.title }}
                </h3>
                <p class="article-summary">
                  {{ article.summary }}
                </p>
                <div class="article-meta-bottom">
                  <div class="meta-left">
                    <span class="article-view">
                      <el-icon><View /></el-icon>
                      {{ article.viewCount || 0 }}
                    </span>
                    <span
                      v-if="article.likeCount"
                      class="article-like"
                    >
                      <el-icon><Star /></el-icon>
                      {{ article.likeCount }}
                    </span>
                  </div>
                  <div
                    v-if="article.tags && article.tags.length > 0"
                    class="article-tags"
                  >
                    <el-tag
                      v-for="tag in article.tags.slice(0, 3)"
                      :key="tag.id"
                      size="small"
                      type="info"
                      class="article-tag"
                      @click.stop="goToTag(tag.id)"
                    >
                      {{ tag.name }}
                    </el-tag>
                    <span
                      v-if="article.tags.length > 3"
                      class="more-tags"
                    >
                      +{{ article.tags.length - 3 }}
                    </span>
                  </div>
                </div>
              </div>
            </div>

            <div
              v-if="articles.length === 0 && !loading"
              class="empty-state"
            >
              <el-icon
                :size="60"
                class="empty-icon"
              >
                <DocumentDelete />
              </el-icon>
              <p>暂无文章</p>
            </div>
          </div>

          <!-- 分页 -->
          <div
            v-if="total > 0"
            class="pagination-wrapper"
          >
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :total="total"
              :page-sizes="[6, 12, 24, 48]"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </section>

        <!-- 侧边栏 -->
        <aside
          v-if="isFeatureEnabled('sidebar_enabled')"
          class="sidebar"
        >
          <!-- 博主信息 -->
          <div class="widget profile-widget">
            <div class="profile-header">
              <div class="avatar-container">
                <img
                  :src="blogAvatarUrl"
                  alt="博主头像"
                  class="profile-avatar"
                  @error="handleAvatarError"
                />
                <div class="avatar-ring"></div>
              </div>
              <h3 class="profile-name">
                {{ blogSettings.blog_author || '博主' }}
              </h3>
            </div>
            <p class="profile-desc">
              {{ blogSettings.author_bio || '这个人很懒，什么都没留下' }}
            </p>
            <div class="profile-stats">
              <div class="profile-stat">
                <span class="stat-value">{{ total }}</span>
                <span class="stat-text">文章</span>
              </div>
              <div class="profile-stat">
                <span class="stat-value">{{ categories.length }}</span>
                <span class="stat-text">分类</span>
              </div>
              <div class="profile-stat">
                <span class="stat-value">{{ tags.length }}</span>
                <span class="stat-text">标签</span>
              </div>
            </div>
          </div>

          <!-- 热门文章 -->
          <div
            class="widget popular-posts-widget"
            :style="{ animationDelay: '0.1s' }"
          >
            <h3 class="widget-title">
              <el-icon><TrendCharts /></el-icon>
              热门文章
            </h3>
            <ul class="popular-posts-list">
              <li
                v-for="(popular, index) in popularArticles"
                :key="popular.id"
                class="popular-post-item"
                :style="{ animationDelay: `${0.2 + index * 0.1}s` }"
                @click="goToArticle(popular.id)"
              >
                <span
                  class="post-rank"
                  :class="`rank-${index + 1}`"
                >{{ index + 1 }}</span>
                <div class="post-info">
                  <span class="post-title">{{ popular.title }}</span>
                  <span class="post-views">
                    <el-icon><View /></el-icon>
                    {{ popular.viewCount || 0 }}
                  </span>
                </div>
              </li>
            </ul>
          </div>

          <!-- 分类列表 -->
          <div
            class="widget categories-widget"
            :style="{ animationDelay: '0.2s' }"
          >
            <h3 class="widget-title">
              <el-icon><FolderOpened /></el-icon>
              分类
            </h3>
            <ul class="categories-list">
              <li
                v-for="(category, index) in categories"
                :key="category.id"
                class="category-item"
                :style="{ animationDelay: `${0.3 + index * 0.05}s` }"
                @click="goToCategory(category.id)"
              >
                <span class="category-name">{{ category.name }}</span>
                <span class="category-count">{{ category.articleCount || 0 }}</span>
              </li>
            </ul>
          </div>

          <!-- 标签云 -->
          <div
            class="widget tags-widget"
            :style="{ animationDelay: '0.3s' }"
          >
            <h3 class="widget-title">
              <el-icon><PriceTag /></el-icon>
              标签云
            </h3>
            <div class="tags-cloud">
              <el-tag
                v-for="(tag, index) in tags"
                :key="tag.id"
                size="small"
                class="tag-item"
                :style="{ animationDelay: `${0.4 + index * 0.03}s` }"
                @click="goToTag(tag.id)"
              >
                {{ tag.name }}
              </el-tag>
            </div>
          </div>

          <!-- 友情链接 -->
          <div
            class="widget links-widget"
            :style="{ animationDelay: '0.4s' }"
          >
            <h3 class="widget-title">
              <el-icon><Link /></el-icon>
              友情链接
            </h3>
            <ul class="links-list">
              <li
                v-for="(link, index) in friendLinks"
                :key="link.id"
                class="link-item"
                :style="{ animationDelay: `${0.5 + index * 0.05}s` }"
              >
                <a
                  :href="link.url"
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  <img
                    v-if="link.logo"
                    :src="link.logo"
                    :alt="link.name"
                    class="link-logo"
                  />
                  <span
                    v-else
                    class="link-icon"
                  >{{ link.name?.charAt(0) || 'L' }}</span>
                  <span class="link-name">{{ link.name }}</span>
                </a>
              </li>
            </ul>
          </div>
        </aside>
      </div>
    </main>

    <!-- 博客底部 -->
    <BlogFooter
      :blog-settings="blogSettings"
      :total-articles="total"
      :category-count="categories.length"
      :tag-count="tags.length"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { FolderOpened, View, Star, DocumentDelete, TrendCharts, PriceTag, Link, Search } from '@element-plus/icons-vue'
import BlogNav from '@/components/BlogNav.vue'
import BlogFooter from '@/components/BlogFooter.vue'
import {
  getArticleList,
  getBlogSettings,
  getHotArticles,
  getCategoryList,
  getTagList,
  getFrontFriendLinkList,
  searchArticles
} from '@/api/blog'
import { processAvatarUrl } from '@/api/blog/avatar'
import { parseTime } from '@/utils/ruoyi'
import { useUserStore } from '@/stores/user'
import { useBlogSettingsStore } from '@/stores/blogSettings'

const router = useRouter()
const userStore = useUserStore()
const blogSettingsStore = useBlogSettingsStore()

// 数据
const articles = ref([])
const popularArticles = ref([])
const categories = ref([])
const tags = ref([])
const friendLinks = ref([])
const blogSettings = computed(() => blogSettingsStore.blogSettings)
const isFeatureEnabled = (feature: string) => blogSettingsStore.isFeatureEnabled(feature)
const loading = ref(true)
const currentPage = ref(1)
const pageSize = ref(6)
const total = ref(0)
const searchKeyword = ref('')
const isSearching = ref(false)

// 处理头像 URL
const blogAvatarUrl = computed(() => {
  const avatar = blogSettings.value.blog_avatar
  console.log('🔍 博客设置中的头像值:', avatar)

  if (!avatar || !avatar.trim()) {
    console.log('⚠️ 头像为空，使用默认头像')
    return '/default-avatar.svg'
  }

  const processedUrl = processAvatarUrl(avatar)
  console.log('✅ 处理后的头像 URL:', processedUrl)

  if (!processedUrl) {
    console.log('⚠️ 处理后的 URL 为空，使用默认头像')
    return '/default-avatar.svg'
  }

  return processedUrl
})

// 处理头像加载错误
const handleAvatarError = (event: Event) => {
  const img = event.target as HTMLImageElement
  const currentSrc = img.src

  // 防止无限循环：如果已经是默认头像，就不再重试
  if (currentSrc.includes('default-avatar.svg')) {
    console.error('❌ 默认头像也加载失败，停止重试')
    return
  }

  console.error('❌ 头像加载失败:', currentSrc)
  console.log('🔄 切换到默认头像 SVG')

  // 使用 SVG 格式的默认头像（更可靠）
  img.src = '/default-avatar.svg'
}

// 格式化日期
const formatDate = (date: string) => {
  return parseTime(date, '{y}-{m}-{d}')
}

// 跳转到文章详情
const goToArticle = (id: number) => {
  router.push(`/blog/article/${id}`)
}

// 跳转到分类页
const goToCategory = (id: number) => {
  router.push(`/blog/category/${id}`)
}

// 跳转到标签页
const goToTag = (id: number) => {
  router.push(`/blog/tag/${id}`)
}

// 分页处理
const handleSizeChange = (size: number) => {
  pageSize.value = size
  if (isSearching.value) {
    handleSearch()
  } else {
    loadArticles()
  }
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  if (isSearching.value) {
    handleSearch()
  } else {
    loadArticles()
  }
}

// 搜索处理
const handleSearch = async () => {
  if (!searchKeyword.value.trim()) {
    isSearching.value = false
    currentPage.value = 1
    await loadArticles()
    return
  }

  try {
    loading.value = true
    isSearching.value = true
    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      keyword: searchKeyword.value.trim()
    }
    const response = await searchArticles(searchKeyword.value.trim(), params)
    articles.value = response.rows || []
    total.value = response.total || 0
  } catch (error) {
    console.error('搜索文章失败:', error)
  } finally {
    loading.value = false
  }
}

// 加载文章列表
const loadArticles = async () => {
  try {
    loading.value = true
    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      status: 1
    }
    const response = await getArticleList(params)
    articles.value = response.rows || []
    total.value = response.total || 0
  } catch (error) {
    console.error('加载文章列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 加载热门文章
const loadPopularArticles = async () => {
  try {
    const response = await getHotArticles({ pageSize: 5 })
    popularArticles.value = response.rows || []
  } catch (error) {
    console.error('加载热门文章失败:', error)
  }
}

// 加载分类列表
const loadCategories = async () => {
  try {
    const response = await getCategoryList({ pageSize: 100 })
    categories.value = response.data || []
  } catch (error) {
    console.error('加载分类列表失败:', error)
  }
}

// 加载标签列表
const loadTags = async () => {
  try {
    const response = await getTagList({ pageSize: 100 })
    tags.value = response.data || []
  } catch (error) {
    console.error('加载标签列表失败:', error)
  }
}

// 加载友情链接
const loadFriendLinks = async () => {
  try {
    const response = await getFrontFriendLinkList()
    console.log('🔗 友情链接API响应:', response)
    console.log('🔗 友情链接数据长度:', response?.data?.length)
    friendLinks.value = response?.data || []
    console.log('🔗 友情链接数据:', friendLinks.value)
  } catch (error) {
    console.error('❌ 加载友情链接失败:', error)
  }
}

// 加载博客设置
const loadBlogSettings = async () => {
  try {
    const response = await getBlogSettings()
    const settings = response.data || {}
    // 更新 blogSettingsStore
    blogSettingsStore.updateBlogSettings(settings)
    console.log('📦 博客设置加载完成:', settings)
    console.log('📦 blog_avatar 值:', settings.blog_avatar)
  } catch (error) {
    console.error('加载博客设置失败:', error)
    // 使用默认值
    console.log('📦 使用默认博客设置')
  }
}

// 初始化数据
onMounted(async () => {
  await Promise.all([
    loadArticles(),
    loadPopularArticles(),
    loadCategories(),
    loadTags(),
    loadFriendLinks(),
    loadBlogSettings()
  ])
})
</script>

<style scoped>
.blog-home-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

/* 博客头部 */
.blog-header {
  position: relative;
  padding: 120px 0 80px;
  text-align: center;
  color: white;
  overflow: hidden;
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
  0%, 100% {
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
  padding: 0 20px;
}

.blog-title {
  font-size: 3.5rem;
  margin-bottom: 20px;
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

.blog-subtitle {
  font-size: 1.4rem;
  opacity: 0.95;
  margin-bottom: 30px;
  font-weight: 300;
  animation: slideDown 0.8s ease 0.2s both;
}

.search-container {
  max-width: 600px;
  margin: 0 auto 40px;
  animation: slideDown 0.8s ease 0.3s both;
}

.search-input {
  border-radius: 30px;
  overflow: hidden;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

.search-input:hover,
.search-input:focus-within {
  box-shadow: 0 12px 35px rgba(102, 126, 234, 0.3);
  transform: translateY(-2px);
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 30px 0 0 30px;
  padding: 8px 20px;
  background: white;
  box-shadow: none;
  border: 2px solid transparent;
  border-right: none;
  transition: all 0.3s ease;
}

.search-input :deep(.el-input__wrapper:hover),
.search-input :deep(.el-input__wrapper.is-focus) {
  border-color: #667eea;
  border-right: none;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.search-input :deep(.el-input__prefix) {
  color: #667eea;
}

.search-input :deep(.el-input-group__append) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-left: none;
  border-radius: 0 30px 30px 0;
  padding: 0;
  width: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-input :deep(.el-input-group__append .el-button) {
  background: transparent;
  border: none;
  color: white;
  font-size: 18px;
  padding: 0;
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-input :deep(.el-input-group__append .el-button:hover) {
  background: rgba(255, 255, 255, 0.1);
}

.header-stats {
  display: flex;
  justify-content: center;
  gap: 60px;
  animation: slideDown 0.8s ease 0.4s both;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.stat-number {
  font-size: 2.5rem;
  font-weight: 700;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
}

.stat-label {
  font-size: 0.9rem;
  opacity: 0.9;
  text-transform: uppercase;
  letter-spacing: 1px;
}

/* 主要内容区域 */
.blog-main {
  max-width: 1200px;
  margin: -40px auto 0;
  padding: 0 20px;
  position: relative;
  z-index: 2;
}

.content-wrapper {
  display: grid;
  grid-template-columns: 1fr 380px;
  gap: 30px;
  align-items: start;
}

/* 文章列表区域 */
.articles-section {
  background: white;
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  animation: fadeInUp 0.8s ease 0.6s both;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.section-header {
  padding: 30px 30px 20px;
  background: linear-gradient(135deg, #fafafa 0%, #f5f5f5 100%);
  border-bottom: 2px solid #f0f0f0;
  position: relative;
}

.section-header h2 {
  margin: 0;
  font-size: 1.8rem;
  color: #333;
  font-weight: 700;
  display: inline-block;
  position: relative;
}

.header-line {
  position: absolute;
  bottom: -2px;
  left: 30px;
  width: 60px;
  height: 3px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  border-radius: 2px;
}

.articles-list {
  padding: 30px;
  min-height: 400px;
}

.article-card {
  display: flex;
  border: 1px solid #e8e8e8;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 24px;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  background: white;
  opacity: 0;
  animation: fadeInUp 0.6s ease forwards;
}

.article-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 30px rgba(102, 126, 234, 0.2);
  border-color: #667eea;
}

.article-image {
  width: 280px;
  height: 200px;
  flex-shrink: 0;
  position: relative;
  overflow: hidden;
}

.article-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.article-card:hover .article-image img {
  transform: scale(1.1);
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.3) 0%, rgba(118, 75, 162, 0.3) 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.article-card:hover .image-overlay {
  opacity: 1;
}

.article-content {
  flex: 1;
  padding: 24px;
  display: flex;
  flex-direction: column;
}

.article-meta-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 15px;
  margin-bottom: 12px;
  font-size: 0.85rem;
  color: #999;
}

.article-category {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 4px 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
}

.article-date {
  color: #999;
}

.article-title {
  margin: 0 0 12px 0;
  font-size: 1.4rem;
  color: #333;
  font-weight: 700;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  transition: color 0.3s ease;
}

.article-card:hover .article-title {
  color: #667eea;
}

.article-summary {
  margin: 0 0 16px 0;
  color: #666;
  line-height: 1.6;
  font-size: 0.95rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  flex: 1;
}

.article-meta-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.meta-left {
  display: flex;
  gap: 20px;
}

.article-view,
.article-like {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 0.85rem;
  color: #999;
  transition: color 0.3s ease;
}

.article-card:hover .article-view,
.article-card:hover .article-like {
  color: #667eea;
}

.article-tags {
  display: flex;
  gap: 6px;
  align-items: center;
  flex-wrap: wrap;
}

.article-tag {
  cursor: pointer;
  transition: all 0.3s ease;
  background: #f5f5f5;
  border-color: #f5f5f5;
  color: #666;
}

.article-tag:hover {
  background: #667eea;
  border-color: #667eea;
  color: white;
  transform: translateY(-2px);
}

.more-tags {
  font-size: 0.8rem;
  color: #999;
  margin-left: 4px;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.empty-icon {
  color: #ddd;
  margin-bottom: 20px;
}

/* 分页 */
.pagination-wrapper {
  padding: 20px 30px 30px;
  display: flex;
  justify-content: center;
}

/* 侧边栏 */
.sidebar {
  display: flex;
  flex-direction: column;
  gap: 24px;
  position: sticky;
  top: 100px;
}

.widget {
  background: white;
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  opacity: 0;
  animation: fadeInUp 0.6s ease forwards;
}

/* 博主信息卡片特殊处理 */
.profile-widget {
  animation-delay: 0s;
}

.widget-title {
  padding: 20px 24px;
  margin: 0;
  background: linear-gradient(135deg, #fafafa 0%, #f5f5f5 100%);
  font-size: 1.2rem;
  color: #333;
  font-weight: 700;
  border-bottom: 2px solid #f0f0f0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.widget-title .el-icon {
  color: #667eea;
}

/* 博主信息卡片 */
.profile-widget {
  padding: 24px;
  text-align: center;
}

.profile-header {
  margin-bottom: 20px;
}

.avatar-container {
  position: relative;
  display: inline-block;
  margin-bottom: 16px;
}

.profile-avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid white;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.avatar-container:hover .profile-avatar {
  transform: scale(1.1) rotate(5deg);
}

.avatar-ring {
  position: absolute;
  top: -8px;
  left: -8px;
  right: -8px;
  bottom: -8px;
  border: 2px solid transparent;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) border-box;
  -webkit-mask: linear-gradient(#fff 0 0) padding-box, linear-gradient(#fff 0 0);
  mask: linear-gradient(#fff 0 0) padding-box, linear-gradient(#fff 0 0);
  -webkit-mask-composite: destination-out;
  mask-composite: exclude;
  animation: rotate 10s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.profile-name {
  margin: 0 0 8px 0;
  font-size: 1.4rem;
  color: #333;
  font-weight: 700;
}

.profile-desc {
  margin: 0 0 20px 0;
  color: #666;
  font-size: 0.9rem;
  line-height: 1.6;
}

.profile-stats {
  display: flex;
  justify-content: space-around;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}

.profile-stat {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.stat-value {
  font-size: 1.5rem;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.stat-text {
  font-size: 0.8rem;
  color: #999;
}

/* 热门文章 */
.popular-posts-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.popular-post-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 16px 24px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: all 0.3s ease;
  opacity: 0;
  animation: fadeInUp 0.5s ease forwards;
}

.popular-post-item:last-child {
  border-bottom: none;
}

.popular-post-item:hover {
  background: linear-gradient(135deg, #f8f9ff 0%, #f5f5ff 100%);
  padding-left: 28px;
}

.post-rank {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border-radius: 8px;
  background: #f0f0f0;
  color: #999;
  font-size: 0.9rem;
  font-weight: 700;
  flex-shrink: 0;
  transition: all 0.3s ease;
}

.rank-1 {
  background: linear-gradient(135deg, #ffd700 0%, #ffa500 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(255, 215, 0, 0.4);
}

.rank-2 {
  background: linear-gradient(135deg, #c0c0c0 0%, #a8a8a8 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(192, 192, 192, 0.4);
}

.rank-3 {
  background: linear-gradient(135deg, #cd7f32 0%, #b8860b 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(205, 127, 50, 0.4);
}

.popular-post-item:hover .post-rank {
  transform: scale(1.1);
}

.post-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
  min-width: 0;
}

.post-title {
  font-size: 0.95rem;
  color: #333;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: color 0.3s ease;
}

.popular-post-item:hover .post-title {
  color: #667eea;
}

.post-views {
  font-size: 0.8rem;
  color: #999;
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 分类列表 */
.categories-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.category-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 24px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: all 0.3s ease;
  opacity: 0;
  animation: fadeInUp 0.5s ease forwards;
}

.category-item:last-child {
  border-bottom: none;
}

.category-item:hover {
  background: linear-gradient(135deg, #f8f9ff 0%, #f5f5ff 100%);
  padding-left: 28px;
}

.category-name {
  flex: 1;
  font-size: 0.95rem;
  color: #333;
  font-weight: 500;
  transition: color 0.3s ease;
}

.category-item:hover .category-name {
  color: #667eea;
}

.category-count {
  padding: 4px 10px;
  background: #f0f0f0;
  border-radius: 12px;
  font-size: 0.8rem;
  color: #999;
  font-weight: 600;
  transition: all 0.3s ease;
}

.category-item:hover .category-count {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

/* 标签云 */
.tags-cloud {
  padding: 20px 24px;
}

.tag-item {
  margin: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #f5f5f5;
  border-color: #f5f5f5;
  color: #666;
  font-weight: 500;
  opacity: 0;
  animation: fadeInUp 0.5s ease forwards;
}

.tag-item:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-color: transparent;
  color: white;
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

/* 友情链接 */
.links-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.link-item {
  padding: 12px 24px;
  border-bottom: 1px solid #f0f0f0;
  transition: all 0.3s ease;
  opacity: 0;
  animation: fadeInUp 0.5s ease forwards;
}

.link-item:last-child {
  border-bottom: none;
}

.link-item:hover {
  background: linear-gradient(135deg, #f8f9ff 0%, #f5f5ff 100%);
}

.link-item a {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #666;
  text-decoration: none;
  transition: color 0.3s ease;
}

.link-item a:hover {
  color: #667eea;
}

.link-logo {
  width: 24px;
  height: 24px;
  border-radius: 6px;
  object-fit: cover;
}

.link-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 6px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 12px;
  font-weight: bold;
}

.link-name {
  font-size: 0.95rem;
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .content-wrapper {
    grid-template-columns: 1fr 320px;
    gap: 24px;
  }

  .blog-title {
    font-size: 3rem;
  }

  .search-container {
    max-width: 500px;
  }

  .article-image {
    width: 240px;
    height: 170px;
  }

  .sidebar {
    position: static;
  }
}

@media (max-width: 768px) {
  .blog-header {
    padding: 100px 0 60px;
  }

  .blog-title {
    font-size: 2.5rem;
  }

  .blog-subtitle {
    font-size: 1.2rem;
  }

  .search-container {
    max-width: 100%;
    padding: 0 20px;
  }

  .header-stats {
    gap: 40px;
  }

  .stat-number {
    font-size: 2rem;
  }

  .content-wrapper {
    grid-template-columns: 1fr;
  }

  .blog-main {
    margin-top: -30px;
  }

  .article-card {
    flex-direction: column;
  }

  .article-image {
    width: 100%;
    height: 200px;
  }

  .section-header,
  .articles-list {
    padding: 20px;
  }

  .pagination-wrapper {
    padding: 20px;
  }
}

@media (max-width: 480px) {
  .blog-header {
    padding: 80px 0 50px;
  }

  .blog-title {
    font-size: 2rem;
  }

  .blog-subtitle {
    font-size: 1rem;
  }

  .search-container {
    margin-bottom: 30px;
  }

  .header-stats {
    gap: 30px;
  }

  .stat-number {
    font-size: 1.5rem;
  }

  .stat-label {
    font-size: 0.75rem;
  }

  .section-header h2 {
    font-size: 1.5rem;
  }

  .article-title {
    font-size: 1.2rem;
  }

  .article-summary {
    font-size: 0.9rem;
  }

  .article-meta-bottom {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}

/* 深色主题 */
html.dark .blog-home-container {
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
}

html.dark .search-input :deep(.el-input__wrapper) {
  background: #252535;
  border-color: #3a3a4a;
}

html.dark .search-input :deep(.el-input__wrapper:hover),
html.dark .search-input :deep(.el-input__wrapper.is-focus) {
  border-color: #667eea;
  background: #2a2a3a;
}

html.dark .articles-section,
html.dark .widget {
  background: #1e1e2e;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
}

html.dark .section-header,
html.dark .widget-title {
  background: linear-gradient(135deg, #252535 0%, #2a2a3a 100%);
  border-bottom-color: #3a3a4a;
}

html.dark .section-header h2,
html.dark .widget-title {
  color: #e0e0e0;
}

html.dark .article-card {
  background: #252535;
  border-color: #3a3a4a;
}

html.dark .article-card:hover {
  border-color: #667eea;
}

html.dark .article-title {
  color: #e0e0e0;
}

html.dark .article-card:hover .article-title {
  color: #667eea;
}

html.dark .article-summary {
  color: #b0b0b0;
}

html.dark .article-meta-bottom {
  border-top-color: #3a3a4a;
}

html.dark .article-view,
html.dark .article-like {
  color: #b0b0b0;
}

html.dark .article-tag {
  background: #3a3a4a;
  border-color: #3a3a4a;
  color: #b0b0b0;
}

html.dark .article-tag:hover {
  background: #667eea;
  border-color: #667eea;
  color: white;
}

html.dark .profile-name {
  color: #e0e0e0;
}

html.dark .profile-desc {
  color: #b0b0b0;
}

html.dark .profile-stats {
  border-top-color: #3a3a4a;
}

html.dark .stat-text {
  color: #b0b0b0;
}

html.dark .popular-post-item,
html.dark .category-item,
html.dark .link-item {
  border-bottom-color: #3a3a4a;
}

html.dark .popular-post-item:hover,
html.dark .category-item:hover,
html.dark .link-item:hover {
  background: linear-gradient(135deg, #2a2a3a 0%, #303040 100%);
}

html.dark .post-title,
html.dark .category-name {
  color: #e0e0e0;
}

html.dark .popular-post-item:hover .post-title,
html.dark .category-item:hover .category-name {
  color: #667eea;
}

html.dark .post-views,
html.dark .category-count {
  color: #b0b0b0;
}

html.dark .category-count {
  background: #3a3a4a;
}

html.dark .category-item:hover .category-count {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

html.dark .tag-item {
  background: #3a3a4a;
  border-color: #3a3a4a;
  color: #b0b0b0;
}

html.dark .tag-item:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-color: transparent;
  color: white;
}

html.dark .link-item a {
  color: #b0b0b0;
}

html.dark .link-item a:hover {
  color: #667eea;
}

html.dark .empty-icon {
  color: #3a3a4a;
}
</style>
