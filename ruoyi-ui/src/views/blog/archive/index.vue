<template>
  <div class="archive-page">
    <div class="page-header">
      <h1 class="page-title">
        文章归档
      </h1>
      <p class="page-description">
        共 {{ totalArticles }} 篇文章
      </p>
    </div>

    <div
      v-loading="loading"
      class="archive-content"
    >
      <div
        v-if="archiveList.length === 0 && !loading"
        class="no-data"
      >
        <el-icon :size="60">
          <Calendar />
        </el-icon>
        <p>暂无归档数据</p>
      </div>

      <div v-else>
        <div
          v-for="archive in archiveList"
          :key="archive.archive_date"
          class="archive-item"
        >
          <div
            class="archive-header"
            @click="toggleArchive(archive.archive_date)"
          >
            <h2 class="archive-date">
              <el-icon
                :size="20"
                class="toggle-icon"
              >
                <component
                  :is="expandedArchives.includes(archive.archive_date) ? 'ArrowDown' : 'ArrowRight'"
                />
              </el-icon>
              {{ formatArchiveDate(archive.archive_date) }}
            </h2>
            <span class="archive-count">{{ archive.article_count }} 篇文章</span>
          </div>

          <div
            v-show="expandedArchives.includes(archive.archive_date)"
            class="archive-articles"
          >
            <div
              v-if="loadingArticles[archive.archive_date]"
              class="loading-articles"
            >
              <el-icon
                :size="24"
                class="is-loading"
              >
                <Loading />
              </el-icon>
              <span>加载中...</span>
            </div>

            <div
              v-else-if="
                articlesByArchive[archive.archive_date] &&
                  articlesByArchive[archive.archive_date].length > 0
              "
              class="article-list"
            >
              <div
                v-for="article in articlesByArchive[archive.archive_date]"
                :key="article.id"
                class="article-item"
              >
                <div
                  v-if="article.coverUrl || article.coverImage"
                  class="article-cover"
                >
                  <img
                    :src="article.coverUrl || article.coverImage"
                    :alt="article.title"
                  />
                </div>
                <div class="article-info">
                  <router-link
                    :to="{ name: 'PublicBlogArticleDetail', params: { id: article.id } }"
                    class="article-title"
                  >
                    {{ article.title }}
                  </router-link>
                  <p class="article-summary">
                    {{
                      article.summary || stripHtmlTags(article.content).substring(0, 120) + '...'
                    }}
                  </p>
                  <div class="article-meta">
                    <span class="meta-item">
                      <el-icon :size="14"><Calendar /></el-icon>
                      {{ formatDate(article.createTime) }}
                    </span>
                    <span class="meta-item">
                      <el-icon :size="14"><View /></el-icon>
                      {{ article.viewCount || 0 }}
                    </span>
                    <span class="meta-item">
                      <el-icon :size="14"><ChatDotRound /></el-icon>
                      {{ article.commentCount || 0 }}
                    </span>
                  </div>
                </div>
              </div>
            </div>

            <div
              v-else-if="!loadingArticles[archive.archive_date]"
              class="no-articles"
            >
              <p>该月份暂无文章</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getArticleArchive, getArticlesByArchive } from '@/api/blog/article'
import {
  Calendar,
  ArrowDown,
  ArrowRight,
  Loading,
  View,
  ChatDotRound
} from '@element-plus/icons-vue'

// 响应式数据
const archiveList = ref([])
const articlesByArchive = ref({})
const loadingArticles = ref({})
const expandedArchives = ref([])
const totalArticles = ref(0)
const loading = ref(false)

// 获取归档数据
const loadArchiveData = async () => {
  try {
    loading.value = true
    const response = await getArticleArchive()
    archiveList.value = response.data || []

    // 计算总文章数
    totalArticles.value = archiveList.value.reduce(
      (total, item) => total + (item.article_count || 0),
      0
    )

    // 默认展开第一个归档
    if (archiveList.value.length > 0) {
      expandedArchives.value = [archiveList.value[0].archive_date]
      await loadArticlesByArchive(archiveList.value[0].archive_date)
    }
  } catch (error) {
    console.error('获取归档数据失败:', error)
  } finally {
    loading.value = false
  }
}

// 根据归档月份获取文章列表
const loadArticlesByArchive = async archiveDate => {
  try {
    loadingArticles.value[archiveDate] = true
    const [year, month] = archiveDate.split('-')
    const response = await getArticlesByArchive(year, month, { pageNum: 1, pageSize: 100 })

    if (response && response.code === 200) {
      articlesByArchive.value[archiveDate] = response.rows || response.data || []
    }
  } catch (error) {
    console.error('获取归档文章失败:', error)
    articlesByArchive.value[archiveDate] = []
  } finally {
    loadingArticles.value[archiveDate] = false
  }
}

// 切换归档展开/收起
const toggleArchive = async archiveDate => {
  const index = expandedArchives.value.indexOf(archiveDate)

  if (index > -1) {
    // 收起
    expandedArchives.value.splice(index, 1)
  } else {
    // 展开
    expandedArchives.value.push(archiveDate)

    // 如果还没有加载文章，则加载
    if (!articlesByArchive.value[archiveDate]) {
      await loadArticlesByArchive(archiveDate)
    }
  }
}

// 格式化归档日期
const formatArchiveDate = dateString => {
  if (!dateString) return ''
  const [year, month] = dateString.split('-')
  const monthNames = [
    '一月',
    '二月',
    '三月',
    '四月',
    '五月',
    '六月',
    '七月',
    '八月',
    '九月',
    '十月',
    '十一月',
    '十二月'
  ]
  return `${year}年 ${monthNames[parseInt(month) - 1]}`
}

// 格式化日期
const formatDate = dateString => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

// 去除HTML标签
const stripHtmlTags = html => {
  if (!html) return ''
  return html.replace(/<[^>]*>/g, '')
}

// 组件挂载时加载数据
onMounted(() => {
  loadArchiveData()
})
</script>

<style scoped>
.archive-page {
  max-width: 900px;
  margin: 0 auto;
  padding: 40px 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
}

.page-title {
  font-size: 2rem;
  color: #333;
  margin-bottom: 10px;
  font-weight: 600;
}

.page-description {
  color: #666;
  font-size: 1.1rem;
}

.archive-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.no-data {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.no-data .el-icon {
  margin-bottom: 15px;
  color: #ddd;
}

.archive-item {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.3s ease;
}

.archive-item:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
}

.archive-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 25px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  cursor: pointer;
  user-select: none;
}

.archive-header:hover {
  background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
}

.archive-date {
  font-size: 1.3rem;
  font-weight: 600;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.toggle-icon {
  transition: transform 0.3s ease;
}

.archive-count {
  font-size: 1rem;
  opacity: 0.9;
  padding: 4px 12px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 20px;
}

.archive-articles {
  padding: 20px 25px;
  background: #fafafa;
}

.loading-articles {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 40px;
  color: #999;
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.article-item {
  display: flex;
  gap: 20px;
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.article-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.article-cover {
  width: 200px;
  height: 120px;
  flex-shrink: 0;
  border-radius: 6px;
  overflow: hidden;
}

.article-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.article-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.article-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
  text-decoration: none;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-bottom: 8px;
  transition: color 0.3s ease;
}

.article-title:hover {
  color: #409eff;
}

.article-summary {
  font-size: 0.9rem;
  color: #666;
  line-height: 1.6;
  margin: 0 0 12px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.article-meta {
  display: flex;
  gap: 15px;
  font-size: 0.85rem;
  color: #999;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.no-articles {
  text-align: center;
  padding: 30px;
  color: #999;
}

@media (max-width: 768px) {
  .archive-page {
    padding: 20px 15px;
  }

  .archive-header {
    padding: 15px 20px;
  }

  .archive-date {
    font-size: 1.1rem;
  }

  .archive-articles {
    padding: 15px 20px;
  }

  .article-item {
    flex-direction: column;
  }

  .article-cover {
    width: 100%;
    height: 160px;
  }

  .article-meta {
    flex-wrap: wrap;
    gap: 10px;
  }
}
</style>
