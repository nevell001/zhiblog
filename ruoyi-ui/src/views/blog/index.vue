<template>
  <div class="blog-home-container">
    <!-- 博客导航 -->
    <BlogNav />

    <!-- 博客头部 -->
    <header class="blog-header">
      <div class="header-content">
        <h1 class="blog-title">
          {{ blogSettings.blog_name || '我的博客' }}
        </h1>
        <p class="blog-subtitle">
          {{ blogSettings.blog_desc || '欢迎来到我的博客' }}
        </p>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <main class="blog-main">
      <div class="content-wrapper">
        <!-- 文章列表 -->
        <section class="articles-section">
          <div class="section-header">
            <h2>最新文章</h2>
          </div>

          <div v-loading="loading" class="articles-list">
            <div
              v-for="article in articles"
              :key="article.id"
              class="article-card"
              @click="goToArticle(article.id)"
            >
              <div v-if="article.coverUrl" class="article-image">
                <img :src="article.coverUrl" :alt="article.title" />
              </div>
              <div class="article-content">
                <h3 class="article-title">
                  {{ article.title }}
                </h3>
                <p class="article-summary">
                  {{ article.summary }}
                </p>
                <div class="article-meta">
                  <span class="article-date">{{ formatDate(article.createTime) }}</span>
                  <span v-if="article.categoryName" class="article-category">
                    {{ article.categoryName }}
                  </span>
                  <span class="article-view">浏览 {{ article.viewCount || 0 }}</span>
                </div>
                <div v-if="article.tags && article.tags.length > 0" class="article-tags">
                  <el-tag
                    v-for="tag in article.tags"
                    :key="tag.id"
                    size="small"
                    type="info"
                    class="article-tag"
                  >
                    {{ tag.name }}
                  </el-tag>
                </div>
              </div>
            </div>

            <div v-if="articles.length === 0 && !loading" class="empty-state">
              <p>暂无文章</p>
            </div>
          </div>

          <!-- 分页 -->
          <div v-if="total > 0" class="pagination-wrapper">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :total="total"
              :page-sizes="[5, 10, 20, 50]"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </section>

        <!-- 侧边栏 -->
        <aside class="sidebar">
          <!-- 博主信息 -->
          <div class="widget profile-widget">
            <div class="profile-header">
              <img
                :src="blogSettings.blog_avatar || '/src/assets/images/profile.jpg'"
                alt="博主头像"
                class="profile-avatar"
              />
              <h3 class="profile-name">
                {{ blogSettings.blog_author || '博主' }}
              </h3>
            </div>
            <p class="profile-desc">
              {{ blogSettings.author_description || '这个人很懒，什么都没留下' }}
            </p>
          </div>

          <!-- 热门文章 -->
          <div class="widget popular-posts-widget">
            <h3 class="widget-title">热门文章</h3>
            <ul class="popular-posts-list">
              <li
                v-for="(popular, index) in popularArticles"
                :key="popular.id"
                class="popular-post-item"
                @click="goToArticle(popular.id)"
              >
                <span class="post-rank" :class="{ 'top-3': index < 3 }">{{ index + 1 }}</span>
                <span class="post-title">{{ popular.title }}</span>
                <span class="post-views">{{ popular.viewCount || 0 }} 浏览</span>
              </li>
            </ul>
          </div>

          <!-- 分类列表 -->
          <div class="widget categories-widget">
            <h3 class="widget-title">分类</h3>
            <ul class="categories-list">
              <li
                v-for="category in categories"
                :key="category.id"
                class="category-item"
                @click="goToCategory(category.id)"
              >
                <span class="category-name">{{ category.name }}</span>
                <span class="category-count">({{ category.articleCount || 0 }})</span>
              </li>
            </ul>
          </div>

          <!-- 标签云 -->
          <div class="widget tags-widget">
            <h3 class="widget-title">标签</h3>
            <div class="tags-cloud">
              <el-tag
                v-for="tag in tags"
                :key="tag.id"
                size="small"
                class="tag-item"
                @click="goToTag(tag.id)"
              >
                {{ tag.name }}
              </el-tag>
            </div>
          </div>

          <!-- 友情链接 -->
          <div class="widget links-widget">
            <h3 class="widget-title">友情链接</h3>
            <ul class="links-list">
              <li v-for="link in friendLinks" :key="link.id" class="link-item">
                <a :href="link.url" target="_blank" rel="noopener noreferrer">
                  <img
                    v-if="link.logo"
                    :src="link.logo"
                    :alt="link.name"
                    class="link-logo"
                  />
                  <span v-else class="link-icon">{{ link.name?.charAt(0) || 'L' }}</span>
                  {{ link.name }}
                </a>
              </li>
            </ul>
          </div>
        </aside>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import BlogNav from '@/components/BlogNav.vue'
// 安全地导入API函数，添加类型检查
import {
  getArticleList,
  getBlogSettings,
  getHotArticles as getPopularArticles,
  getCategoryList,
  getTagList,
  getFrontFriendLinkList as getFriendLinks
} from '@/api/blog'
import { parseTime } from '@/utils/ruoyi'

// 添加运行时检查，确保所有导入的函数都存在
const apiFunctions = {
  getArticleList,
  getBlogSettings,
  getPopularArticles,
  getCategoryList,
  getTagList,
  getFriendLinks
}

// 检查所有API函数是否可用
Object.keys(apiFunctions).forEach(key => {
  if (typeof apiFunctions[key] !== 'function') {
    console.error(`API函数 ${key} 未定义或不是函数类型`)
  }
})

const router = useRouter()

// 数据
const articles = ref([])
const popularArticles = ref([])
const categories = ref([])
const tags = ref([])
const friendLinks = ref([])
const blogSettings = ref<any>({})
const loading = ref(true)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

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
  loadArticles()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  loadArticles()
}

// 加载文章列表
const loadArticles = async () => {
  try {
    loading.value = true
    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      status: 1 // 只获取已发布的文章
    }
    // 检查API函数是否存在
    if (typeof getArticleList !== 'function') {
      console.error('getArticleList 函数未定义')
      return
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
    if (typeof getPopularArticles !== 'function') {
      console.error('getPopularArticles 函数未定义')
      return
    }
    const response = await getPopularArticles({ pageSize: 5 })
    popularArticles.value = response.rows || []
  } catch (error) {
    console.error('加载热门文章失败:', error)
  }
}

// 加载分类列表
const loadCategories = async () => {
  try {
    const response = await getCategoryList({ pageSize: 100 }) // 获取所有分类
    categories.value = response.rows || []
  } catch (error) {
    console.error('加载分类列表失败:', error)
  }
}

// 加载标签列表
const loadTags = async () => {
  try {
    const response = await getTagList({ pageSize: 100 }) // 获取所有标签
    tags.value = response.rows || []
  } catch (error) {
    console.error('加载标签列表失败:', error)
  }
}

// 加载友情链接
const loadFriendLinks = async () => {
  try {
    const response = await getFriendLinks() // 获取所有友链
    friendLinks.value = response || []
  } catch (error) {
    console.error('加载友情链接失败:', error)
  }
}

// 加载博客设置
const loadBlogSettings = async () => {
  try {
    const response = await getBlogSettings()
    blogSettings.value = response.data || {}
  } catch (error) {
    console.error('加载博客设置失败:', error)
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
  background-color: #f9f9f9;
}

.blog-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 80px 0;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.blog-title {
  font-size: 2.5rem;
  margin-bottom: 10px;
  font-weight: bold;
}

.blog-subtitle {
  font-size: 1.2rem;
  opacity: 0.9;
}

.blog-main {
  max-width: 1200px;
  margin: -40px auto 40px;
  padding: 0 20px;
}

.content-wrapper {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 30px;
}

.articles-section {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.section-header {
  padding: 20px;
  border-bottom: 1px solid #eee;
  background: #fafafa;
}

.section-header h2 {
  margin: 0;
  font-size: 1.5rem;
  color: #333;
}

.articles-list {
  padding: 20px;
}

.article-card {
  display: flex;
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 20px;
  cursor: pointer;
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
}

.article-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.article-image {
  width: 200px;
  height: 140px;
  flex-shrink: 0;
}

.article-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.article-content {
  flex: 1;
  padding: 15px;
}

.article-title {
  margin: 0 0 10px 0;
  font-size: 1.2rem;
  color: #333;
  font-weight: 600;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-summary {
  margin: 0 0 15px 0;
  color: #666;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-meta {
  display: flex;
  gap: 15px;
  font-size: 0.9rem;
  color: #999;
  margin-bottom: 10px;
}

.article-tags {
  display: flex;
  gap: 5px;
  flex-wrap: wrap;
}

.article-tag {
  cursor: pointer;
}

.pagination-wrapper {
  text-align: center;
  margin-top: 20px;
}

.sidebar {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.widget {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.widget-title {
  padding: 15px 20px;
  margin: 0;
  background: #fafafa;
  font-size: 1.1rem;
  color: #333;
  border-bottom: 1px solid #eee;
}

.profile-widget .profile-header {
  padding: 20px;
  text-align: center;
  border-bottom: 1px solid #eee;
}

.profile-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 10px;
}

.profile-name {
  margin: 10px 0 5px 0;
  font-size: 1.2rem;
  color: #333;
}

.profile-desc {
  margin: 0;
  color: #666;
  font-size: 0.9rem;
  line-height: 1.5;
}

.popular-posts-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.popular-post-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 20px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.popular-post-item:last-child {
  border-bottom: none;
}

.popular-post-item:hover {
  background-color: #f5f5f5;
}

.post-rank {
  display: inline-block;
  width: 20px;
  height: 20px;
  text-align: center;
  line-height: 20px;
  border-radius: 50%;
  background-color: #ddd;
  color: #666;
  font-size: 0.8rem;
  margin-right: 10px;
}

.top-3 {
  background: linear-gradient(135deg, #ffd700 0%, #ffa500 100%);
  color: white;
}

.post-title {
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-right: 10px;
}

.post-views {
  font-size: 0.8rem;
  color: #999;
}

.categories-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.category-item {
  display: flex;
  justify-content: space-between;
  padding: 12px 20px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.category-item:last-child {
  border-bottom: none;
}

.category-item:hover {
  background-color: #f5f5f5;
}

.category-name {
  flex: 1;
}

.category-count {
  color: #999;
}

.tags-cloud {
  padding: 15px 20px 20px;
}

.tag-item {
  margin: 5px;
  cursor: pointer;
}

.links-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.link-item {
  padding: 10px 20px;
  border-bottom: 1px solid #eee;
}

.link-item:last-child {
  border-bottom: none;
}

.link-item a {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
  text-decoration: none;
  transition: color 0.3s ease;
}

.link-item a:hover {
  color: #409eff;
}

.link-logo {
  width: 16px;
  height: 16px;
  border-radius: 2px;
}

.link-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  border-radius: 2px;
  background-color: #409eff;
  color: #fff;
  font-size: 10px;
  font-weight: bold;
  margin-right: 4px;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: #999;
}

@media (max-width: 768px) {
  .content-wrapper {
    grid-template-columns: 1fr;
  }

  .blog-header {
    padding: 60px 0;
  }

  .blog-title {
    font-size: 2rem;
  }

  .article-card {
    flex-direction: column;
  }

  .article-image {
    width: 100%;
    height: 180px;
  }
}
</style>
