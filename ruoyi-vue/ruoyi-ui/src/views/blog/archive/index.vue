<template>
  <div class="archive-page">
    <div class="page-header">
      <h1 class="page-title">文章归档</h1>
      <p class="page-description">共 {{ totalArticles }} 篇文章</p>
    </div>

    <div class="archive-content">
      <div v-for="archive in archiveList" :key="archive.archive_date" class="archive-item">
        <h2 class="archive-date">{{ formatArchiveDate(archive.archive_date) }}</h2>
        <div class="archive-articles">
          <!-- 这里可以添加按月份获取文章的接口来显示具体文章 -->
          <div class="article-count">{{ archive.article_count }} 篇文章</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getArticleArchive } from '@/api/blog/article'

// 响应式数据
const archiveList = ref([])
const totalArticles = ref(0)

// 获取归档数据
const loadArchiveData = async () => {
  try {
    const response = await getArticleArchive()
    archiveList.value = response.data || []
    
    // 计算总文章数
    totalArticles.value = archiveList.value.reduce((total, item) => total + (item.article_count || 0), 0)
  } catch (error) {
    console.error('获取归档数据失败:', error)
  }
}

// 格式化归档日期
const formatArchiveDate = (dateString) => {
  if (!dateString) return ''
  const [year, month] = dateString.split('-')
  const monthNames = ['一月', '二月', '三月', '四月', '五月', '六月', 
                     '七月', '八月', '九月', '十月', '十一月', '十二月']
  return `${year}年 ${monthNames[parseInt(month) - 1]}`
}

// 组件挂载时加载数据
onMounted(() => {
  loadArchiveData()
})
</script>

<style scoped>
.archive-page {
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

.archive-content {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.archive-item {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 25px;
}

.archive-date {
  font-size: 1.5rem;
  color: #333;
  margin: 0 0 15px 0;
  padding-bottom: 10px;
  border-bottom: 2px solid #409eff;
}

.article-count {
  color: #666;
  font-size: 1rem;
}

@media (max-width: 768px) {
  .archive-page {
    padding: 20px 15px;
  }
}
</style>