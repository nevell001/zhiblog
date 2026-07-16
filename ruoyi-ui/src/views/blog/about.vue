<template>
  <BlogLayout>
    <div class="about-page">
      <!-- 个人简介 -->
      <header class="about-intro">
        <div class="about-intro-inner">
          <div class="about-avatar-wrap">
            <img :src="blogAvatarUrl" :alt="blogSettings.blog_author" @error="handleAvatarError" />
          </div>
          <div class="about-intro-copy">
            <span class="section-label">About</span>
            <h1 class="about-name">
              {{ blogSettings.blog_author || 'Nevell' }}
            </h1>
            <p class="about-role">
              {{ blogSettings.author_title || '全栈开发工程师' }}
            </p>
            <p class="about-desc">
              {{ blogSettings.blog_desc || '热爱技术，热爱生活，专注于Web开发和用户体验设计。' }}
            </p>
          </div>
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
  color: #1c1917;
  background: #fafaf9;
  min-height: 100vh;
  padding-top: 0;
}

/* ===== 个人简介 ===== */
.about-intro {
  padding: 96px 24px 56px;
  background: #fafaf9;
}

.about-intro-inner {
  display: grid;
  grid-template-columns: auto minmax(0, 1fr);
  align-items: center;
  gap: 32px;
  max-width: 900px;
  margin: 0 auto;
  padding: 32px;
  background: #fff;
  border: 1px solid #e7e5e4;
  border-radius: 8px;
  box-shadow: 0 14px 40px rgba(28, 25, 23, 0.06);
}

.about-avatar-wrap img {
  width: 140px;
  height: 140px;
  border-radius: 50%;
  border: 4px solid #eef2ff;
  box-shadow: 0 12px 32px rgba(79, 70, 229, 0.12);
  object-fit: cover;
  transition: transform 0.3s;
}

.about-avatar-wrap img:hover {
  transform: scale(1.05);
}

.about-intro-copy {
  min-width: 0;
}

.about-name {
  font-size: 2.4rem;
  font-weight: 800;
  color: #1c1917;
  margin-bottom: 8px;
}

.about-role {
  font-size: 1.1rem;
  color: #4f46e5;
  margin-bottom: 16px;
  font-weight: 500;
}

.about-desc {
  font-size: 0.95rem;
  color: #57534e;
  line-height: 1.7;
  max-width: 480px;
  margin: 0;
}

/* ===== 统计数据 ===== */
.stats-section {
  padding: 60px 0;
  background: #fafaf9;
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
  box-shadow: 0 12px 32px rgba(79, 70, 229, 0.12);
}

.stat-num {
  font-size: 2rem;
  font-weight: 800;
  color: #4f46e5;
  line-height: 1;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 0.85rem;
  color: #78716c;
  font-weight: 600;
  letter-spacing: 1px;
}

/* ===== 公共样式 ===== */
.section-label {
  display: block;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 2px;
  color: #a8a29e;
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
  background: #fafaf9;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
  overflow: hidden;
}

.about-content {
  padding: 40px;
  line-height: 1.8;
  color: #1c1917;
  font-size: 0.95rem;
}

.about-content :deep(h1),
.about-content :deep(h2),
.about-content :deep(h3) {
  color: #1c1917;
  margin-top: 24px;
  margin-bottom: 16px;
  font-weight: 700;
}

.about-content :deep(p) {
  margin-bottom: 16px;
}

.about-content :deep(a) {
  color: #4f46e5;
  text-decoration: none;
}

.about-content :deep(a:hover) {
  text-decoration: underline;
}

/* ===== 联系方式 ===== */
.contact-section {
  padding: 48px 0 80px;
  background: #fafaf9;
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
  box-shadow: 0 8px 24px rgba(79, 70, 229, 0.1);
}

.contact-icon {
  width: 44px;
  height: 44px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #eef2ff;
  color: #4f46e5;
  flex-shrink: 0;
}

.email-icon {
  background: #eef2ff;
}
.location-icon {
  background: #eef2ff;
}
.github-icon {
  background: #eef2ff;
}
.weibo-icon {
  background: #eef2ff;
}
.website-icon {
  background: #eef2ff;
}

.contact-info {
  flex: 1;
  min-width: 0;
}

.contact-type {
  font-size: 0.8rem;
  color: #78716c;
  font-weight: 600;
  letter-spacing: 0.5px;
  margin-bottom: 4px;
}

.contact-value {
  font-size: 0.95rem;
  color: #1c1917;
  font-weight: 600;
  text-decoration: none;
  word-break: break-all;
}

a.contact-value {
  color: #4f46e5;
}

a.contact-value:hover {
  opacity: 0.7;
}

/* ===== 响应式 ===== */
@media (max-width: 900px) {
  .about-name {
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
  .about-intro {
    padding: 80px 16px 40px;
  }

  .about-intro-inner {
    grid-template-columns: 1fr;
    gap: 20px;
    padding: 24px 20px;
    text-align: center;
  }

  .about-avatar-wrap img {
    width: 110px;
    height: 110px;
  }

  .about-name {
    font-size: 1.6rem;
  }

  .about-desc {
    margin: 0 auto;
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
  background: #1c1917;
  color: #e2e8f0;
}

html.dark .about-intro {
  background: #1c1917;
}

html.dark .about-intro-inner {
  background: #292524;
  border-color: #44403c;
  box-shadow: 0 14px 40px rgba(0, 0, 0, 0.2);
}

html.dark .about-avatar-wrap img {
  border-color: #3730a3;
}

html.dark .about-name {
  color: #f5f5f4;
}

html.dark .about-role {
  color: #a5b4fc;
}

html.dark .about-desc {
  color: #d6d3d1;
}

html.dark .stats-section {
  background: #1c1917;
}

html.dark .stat-item {
  background: #292524;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

html.dark .stat-num {
  color: #a5b4fc;
}

html.dark .stat-label {
  color: #94a3b8;
}

html.dark .content-section {
  background: #292524;
}

html.dark .content-card {
  background: #1c1917;
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
  color: #a5b4fc;
}

html.dark .contact-section {
  background: #1c1917;
}

html.dark .contact-card {
  background: #292524;
}

html.dark .contact-type {
  color: #94a3b8;
}

html.dark .contact-value {
  color: #e2e8f0;
}

html.dark a.contact-value {
  color: #a5b4fc;
}

html.dark .section-label {
  color: #78716c;
}
</style>
