<template>
  <div class="article-detail-container">
    <!-- 博客导航 -->
    <BlogNav />

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <div class="loading-skeleton">
        <el-skeleton :loading="loading" animated class="article-skeleton">
          <template #template>
            <div class="skeleton-content">
              <el-skeleton-item variant="h1" style="width: 60%; margin-bottom: 20px;" />
              <el-skeleton-item variant="text" style="width: 100%; margin-bottom: 10px;" />
              <el-skeleton-item variant="text" style="width: 90%; margin-bottom: 10px;" />
              <el-skeleton-item variant="text" style="width: 80%; margin-bottom: 20px;" />
              <el-skeleton-item variant="rect" style="width: 100%; height: 400px; margin-bottom: 20px;" />
              <el-skeleton-item variant="text" style="width: 100%; height: 20px; margin-bottom: 8px;" />
              <el-skeleton-item variant="text" style="width: 95%; height: 20px; margin-bottom: 8px;" />
              <el-skeleton-item variant="text" style="width: 85%; height: 20px; margin-bottom: 8px;" />
            </div>
          </template>
        </el-skeleton>
      </div>
    </div>

    <!-- 文章不存在 -->
    <div v-else-if="!article" class="not-found-container">
      <div class="not-found-content">
        <i class="el-icon-document-copy not-found-icon"></i>
        <h2>文章不存在</h2>
        <p>抱歉，您访问的文章不存在或已被删除。</p>
        <router-link to="/" class="back-home-btn">
          <el-button type="primary" size="large">返回首页</el-button>
        </router-link>
      </div>
    </div>

    <!-- 文章详情 -->
    <div v-else class="article-detail">
      <div class="article-header">
        <div class="article-meta">
          <div class="meta-info">
            <span class="category" v-if="article.categoryName">{{ article.categoryName }}</span>
            <span class="publish-time">
              <i class="el-icon-date"></i>
              {{ formatDate(article.createTime) }}
            </span>
            <span class="view-count">
              <i class="el-icon-view"></i>
              {{ article.viewCount || 0 }} 阅读
            </span>
            <span class="like-count" v-if="article.likeCount">
              <i class="el-icon-star-off"></i>
              {{ article.likeCount }} 点赞
            </span>
            <span class="comment-count" v-if="article.commentCount">
              <i class="el-icon-chat-line-round"></i>
              {{ article.commentCount }} 评论
            </span>
          </div>
        </div>

        <h1 class="article-title">{{ article.title }}</h1>

        <div class="article-tags" v-if="article.tags && article.tags.length">
          <span v-for="tag in article.tags" :key="tag.id" class="tag-item" :style="{ backgroundColor: tag.color || '#409EFF' }">
            <i class="el-icon-price-tag"></i>
            {{ tag.name }}
          </span>
        </div>
      </div>

      <div class="article-content">
        <!-- 封面图片 -->
        <div v-if="article.coverUrl" class="article-cover">
          <img :src="article.coverUrl" :alt="article.title" />
        </div>

        <!-- 文章内容 -->
        <div class="content-body" v-html="article.content"></div>
      </div>

      <!-- 文章操作 -->
      <div class="article-actions">
        <div class="action-buttons">
          <el-button @click="handleLike" :loading="likeLoading" :type="article.isLiked ? 'success' : 'default'" plain>
            <i class="el-icon-star-off"></i>
            点赞 {{ article.likeCount || 0 }}
          </el-button>
          <el-button @click="handleShare" type="default" plain>
            <i class="el-icon-share"></i>
            分享
          </el-button>
          <el-button @click="handleBookmark" :type="article.isBookmarked ? 'warning' : 'default'" plain>
            <i class="el-icon-collection-tag"></i>
            {{ article.isBookmarked ? '已收藏' : '收藏' }}
          </el-button>
        </div>
      </div>

      <!-- 上下篇文章 -->
      <div class="article-navigation" v-if="prevArticle || nextArticle">
        <div class="nav-item prev-article" v-if="prevArticle">
          <router-link :to="`/blog/article/${prevArticle.id}`" class="nav-link">
            <div class="nav-arrow">
              <i class="el-icon-arrow-left"></i>
            </div>
            <div class="nav-content">
              <div class="nav-label">上一篇</div>
              <div class="nav-title">{{ prevArticle.title }}</div>
            </div>
          </router-link>
        </div>
        <div class="nav-item next-article" v-if="nextArticle">
          <router-link :to="`/blog/article/${nextArticle.id}`" class="nav-link">
            <div class="nav-content">
              <div class="nav-label">下一篇</div>
              <div class="nav-title">{{ nextArticle.title }}</div>
            </div>
            <div class="nav-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </router-link>
        </div>
      </div>

      <!-- 相关文章 -->
      <div class="related-articles" v-if="relatedArticles.length > 0">
        <h3 class="section-title">
          <i class="el-icon-document-copy"></i>
          相关文章
        </h3>
        <div class="related-list">
          <div v-for="related in relatedArticles.slice(0, 6)" :key="related.id" class="related-item">
            <router-link :to="`/blog/article/${related.id}`" class="related-link">
              <div class="related-cover" v-if="related.coverUrl">
                <img :src="related.coverUrl" :alt="related.title" />
              </div>
              <div class="related-info">
                <h4 class="related-title">{{ related.title }}</h4>
                <div class="related-meta">
                  <span class="related-date">{{ formatDate(related.createTime) }}</span>
                  <span class="related-views">{{ related.viewCount || 0 }} 阅读</span>
                </div>
              </div>
            </router-link>
          </div>
        </div>
      </div>

      <!-- 评论区域 -->
      <div class="comment-section">
        <h3 class="section-title">
          <i class="el-icon-chat-dot-round"></i>
          评论 ({{ totalComments }})
        </h3>

        <!-- 评论列表 -->
        <div class="comment-list" v-if="commentList.length > 0">
          <div v-for="comment in commentList" :key="comment.id" class="comment-item">
            <div class="comment-avatar">
              <img :src="comment.avatar || 'https://via.placeholder.com/40x40/409EFF/FFFFFF?text=' + (comment.nickname ? comment.nickname.charAt(0) : 'U')" :alt="comment.nickname" />
            </div>
            <div class="comment-content">
              <div class="comment-header">
                <span class="comment-author">{{ comment.nickname || '匿名' }}</span>
                <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
              </div>
              <div class="comment-text">{{ comment.content }}</div>
              <div class="comment-actions">
                <el-button size="mini" @click="handleReply(comment)" type="text">回复</el-button>
                <el-button size="mini" @click="handleLikeComment(comment)" type="text">
                  <i class="el-icon-star-off"></i>
                  {{ comment.likeCount || 0 }}
                </el-button>
              </div>

              <!-- 回复列表 -->
              <div v-if="comment.replies && comment.replies.length > 0" class="comment-replies">
                <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
                  <div class="reply-avatar">
                    <img :src="reply.avatar || 'https://via.placeholder.com/32x32/409EFF/FFFFFF?text=' + (reply.nickname ? reply.nickname.charAt(0) : 'U')" :alt="reply.nickname" />
                  </div>
                  <div class="reply-content">
                    <div class="reply-header">
                      <span class="reply-author">{{ reply.nickname || '匿名' }}</span>
                      <span class="reply-time">{{ formatDate(reply.createTime) }}</span>
                    </div>
                    <div class="reply-text">{{ reply.content }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 发表评论 -->
        <div class="comment-form">
          <div class="form-header">
            <h4>发表评论</h4>
          </div>
          <el-form :model="commentForm" :rules="commentRules" ref="commentFormRef" label-width="0">
            <el-form-item prop="nickname" v-if="!isLoggedIn">
              <el-input
                v-model="commentForm.nickname"
                placeholder="请输入您的昵称"
                size="large"
              />
            </el-form-item>
            <el-form-item prop="email" v-if="!isLoggedIn">
              <el-input
                v-model="commentForm.email"
                placeholder="请输入您的邮箱（可选）"
                size="large"
              />
            </el-form-item>
            <el-form-item prop="content">
              <el-input
                v-model="commentForm.content"
                type="textarea"
                placeholder="写下您的评论..."
                :rows="4"
                size="large"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitComment" :loading="commentSubmitting" size="large">
                发表评论
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import BlogNav from '@/components/BlogNav.vue'
import { getArticleDetail, getRelatedArticles, getArticleComments, submitComment as apiSubmitComment } from '@/api/blog/article'

const route = useRoute()

// 响应式数据
const article = ref(null)
const prevArticle = ref(null)
const nextArticle = ref(null)
const relatedArticles = ref([])
const commentList = ref([])
const totalComments = ref(0)
const loading = ref(false)
const likeLoading = ref(false)
const commentSubmitting = ref(false)
const isLoggedIn = ref(false) // 假设有登录状态

// 评论表单
const commentForm = reactive({
  nickname: '',
  email: '',
  content: ''
})

const commentRules = {
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入评论内容', trigger: 'blur' },
    { min: 1, max: 500, message: '评论内容长度在1到500个字符', trigger: 'blur' }
  ]
}

const commentFormRef = ref(null)

// 获取文章详情
const loadArticleDetail = async () => {
  try {
    loading.value = true
    const articleId = route.params.id

    // 获取文章详情
    const response = await getArticleDetail(articleId)
    console.log('文章详情响应:', response)
    article.value = response.data || response

    // 获取相关文章
    if (article.value) {
      const relatedResponse = await getRelatedArticles(articleId, { pageNum: 1, pageSize: 6 })
      relatedArticles.value = relatedResponse.rows || []
    }

    // 获取评论列表
    await loadComments()

  } catch (error) {
    console.error('获取文章详情失败:', error)
    ElMessage.error('获取文章详情失败')
  } finally {
    loading.value = false
  }
}

// 获取评论列表
const loadComments = async () => {
  try {
    const articleId = route.params.id
    const response = await getArticleComments(articleId, { pageNum: 1, pageSize: 20 })
    console.log('评论列表响应:', response)
    commentList.value = response.rows || []
    totalComments.value = response.total || 0
  } catch (error) {
    console.error('获取评论列表失败:', error)
  }
}

// 点赞文章
const handleLike = async () => {
  if (likeLoading.value) return

  try {
    likeLoading.value = true
    // 这里应该调用点赞API
    // await likeArticle(article.value.id)

    article.value.isLiked = !article.value.isLiked
    article.value.likeCount = (article.value.likeCount || 0) + (article.value.isLiked ? 1 : -1)

    ElMessage.success(article.value.isLiked ? '点赞成功' : '取消点赞')
  } catch (error) {
    console.error('点赞失败:', error)
    ElMessage.error('操作失败')
  } finally {
    likeLoading.value = false
  }
}

// 分享文章
const handleShare = () => {
  const url = window.location.href
  const title = article.value.title

  if (navigator.share) {
    navigator.share({
      title: title,
      url: url
    })
  } else {
    // 复制链接到剪贴板
    navigator.clipboard.writeText(url).then(() => {
      ElMessage.success('链接已复制到剪贴板')
    }).catch(() => {
      ElMessage.warning('请手动复制链接：' + url)
    })
  }
}

// 收藏文章
const handleBookmark = () => {
  article.value.isBookmarked = !article.value.isBookmarked
  ElMessage.success(article.value.isBookmarked ? '收藏成功' : '取消收藏')
}

// 回复评论
const handleReply = (comment) => {
  // 这里可以实现回复功能
  ElMessage.info('回复功能开发中')
}

// 点赞评论
const handleLikeComment = (comment) => {
  comment.likeCount = (comment.likeCount || 0) + 1
  ElMessage.success('点赞成功')
}

// 提交评论
const submitComment = async () => {
  try {
    await commentFormRef.value.validate()

    commentSubmitting.value = true

    const commentData = {
      articleId: article.value.id,
      ...commentForm
    }

    await apiSubmitComment(commentData)

    ElMessage.success('评论发表成功')
    commentForm.content = ''
    await loadComments()

  } catch (error) {
    console.error('提交评论失败:', error)
    if (error !== 'validation_failed') {
      ElMessage.error('评论发表失败')
    }
  } finally {
    commentSubmitting.value = false
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
})
</script>

<style scoped>
.article-detail-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.article-detail {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  margin-top: 20px;
  margin-bottom: 20px;
}

.article-header {
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 30px;
  margin-bottom: 30px;
}

.article-meta {
  margin-bottom: 20px;
}

.meta-info {
  display: flex;
  align-items: center;
  gap: 20px;
  flex-wrap: wrap;
  color: #666;
  font-size: 0.9rem;
}

.category {
  background: linear-gradient(135deg, #409eff, #337ecc);
  color: white;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 500;
}

.publish-time,
.view-count,
.like-count,
.comment-count {
  display: flex;
  align-items: center;
  gap: 4px;
}

.article-title {
  font-size: 2.5rem;
  font-weight: 700;
  line-height: 1.2;
  margin: 20px 0;
  color: #1a1a1a;
}

.article-tags {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.tag-item {
  color: white;
  padding: 6px 12px;
  border-radius: 16px;
  font-size: 0.85rem;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 4px;
  transition: transform 0.3s ease;
}

.tag-item:hover {
  transform: translateY(-2px);
}

.article-content {
  margin-bottom: 40px;
}

.article-cover {
  margin-bottom: 30px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.article-cover img {
  width: 100%;
  height: auto;
  display: block;
}

.content-body {
  line-height: 1.8;
  color: #333;
  font-size: 1.1rem;
}

.content-body :deep(h1),
.content-body :deep(h2),
.content-body :deep(h3),
.content-body :deep(h4),
.content-body :deep(h5),
.content-body :deep(h6) {
  margin-top: 1.5em;
  margin-bottom: 0.5em;
  font-weight: 600;
  color: #1a1a1a;
}

.content-body :deep(p) {
  margin-bottom: 1em;
}

.content-body :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 6px;
  margin: 1em 0;
}

.content-body :deep(pre) {
  background: #f6f8fa;
  border-radius: 6px;
  padding: 16px;
  overflow-x: auto;
  margin: 1em 0;
}

.content-body :deep(code) {
  background: #f6f8fa;
  padding: 2px 4px;
  border-radius: 3px;
  font-size: 0.9em;
}

.article-actions {
  border-top: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;
  padding: 20px 0;
  margin-bottom: 40px;
}

.action-buttons {
  display: flex;
  gap: 15px;
  justify-content: center;
  flex-wrap: wrap;
}

.article-navigation {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 40px;
  flex-wrap: wrap;
}

.nav-item {
  flex: 1;
  min-width: 200px;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  text-decoration: none;
  color: #333;
  transition: all 0.3s ease;
  border: 1px solid #e9ecef;
}

.nav-link:hover {
  background: #e9ecef;
  transform: translateX(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.nav-item.next-article .nav-link:hover {
  transform: translateX(2px);
}

.nav-arrow {
  font-size: 1.5rem;
  color: #409eff;
}

.nav-content {
  flex: 1;
}

.nav-label {
  font-size: 0.8rem;
  color: #999;
  margin-bottom: 4px;
}

.nav-title {
  font-weight: 600;
  line-height: 1.3;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.related-articles {
  border-top: 1px solid #f0f0f0;
  padding-top: 40px;
  margin-bottom: 40px;
}

.section-title {
  font-size: 1.5rem;
  margin-bottom: 20px;
  color: #1a1a1a;
  display: flex;
  align-items: center;
  gap: 8px;
}

.related-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.related-item {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.related-item:hover {
  transform: translateY(-4px);
}

.related-link {
  display: flex;
  text-decoration: none;
  color: #333;
}

.related-cover {
  width: 100px;
  height: 70px;
  flex-shrink: 0;
  overflow: hidden;
}

.related-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.related-info {
  padding: 12px 15px;
  flex: 1;
}

.related-title {
  font-size: 0.9rem;
  font-weight: 600;
  line-height: 1.3;
  margin-bottom: 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.related-meta {
  display: flex;
  gap: 10px;
  font-size: 0.8rem;
  color: #999;
}

.comment-section {
  border-top: 1px solid #f0f0f0;
  padding-top: 40px;
}

.comment-list {
  margin-bottom: 40px;
}

.comment-item {
  display: flex;
  gap: 15px;
  padding: 20px 0;
  border-bottom: 1px solid #f0f0f0;
}

.comment-avatar {
  flex-shrink: 0;
}

.comment-avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.comment-author {
  font-weight: 600;
  color: #1a1a1a;
}

.comment-time {
  font-size: 0.8rem;
  color: #999;
}

.comment-text {
  line-height: 1.6;
  margin-bottom: 10px;
  color: #333;
}

.comment-actions {
  display: flex;
  gap: 10px;
}

.comment-replies {
  margin-top: 15px;
  padding-left: 20px;
  border-left: 2px solid #f0f0f0;
}

.reply-item {
  display: flex;
  gap: 10px;
  margin-bottom: 12px;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 6px;
}

.reply-avatar img {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

.reply-content {
  flex: 1;
}

.reply-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
}

.reply-author {
  font-weight: 500;
  font-size: 0.9rem;
  color: #1a1a1a;
}

.reply-time {
  font-size: 0.75rem;
  color: #999;
}

.reply-text {
  font-size: 0.9rem;
  line-height: 1.5;
  color: #333;
}

.comment-form {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
}

.form-header h4 {
  margin: 0 0 20px 0;
  color: #1a1a1a;
}

.loading-container {
  padding: 100px 20px;
  text-align: center;
}

.loading-skeleton {
  max-width: 800px;
  margin: 0 auto;
}

.article-skeleton {
  width: 100%;
}

.skeleton-content {
  padding: 20px;
}

.not-found-container {
  padding: 100px 20px;
  text-align: center;
}

.not-found-content {
  max-width: 400px;
  margin: 0 auto;
}

.not-found-icon {
  font-size: 4rem;
  color: #c0c4cc;
  margin-bottom: 20px;
}

.not-found-content h2 {
  margin: 0 0 10px 0;
  color: #303133;
}

.back-home-btn {
  margin-top: 20px;
  text-decoration: none;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .article-detail {
    margin: 10px;
    padding: 20px 15px;
    border-radius: 6px;
  }

  .article-title {
    font-size: 1.8rem;
  }

  .meta-info {
    font-size: 0.85rem;
    gap: 15px;
  }

  .article-navigation {
    flex-direction: column;
    gap: 15px;
  }

  .related-list {
    grid-template-columns: 1fr;
  }

  .action-buttons {
    flex-direction: column;
    align-items: stretch;
  }

  .action-buttons .el-button {
    width: 100%;
  }

  .comment-item {
    flex-direction: column;
    gap: 10px;
  }
}
</style>