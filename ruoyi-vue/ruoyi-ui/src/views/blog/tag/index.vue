<template>
  <div class="tag-page">
    <div class="page-header">
      <h1 class="page-title">标签: {{ tagName }}</h1>
      <p class="page-description">共 {{ total }} 篇文章</p>
    </div>

    <div class="article-list">
      <div v-for="article in articleList" :key="article.id" class="article-item">
        <div class="article-cover" v-if="article.coverUrl">
          <img :src="article.coverUrl" :alt="article.title" />
        </div>
        <div class="article-content">
          <h2 class="article-title">
            <router-link :to="`/blog/article/${article.id}`">{{ article.title }}</router-link>
          </h2>
          <div class="article-meta">
            <span class="meta-item">
              <i class="el-icon-date"></i>
              {{ formatDate(article.createTime) }}
            </span>
            <span class="meta-item">
              <i class="el-icon-view"></i>
              {{ article.viewCount || 0 }} 阅读
            </span>
          </div>
          <p class="article-summary">{{ article.summary || article.content.substring(0, 150) + '...' }}</p>
          <div class="article-footer">
            <router-link :to="`/blog/article/${article.id}`" class="read-more">
              阅读全文 <i class="el-icon-arrow-right"></i>
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination-container" v-if="total > queryParams.pageSize">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="queryParams.pageSize"
        :current-page="queryParams.pageNum"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getArticleList } from '@/api/blog/article'
import { getTagDetail } from '@/api/blog/tag'

const route = useRoute()
const tagId = route.params.id

// 响应式数据
const articleList = ref([])
const tagDetail = ref({})
const total = ref(0)

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  tagId: tagId,
  status: 1
})

// 计算属性
const tagName = computed(() => tagDetail.value.name || '未知标签')

// 获取文章列表
const loadArticleList = async () => {
  try {
    const response = await getArticleList(queryParams)
    articleList.value = response.rows || []
    total.value = response.total || 0
  } catch (error) {
    console.error('获取文章列表失败:', error)
  }
}

// 获取标签详情
const loadTagDetail = async () => {
  try {
    const response = await getTagDetail(tagId)
    tagDetail.value = response.data || {}
  } catch (error) {
    console.error('获取标签详情失败:', error)
  }
}

// 分页处理
const handlePageChange = (page) => {
  queryParams.pageNum = page
  loadArticleList()
}

// 日期格式化
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

// 组件挂载时加载数据
onMounted(() => {
  loadTagDetail()
  loadArticleList()
})
</script>

<style scoped>
.tag-page {
  max-width: 800px;
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
}

.page-description {
  color: #666;
  font-size: 1.1rem;
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.article-item {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.article-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.article-cover {
  height: 200px;
  overflow: hidden;
}

.article-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.article-content {
  padding: 25px;
}

.article-title {
  margin: 0 0 15px 0;
  font-size: 1.5rem;
}

.article-title a {
  color: #333;
  text-decoration: none;
  transition: color 0.3s ease;
}

.article-title a:hover {
  color: #409eff;
}

.article-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
  font-size: 0.9rem;
  color: #666;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.article-summary {
  color: #666;
  line-height: 1.6;
  margin-bottom: 15px;
}

.article-footer {
  display: flex;
  justify-content: flex-end;
}

.read-more {
  color: #409eff;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.read-more:hover {
  color: #337ecc;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

@media (max-width: 768px) {
  .tag-page {
    padding: 20px 15px;
  }
  
  .article-meta {
    flex-direction: column;
    gap: 5px;
  }
}
</style>