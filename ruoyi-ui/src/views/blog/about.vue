<template>
  <BlogLayout>
    <div class="about-page">
      <!-- Hero 区域 -->
      <header class="hero-section">
        <div class="hero-wave-bg">
          <svg class="wave-svg" viewBox="0 0 1440 320" preserveAspectRatio="none">
            <path
              fill="rgba(255,255,255,0.08)"
              d="M0,160L48,176C96,192,192,224,288,213.3C384,203,480,149,576,138.7C672,128,768,160,864,181.3C960,203,1056,213,1152,192C1248,171,1344,117,1392,90.7L1440,64L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"
            />
            <path
              fill="rgba(255,255,255,0.05)"
              d="M0,224L48,213.3C96,203,192,181,288,181.3C384,181,480,203,576,224C672,245,768,267,864,261.3C960,256,1056,224,1152,208C1248,192,1344,192,1392,192L1440,192L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"
            />
          </svg>
        </div>
        <div class="hero-inner">
          <div class="hero-avatar-wrap">
            <img :src="blogAvatarUrl" :alt="blogSettings.blog_author" @error="handleAvatarError" />
          </div>
          <h1 class="hero-name">
            {{ blogSettings.blog_author || 'Nevell' }}
          </h1>
          <p class="hero-role">
            {{ blogSettings.author_title || '全栈开发工程师' }}
          </p>
          <p class="hero-desc">
            {{ blogSettings.blog_desc || '热爱技术，热爱生活，专注于Web开发和用户体验设计。' }}
          </p>
        </div>
        <!-- Hero 底部圆角过渡 -->
        <div class="hero-bottom-wave">
          <svg viewBox="0 0 1440 120" preserveAspectRatio="none">
            <path
              fill="#fafbfc"
              d="M0,64L48,69.3C96,75,192,85,288,80C384,75,480,53,576,48C672,43,768,53,864,64C960,75,1056,85,1152,80C1248,75,1344,53,1392,42.7L1440,32L1440,120L1392,120C1344,120,1248,120,1152,120C1056,120,960,120,864,120C768,120,672,120,576,120C480,120,384,120,288,120C192,120,96,120,48,120L0,120Z"
            />
          </svg>
        </div>
      </header>

      <!-- 统计数据 -->
      <section class="stats-section">
        <div class="stats-inner">
          <div v-animate="'fade-in-up'" class="stat-item">
            <div class="stat-num">
              {{ stats.articleCount || 0 }}
            </div>
            <div class="stat-label">篇文章</div>
          </div>
          <div v-animate="'fade-in-up'" class="stat-item">
            <div class="stat-num">
              {{ stats.categoryCount || 0 }}
            </div>
            <div class="stat-label">个分类</div>
          </div>
          <div v-animate="'fade-in-up'" class="stat-item">
            <div class="stat-num">
              {{ stats.tagCount || 0 }}
            </div>
            <div class="stat-label">个标签</div>
          </div>
          <div v-animate="'fade-in-up'" class="stat-item">
            <div class="stat-num">
              {{ stats.commentCount || 0 }}
            </div>
            <div class="stat-label">条评论</div>
          </div>
          <div v-animate="'fade-in-up'" class="stat-item">
            <div class="stat-num">
              {{ formatNumber(stats.totalViews || 0) }}
            </div>
            <div class="stat-label">总访问</div>
          </div>
        </div>
      </section>

      <!-- 关于内容 -->
      <section class="content-section">
        <div class="content-inner">
          <span class="section-label">关于我</span>
          <div class="content-card">
            <div class="about-content" v-html="blogSettings.about_content || '暂无关于内容'"></div>
          </div>
        </div>
      </section>

      <!-- 联系方式 -->
      <section class="contact-section">
        <div class="contact-inner">
          <span class="section-label">联系我</span>
          <div class="contact-grid">
            <div v-if="blogSettings.blog_email" class="contact-card">
              <div class="contact-icon email-icon">
                <svg
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                >
                  <rect x="2" y="4" width="20" height="16" rx="2" />
                  <path d="M22 7l-10 7L2 7" />
                </svg>
              </div>
              <div class="contact-info">
                <div class="contact-type">邮箱</div>
                <a :href="`mailto:${blogSettings.blog_email}`" class="contact-value">
                  {{ blogSettings.blog_email }}
                </a>
              </div>
            </div>
            <div v-if="blogSettings.author_location" class="contact-card">
              <div class="contact-icon location-icon">
                <svg
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                >
                  <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z" />
                  <circle cx="12" cy="10" r="3" />
                </svg>
              </div>
              <div class="contact-info">
                <div class="contact-type">位置</div>
                <div class="contact-value">
                  {{ blogSettings.author_location }}
                </div>
              </div>
            </div>
            <div v-if="blogSettings.github_url" class="contact-card">
              <div class="contact-icon github-icon">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
                  <path
                    d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.127 1.934 2.98 1.395 3.774 1.062.116-.758.441-1.395.799-1.726-2.843-.322-5.833-1.428-5.833-6.342 0-1.395.498-2.527 1.328-3.435-.132-.322-.568-1.625.132-3.387 0 0 1.076-.344 3.527 1.318 1.026-.222 2.092-.333 3.158-.333 1.066 0 2.132.111 3.158.333 2.451-1.662 3.527-1.318 3.527-1.318.7 1.763.264 3.065.132 3.387.83.908 1.328 2.04 1.328 3.435 0 4.922-3.004 6.015-5.843 6.332.46.418.908 1.24.908 2.478v3.677c0 .315.192.693.797.574C20.567 21.803 24 17.303 24 12c0-6.627-5.373-12-12-12z"
                  />
                </svg>
              </div>
              <div class="contact-info">
                <div class="contact-type">GitHub</div>
                <a
                  :href="formatUrl(blogSettings.github_url)"
                  target="_blank"
                  rel="noopener"
                  class="contact-value"
                >
                  {{ blogSettings.github_url }}
                </a>
              </div>
            </div>
            <div v-if="blogSettings.weibo_url" class="contact-card">
              <div class="contact-icon weibo-icon">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
                  <path
                    d="M10.098 20.323c-3.977.391-7.458-1.456-7.562-4.137-.104-2.68 3.194-5.196 7.17-5.588 3.977-.391 7.458 1.456 7.562 4.137.104 2.68-3.194 5.196-7.17 5.588zm7.627-11.364c-.337-.743-1.13-.99-1.773-.554-.644.437-.867 1.386-.529 2.13.337.744 1.13.99 1.773.554.644-.436.867-1.386.53-2.129zm1.162-3.905c-1.126-2.49-3.87-3.389-6.14-2.01-2.27 1.379-3.07 4.322-1.943 6.812 1.126 2.49 3.87 3.39 6.14 2.01 2.27-1.379 3.07-4.322 1.943-6.812z"
                  />
                </svg>
              </div>
              <div class="contact-info">
                <div class="contact-type">微博</div>
                <a
                  :href="formatUrl(blogSettings.weibo_url)"
                  target="_blank"
                  rel="noopener"
                  class="contact-value"
                >
                  {{ blogSettings.weibo_url }}
                </a>
              </div>
            </div>
            <div v-if="blogSettings.personal_website" class="contact-card">
              <div class="contact-icon website-icon">
                <svg
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                >
                  <circle cx="12" cy="12" r="10" />
                  <line x1="2" y1="12" x2="22" y2="12" />
                  <path
                    d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"
                  />
                </svg>
              </div>
              <div class="contact-info">
                <div class="contact-type">个人网站</div>
                <a
                  :href="formatUrl(blogSettings.personal_website)"
                  target="_blank"
                  rel="noopener"
                  class="contact-value"
                >
                  {{ blogSettings.personal_website }}
                </a>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  </BlogLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import BlogLayout from '@/components/BlogLayout.vue'
import { getBlogSettingsAnonymous } from '@/api/blog/setting'
import { getStatisticsOverview } from '@/api/statistics'
import { useBlogSettingsStore } from '@/stores/blogSettings'
import { processAvatarUrl } from '@/api/blog/avatar'

const blogSettingsStore = useBlogSettingsStore()
const blogSettings = computed(() => blogSettingsStore.blogSettings)

interface BlogStats {
  articleCount?: number
  categoryCount?: number
  tagCount?: number
  commentCount?: number
  totalViews?: number
}

const stats = ref<BlogStats>({})

const blogAvatarUrl = computed(() => {
  const avatar = blogSettings.value.blog_avatar
  if (!avatar || !avatar.trim()) return '/default-avatar.svg'
  const processedUrl = processAvatarUrl(avatar)
  return processedUrl || '/default-avatar.svg'
})

const handleAvatarError = (event: Event) => {
  const img = event.target as HTMLImageElement
  if (img.src.includes('default-avatar.svg')) return
  img.src = '/default-avatar.svg'
}

const loadBlogSettings = async () => {
  try {
    const response = await getBlogSettingsAnonymous()
    const settings = response?.data || {}
    blogSettingsStore.updateBlogSettings(settings)
  } catch (error) {
    console.error('加载博客设置失败:', error)
  }
}

const loadStats = async () => {
  try {
    const response = await getStatisticsOverview()
    const data = response?.data || {}
    stats.value = {
      articleCount: data.articleCount || 0,
      categoryCount: data.categoryCount || 0,
      tagCount: data.tagCount || 0,
      commentCount: data.commentCount || 0,
      totalViews: data.totalViews || 0
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
    stats.value = { articleCount: 0, categoryCount: 0, tagCount: 0, commentCount: 0, totalViews: 0 }
  }
}

const formatNumber = (num: number) => {
  if (num >= 10000) return (num / 10000).toFixed(1) + 'W'
  if (num >= 1000) return (num / 1000).toFixed(1) + 'K'
  return num.toString()
}

const formatUrl = (url: string) => {
  if (!url) return ''
  if (typeof url !== 'string') return url
  if (!url.startsWith('http://') && !url.startsWith('https://')) return 'https://' + url
  return url
}

onMounted(() => {
  loadBlogSettings()
  loadStats()
})
</script>

<style scoped>
.about-page {
  font-family:
    -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'PingFang SC', 'Microsoft YaHei',
    sans-serif;
  color: #1a1a2e;
  background: #fafbfc;
  min-height: 100vh;
  padding-top: 0;
}

/* ===== Hero 区域 ===== */
.hero-section {
  position: relative;
  background: linear-gradient(135deg, #4a7bff 0%, #6b8cff 100%);
  padding: 120px 24px 80px;
  text-align: center;
  overflow: hidden;
}

.hero-wave-bg {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 160px;
}

.wave-svg {
  width: 100%;
  height: 100%;
  display: block;
}

.hero-inner {
  position: relative;
  z-index: 2;
  max-width: 640px;
  margin: 0 auto;
}

.hero-avatar-wrap {
  margin-bottom: 24px;
}

.hero-avatar-wrap img {
  width: 140px;
  height: 140px;
  border-radius: 50%;
  border: 4px solid rgba(255, 255, 255, 0.9);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.2);
  object-fit: cover;
  transition: transform 0.3s;
}

.hero-avatar-wrap img:hover {
  transform: scale(1.05);
}

.hero-name {
  font-size: 2.4rem;
  font-weight: 800;
  color: #fff;
  margin-bottom: 8px;
  letter-spacing: -0.5px;
}

.hero-role {
  font-size: 1.1rem;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 16px;
  font-weight: 500;
}

.hero-desc {
  font-size: 0.95rem;
  color: rgba(255, 255, 255, 0.85);
  line-height: 1.7;
  max-width: 480px;
  margin: 0 auto;
}

/* Hero 底部波浪过渡到白色 */
.hero-bottom-wave {
  position: absolute;
  bottom: -1px;
  left: 0;
  right: 0;
  height: 60px;
  z-index: 3;
}

.hero-bottom-wave svg {
  width: 100%;
  height: 100%;
  display: block;
}

/* ===== 统计数据 ===== */
.stats-section {
  padding: 60px 0;
  background: #fafbfc;
}

.stats-inner {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  justify-content: center;
  gap: 32px;
}

.stat-item {
  text-align: center;
  padding: 24px 20px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  min-width: 120px;
  flex: 1;
  transition:
    transform 0.3s,
    box-shadow 0.3s;
}

.stat-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(74, 123, 255, 0.12);
}

.stat-num {
  font-size: 2rem;
  font-weight: 800;
  color: #4a7bff;
  line-height: 1;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 0.85rem;
  color: #7b7b9e;
  font-weight: 600;
  letter-spacing: 1px;
}

/* ===== 公共样式 ===== */
.section-label {
  display: block;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 2px;
  color: #9aa5b8;
  text-transform: uppercase;
  margin-bottom: 24px;
}

/* ===== 关于内容 ===== */
.content-section {
  padding: 48px 0;
  background: #fff;
}

.content-inner {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 24px;
}

.content-card {
  background: #fafbfc;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
  overflow: hidden;
}

.about-content {
  padding: 40px;
  line-height: 1.8;
  color: #1a1a2e;
  font-size: 0.95rem;
}

.about-content :deep(h1),
.about-content :deep(h2),
.about-content :deep(h3) {
  color: #1a1a2e;
  margin-top: 24px;
  margin-bottom: 16px;
  font-weight: 700;
}

.about-content :deep(p) {
  margin-bottom: 16px;
}

.about-content :deep(a) {
  color: #4a7bff;
  text-decoration: none;
}

.about-content :deep(a:hover) {
  text-decoration: underline;
}

/* ===== 联系方式 ===== */
.contact-section {
  padding: 48px 0 80px;
  background: #fafbfc;
}

.contact-inner {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 24px;
}

.contact-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.contact-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px 24px;
  background: #fff;
  border-radius: 14px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  transition:
    transform 0.2s,
    box-shadow 0.2s;
}

.contact-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(74, 123, 255, 0.1);
}

.contact-icon {
  width: 44px;
  height: 44px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
}

.email-icon {
  background: #ea4335;
}
.location-icon {
  background: #4caf50;
}
.github-icon {
  background: #24292e;
}
.weibo-icon {
  background: #e6162d;
}
.website-icon {
  background: #4a7bff;
}

.contact-info {
  flex: 1;
  min-width: 0;
}

.contact-type {
  font-size: 0.8rem;
  color: #7b7b9e;
  font-weight: 600;
  letter-spacing: 0.5px;
  margin-bottom: 4px;
}

.contact-value {
  font-size: 0.95rem;
  color: #1a1a2e;
  font-weight: 600;
  text-decoration: none;
  word-break: break-all;
}

a.contact-value {
  color: #4a7bff;
}

a.contact-value:hover {
  opacity: 0.7;
}

/* ===== 响应式 ===== */
@media (max-width: 900px) {
  .hero-name {
    font-size: 2rem;
  }
  .stats-inner {
    flex-wrap: wrap;
  }
  .stat-item {
    min-width: calc(33% - 16px);
  }
  .contact-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .hero-section {
    padding: 100px 20px 60px;
  }
  .hero-avatar-wrap img {
    width: 110px;
    height: 110px;
  }
  .hero-name {
    font-size: 1.6rem;
  }
  .stats-inner {
    gap: 16px;
  }
  .stat-item {
    min-width: calc(50% - 8px);
    padding: 16px 12px;
  }
  .stat-num {
    font-size: 1.5rem;
  }
  .about-content {
    padding: 24px 16px;
  }
  .contact-card {
    padding: 16px;
  }
}

/* ===== 深色模式 ===== */
html.dark .about-page {
  background: #1e1e2e;
  color: #e2e8f0;
}

html.dark .hero-section {
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 100%);
}

html.dark .hero-bottom-wave svg path {
  fill: #1e1e2e;
}

html.dark .stats-section {
  background: #1e1e2e;
}

html.dark .stat-item {
  background: #252535;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

html.dark .stat-num {
  color: #60a5fa;
}

html.dark .stat-label {
  color: #94a3b8;
}

html.dark .content-section {
  background: #252535;
}

html.dark .content-card {
  background: #1e1e2e;
}

html.dark .about-content {
  color: #e2e8f0;
}

html.dark .about-content :deep(h1),
html.dark .about-content :deep(h2),
html.dark .about-content :deep(h3) {
  color: #e2e8f0;
}

html.dark .about-content :deep(a) {
  color: #60a5fa;
}

html.dark .contact-section {
  background: #1e1e2e;
}

html.dark .contact-card {
  background: #252535;
}

html.dark .contact-type {
  color: #94a3b8;
}

html.dark .contact-value {
  color: #e2e8f0;
}

html.dark a.contact-value {
  color: #60a5fa;
}

html.dark .section-label {
  color: #64748b;
}
</style>
