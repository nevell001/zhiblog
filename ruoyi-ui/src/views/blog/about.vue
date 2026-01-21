<template>
  <div class="about-container">
    <!-- 博客导航 -->
    <BlogNav />

    <!-- 博主介绍头部 -->
    <div class="hero-section">
      <div class="hero-content">
        <div
          v-animate="'fade-in-up'"
          class="hero-avatar"
        >
          <img
            :src="blogAvatarUrl"
            :alt="blogSettings.blog_author"
            @error="handleAvatarError"
          />
          <div class="avatar-decoration"></div>
        </div>
        <div
          v-animate="'fade-in-up'"
          class="hero-info"
        >
          <h1 class="hero-title">
            {{ blogSettings.blog_author || 'Nevell' }}
          </h1>
          <p class="hero-subtitle">
            {{ blogSettings.author_title || '全栈开发工程师' }}
          </p>
          <p class="hero-description">
            {{
              blogSettings.blog_desc ||
                '热爱技术，热爱生活，专注于Web开发和用户体验设计，分享技术心得与生活感悟。'
            }}
          </p>

          <!-- 社交链接 -->
          <div
            v-animate="'fade-in-up'"
            class="hero-social"
          >
            <a
              v-if="blogSettings.github_url"
              :href="blogSettings.github_url"
              class="social-link social-github"
              title="GitHub"
              target="_blank"
              rel="noopener"
            >
              <el-icon><Promotion /></el-icon>
              <span class="social-tooltip">GitHub</span>
            </a>
            <a
              v-if="blogSettings.blog_email"
              :href="`mailto:${blogSettings.blog_email}`"
              class="social-link social-email"
              title="邮箱"
            >
              <el-icon><Message /></el-icon>
              <span class="social-tooltip">邮箱</span>
            </a>
            <a
              v-if="blogSettings.weibo_url"
              :href="blogSettings.weibo_url"
              class="social-link social-weibo"
              title="微博"
              target="_blank"
              rel="noopener"
            >
              <el-icon><Star /></el-icon>
              <span class="social-tooltip">微博</span>
            </a>
            <a
              v-if="blogSettings.personal_website"
              :href="formatUrl(blogSettings.personal_website)"
              class="social-link social-website"
              title="个人网站"
              target="_blank"
              rel="noopener"
            >
              <el-icon><Connection /></el-icon>
              <span class="social-tooltip">个人网站</span>
            </a>
          </div>
        </div>
      </div>
    </div>

    <!-- 统计数据 -->
    <div
      v-animate="'fade-in-up'"
      class="stats-section"
    >
      <div class="stats-container">
        <div class="stat-card">
          <div class="stat-icon">
            <i class="el-icon-document"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">
              {{ stats.articleCount || 0 }}
            </div>
            <div class="stat-label">
              篇文章
            </div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">
            <i class="el-icon-folder-opened"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">
              {{ categories.length }}
            </div>
            <div class="stat-label">
              个分类
            </div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">
            <i class="el-icon-price-tag"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">
              {{ tags.length }}
            </div>
            <div class="stat-label">
              个标签
            </div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">
            <i class="el-icon-chat-dot-round"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">
              {{ stats.commentCount || 0 }}
            </div>
            <div class="stat-label">
              条评论
            </div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">
            <i class="el-icon-view"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">
              {{ formatNumber(stats.totalViews || 0) }}
            </div>
            <div class="stat-label">
              总访问
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 关于内容 -->
    <div
      v-animate="'fade-in-up'"
      class="about-content-section"
    >
      <h2 class="section-title">
        关于我
      </h2>
      <div class="about-content-container">
        <div
          class="about-content"
          v-html="blogSettings.about_content || '暂无关于内容'"
        ></div>
      </div>
    </div>

    <!-- 联系方式 -->
    <div
      v-animate="'fade-in-up'"
      class="contact-section"
    >
      <h2 class="section-title">
        联系我
      </h2>
      <div class="contact-container">
        <div class="contact-item contact-email">
          <div class="contact-icon-wrapper">
            <div class="contact-icon">
              <svg viewBox="0 0 24 24" fill="currentColor">
                <path d="M20 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z"/>
              </svg>
            </div>
            <div class="contact-icon-bg"></div>
          </div>
          <div class="contact-details">
            <div class="contact-label">
              邮箱
            </div>
            <div class="contact-value">
              <a
                v-if="blogSettings.blog_email"
                :href="`mailto:${blogSettings.blog_email}`"
                class="contact-link"
              >
                {{ blogSettings.blog_email }}
              </a>
              <span v-else>暂无邮箱</span>
            </div>
          </div>
        </div>
        <div class="contact-item contact-location">
          <div class="contact-icon-wrapper">
            <div class="contact-icon">
              <svg viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/>
              </svg>
            </div>
            <div class="contact-icon-bg"></div>
          </div>
          <div class="contact-details">
            <div class="contact-label">
              位置
            </div>
            <div class="contact-value">
              {{ blogSettings.author_location || '暂无位置信息' }}
            </div>
          </div>
        </div>
        <div class="contact-item contact-github">
          <div class="contact-icon-wrapper">
            <div class="contact-icon">
              <svg viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0024 12c0-6.63-5.37-12-12-12z"/>
              </svg>
            </div>
            <div class="contact-icon-bg"></div>
          </div>
          <div class="contact-details">
            <div class="contact-label">
              GitHub
            </div>
            <div class="contact-value">
              <a
                v-if="blogSettings.github_url"
                :href="formatUrl(blogSettings.github_url)"
                target="_blank"
                rel="noopener"
                class="contact-link"
              >
                {{ blogSettings.github_url }}
              </a>
              <span v-else>暂无 GitHub</span>
            </div>
          </div>
        </div>
        <div class="contact-item contact-weibo">
          <div class="contact-icon-wrapper">
            <div class="contact-icon">
              <svg viewBox="0 0 24 24" fill="currentColor">
                <path d="M12.03 2C6.55 2 2.11 6.44 2.11 11.92c0 5.48 4.44 9.92 9.92 9.92 5.48 0 9.92-4.44 9.92-9.92S17.51 2 12.03 2zm0 17.83c-4.35 0-7.91-3.56-7.91-7.91 0-4.35 3.56-7.91 7.91-7.91 4.35 0 7.91 3.56 7.91 7.91 0 4.35-3.56 7.91-7.91 7.91zm5.23-9.12c-.45-.23-1.01-.08-1.24.37-.23.45-.08 1.01.37 1.24.45.23 1.01.08 1.24-.37.23-.45.08-1.01-.37-1.24zm-2.35 2.35c-.45-.23-1.01-.08-1.24.37-.23.45-.08 1.01.37 1.24.45.23 1.01.08 1.24-.37.23-.45.08-1.01-.37-1.24zm-2.35 2.35c-.45-.23-1.01-.08-1.24.37-.23.45-.08 1.01.37 1.24.45.23 1.01.08 1.24-.37.23-.45.08-1.01-.37-1.24z"/>
              </svg>
            </div>
            <div class="contact-icon-bg"></div>
          </div>
          <div class="contact-details">
            <div class="contact-label">
              微博
            </div>
            <div class="contact-value">
              <a
                v-if="blogSettings.weibo_url"
                :href="formatUrl(blogSettings.weibo_url)"
                target="_blank"
                rel="noopener"
                class="contact-link"
              >
                {{ blogSettings.weibo_url }}
              </a>
              <span v-else>暂无微博</span>
            </div>
          </div>
        </div>
        <div class="contact-item contact-website">
          <div class="contact-icon-wrapper">
            <div class="contact-icon">
              <svg viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 17.93c-3.95-.49-7-3.85-7-7.93 0-.62.08-1.21.21-1.79L9 15v1c0 1.1.9 2 2 2v1.93zm6.9-2.54c-.26-.81-1-1.39-1.9-1.39h-1v-3c0-.55-.45-1-1-1H8v-2h2c.55 0 1-.45 1-1V7h2c1.1 0 2-.9 2-2v-.41c2.93 1.19 5 4.06 5 7.41 0 2.08-.8 3.97-2.1 5.39z"/>
              </svg>
            </div>
            <div class="contact-icon-bg"></div>
          </div>
          <div class="contact-details">
            <div class="contact-label">
              个人网站
            </div>
            <div class="contact-value">
              <a
                v-if="blogSettings.personal_website"
                :href="formatUrl(blogSettings.personal_website)"
                target="_blank"
                rel="noopener"
                class="contact-link"
              >
                {{ blogSettings.personal_website }}
              </a>
              <span v-else>暂无个人网站</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import BlogNav from '@/components/BlogNav.vue'
import { Promotion, Message, Star, Connection } from '@element-plus/icons-vue'
import { getBlogSettingsAnonymous } from '@/api/blog/setting'
import { getStatisticsOverview } from '@/api/statistics'
import { getCategoryList } from '@/api/blog/category'
import { getTagList } from '@/api/blog/tag'
import { useUserStore } from '@/stores/user'
import { useBlogSettingsStore } from '@/stores/blogSettings'
import { processAvatarUrl } from '@/api/blog/avatar'

const userStore = useUserStore()
const blogSettingsStore = useBlogSettingsStore()

// 响应式数据
interface BlogSettings {
  blog_email?: string
  weibo_url?: string
  github_url?: string
  wechat_qr?: string
  about_content?: string
  author_location?: string
  personal_website?: string
  [key: string]: any
}

const blogSettings = computed(() => blogSettingsStore.blogSettings)
const stats = ref({})
const categories = ref([])
const tags = ref([])

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

// 加载博客设置
const loadBlogSettings = async () => {
  try {
    console.log('🔄 开始加载博客设置...')
    const response = await getBlogSettingsAnonymous()
    // 正确提取data字段
    const settings = response?.data || {}
    
    // 更新 blogSettingsStore
    blogSettingsStore.updateBlogSettings(settings)
    
    console.log('📦 博客设置加载完成:', {
      github_url: settings.github_url,
      weibo_url: settings.weibo_url,
      author_location: settings.author_location,
      personal_website: settings.personal_website,
      blog_email: settings.blog_email
    })
    
    console.log('🔍 联系信息字段值:', {
      'github_url 是否存在': !!settings.github_url,
      'github_url 值': settings.github_url,
      'weibo_url 是否存在': !!settings.weibo_url,
      'weibo_url 值': settings.weibo_url,
      'author_location 是否存在': !!settings.author_location,
      'author_location 值': settings.author_location,
      'personal_website 是否存在': !!settings.personal_website,
      'personal_website 值': settings.personal_website,
      'blog_email 是否存在': !!settings.blog_email,
      'blog_email 值': settings.blog_email
    })
  } catch (error) {
    console.error('❌ 加载博客设置失败:', error)
    // 使用默认值
    console.log('📦 使用默认博客设置')
  }
}

// 加载统计数据
const loadStats = async () => {
  try {
    console.log('🔄 开始加载统计数据...')
    const response = await getStatisticsOverview()
    console.log('📦 统计数据API响应:', response)

    // 正确提取data字段
    const data = response?.data || {}

    stats.value = {
      articleCount: data.articleCount || 0,
      commentCount: data.commentCount || 0,
      totalViews: data.totalViews || 0
    }

    console.log('✅ 统计数据加载完成:', stats.value)
  } catch (error) {
    console.error('❌ 加载统计数据失败:', error)
    // 使用默认数据
    stats.value = {
      articleCount: 0,
      commentCount: 0,
      totalViews: 0
    }
  }
}

// 加载分类列表
const loadCategories = async () => {
  try {
    const response = await getCategoryList({ pageSize: 100 })
    categories.value = response.data || []
    console.log('✅ 分类列表加载完成，数量:', categories.value.length)
  } catch (error) {
    console.error('❌ 加载分类列表失败:', error)
    categories.value = []
  }
}

// 加载标签列表
const loadTags = async () => {
  try {
    const response = await getTagList({ pageSize: 100 })
    tags.value = response.data || []
    console.log('✅ 标签列表加载完成，数量:', tags.value.length)
  } catch (error) {
    console.error('❌ 加载标签列表失败:', error)
    tags.value = []
  }
}

// 格式化数字
const formatNumber = num => {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + 'W'
  } else if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'K'
  }
  return num.toString()
}

// 格式化URL，确保有协议前缀
const formatUrl = url => {
  if (!url) return ''
  if (typeof url !== 'string') return url
  // 如果没有协议前缀，添加 https://
  if (!url.startsWith('http://') && !url.startsWith('https://')) {
    return 'https://' + url
  }
  return url
}

onMounted(() => {
  loadBlogSettings()
  loadStats()
  loadCategories()
  loadTags()
})</script>

<style scoped>
.about-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  position: relative;
}

/* 博主介绍头部 */
.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 120px 0 80px;
  position: relative;
  overflow: hidden;
}

.hero-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background:
    radial-gradient(circle at 30% 20%, rgba(255, 255, 255, 0.1) 3px, transparent 4px),
    radial-gradient(circle at 70% 60%, rgba(255, 255, 255, 0.08) 2px, transparent 3px),
    radial-gradient(circle at 40% 80%, rgba(255, 255, 255, 0.06) 4px, transparent 5px);
  background-size:
    150px 150px,
    100px 100px,
    200px 200px;
  animation: floatStars 25s linear infinite;
}

.hero-section::after {
  content: '';
  position: absolute;
  bottom: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.05) 0%, transparent 70%);
  animation: pulse 8s ease-in-out infinite;
}

@keyframes floatStars {
  0% {
    transform: translate(0, 0);
  }
  100% {
    transform: translate(-50px, -50px);
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    opacity: 0.5;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.8;
  }
}

.hero-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  gap: 60px;
  position: relative;
  z-index: 1;
}

.hero-avatar {
  position: relative;
  flex-shrink: 0;
}

.hero-avatar img {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  border: 6px solid rgba(255, 255, 255, 0.9);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
  position: relative;
  z-index: 2;
}

.hero-avatar:hover img {
  transform: scale(1.05);
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.3);
}

.avatar-decoration {
  position: absolute;
  top: -10px;
  left: -10px;
  right: -10px;
  bottom: -10px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  animation: rotate 20s linear infinite;
}

.avatar-decoration::before {
  content: '';
  position: absolute;
  top: -5px;
  left: -5px;
  right: -5px;
  bottom: -5px;
  border: 2px dashed rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  animation: rotate 30s linear infinite reverse;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.hero-info {
  flex: 1;
}

.hero-title {
  font-size: 3.5rem;
  font-weight: 700;
  margin-bottom: 15px;
  background: linear-gradient(45deg, #fff, #f0f8ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  position: relative;
  display: inline-block;
}

.hero-title::after {
  content: '';
  position: absolute;
  bottom: -5px;
  left: 0;
  width: 100%;
  height: 3px;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.5), transparent);
  border-radius: 2px;
}

.hero-subtitle {
  font-size: 1.5rem;
  margin-bottom: 20px;
  opacity: 0.9;
  font-weight: 500;
  position: relative;
  display: inline-block;
}

.hero-subtitle::before {
  content: '✨';
  margin-right: 8px;
  animation: sparkle 2s ease-in-out infinite;
}

@keyframes sparkle {
  0%, 100% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.5;
    transform: scale(1.2);
  }
}

.hero-description {
  font-size: 1.1rem;
  line-height: 1.8;
  margin-bottom: 30px;
  opacity: 0.85;
  max-width: 600px;
}

.hero-social {
  display: flex;
  gap: 15px;
}

.social-link {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 56px;
  height: 56px;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 16px;
  color: white;
  font-size: 1.3rem;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
}

.social-link svg {
  width: 26px;
  height: 26px;
  position: relative;
  z-index: 1;
}

.social-link:hover {
  transform: translateY(-8px) scale(1.1);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.25);
}

/* GitHub 特有样式 */
.social-github {
  background: rgba(31, 31, 31, 0.15);
  border-color: rgba(31, 31, 31, 0.3);
}

.social-github:hover {
  background: linear-gradient(135deg, #24292e, #1a1e22);
  box-shadow: 0 15px 35px rgba(36, 41, 46, 0.4);
}

/* 邮箱特有样式 */
.social-email {
  background: rgba(234, 67, 53, 0.15);
  border-color: rgba(234, 67, 53, 0.3);
}

.social-email:hover {
  background: linear-gradient(135deg, #ea4335, #d93025);
  box-shadow: 0 15px 35px rgba(234, 67, 53, 0.4);
}

/* 微博特有样式 */
.social-weibo {
  background: rgba(230, 22, 45, 0.15);
  border-color: rgba(230, 22, 45, 0.3);
}

.social-weibo:hover {
  background: linear-gradient(135deg, #e6162d, #d61228);
  box-shadow: 0 15px 35px rgba(230, 22, 45, 0.4);
}

/* 个人网站特有样式 */
.social-website {
  background: rgba(64, 158, 255, 0.15);
  border-color: rgba(64, 158, 255, 0.3);
}

.social-website:hover {
  background: linear-gradient(135deg, #409eff, #3a8ee6);
  box-shadow: 0 15px 35px rgba(64, 158, 255, 0.4);
}

/* Tooltip */
.social-tooltip {
  position: absolute;
  bottom: -30px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 0.75rem;
  white-space: nowrap;
  opacity: 0;
  pointer-events: none;
  transition: all 0.3s ease;
  font-weight: 500;
  z-index: 10;
}

.social-link:hover .social-tooltip {
  opacity: 1;
  bottom: -35px;
}

/* 光效动画 */
.social-link::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.4), transparent);
  transition: left 0.6s ease;
  z-index: 2;
}

.social-link:hover::before {
  left: 100%;
}

/* 统计数据 */
.stats-section {
  padding: 80px 20px;
  background: white;
}

.stats-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  gap: 30px;
}

.stat-card {
  flex: 1;
  background: white;
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  gap: 20px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(0, 0, 0, 0.05);
  backdrop-filter: blur(10px);
  position: relative;
  overflow: hidden;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, #409eff, #337ecc);
  transform: scaleX(0);
  transition: transform 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.stat-card:hover::before {
  transform: scaleX(1);
}

.stat-icon {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #409eff, #337ecc);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  color: white;
  transition: all 0.3s ease;
}

.stat-card:hover .stat-icon {
  transform: scale(1.1) rotate(5deg);
  box-shadow: 0 8px 20px rgba(64, 158, 255, 0.3);
}

.stat-number {
  font-size: 2.5rem;
  font-weight: 700;
  color: #333;
  line-height: 1;
  margin-bottom: 5px;
  position: relative;
  display: inline-block;
}

.stat-number::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, #409eff, #337ecc);
  transition: width 0.3s ease;
}

.stat-card:hover .stat-number::after {
  width: 100%;
}

.stat-label {
  font-size: 0.95rem;
  color: #666;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* 关于内容 */
.about-content-section {
  padding: 80px 20px;
  background: white;
}

.section-title {
  text-align: center;
  font-size: 2.5rem;
  color: #333;
  margin-bottom: 50px;
  position: relative;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 4px;
  background: linear-gradient(90deg, #409eff, #337ecc);
  border-radius: 2px;
}

.about-content-container {
  max-width: 900px;
  margin: 0 auto;
}

.about-content {
  background: #f8f9fa;
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
  line-height: 1.8;
  color: #333;
  font-size: 1rem;
  position: relative;
  overflow: hidden;
}

.about-content::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, #409eff, #337ecc, #409eff);
  background-size: 200% 100%;
  animation: gradientMove 3s linear infinite;
}

@keyframes gradientMove {
  0% {
    background-position: 0% 50%;
  }
  100% {
    background-position: 200% 50%;
  }
}

.about-content :deep(h1),
.about-content :deep(h2),
.about-content :deep(h3) {
  color: #333;
  margin-top: 24px;
  margin-bottom: 16px;
}

.about-content :deep(p) {
  margin-bottom: 16px;
}

.about-content :deep(a) {
  color: #409eff;
  text-decoration: none;
}

.about-content :deep(a:hover) {
  text-decoration: underline;
}

/* 联系方式 */
.contact-section {
  padding: 80px 20px 100px;
  background: linear-gradient(180deg, #f8f9fa 0%, #e9ecef 100%);
  position: relative;
}

.contact-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(64, 158, 255, 0.3), transparent);
}

.contact-container {
  max-width: 900px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 28px 32px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(0, 0, 0, 0.05);
  position: relative;
  overflow: hidden;
}

.contact-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: linear-gradient(180deg, #409eff, #337ecc);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.contact-item:hover {
  transform: translateY(-8px) translateX(4px);
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.15);
  border-color: rgba(64, 158, 255, 0.2);
}

.contact-item:hover::before {
  opacity: 1;
}

/* 图标包装器 */
.contact-icon-wrapper {
  position: relative;
  width: 70px;
  height: 70px;
  flex-shrink: 0;
}

.contact-icon {
  position: relative;
  z-index: 2;
  width: 70px;
  height: 70px;
  border-radius: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  color: white;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.contact-icon svg {
  width: 32px;
  height: 32px;
}

.contact-icon-bg {
  position: absolute;
  top: 4px;
  left: 4px;
  width: 70px;
  height: 70px;
  border-radius: 18px;
  opacity: 0.3;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 1;
}

.contact-item:hover .contact-icon {
  transform: scale(1.1) rotate(5deg);
  box-shadow: 0 10px 30px rgba(64, 158, 255, 0.4);
}

.contact-item:hover .contact-icon-bg {
  transform: scale(1.15) rotate(-5deg);
  opacity: 0.2;
}

/* 邮箱特有样式 */
.contact-email .contact-icon,
.contact-email .contact-icon-bg {
  background: linear-gradient(135deg, #ea4335, #d33426);
}

.contact-email:hover .contact-icon {
  transform: rotate(10deg) scale(1.1);
  box-shadow: 0 10px 30px rgba(234, 67, 53, 0.4);
}

.contact-email::before {
  background: linear-gradient(180deg, #ea4335, #d33426);
}

/* 位置特有样式 */
.contact-location .contact-icon,
.contact-location .contact-icon-bg {
  background: linear-gradient(135deg, #4CAF50, #388E3C);
}

.contact-location:hover .contact-icon {
  transform: rotate(-10deg) scale(1.1);
  box-shadow: 0 10px 30px rgba(76, 175, 80, 0.4);
}

.contact-location::before {
  background: linear-gradient(180deg, #4CAF50, #388E3C);
}

/* GitHub 特有样式 */
.contact-github .contact-icon,
.contact-github .contact-icon-bg {
  background: linear-gradient(135deg, #333, #24292e);
}

.contact-github:hover .contact-icon {
  transform: scale(1.1);
  box-shadow: 0 10px 30px rgba(51, 51, 51, 0.4);
}

.contact-github::before {
  background: linear-gradient(180deg, #333, #24292e);
}

/* 微博特有样式 */
.contact-weibo .contact-icon,
.contact-weibo .contact-icon-bg {
  background: linear-gradient(135deg, #e6162d, #c81227);
}

.contact-weibo:hover .contact-icon {
  transform: rotate(10deg) scale(1.1);
  box-shadow: 0 10px 30px rgba(230, 22, 45, 0.4);
}

.contact-weibo::before {
  background: linear-gradient(180deg, #e6162d, #c81227);
}

/* 个人网站特有样式 */
.contact-website .contact-icon,
.contact-website .contact-icon-bg {
  background: linear-gradient(135deg, #409eff, #337ecc);
}

.contact-website:hover .contact-icon {
  transform: rotate(-10deg) scale(1.1);
  box-shadow: 0 10px 30px rgba(64, 158, 255, 0.4);
}

.contact-website::before {
  background: linear-gradient(180deg, #409eff, #337ecc);
}

.contact-label {
  font-size: 0.85rem;
  color: #666;
  margin-bottom: 6px;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.contact-value {
  font-size: 1.15rem;
  font-weight: 600;
  color: #333;
  line-height: 1.4;
}

.contact-link {
  color: #409eff;
  text-decoration: none;
  transition: all 0.3s ease;
  display: inline-block;
  position: relative;
}

.contact-link::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, #409eff, #337ecc);
  transition: width 0.3s ease;
}

.contact-link:hover {
  color: #337ecc;
}

.contact-link:hover::after {
  width: 100%;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .hero-content {
    flex-direction: column;
    text-align: center;
    gap: 40px;
  }

  .hero-title {
    font-size: 2.8rem;
  }

  .hero-title::after {
    left: 50%;
    transform: translateX(-50%);
  }

  .hero-description {
    margin-left: auto;
    margin-right: auto;
  }

  .stats-container {
    flex-wrap: wrap;
  }

  .stat-card {
    min-width: calc(50% - 15px);
  }

  .about-content {
    padding: 30px 20px;
  }
}

@media (max-width: 768px) {
  .hero-section {
    padding: 80px 0 60px;
  }

  .hero-avatar img {
    width: 150px;
    height: 150px;
  }

  .hero-title {
    font-size: 2.2rem;
  }

  .hero-subtitle {
    font-size: 1.2rem;
  }

  .hero-description {
    font-size: 1rem;
  }

  .stats-container {
    flex-direction: column;
  }

  .stat-card {
    min-width: 100%;
  }

  .section-title {
    font-size: 2rem;
  }

  .about-content {
    padding: 25px 15px;
    font-size: 0.95rem;
  }

  .contact-item {
    padding: 20px;
  }
}

@media (max-width: 480px) {
  .hero-title {
    font-size: 1.8rem;
  }

  .hero-description {
    font-size: 1rem;
  }

  .social-link {
    width: 48px;
    height: 48px;
    font-size: 1.1rem;
  }

  .social-link svg {
    width: 22px;
    height: 22px;
  }

  .social-tooltip {
    display: none;
  }

  .stat-number {
    font-size: 2rem;
  }

  .contact-item {
    padding: 20px 24px;
    gap: 16px;
  }

  .contact-icon-wrapper {
    width: 56px;
    height: 56px;
  }

  .contact-icon {
    width: 56px;
    height: 56px;
  }

  .contact-icon-bg {
    width: 56px;
    height: 56px;
  }

  .contact-icon svg {
    width: 26px;
    height: 26px;
  }

  .contact-label {
    font-size: 0.8rem;
  }

  .contact-value {
    font-size: 1rem;
  }
}

/* 深色主题适配 */
html.dark .about-container {
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
}

html.dark .hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

html.dark .stats-section {
  background: #2a2a3e;
}

html.dark .stat-card {
  background: #2a2a3e;
  border-color: #333;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

html.dark .stat-card:hover {
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

html.dark .stat-card::before {
  background: linear-gradient(90deg, #667eea, #764ba2);
}

html.dark .stat-icon {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

html.dark .stat-card:hover .stat-icon {
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
}

html.dark .stat-number {
  color: #e0e0e0;
}

html.dark .stat-number::after {
  background: linear-gradient(90deg, #667eea, #764ba2);
}

html.dark .stat-label {
  color: #b0b0b0;
}

html.dark .about-content-section {
  background: #2a2a3e;
}

html.dark .section-title {
  color: #e0e0e0;
}

html.dark .section-title::after {
  background: linear-gradient(90deg, #667eea, #764ba2);
}

html.dark .about-content {
  background: #1e1e2e;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
  color: #e0e0e0;
}

html.dark .about-content::before {
  background: linear-gradient(90deg, #667eea, #764ba2, #667eea);
  background-size: 200% 100%;
}

html.dark .about-content :deep(h1),
html.dark .about-content :deep(h2),
html.dark .about-content :deep(h3) {
  color: #e0e0e0;
}

html.dark .about-content :deep(a) {
  color: #667eea;
}

html.dark .about-content :deep(a:hover) {
  color: #9f7aea;
}

html.dark .contact-section {
  background: linear-gradient(180deg, #1a1a2e 0%, #16213e 100%);
}

html.dark .contact-section::before {
  background: linear-gradient(90deg, transparent, rgba(102, 126, 234, 0.3), transparent);
}

html.dark .contact-item {
  background: #2a2a3e;
  border-color: #333;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.2);
}

html.dark .contact-item:hover {
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.3);
  border-color: rgba(102, 126, 234, 0.2);
}

html.dark .contact-label {
  color: #b0b0b0;
}

html.dark .contact-value {
  color: #e0e0e0;
}

html.dark .contact-link {
  color: #667eea;
}

html.dark .contact-link:hover {
  color: #9f7aea;
}

html.dark .contact-link::after {
  background: linear-gradient(90deg, #667eea, #9f7aea);
}

/* 深色主题社交链接 */
html.dark .social-github {
  background: rgba(31, 31, 31, 0.25);
  border-color: rgba(31, 31, 31, 0.4);
}

html.dark .social-github:hover {
  background: linear-gradient(135deg, #24292e, #1a1e22);
  box-shadow: 0 15px 35px rgba(36, 41, 46, 0.5);
}

html.dark .social-email {
  background: rgba(234, 67, 53, 0.25);
  border-color: rgba(234, 67, 53, 0.4);
}

html.dark .social-email:hover {
  background: linear-gradient(135deg, #ea4335, #d93025);
  box-shadow: 0 15px 35px rgba(234, 67, 53, 0.5);
}

html.dark .social-weibo {
  background: rgba(230, 22, 45, 0.25);
  border-color: rgba(230, 22, 45, 0.4);
}

html.dark .social-weibo:hover {
  background: linear-gradient(135deg, #e6162d, #d61228);
  box-shadow: 0 15px 35px rgba(230, 22, 45, 0.5);
}

html.dark .social-website {
  background: rgba(64, 158, 255, 0.25);
  border-color: rgba(64, 158, 255, 0.4);
}

html.dark .social-website:hover {
  background: linear-gradient(135deg, #409eff, #3a8ee6);
  box-shadow: 0 15px 35px rgba(64, 158, 255, 0.5);
}
</style>
