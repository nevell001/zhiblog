<template>
  <div class="search-page">
    <BlogLayout>
      <div class="search-container">
        <div class="search-head">
          <h1 class="search-title">搜索文章</h1>
          <div v-if="isSearchEnabled" class="search-form">
            <input
              v-model="keyword"
              type="text"
              placeholder="输入关键词搜索文章..."
              @keyup.enter="runSearch"
            />
            <button type="button" @click="runSearch">搜索</button>
          </div>
          <p v-else class="search-disabled-tip">搜索功能已关闭</p>
        </div>

        <div v-if="isSearchEnabled" class="search-body">
          <div v-if="loading" v-loading="loading" class="search-loading"></div>

          <template v-else-if="hasSearched">
            <div v-if="results.length > 0" class="search-summary">
              找到 {{ total }} 篇与
              <strong>“{{ query }}”</strong>
              相关的文章
            </div>
            <el-empty v-else-if="query" description="没有找到相关文章，换个关键词试试吧">
              <el-button type="primary" @click="resetSearch">清除搜索</el-button>
            </el-empty>

            <article
              v-for="article in results"
              :key="article.id"
              class="result-card"
              @click="goArticle(article.id)"
            >
              <div class="result-card-title">{{ article.title }}</div>
              <p v-if="article.summary" class="result-card-excerpt">{{ article.summary }}</p>
              <div class="result-card-meta">
                <span class="author">{{ article.authorName || '作者' }}</span>
                <span class="dot">·</span>
                <span>{{ formatDate(article.createTime) }}</span>
                <span class="dot">·</span>
                <span>👁 {{ article.viewCount || 0 }}</span>
                <span class="dot">·</span>
                <span>💬 {{ article.commentCount || 0 }}</span>
              </div>
            </article>

            <div v-if="total > pageSize" class="search-pagination">
              <button :disabled="currentPage <= 1" @click="changePage(currentPage - 1)">
                上一页
              </button>
              <span class="page-info">第 {{ currentPage }} / {{ totalPages }} 页</span>
              <button :disabled="currentPage >= totalPages" @click="changePage(currentPage + 1)">
                下一页
              </button>
            </div>
          </template>
        </div>
      </div>
    </BlogLayout>
  </div>
</template>

<script setup lang="ts" name="BlogSearch">
import { computed, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import BlogLayout from '@/components/BlogLayout.vue'
import { searchArticles } from '@/api/blog/article'
import { useBlogSettingsStore } from '@/stores/blogSettings'
import { parseTime } from '@/utils/zhi'

const route = useRoute()
const router = useRouter()
const blogSettingsStore = useBlogSettingsStore()

const blogSettings = computed(() => blogSettingsStore.blogSettings)
const isSearchEnabled = computed(() => {
  const v = (blogSettings.value as any).search_enabled
  return v === undefined || v === null || v === 'true' || v === true
})

const keyword = ref('')
const query = ref('')
const results = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const loading = ref(false)
const hasSearched = ref(false)

const totalPages = computed(() => Math.max(1, Math.ceil(total.value / pageSize.value)))

const formatDate = (date: string) => {
  return parseTime(date, '{y}-{m}-{d}')
}

const doSearch = async (page = 1) => {
  const kw = keyword.value.trim() || String(route.query.q || '').trim()
  if (!kw) {
    hasSearched.value = true
    results.value = []
    total.value = 0
    return
  }
  // 同步 URL，便于分享与刷新
  router.replace({ path: '/blog/search', query: kw ? { q: kw } : {} })

  loading.value = true
  try {
    const params = { pageNum: page, pageSize: pageSize.value }
    const response = await searchArticles(kw, params)
    results.value = response.rows || []
    total.value = response.total || 0
    query.value = kw
    keyword.value = kw
    currentPage.value = page
    hasSearched.value = true
  } catch {
    // 静默失败，展示空状态
    results.value = []
    total.value = 0
    hasSearched.value = true
  } finally {
    loading.value = false
  }
}

const changePage = (page: number) => {
  if (page < 1) return
  doSearch(page)
}

// 供模板点击/回车调用的无参入口
const runSearch = () => {
  doSearch(1)
}

const resetSearch = () => {
  keyword.value = ''
  results.value = []
  total.value = 0
  query.value = ''
  hasSearched.value = false
  router.replace('/blog/search')
}

const goArticle = (id: number) => {
  if (id) router.push(`/blog/article/${id}`)
}

// 从 URL 初始化或关键词变化时重新搜索
const initialKeyword = String(route.query.q || '').trim()
if (initialKeyword) {
  keyword.value = initialKeyword
  doSearch(1)
}

watch(
  () => route.query.q,
  q => {
    const next = String(q || '').trim()
    if (next && next !== query.value) {
      keyword.value = next
      doSearch(1)
    }
  }
)
</script>

<style scoped>
.search-page {
  min-height: 100vh;
}
.search-container {
  max-width: 860px;
  margin: 0 auto;
  padding: 96px 20px 48px;
}
.search-head {
  margin-bottom: 24px;
}
.search-title {
  margin: 0 0 14px;
  font-size: 26px;
  font-weight: 700;
  color: var(--el-text-color-primary, #303133);
}
.search-form {
  display: flex;
  gap: 10px;
  max-width: 560px;
}
.search-form input {
  flex: 1;
  padding: 10px 14px;
  border: 1px solid var(--el-border-color, #dcdfe6);
  border-radius: 8px;
  font-size: 14px;
  outline: none;
  background: var(--el-bg-color-overlay, #fff);
  color: var(--el-text-color-primary, #303133);
}
.search-form input:focus {
  border-color: var(--el-color-primary, #409eff);
}
.search-form button {
  padding: 0 20px;
  border: none;
  border-radius: 8px;
  background: var(--el-color-primary, #409eff);
  color: #fff;
  font-size: 14px;
  cursor: pointer;
}
.search-disabled-tip {
  color: var(--el-text-color-secondary, #909399);
  font-size: 14px;
}
.search-loading {
  min-height: 160px;
}
.search-summary {
  margin-bottom: 14px;
  font-size: 14px;
  color: var(--el-text-color-secondary, #606266);
}
.result-card {
  padding: 16px 18px;
  margin-bottom: 12px;
  background: var(--el-bg-color-overlay, #fff);
  border: 1px solid var(--el-border-color-lighter, #ebeef5);
  border-radius: 10px;
  cursor: pointer;
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
}
.result-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(15, 23, 42, 0.08);
}
.result-card-title {
  font-size: 17px;
  font-weight: 600;
  color: var(--el-text-color-primary, #303133);
}
.result-card:hover .result-card-title {
  color: var(--el-color-primary, #409eff);
}
.result-card-excerpt {
  margin: 8px 0 10px;
  font-size: 13px;
  line-height: 1.6;
  color: var(--el-text-color-regular, #606266);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.result-card-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  font-size: 12px;
  color: var(--el-text-color-secondary, #909399);
}
.result-card-meta .author {
  color: var(--el-color-primary, #409eff);
}
.dot {
  color: var(--el-border-color, #dcdfe6);
}
.search-pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 14px;
  margin-top: 22px;
}
.search-pagination button {
  padding: 6px 16px;
  border: 1px solid var(--el-border-color, #dcdfe6);
  border-radius: 8px;
  background: var(--el-bg-color-overlay, #fff);
  color: var(--el-text-color-primary, #303133);
  cursor: pointer;
}
.search-pagination button:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}
.page-info {
  font-size: 13px;
  color: var(--el-text-color-secondary, #909399);
}
</style>
