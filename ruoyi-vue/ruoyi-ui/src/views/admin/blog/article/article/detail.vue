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
      <img :src="article.coverUrl" :alt="article.title" loading="lazy" />
      <div class="article-cover-overlay">
        <div class="article-category-tag" v-if="article.categoryName">
          <i class="el-icon-folder"></i>
          {{ article.categoryName }}
        </div>
        <div class="article-stats">
          <span class="stat">
            <i class="el-icon-view"></i>
            {{ article.viewCount || 0 }}
          </span>
          <span class="stat">
            <i class="el-icon-star-off"></i>
            {{ article.likeCount || 0 }}
          </span>
          <span class="stat">
            <i class="el-icon-chat-line-round"></i>
            {{ article.commentCount || 0 }}
          </span>
        </div>
      </div>
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
      <h3 class="comment-title">
        <i class="el-icon-chat-dot-round"></i>
        评论区 ({{ comments.length }})
      </h3>

      <!-- 评论表单 -->
      <div class="comment-form">
        <div class="form-header">
          <div class="user-avatar">
            <i class="el-icon-user-solid"></i>
          </div>
          <div class="form-info">
            <div class="user-name">匿名用户</div>
            <div class="form-tip">发表评论...</div>
          </div>
        </div>
        <el-input
          type="textarea"
          :rows="4"
          placeholder="写下你的评论..."
          v-model="commentContent"
          class="comment-input"
          maxlength="500"
          show-word-limit
        ></el-input>
        <div class="form-actions">
          <el-button type="primary" @click="submitComment" :loading="submittingComment" round>
            <i class="el-icon-s-promotion"></i>
            发表评论
          </el-button>
        </div>
      </div>

      <!-- 评论列表 -->
      <div class="comment-list">
        <div v-if="comments.length === 0" class="no-comments">
          <i class="el-icon-chat-line-round"></i>
          <p>暂无评论，快来发表第一条评论吧！</p>
        </div>
        <div v-for="comment in comments" :key="comment.id" class="comment-item">
          <div class="comment-avatar">
            <img :src="getAvatarUrl(comment)" :alt="comment.nickname" />
          </div>
          <div class="comment-content">
            <div class="comment-header">
              <span class="comment-author">{{ comment.nickname || '匿名用户' }}</span>
              <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
            </div>
            <div class="comment-text">{{ comment.content }}</div>
            <div class="comment-actions">
              <el-button type="text" size="mini" @click="replyToComment(comment)">
                <i class="el-icon-chat-line-round"></i>
                回复
              </el-button>
              <el-button type="text" size="mini" @click="likeComment(comment)">
                <i class="el-icon-star-off"></i>
                点赞
              </el-button>
            </div>
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
const submittingComment = ref(false)
const replyTo = ref(null)

// 获取文章详情
const loadArticleDetail = async () => {
  try {
    const response = await getArticleDetail(articleId)
    const data = response.data || {}
    
    // 设置文章数据，确保所有字段都有默认值
    const articleData = data.article || {}
    article.value = {
      id: articleData.id || 0,
      title: articleData.title || '',
      summary: articleData.summary || '',
      content: articleData.content || '',
      coverUrl: articleData.coverUrl || '',
      categoryId: articleData.categoryId || 0,
      authorId: articleData.authorId || 0,
      author: articleData.author || '',
      authorName: articleData.authorName || '',
      isTop: articleData.isTop || 0,
      isRecommend: articleData.isRecommend || 0,
      status: articleData.status || 0,
      viewCount: articleData.viewCount || 0,
      likeCount: articleData.likeCount || 0,
      commentCount: articleData.commentCount || 0,
      createTime: articleData.createTime || '',
      updateTime: articleData.updateTime || '',
      delFlag: articleData.delFlag || 0
    }
    
    // 设置上下篇文章数据，确保字段安全访问
    if (data.extraInfo) {
      const prev = data.extraInfo.prevArticle
      const next = data.extraInfo.nextArticle
      
      prevArticle.value = prev ? {
        id: prev.id || 0,
        title: prev.title || '上一篇'
      } : null
      
      nextArticle.value = next ? {
        id: next.id || 0,
        title: next.title || '下一篇'
      } : null
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

  submittingComment.value = true
  try {
    await addBlogComment({
      articleId: articleId,
      content: commentContent.value.trim(),
      parentId: replyTo.value?.id || 0
    })

    ElMessage.success('评论发表成功')
    commentContent.value = ''
    replyTo.value = null
    loadComments() // 重新加载评论列表
  } catch (error) {
    console.error('发表评论失败:', error)
    ElMessage.error('评论发表失败')
  } finally {
    submittingComment.value = false
  }
}

// 回复评论
const replyToComment = (comment) => {
  replyTo.value = comment
  commentContent.value = `@${comment.nickname || '匿名用户'} `
  // 聚焦到评论输入框
  nextTick(() => {
    const input = document.querySelector('.comment-input textarea')
    if (input) {
      input.focus()
      input.setSelectionRange(input.value.length, input.value.length)
    }
  })
}

// 点赞评论
const likeComment = (comment) => {
  // 这里可以调用点赞API
  ElMessage.success('点赞成功')
}

// 获取用户头像
const getAvatarUrl = (comment) => {
  // 如果有用户头像，返回用户头像，否则返回默认头像
  return comment.avatar || `https://via.placeholder.com/40x40/409EFF/FFFFFF?text=${(comment.nickname || '匿名').charAt(0)}`
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
  position: relative;
}

.article-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.article-cover:hover img {
  transform: scale(1.02);
}

.article-cover-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, rgba(0,0,0,0.1) 0%, rgba(0,0,0,0.3) 100%);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 20px;
  color: white;
}

.article-category-tag {
  align-self: flex-start;
  background: rgba(64, 158, 255, 0.9);
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 5px;
}

.article-stats {
  align-self: flex-end;
  display: flex;
  gap: 15px;
}

.article-stats .stat {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 0.9rem;
  background: rgba(0,0,0,0.5);
  padding: 4px 8px;
  border-radius: 12px;
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
  display: flex;
  align-items: center;
  gap: 10px;
}

.comment-title i {
  color: #409eff;
}

.comment-form {
  margin-bottom: 40px;
  background: #fafafa;
  border-radius: 12px;
  padding: 20px;
  border: 1px solid #f0f0f0;
}

.form-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 15px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  background: linear-gradient(45deg, #409eff, #67c23a);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.2rem;
}

.user-name {
  font-weight: 600;
  color: #333;
}

.form-tip {
  color: #999;
  font-size: 0.9rem;
}

.comment-input {
  margin-bottom: 15px;
}

.comment-input :deep(.el-textarea__inner) {
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  transition: border-color 0.3s ease;
}

.comment-input :deep(.el-textarea__inner):focus {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
}

.form-actions {
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
  background: white;
  border-radius: 12px;
  border: 1px solid #f0f0f0;
  transition: box-shadow 0.3s ease;
}

.comment-item:hover {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.comment-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
}

.comment-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.comment-author {
  font-weight: 600;
  color: #333;
}

.comment-time {
  color: #999;
  font-size: 0.85rem;
}

.comment-text {
  color: #333;
  line-height: 1.6;
  margin-bottom: 10px;
  word-wrap: break-word;
}

.comment-actions {
  display: flex;
  gap: 15px;
}

.comment-actions .el-button {
  padding: 0;
  color: #999;
  font-size: 0.85rem;
}

.comment-actions .el-button:hover {
  color: #409eff;
}

.no-comments {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.no-comments i {
  font-size: 3rem;
  margin-bottom: 15px;
  display: block;
  opacity: 0.5;
}

.no-comments p {
  margin: 0;
  font-size: 1rem;
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