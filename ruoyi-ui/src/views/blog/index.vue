<template>
  <BlogLayout ref="blogLayoutRef">
    <div class="blog-home-v2">
      <!-- Hero 区域 -->
      <header class="hero-section">
        <div class="hero-wave-bg">
          <svg class="wave-svg" viewBox="0 0 1440 320" preserveAspectRatio="none">
            <path
              fill="rgba(255,255,255,0.08)"
              d="M0,160L48,176C96,192,192,224,288,213.3C384,203,480,149,576,138.7C672,128,768,160,864,181.3C960,203,1056,213,1152,192C1248,171,1344,117,1392,90.7L1440,64L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"
            ></path>
            <path
              fill="rgba(255,255,255,0.05)"
              d="M0,224L48,213.3C96,203,192,181,288,181.3C384,181,480,203,576,224C672,245,768,267,864,261.3C960,256,1056,224,1152,208C1248,192,1344,192,1392,192L1440,192L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"
            ></path>
          </svg>
        </div>
        <div class="hero-content">
          <h1 class="hero-title">{{ blogSettings.blog_name || '我的博客' }}</h1>
          <p class="hero-subtitle">
            {{ blogSettings.blog_desc || '发现故事、思考与专业知识，涵盖任何你感兴趣的话题' }}
          </p>
          <div class="hero-search">
            <svg
              class="search-icon"
              width="18"
              height="18"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
            >
              <circle cx="11" cy="11" r="8" />
              <line x1="21" y1="21" x2="16.65" y2="16.65" />
            </svg>
            <input
              v-model="searchKeyword"
              type="text"
              placeholder="搜索文章..."
              @keyup.enter="handleSearch"
            />
            <button
              class="search-clear-btn"
              v-if="searchKeyword"
              @click="clearSearch"
              title="清除搜索"
            >
              <svg
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
              >
                <line x1="18" y1="6" x2="6" y2="18" />
                <line x1="6" y1="6" x2="18" y2="18" />
              </svg>
            </button>
          </div>
        </div>
      </header>

      <!-- POPULAR 热门文章（搜索时隐藏） -->
      <section class="popular-section" v-if="popularArticles.length > 0 && !isSearching">
        <div class="section-inner">
          <span class="section-label">热门推荐</span>
          <div class="popular-featured">
            <div class="popular-text">
              <h2 class="popular-title">{{ popularArticles[0]?.title || '精选文章' }}</h2>
              <p class="popular-desc">
                {{ popularArticles[0]?.summary || '一段引人入胜的故事，呈现现代博客的核心精髓' }}
              </p>
              <div class="popular-tags">
                <span class="tag" v-if="popularArticles[0]?.tags?.length">
                  {{ popularArticles[0].tags[0].name }}
                </span>
                <span class="tag" v-if="popularArticles[0]?.tags?.length > 1">
                  {{ popularArticles[0].tags[1].name }}
                </span>
              </div>
            </div>
            <div class="popular-image" @click="goToArticle(popularArticles[0]?.id)">
              <img
                :src="popularArticles[0]?.coverUrl || '/default-cover.jpg'"
                alt="Featured"
                loading="lazy"
              />
            </div>
          </div>

          <!-- 3张小卡片 -->
          <div class="popular-cards">
            <div
              class="popular-card"
              v-for="(article, idx) in popularCards"
              :key="article.id"
              @click="goToArticle(article.id)"
            >
              <div class="card-image">
                <img :src="article.coverUrl || '/default-cover.jpg'" alt="" loading="lazy" />
              </div>
              <h3 class="card-title">{{ article.title }}</h3>
              <p class="card-desc">{{ article.summary }}</p>
              <div class="card-tags">
                <span class="tag" v-if="article.tags?.length">{{ article.tags[0].name }}</span>
              </div>
            </div>
          </div>

          <div class="section-footer-link" v-if="popularArticles.length > 4">
            <a href="#" @click.prevent="scrollToAllArticles">查看更多热门文章</a>
          </div>
        </div>
      </section>

      <!-- ALL ARTICLE 全部文章 / 搜索结果 -->
      <section class="all-articles-section" id="all-articles">
        <div class="section-inner">
          <span class="section-label" v-if="isSearching">
            搜索结果：{{ searchKeyword }}（共 {{ total }} 篇）
            <button class="clear-search-link" @click="clearSearch">返回全部文章</button>
          </span>
          <span class="section-label" v-else>全部文章</span>
          <div class="articles-list-v2">
            <div
              class="article-row"
              v-for="article in articles"
              :key="article.id"
              @click="goToArticle(article.id)"
            >
              <div class="row-image">
                <img :src="article.coverUrl || '/default-cover.jpg'" alt="" loading="lazy" />
              </div>
              <div class="row-body">
                <h3 class="row-title">{{ article.title }}</h3>
                <p class="row-desc">{{ article.summary }}</p>
                <div class="row-tags" v-if="article.tags?.length">
                  <span class="tag" v-for="tag in article.tags.slice(0, 2)" :key="tag.id">
                    {{ tag.name }}
                  </span>
                </div>
                <div class="row-meta">
                  <span class="author">{{ article.authorName || '作者' }}</span>
                  <span class="date">{{ formatDate(article.createTime) }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 分页 -->
          <div class="pagination-v2" v-if="total > 0">
            <button
              class="page-btn"
              :disabled="currentPage <= 1"
              @click="handleCurrentChange(currentPage - 1)"
            >
              <svg
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
              >
                <polyline points="15 18 9 12 15 6" />
              </svg>
              较新文章
            </button>
            <button
              class="page-btn"
              :disabled="currentPage * pageSize >= total"
              @click="handleCurrentChange(currentPage + 1)"
            >
              较早文章
              <svg
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
              >
                <polyline points="9 18 15 12 9 6" />
              </svg>
            </button>
          </div>
        </div>
      </section>

      <!-- CTA 区域 -->
      <section class="cta-section">
        <div class="cta-inner">
          <div class="cta-content">
            <h2 class="cta-title">
              想要与我们的
              <br />
              团队合作吗？
            </h2>
            <p class="cta-desc">
              无论你是想交流技术、分享见解，还是寻求合作机会，我们都期待与你建立联系。
            </p>
            <button class="cta-btn" @click="showContact = true">联系我们</button>
          </div>
        </div>
        <div class="cta-wave-bottom">
          <svg viewBox="0 0 1440 120" preserveAspectRatio="none">
            <path
              fill="#4a7bff"
              d="M0,64L48,69.3C96,75,192,85,288,80C384,75,480,53,576,48C672,43,768,53,864,64C960,75,1056,85,1152,80C1248,75,1344,53,1392,42.7L1440,32L1440,120L1392,120C1344,120,1248,120,1152,120C1056,120,960,120,864,120C768,120,672,120,576,120C480,120,384,120,288,120C192,120,96,120,48,120L0,120Z"
            ></path>
          </svg>
        </div>
      </section>

      <!-- 联系弹窗 -->
      <div class="contact-modal" v-if="showContact" @click.self="showContact = false">
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
import { useBlogSettingsStore } from '@/stores/blogSettings'
import { parseTime } from '@/utils/ruoyi'

const router = useRouter()
const blogSettingsStore = useBlogSettingsStore()

const articles = ref([])
const popularArticles = ref([])
const blogSettings = computed(() => blogSettingsStore.blogSettings)
const loading = ref(true)
const currentPage = ref(1)
const pageSize = ref(5)
const total = ref(0)
const searchKeyword = ref('')
const isSearching = ref(false)
const showContact = ref(false)
const blogLayoutRef = ref(null)

const popularCards = computed(() => popularArticles.value.slice(1, 4))

const formatDate = (date: string) => {
  return parseTime(date, '{y}-{m}-{d}')
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
  await Promise.all([loadArticles(), loadPopularArticles(), loadBlogSettings()])
})
</script>

<style scoped>
.blog-home-v2 {
  font-family:
    -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'PingFang SC', 'Microsoft YaHei',
    sans-serif;
  color: #1a1a2e;
  background: #fff;
  min-height: 100vh;
  padding-top: 64px;
}

/* Hero 区域 */
.hero-section {
  position: relative;
  background: linear-gradient(135deg, #4a7bff 0%, #6b8cff 100%);
  padding: 140px 24px 120px;
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
.hero-content {
  position: relative;
  z-index: 2;
  max-width: 640px;
  margin: 0 auto;
}
.hero-title {
  font-size: 2.8rem;
  font-weight: 800;
  color: #fff;
  margin-bottom: 16px;
  letter-spacing: -1px;
}
.hero-subtitle {
  font-size: 1rem;
  color: rgba(255, 255, 255, 0.85);
  margin-bottom: 32px;
  line-height: 1.6;
}
.hero-search {
  max-width: 480px;
  margin: 0 auto;
  position: relative;
}
.hero-search input {
  width: 100%;
  padding: 14px 16px 14px 44px;
  border-radius: 12px;
  border: none;
  background: rgba(255, 255, 255, 0.95);
  font-size: 15px;
  color: #1a1a2e;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.1);
  outline: none;
  transition:
    box-shadow 0.3s,
    background 0.3s;
}
.hero-search input:focus {
  box-shadow: 0 6px 32px rgba(0, 0, 0, 0.15);
  background: #fff;
}
.hero-search input::placeholder {
  color: #9aa5b8;
}
.search-icon {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  color: #9aa5b8;
  pointer-events: none;
}
.search-clear-btn {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #9aa5b8;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  transition: color 0.2s;
}
.search-clear-btn:hover {
  color: #4a7bff;
}

/* 公共 inner */
.section-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}
.section-label {
  display: block;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 2px;
  color: #9aa5b8;
  text-transform: uppercase;
  margin-bottom: 24px;
}
.clear-search-link {
  background: none;
  border: none;
  color: #4a7bff;
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
  margin-left: 12px;
  letter-spacing: 0;
  text-transform: none;
}
.clear-search-link:hover {
  opacity: 0.7;
}

/* POPULAR 区域 */
.popular-section {
  padding: 64px 0 48px;
  background: #fff;
}
.popular-featured {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
  align-items: center;
  margin-bottom: 48px;
}
.popular-text {
  padding: 8px 0;
}
.popular-title {
  font-size: 1.8rem;
  font-weight: 700;
  color: #1a1a2e;
  line-height: 1.3;
  margin-bottom: 16px;
  cursor: pointer;
  transition: color 0.2s;
}
.popular-title:hover {
  color: #4a7bff;
}
.popular-desc {
  font-size: 0.95rem;
  color: #7b7b9e;
  line-height: 1.7;
  margin-bottom: 20px;
}
.popular-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 20px;
}
.tag {
  display: inline-block;
  padding: 4px 12px;
  background: #f0f4ff;
  color: #4a7bff;
  font-size: 12px;
  font-weight: 600;
  border-radius: 6px;
}

.popular-image {
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  box-shadow: 0 12px 40px rgba(74, 123, 255, 0.15);
  transition:
    transform 0.3s,
    box-shadow 0.3s;
}
.popular-image:hover {
  transform: translateY(-4px);
  box-shadow: 0 20px 50px rgba(74, 123, 255, 0.22);
}
.popular-image img {
  width: 100%;
  height: 320px;
  object-fit: cover;
  display: block;
}

/* 3张卡片 */
.popular-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}
.popular-card {
  cursor: pointer;
  transition: transform 0.3s;
}
.popular-card:hover {
  transform: translateY(-6px);
}
.card-image {
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}
.card-image img {
  width: 100%;
  height: 180px;
  object-fit: cover;
  display: block;
  transition: transform 0.4s;
}
.popular-card:hover .card-image img {
  transform: scale(1.05);
}
.card-title {
  font-size: 1rem;
  font-weight: 700;
  color: #1a1a2e;
  line-height: 1.4;
  margin-bottom: 8px;
  transition: color 0.2s;
}
.popular-card:hover .card-title {
  color: #4a7bff;
}
.card-desc {
  font-size: 0.85rem;
  color: #7b7b9e;
  line-height: 1.6;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.card-tags {
  margin-bottom: 12px;
}

.section-footer-link {
  text-align: right;
  margin-top: 24px;
}
.section-footer-link a {
  font-size: 13px;
  font-weight: 600;
  color: #4a7bff;
  text-decoration: none;
  transition: opacity 0.2s;
}
.section-footer-link a:hover {
  opacity: 0.7;
}

/* ALL ARTICLE */
.all-articles-section {
  padding: 48px 0 64px;
  background: #fafbfc;
}
.article-row {
  display: grid;
  grid-template-columns: 240px 1fr;
  gap: 28px;
  align-items: start;
  padding: 28px 0;
  border-bottom: 1px solid #eef1f5;
  cursor: pointer;
  transition: background 0.2s;
}
.article-row:hover .row-title {
  color: #4a7bff;
}
.row-image {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}
.row-image img {
  width: 100%;
  height: 160px;
  object-fit: cover;
  display: block;
  transition: transform 0.4s;
}
.article-row:hover .row-image img {
  transform: scale(1.04);
}
.row-body {
  padding-top: 4px;
}
.row-title {
  font-size: 1.15rem;
  font-weight: 700;
  color: #1a1a2e;
  line-height: 1.4;
  margin-bottom: 10px;
  transition: color 0.2s;
}
.row-desc {
  font-size: 0.9rem;
  color: #7b7b9e;
  line-height: 1.6;
  margin-bottom: 14px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.row-tags {
  display: flex;
  gap: 6px;
  margin-bottom: 14px;
}
.row-meta {
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: #9aa5b8;
  margin-bottom: 14px;
}

/* 分页 */
.pagination-v2 {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 32px;
  padding-top: 16px;
}
.page-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border-radius: 10px;
  border: 1px solid #e2e8f0;
  background: #fff;
  color: #4a7bff;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}
.page-btn:hover:not(:disabled) {
  background: #4a7bff;
  color: #fff;
  border-color: #4a7bff;
  box-shadow: 0 4px 16px rgba(74, 123, 255, 0.25);
}
.page-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

/* CTA 区域 */
.cta-section {
  position: relative;
  background: #4a7bff;
  padding: 80px 24px 0;
  overflow: hidden;
}
.cta-inner {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 40px;
  padding-bottom: 80px;
}
.cta-content {
  max-width: 480px;
}
.cta-title {
  font-size: 2rem;
  font-weight: 700;
  color: #fff;
  line-height: 1.3;
  margin-bottom: 16px;
}
.cta-desc {
  font-size: 0.95rem;
  color: rgba(255, 255, 255, 0.8);
  line-height: 1.7;
  margin-bottom: 28px;
}
.cta-btn {
  padding: 14px 32px;
  border-radius: 10px;
  border: none;
  background: #fff;
  color: #4a7bff;
  font-size: 14px;
  font-weight: 700;
  cursor: pointer;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}
.cta-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
}
.cta-wave-bottom {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 80px;
}
.cta-wave-bottom svg {
  width: 100%;
  height: 100%;
  display: block;
}

/* 联系弹窗 */
.contact-modal {
  position: fixed;
  inset: 0;
  z-index: 200;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(4px);
}
.contact-card {
  background: #fff;
  border-radius: 16px;
  padding: 32px;
  max-width: 400px;
  width: 90%;
  position: relative;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}
.contact-card h3 {
  font-size: 1.4rem;
  font-weight: 700;
  margin-bottom: 16px;
  color: #1a1a2e;
}
.contact-card p {
  color: #7b7b9e;
  font-size: 14px;
  margin-bottom: 8px;
}
.contact-close {
  position: absolute;
  top: 16px;
  right: 20px;
  background: none;
  border: none;
  font-size: 24px;
  color: #9aa5b8;
  cursor: pointer;
  line-height: 1;
}

/* 响应式 */
@media (max-width: 900px) {
  .popular-featured {
    grid-template-columns: 1fr;
  }
  .popular-image img {
    height: 240px;
  }
  .popular-cards {
    grid-template-columns: 1fr;
  }
  .article-row {
    grid-template-columns: 1fr;
  }
  .row-image img {
    height: 200px;
  }
  .cta-inner {
    flex-direction: column;
    text-align: center;
  }
  .footer-links {
    gap: 40px;
  }
  .hero-title {
    font-size: 2.2rem;
  }
}
@media (max-width: 640px) {
  .hero-section {
    padding: 120px 20px 100px;
  }
  .hero-title {
    font-size: 1.8rem;
  }
  .footer-inner {
    flex-direction: column;
    gap: 32px;
  }
  .footer-links {
    gap: 32px;
  }
  .section-inner {
    padding: 0 20px;
  }
  .popular-section,
  .all-articles-section {
    padding: 40px 0;
  }
}

/* 深色模式 */
html.dark .blog-home-v2 {
  background: #0f172a;
  color: #e2e8f0;
}
html.dark .popular-section {
  background: #0f172a;
}
html.dark .all-articles-section {
  background: #1e293b;
}
html.dark .popular-title,
html.dark .card-title,
html.dark .row-title,
html.dark .cta-title {
  color: #e2e8f0;
}
html.dark .popular-desc,
html.dark .card-desc,
html.dark .row-desc,
html.dark .contact-card p {
  color: #94a3b8;
}
html.dark .tag {
  background: #1e3a5f;
  color: #60a5fa;
}
html.dark .article-row {
  border-bottom-color: #334155;
}
html.dark .page-btn {
  background: #1e293b;
  border-color: #334155;
  color: #60a5fa;
}
html.dark .page-btn:hover:not(:disabled) {
  background: #3b82f6;
  color: #fff;
}
html.dark .contact-card {
  background: #1e293b;
}
html.dark .contact-card h3 {
  color: #e2e8f0;
}
html.dark .row-meta {
  color: #64748b;
}
html.dark .section-label {
  color: #64748b;
}
</style>
