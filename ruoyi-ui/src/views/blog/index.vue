<template>
  <BlogLayout ref="blogLayoutRef">
    <div class="blog-home-v2 mo-home-page">
      <header class="home-hero">
        <h1>记录与分享</h1>
        <p>
          {{
            blogSettings.blog_desc ||
            '在这里，每一篇文章都是一次思考的沉淀。无论技术随笔、生活感悟还是行业观察，都值得被认真对待。'
          }}
        </p>
        <div class="search-box">
          <input
            v-model="searchKeyword"
            type="text"
            placeholder="搜索文章、作者、标签..."
            @keyup.enter="handleSearch"
          />
          <button type="button" @click="handleSearch">搜索</button>
        </div>
      </header>

      <main class="home-layout">
        <section>
          <div v-if="isSearching" class="search-result-note">
            搜索结果：{{ searchKeyword }}（共 {{ total }} 篇）
            <button type="button" @click="clearSearch">返回全部文章</button>
          </div>

          <article
            v-for="article in articles"
            :key="article.id"
            class="article-card"
            @click="goToArticle(article.id)"
          >
            <div
              class="thumb"
              :style="{
                background: article.coverUrl
                  ? `url(${article.coverUrl}) center/cover`
                  : fallbackGradient(article.id)
              }"
            ></div>
            <div class="body">
              <div v-if="article.tags?.length" class="tags">
                <span
                  v-for="(tag, index) in article.tags.slice(0, 2)"
                  :key="tag.id"
                  class="tag"
                  :class="tagClass(Number(index))"
                >
                  {{ tag.name }}
                </span>
              </div>
              <h2 class="title">{{ article.title }}</h2>
              <p class="excerpt">
                {{ article.summary || '这篇文章暂时没有摘要，点击进入阅读全文。' }}
              </p>
              <div class="meta">
                <span class="author">
                  <span class="avatar-sm">{{ (article.authorName || '作').charAt(0) }}</span>
                  {{ article.authorName || '作者' }}
                </span>
                <span class="dot-sep">·</span>
                <span>{{ formatDate(article.createTime) }}</span>
                <span class="dot-sep">·</span>
                <span>👁 {{ article.viewCount || 0 }}</span>
                <span class="dot-sep">·</span>
                <span>💬 {{ article.commentCount || 0 }}</span>
              </div>
            </div>
          </article>

          <div v-if="total > 0" class="pagination">
            <button
              class="pg"
              :disabled="currentPage <= 1"
              @click="handleCurrentChange(currentPage - 1)"
            >
              ‹
            </button>
            <span class="pg active">{{ currentPage }}</span>
            <button
              class="pg"
              :disabled="currentPage * pageSize >= total"
              @click="handleCurrentChange(currentPage + 1)"
            >
              ›
            </button>
          </div>
        </section>

        <aside>
          <div class="sidebar-widget">
            <div class="widget-title">分类导航</div>
            <div class="cat-list">
              <router-link to="/blog" class="cat-item active">
                全部文章
                <span class="count">{{ total }}</span>
              </router-link>
              <router-link
                v-for="category in categories"
                :key="category.id"
                :to="`/blog/category/${category.id}`"
                class="cat-item"
              >
                {{ category.name }}
                <span class="count">{{ category.articleCount || category.count || 0 }}</span>
              </router-link>
            </div>
          </div>

          <div class="sidebar-widget">
            <div class="widget-title">热门标签</div>
            <div class="tag-cloud">
              <router-link
                v-for="(tag, index) in tags"
                :key="tag.id || tag.name"
                :to="`/blog/tag/${tag.id}`"
                class="tc"
                :class="{ lg: index % 5 === 0, md: index % 3 === 0 }"
              >
                {{ tag.name }}
              </router-link>
            </div>
          </div>

          <div class="sidebar-widget">
            <div class="widget-title">热门文章</div>
            <div
              v-for="(article, index) in popularArticles.slice(0, 5)"
              :key="article.id"
              class="hot-item"
              @click="goToArticle(article.id)"
            >
              <span class="rank" :class="{ top: index < 3 }">{{ index + 1 }}</span>
              <div>
                <div class="htitle">{{ article.title }}</div>
                <div class="hmeta">
                  👁 {{ article.viewCount || 0 }} · 💬 {{ article.commentCount || 0 }}
                </div>
              </div>
            </div>
          </div>
        </aside>
      </main>

      <!-- 联系弹窗 -->
      <div v-if="showContact" class="contact-modal" @click.self="showContact = false">
        <div class="contact-card">
          <button class="contact-close" @click="showContact = false">&times;</button>
          <h3>联系我们</h3>
          <p>邮箱：{{ blogSettings.blog_email || 'hello@example.com' }}</p>
        </div>
      </div>
    </div>
  </BlogLayout>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import BlogLayout from '@/components/BlogLayout.vue'
import { getArticleList, getBlogSettings, getHotArticles, searchArticles } from '@/api/blog'
import { getCategoryList } from '@/api/blog/category'
import { getTagCloud } from '@/api/blog/tag'
import { useBlogSettingsStore } from '@/stores/blogSettings'
import { parseTime } from '@/utils/ruoyi'

const router = useRouter()
const blogSettingsStore = useBlogSettingsStore()

const articles = ref([])
const popularArticles = ref([])
const categories = ref([])
const tags = ref([])
const blogSettings = computed(() => blogSettingsStore.blogSettings)
const loading = ref(true)
const currentPage = ref(1)
const pageSize = ref(5)
const total = ref(0)
const searchKeyword = ref('')
const isSearching = ref(false)
const showContact = ref(false)
const blogLayoutRef = ref<any>(null)

const formatDate = (date: string) => {
  return parseTime(date, '{y}-{m}-{d}')
}

const tagClass = (index: number) => {
  return index === 0 ? 'tag-blue' : 'tag-gray'
}

const fallbackGradient = (id: number) => {
  return Math.abs(Number(id) || 0) % 2 === 0
    ? 'linear-gradient(135deg,#eef2ff,#fafaf9)'
    : 'linear-gradient(135deg,#f5f5f4,#e0e7ff)'
}

const goToArticle = (id: number) => {
  if (id) router.push(`/blog/article/${id}`)
}

const scrollToAllArticles = () => {
  document.getElementById('all-articles')?.scrollIntoView({ behavior: 'smooth' })
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  if (isSearching.value) {
    handleSearch()
  } else {
    loadArticles()
  }
}

const handleSearch = async () => {
  if (!searchKeyword.value.trim()) {
    clearSearch()
    return
  }
  try {
    loading.value = true
    isSearching.value = true
    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize.value
    }
    const response = await searchArticles(searchKeyword.value.trim(), params)
    articles.value = response.rows || []
    total.value = response.total || 0
  } catch (error) {
    console.error('搜索失败:', error)
  } finally {
    loading.value = false
  }
}

const clearSearch = async () => {
  searchKeyword.value = ''
  isSearching.value = false
  currentPage.value = 1
  await loadArticles()
}

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
    console.error('加载文章失败:', error)
  } finally {
    loading.value = false
  }
}

const loadPopularArticles = async () => {
  try {
    const response = await getHotArticles({ pageSize: 6 })
    popularArticles.value = response.rows || []
  } catch (error) {
    console.error('加载热门文章失败:', error)
  }
}

const loadSidebarData = async () => {
  try {
    const [categoryResponse, tagResponse] = await Promise.all([
      getCategoryList({ pageNum: 1, pageSize: 8 }),
      getTagCloud()
    ])
    categories.value = categoryResponse.rows || categoryResponse.data || []
    const tagResult = tagResponse as any
    tags.value = Array.isArray(tagResult) ? tagResult : tagResult.data || []
  } catch (error) {
    console.error('加载侧栏数据失败:', error)
    categories.value = []
    tags.value = []
  }
}

const loadBlogSettings = async () => {
  try {
    const response = await getBlogSettings()
    const settings = response.data || {}
    blogSettingsStore.updateBlogSettings(settings)
  } catch (error) {
    console.error('加载设置失败:', error)
  }
}

onMounted(async () => {
  await Promise.all([loadArticles(), loadPopularArticles(), loadBlogSettings(), loadSidebarData()])
})
</script>

<style scoped>
.mo-home-page {
  --p50: #eef2ff;
  --p100: #e0e7ff;
  --p300: #a5b4fc;
  --p500: #6366f1;
  --p600: #4f46e5;
  --p700: #4338ca;
  --p800: #3730a3;
  --n50: #fafaf9;
  --n100: #f5f5f4;
  --n200: #e7e5e4;
  --n300: #d6d3d1;
  --n400: #a8a29e;
  --n500: #78716c;
  --n600: #57534e;
  --n800: #292524;
  --n900: #1c1917;
  --r-md: 8px;
  --r-lg: 12px;
  --r-full: 9999px;
  --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.07), 0 2px 4px -2px rgba(0, 0, 0, 0.05);
  min-height: 100vh;
  padding-top: 60px;
  background: var(--n50);
  color: var(--n800);
  font-family: Inter, 'PingFang SC', 'Microsoft YaHei', system-ui, sans-serif;
}

.mo-home-page .home-hero {
  padding: 56px 32px;
  background: linear-gradient(135deg, var(--p600), var(--p800));
  color: #fff;
  text-align: center;
}

.mo-home-page .home-hero h1 {
  margin: 0 0 10px;
  color: #fff;
  font-size: 32px;
  font-weight: 700;
  letter-spacing: 0;
}

.mo-home-page .home-hero p {
  max-width: 520px;
  margin: 0 auto;
  opacity: 0.85;
  font-size: 15px;
  line-height: 1.7;
}

.mo-home-page .home-hero .search-box {
  display: flex;
  align-items: center;
  max-width: 440px;
  width: 100%;
  margin: 24px auto 0;
  padding: 4px;
  border: none;
  border-radius: var(--r-full);
  background: #fff;
}

.mo-home-page .home-hero .search-box input {
  flex: 1;
  min-width: 0;
  padding: 10px 18px;
  border: none;
  outline: none;
  background: transparent;
  color: var(--n800);
  font-size: 14px;
}

.mo-home-page .home-hero .search-box button {
  padding: 9px 24px;
  border: none;
  border-radius: var(--r-full);
  background: var(--p600);
  color: #fff;
  font-size: 13px;
  font-weight: 500;
}

.mo-home-page .home-layout {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 300px;
  gap: 28px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 32px;
}

.mo-home-page .article-card {
  margin-bottom: 18px;
  overflow: hidden;
  border: 1px solid var(--n200);
  border-radius: var(--r-lg);
  background: #fff;
  cursor: pointer;
  transition: all 0.2s;
}

.mo-home-page .article-card:hover {
  border-color: var(--p300);
  box-shadow: var(--shadow-md);
  transform: translateY(-1px);
}

.mo-home-page .article-card .thumb {
  height: 180px;
  background-position: center;
  background-size: cover;
}

.mo-home-page .article-card .body {
  padding: 22px;
}

.mo-home-page .article-card .tags {
  display: flex;
  gap: 6px;
  margin-bottom: 10px;
}

.mo-home-page .article-card .title {
  margin: 0 0 8px;
  color: var(--n900);
  font-size: 18px;
  font-weight: 600;
  line-height: 1.4;
}

.mo-home-page .article-card .excerpt {
  display: -webkit-box;
  margin: 0 0 14px;
  overflow: hidden;
  color: var(--n500);
  font-size: 14px;
  line-height: 1.7;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
}

.mo-home-page .article-card .meta {
  display: flex;
  align-items: center;
  gap: 12px;
  color: var(--n400);
  font-size: 12px;
}

.mo-home-page .article-card .meta .author {
  display: flex;
  align-items: center;
  gap: 6px;
  color: var(--n600);
  font-weight: 500;
}

.mo-home-page .avatar-sm {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  background: var(--p100);
  color: var(--p600);
  font-size: 10px;
  font-weight: 600;
}

.mo-home-page .dot-sep {
  color: var(--n300);
}

.mo-home-page .sidebar-widget {
  margin-bottom: 18px;
  padding: 20px;
  border: 1px solid var(--n200);
  border-radius: var(--r-lg);
  background: #fff;
}

.mo-home-page .widget-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 14px;
  color: var(--n800);
  font-size: 14px;
  font-weight: 600;
}

.mo-home-page .widget-title::before {
  content: '';
  width: 3px;
  height: 16px;
  border-radius: 2px;
  background: var(--p500);
}

.mo-home-page .cat-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.mo-home-page .cat-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  border-radius: var(--r-md);
  color: var(--n600);
  font-size: 13px;
  cursor: pointer;
  text-decoration: none;
}

.mo-home-page .cat-item:hover,
.mo-home-page .cat-item.active {
  background: var(--p50);
  color: var(--p700);
  font-weight: 500;
}

.mo-home-page .cat-item .count {
  padding: 2px 8px;
  border-radius: var(--r-full);
  background: var(--n100);
  color: var(--n400);
  font-size: 11px;
}

.mo-home-page .tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.mo-home-page .tc {
  padding: 4px 12px;
  border-radius: var(--r-full);
  background: var(--n100);
  color: var(--n600);
  font-size: 12px;
  text-decoration: none;
}

.mo-home-page .tc:hover,
.mo-home-page .tc.lg {
  background: var(--p50);
  color: var(--p700);
}

.mo-home-page .tc.md {
  background: var(--n100);
  color: var(--n600);
}

.mo-home-page .hot-item {
  display: flex;
  gap: 10px;
  align-items: flex-start;
  margin-bottom: 12px;
  cursor: pointer;
}

.mo-home-page .rank {
  width: 24px;
  flex-shrink: 0;
  color: var(--p300);
  font-size: 18px;
  font-weight: 700;
  text-align: center;
}

.mo-home-page .rank.top {
  color: var(--p600);
}

.mo-home-page .htitle {
  color: var(--n800);
  font-size: 13px;
  font-weight: 500;
  line-height: 1.4;
}

.mo-home-page .hot-item:hover .htitle {
  color: var(--p600);
}

.mo-home-page .hmeta {
  margin-top: 2px;
  color: var(--n400);
  font-size: 11px;
}

.mo-home-page .pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 28px 0;
}

.mo-home-page .pg {
  min-width: 34px;
  height: 34px;
  padding: 0 10px;
  border: 1px solid var(--n200);
  border-radius: var(--r-md);
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  color: var(--n600);
  font-size: 13px;
}

.mo-home-page .pg.active {
  border-color: var(--p600);
  background: var(--p600);
  color: #fff;
}

.mo-home-page .tag {
  display: inline-flex;
  align-items: center;
  padding: 3px 12px;
  border-radius: var(--r-full);
  font-size: 12px;
  font-weight: 500;
}

.mo-home-page .tag-blue {
  background: var(--p50);
  color: var(--p700);
}

.mo-home-page .tag-gray {
  background: var(--n100);
  color: var(--n600);
}

.mo-home-page .search-result-note {
  margin-bottom: 16px;
  color: var(--n600);
  font-size: 13px;
}

.mo-home-page .search-result-note button {
  margin-left: 12px;
  color: var(--p600);
}

@media (max-width: 768px) {
  .mo-home-page .home-hero {
    padding: 32px 16px;
  }

  .mo-home-page .home-hero h1 {
    font-size: 24px;
  }

  .mo-home-page .home-layout {
    grid-template-columns: 1fr;
    gap: 16px;
    padding: 16px;
  }
}

.contact-modal {
  position: fixed;
  inset: 0;
  z-index: 200;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background: rgba(28, 25, 23, 0.42);
  backdrop-filter: blur(4px);
}

.contact-card {
  position: relative;
  width: min(400px, 100%);
  padding: 32px;
  border: 1px solid var(--n200);
  border-radius: var(--r-lg);
  background: #fff;
  box-shadow: var(--shadow-md);
}

.contact-card h3 {
  margin: 0 0 14px;
  color: var(--n900);
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 0;
}

.contact-card p {
  margin: 0;
  color: var(--n500);
  font-size: 14px;
  line-height: 1.7;
}

.contact-close {
  position: absolute;
  top: 14px;
  right: 16px;
  border: none;
  background: transparent;
  color: var(--n400);
  cursor: pointer;
  font-size: 24px;
  line-height: 1;
}

.contact-close:hover {
  color: var(--p600);
}
</style>
