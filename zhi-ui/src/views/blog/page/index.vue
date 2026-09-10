<template>
  <BlogLayout>
    <div class="custom-page mo-custom-page">
      <div class="custom-page-shell">
        <div v-if="loading" v-loading="loading" class="custom-page-panel custom-page-loading"></div>

        <div v-else-if="notFound || !page" class="custom-page-panel custom-page-empty">
          <div class="empty-code">404</div>
          <h1 class="empty-title">页面不存在或未发布</h1>
          <p class="empty-desc">该页面可能已被删除、更名，或者尚未发布。</p>
          <div class="empty-actions">
            <el-button type="primary" @click="$router.push('/blog')">返回首页</el-button>
            <el-button @click="loadPage">重新加载</el-button>
          </div>
        </div>

        <article v-else class="custom-page-panel custom-page-article">
          <header class="custom-page-head">
            <span class="section-label">Page</span>
            <h1 class="custom-page-title">{{ page.title }}</h1>
            <p v-if="page.summary" class="custom-page-summary">{{ page.summary }}</p>
            <div class="custom-page-meta">
              <span v-if="page.updateTime">更新于 {{ formatTime(page.updateTime) }}</span>
              <span>浏览 {{ formatNumber(page.viewCount) }}</span>
            </div>
          </header>

          <div v-if="contentHtml" class="custom-page-content" v-html="contentHtml"></div>
          <p v-else class="custom-page-no-content">该页面暂无内容</p>
        </article>
      </div>
    </div>
  </BlogLayout>
</template>

<script setup lang="ts" name="PublicBlogPage">
import BlogLayout from '@/components/BlogLayout.vue'
import { useBlogSettingsStore } from '@/stores/blogSettings'
import { getPublishedPageBySlug, type BlogPage } from '@/api/blog/page'
import { renderMarkdown } from '@/utils/markdown'
import { sanitizeArticleContent } from '@/utils/sanitize'
import { applySeo, canonicalUrl } from '@/utils/seo'
import { logger } from '@/utils/logger'

const route = useRoute()
const blogSettingsStore = useBlogSettingsStore()
const blogSettings = computed(() => blogSettingsStore.blogSettings)

const loading = ref(true)
const notFound = ref(false)
const page = ref<BlogPage | null>(null)

const slug = computed(() => {
  const raw = route.params.slug
  const value = Array.isArray(raw) ? raw[0] : raw
  return value ? String(value) : ''
})

// Markdown → HTML → 消毒，复用项目现有渲染链路
const contentHtml = computed(() => {
  const raw = page.value?.content
  if (!raw || typeof raw !== 'string' || !raw.trim()) return ''
  return sanitizeArticleContent(renderMarkdown(raw))
})

const formatNumber = (value?: number | null) => {
  const num = Number(value) || 0
  if (num >= 10000) return (num / 10000).toFixed(1) + 'W'
  if (num >= 1000) return (num / 1000).toFixed(1) + 'K'
  return String(num)
}

const formatTime = (time?: string | null) => {
  if (!time) return ''
  const date = new Date(String(time).replace(' ', 'T'))
  if (Number.isNaN(date.getTime())) return String(time)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const loadPage = async () => {
  const currentSlug = slug.value
  if (!currentSlug) {
    page.value = null
    notFound.value = true
    loading.value = false
    return
  }

  loading.value = true
  notFound.value = false
  try {
    const response = await getPublishedPageBySlug(currentSlug)
    if (!response || response.code !== 200 || !response.data) {
      page.value = null
      notFound.value = true
      return
    }

    page.value = response.data
    const siteName = (blogSettings.value as { blog_name?: string }).blog_name || '我的博客'
    applySeo({
      title: page.value.seoTitle || `${page.value.title} - ${siteName}`,
      description: page.value.seoDescription || page.value.summary || '',
      keywords: page.value.seoKeywords || '',
      canonical: canonicalUrl()
    })
  } catch (error) {
    // 后端返回「页面不存在或未发布」时会 reject，这里降级为友好的 404 提示
    logger.error('加载自定义页面失败:', error)
    page.value = null
    notFound.value = true
  } finally {
    loading.value = false
  }
}

watch(slug, () => {
  loadPage()
})

onMounted(loadPage)
</script>

<style scoped>
.mo-custom-page {
  min-height: 100vh;
  padding: 84px 24px 72px;
  background: var(--mo-n50);
  color: var(--mo-n800);
  font-family: var(--mo-font-sans);
}

.custom-page-shell {
  width: min(880px, 100%);
  margin: 0 auto;
}

.custom-page-panel {
  border: 1px solid var(--mo-n200);
  border-radius: var(--mo-r-lg);
  background: #fff;
  box-shadow: var(--mo-shadow-sm);
}

.custom-page-loading {
  min-height: 240px;
}

.custom-page-empty {
  padding: 56px 32px;
  text-align: center;
}

.empty-code {
  color: var(--mo-p200);
  font-family: var(--mo-font-serif);
  font-size: 64px;
  font-weight: 700;
  line-height: 1;
}

.empty-title {
  margin: 16px 0 8px;
  color: var(--mo-n900);
  font-family: var(--mo-font-serif);
  font-size: 22px;
  font-weight: 700;
}

.empty-desc {
  margin: 0 0 22px;
  color: var(--mo-n500);
  font-size: 14px;
}

.empty-actions {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.custom-page-article {
  padding: 32px;
}

.section-label {
  display: inline-flex;
  margin-bottom: 10px;
  padding: 4px 10px;
  border-radius: var(--mo-r-full);
  background: var(--mo-p50);
  color: var(--mo-p700);
  font-size: 12px;
  font-weight: 700;
}

.custom-page-title {
  margin: 0 0 10px;
  color: var(--mo-n900);
  font-family: var(--mo-font-serif);
  font-size: 32px;
  font-weight: 700;
  line-height: 1.2;
}

.custom-page-summary {
  margin: 0;
  color: var(--mo-n600);
  font-size: 15px;
  line-height: 1.8;
}

.custom-page-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid var(--mo-n200);
  color: var(--mo-n500);
  font-size: 13px;
}

.custom-page-content {
  margin-top: 26px;
  color: var(--mo-n700);
  font-family: var(--mo-font-serif);
  font-size: 16px;
  line-height: 1.9;
}

.custom-page-content :deep(h1),
.custom-page-content :deep(h2),
.custom-page-content :deep(h3),
.custom-page-content :deep(h4) {
  margin: 28px 0 14px;
  color: var(--mo-n900);
  font-family: var(--mo-font-serif);
  font-weight: 700;
  line-height: 1.35;
}

.custom-page-content :deep(p) {
  margin: 0 0 16px;
}

.custom-page-content :deep(a) {
  color: var(--mo-p700);
  text-decoration: none;
}

.custom-page-content :deep(a:hover) {
  color: var(--mo-p800);
  text-decoration: underline;
}

.custom-page-content :deep(ul),
.custom-page-content :deep(ol) {
  padding-left: 22px;
}

.custom-page-content :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: var(--mo-r-md);
}

.custom-page-content :deep(pre) {
  overflow-x: auto;
  padding: 14px 16px;
  border-radius: var(--mo-r-md);
  background: var(--mo-n100);
}

.custom-page-content :deep(code) {
  font-family: ui-monospace, SFMono-Regular, Menlo, monospace;
  font-size: 14px;
}

.custom-page-content :deep(blockquote) {
  margin: 0 0 16px;
  padding: 10px 16px;
  border-left: 3px solid var(--mo-p400);
  background: var(--mo-p50);
  color: var(--mo-n600);
}

.custom-page-no-content {
  margin: 26px 0 0;
  color: var(--mo-n500);
  font-size: 14px;
}

html.dark .mo-custom-page {
  background: var(--mo-n900);
  color: var(--mo-n100);
}

html.dark .custom-page-panel {
  border-color: var(--mo-n700);
  background: var(--mo-n800);
}

html.dark .section-label {
  background: rgba(99, 102, 241, 0.16);
  color: var(--mo-p300);
}

html.dark .custom-page-title,
html.dark .empty-title,
html.dark .custom-page-content :deep(h1),
html.dark .custom-page-content :deep(h2),
html.dark .custom-page-content :deep(h3),
html.dark .custom-page-content :deep(h4) {
  color: var(--mo-n50);
}

html.dark .empty-code {
  color: var(--mo-p700);
}

html.dark .custom-page-summary,
html.dark .empty-desc,
html.dark .custom-page-meta,
html.dark .custom-page-no-content {
  color: var(--mo-n400);
}

html.dark .custom-page-meta {
  border-top-color: var(--mo-n700);
}

html.dark .custom-page-content {
  color: var(--mo-n400);
}

html.dark .custom-page-content :deep(pre) {
  background: var(--mo-n900);
}

html.dark .custom-page-content :deep(blockquote) {
  background: rgba(99, 102, 241, 0.12);
  color: var(--mo-n400);
}

@media (max-width: 760px) {
  .mo-custom-page {
    padding: 72px 16px 48px;
  }

  .custom-page-article {
    padding: 22px 20px 24px;
  }

  .custom-page-title {
    font-size: 26px;
  }
}
</style>
