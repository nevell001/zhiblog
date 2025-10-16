<template>
  <div class="article-detail">
    <!-- 博客导航 -->
    <BlogNav />
    
    <!-- 文章头部 -->
    <div class="article-header">
      <div class="header-content">
        <h1 class="article-title">{{ article.title }}</h1>
        <div class="article-meta">
          <span class="meta-item">
            <i class="el-icon-date"></i>
            {{ formatDate(article.createTime) }}
          </span>
          <span class="meta-item">
            <i class="el-icon-view"></i>
            {{ article.viewCount || 0 }} 阅读
          </span>
          <span class="meta-item" v-if="article.categoryName">
            <i class="el-icon-folder"></i>
            {{ article.categoryName }}
          </span>
          <span class="meta-item" v-if="article.tags && article.tags.length">
            <i class="el-icon-collection-tag"></i>
            <span v-for="tag in article.tags" :key="tag.id" class="tag">{{ tag.name }}</span>
          </span>
        </div>
      </div>
    </div>

    <!-- 文章封面 -->
    <div class="article-cover" v-if="article.coverUrl">
      <img :src="article.coverUrl" :alt="article.title" />
    </div>

    <!-- 文章内容 -->
    <div class="article-content">
      <div class="content-wrapper" v-html="article.content"></div>
    </div>

    <!-- 文章底部 -->
    <div class="article-footer">
      <div class="footer-content">
        <div class="copyright">
          <p>本文由 {{ article.author || '博主' }} 创作，采用 <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/" target="_blank">CC BY-NC-SA 4.0</a> 许可协议</p>
        </div>
        <div class="navigation">
          <router-link v-if="prevArticle" :to="`/blog/article/${prevArticle.id}`" class="nav-link prev">
            <i class="el-icon-arrow-left"></i>
            {{ prevArticle.title }}
          </router-link>
          <router-link v-if="nextArticle" :to="`/blog/article/${nextArticle.id}`" class="nav-link next">
            {{ nextArticle.title }}
            <i class="el-icon-arrow-right"></i>
          </router-link>
        </div>
      </div>
    </div>

    <!-- 评论区域 -->
    <div class="comment-section">
      <h3 class="comment-title">评论</h3>
      <div class="comment-form">
        <el-input
          type="textarea"
          :rows="4"
          placeholder="请输入评论内容..."
          v-model="commentContent"
        ></el-input>
        <div class="form-actions">
          <el-button type="primary" @click="submitComment">发表评论</el-button>
        </div>
      </div>
      <div class="comment-list">
        <div v-for="comment in comments" :key="comment.id" class="comment-item">
          <div class="comment-avatar">
            <i class="el-icon-user-solid"></i>
          </div>
          <div class="comment-content">
            <div class="comment-header">
              <span class="comment-author">{{ comment.nickname || '匿名用户' }}</span>
              <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
            </div>
            <div class="comment-text">{{ comment.content }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getArticleDetail, updateArticleViewCount } from '@/api/blog/article'
import { getArticleComments, addBlogComment } from '@/api/blog/comment'
import BlogNav from '@/components/BlogNav.vue'

const route = useRoute()
const articleId = route.params.id

// 响应式数据
const article = ref({})
const comments = ref([])
const prevArticle = ref(null)
const nextArticle = ref(null)
const commentContent = ref('')

// 获取文章详情
const loadArticleDetail = async () => {
  try {
    const response = await getArticleDetail(articleId)
    const data = response.data || {}
    
    // 设置文章数据
    article.value = data.article || {}
    
    // 设置上下篇文章数据
    if (data.extraInfo) {
      prevArticle.value = data.extraInfo.prevArticle || null
      nextArticle.value = data.extraInfo.nextArticle || null
    }
    
    // 更新阅读量
    await updateArticleViewCount(articleId)
  } catch (error) {
    console.error('获取文章详情失败:', error)
  }
}

// 获取评论列表
const loadComments = async () => {
  try {
    const response = await getArticleComments(articleId)
    comments.value = response.rows || []
  } catch (error) {
    console.error('获取评论列表失败:', error)
  }
}

// 提交评论
const submitComment = async () => {
  if (!commentContent.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  try {
    await addBlogComment({
      articleId: articleId,
      content: commentContent.value.trim()
    })
    
    ElMessage.success('评论发表成功')
    commentContent.value = ''
    loadComments() // 重新加载评论列表
  } catch (error) {
    console.error('发表评论失败:', error)
    ElMessage.error('评论发表失败')
  }
}

// 日期格式化
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 组件挂载时加载数据
onMounted(() => {
  loadArticleDetail()
  loadComments()
})
</script>

<style scoped>
.article-detail {
  max-width: 800px;
  margin: 0 auto;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.article-header {
  padding: 40px;
  border-bottom: 1px solid #f0f0f0;
}

.header-content {
  text-align: center;
}

.article-title {
  font-size: 2rem;
  margin: 0 0 20px 0;
  color: #333;
  line-height: 1.3;
}

.article-meta {
  display: flex;
  justify-content: center;
  gap: 20px;
  color: #666;
  font-size: 0.9rem;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.tag {
  background: #f0f0f0;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
  margin-left: 5px;
}

.article-cover {
  height: 400px;
  overflow: hidden;
}

.article-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.article-content {
  padding: 40px;
}

.content-wrapper {
  line-height: 1.8;
  font-size: 1.1rem;
  color: #333;
}

.content-wrapper :deep(h1),
.content-wrapper :deep(h2),
.content-wrapper :deep(h3) {
  margin-top: 30px;
  margin-bottom: 15px;
  color: #333;
}

.content-wrapper :deep(p) {
  margin-bottom: 20px;
}

.content-wrapper :deep(code) {
  background: #f5f5f5;
  padding: 2px 6px;
  border-radius: 3px;
  font-family: 'Courier New', monospace;
}

.content-wrapper :deep(blockquote) {
  border-left: 4px solid #409eff;
  background: #f8f9fa;
  padding: 15px 20px;
  margin: 20px 0;
  font-style: italic;
}

.article-footer {
  padding: 30px 40px;
  border-top: 1px solid #f0f0f0;
  background: #fafafa;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.copyright {
  color: #666;
  font-size: 0.9rem;
}

.copyright a {
  color: #409eff;
  text-decoration: none;
}

.navigation {
  display: flex;
  gap: 20px;
}

.nav-link {
  color: #333;
  text-decoration: none;
  font-size: 0.9rem;
  transition: color 0.3s ease;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.nav-link:hover {
  color: #409eff;
}

.nav-link.prev {
  text-align: left;
}

.nav-link.next {
  text-align: right;
}

.comment-section {
  padding: 40px;
  border-top: 1px solid #f0f0f0;
}

.comment-title {
  font-size: 1.5rem;
  margin-bottom: 30px;
  color: #333;
}

.comment-form {
  margin-bottom: 40px;
}

.form-actions {
  margin-top: 15px;
  text-align: right;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-item {
  display: flex;
  gap: 15px;
  padding: 20px;
  background: #fafafa;
  border-radius: 8px;
}

.comment-avatar {
  width: 40px;
  height: 40px;
  background: #409eff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.2rem;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.comment-author {
  font-weight: bold;
  color: #333;
}

.comment-time {
  color: #666;
  font-size: 0.9rem;
}

.comment-text {
  color: #333;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .article-header {
    padding: 20px;
  }
  
  .article-title {
    font-size: 1.5rem;
  }
  
  .article-meta {
    flex-direction: column;
    gap: 10px;
  }
  
  .article-content {
    padding: 20px;
  }
  
  .article-footer {
    padding: 20px;
  }
  
  .footer-content {
    flex-direction: column;
    gap: 20px;
    text-align: center;
  }
  
  .comment-section {
    padding: 20px;
  }
}
</style>