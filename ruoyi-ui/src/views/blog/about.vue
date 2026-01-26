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
            <el-icon :size="28"><Document /></el-icon>
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
            <el-icon :size="28"><Folder /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-number">
              {{ stats.categoryCount || 0 }}
            </div>
            <div class="stat-label">
              个分类
            </div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">
            <el-icon :size="28"><PriceTag /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-number">
              {{ stats.tagCount || 0 }}
            </div>
            <div class="stat-label">
              个标签
            </div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">
            <el-icon :size="28"><ChatDotRound /></el-icon>
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
            <el-icon :size="28"><View /></el-icon>
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
            <el-icon :size="24"><Message /></el-icon>
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
            <el-icon :size="24"><Location /></el-icon>
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
            <el-icon :size="24"><Position /></el-icon>
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
            <el-icon :size="24"><Star /></el-icon>
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
            <el-icon :size="24"><Connection /></el-icon>
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
import {
  Message,
  Star,
  Connection,
  Document,
  Folder,
  PriceTag,
  ChatDotRound,
  View,
  Location,
  Position
} from '@element-plus/icons-vue'
import { getBlogSettingsAnonymous } from '@/api/blog/setting'
import { getStatisticsOverview } from '@/api/statistics'
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
      categoryCount: data.categoryCount || 0,
      tagCount: data.tagCount || 0,
      commentCount: data.commentCount || 0,
      totalViews: data.totalViews || 0
    }

    console.log('✅ 统计数据加载完成:', stats.value)
  } catch (error) {
    console.error('❌ 加载统计数据失败:', error)
    // 使用默认数据
    stats.value = {
      articleCount: 0,
      categoryCount: 0,
      tagCount: 0,
      commentCount: 0,
      totalViews: 0
    }
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
})
</script>

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
    radial-gradient(circle at 30% 20%, rgba(255, 255, 255, 0.08) 2px, transparent 3px),
    radial-gradient(circle at 70% 60%, rgba(255, 255, 255, 0.06) 2px, transparent 3px),
    radial-gradient(circle at 40% 80%, rgba(255, 255, 255, 0.05) 3px, transparent 4px);
  background-size:
    150px 150px,
    100px 100px,
    200px 200px;
}

.hero-section::after {
  content: '';
  position: absolute;
  bottom: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.03) 0%, transparent 70%);
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
  top: -8px;
  left: -8px;
  right: -8px;
  bottom: -8px;
  border: 2px solid rgba(255, 255, 255, 0.25);
  border-radius: 50%;
  animation: rotate 30s linear infinite;
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



.hero-description {
  font-size: 1.1rem;
  line-height: 1.8;
  margin-bottom: 30px;
  opacity: 0.85;
  max-width: 600px;
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
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #409eff, #337ecc);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  transition: all 0.3s ease;
}

.stat-card:hover .stat-icon {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.25);
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
  height: 3px;
  background: linear-gradient(90deg, #409eff, #337ecc);
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
  width: 48px;
  height: 48px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #409eff, #337ecc);
  border-radius: 12px;
  color: white;
  transition: all 0.3s ease;
}

.contact-item:hover .contact-icon-wrapper {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.25);
}

/* 邮箱特有样式 */
.contact-email .contact-icon-wrapper {
  background: linear-gradient(135deg, #ea4335, #d33426);
}

.contact-email::before {
  background: linear-gradient(180deg, #ea4335, #d33426);
}

/* 位置特有样式 */
.contact-location .contact-icon-wrapper {
  background: linear-gradient(135deg, #4caf50, #388e3c);
}

.contact-location::before {
  background: linear-gradient(180deg, #4caf50, #388e3c);
}

/* GitHub 特有样式 */
.contact-github .contact-icon-wrapper {
  background: linear-gradient(135deg, #333, #24292e);
}

.contact-github::before {
  background: linear-gradient(180deg, #333, #24292e);
}

/* 微博特有样式 */
.contact-weibo .contact-icon-wrapper {
  background: linear-gradient(135deg, #e6162d, #c81227);
}

.contact-weibo::before {
  background: linear-gradient(180deg, #e6162d, #c81227);
}

/* 个人网站特有样式 */
.contact-website .contact-icon-wrapper {
  background: linear-gradient(135deg, #409eff, #337ecc);
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

  .stat-number {
    font-size: 2rem;
  }

  .contact-item {
    padding: 20px 24px;
    gap: 16px;
  }

  .contact-icon-wrapper {
    width: 42px;
    height: 42px;
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
  background: linear-gradient(90deg, #667eea, #764ba2);
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

</style>
