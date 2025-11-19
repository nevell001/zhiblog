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
            <i class="el-icon-user"></i>
            作者：{{ article.author || article.authorName || '未知作者' }}
          </span>
          <span class="meta-item">
            <i class="el-icon-view"></i>
            {{ article.viewCount || 0 }} 阅读
          </span>
          <span class="meta-item" v-if="article.categoryName || category">
            <i class="el-icon-folder"></i>
            分类：{{ article.categoryName || category.name }}
          </span>
          <div class="article-stats">
            <span class="stat-item">
              <i class="el-icon-star-off"></i>
              点赞：{{ article.likeCount || 0 }}
            </span>
            <span class="stat-item">
              <i class="el-icon-chat-line-round"></i>
              评论：{{ article.commentCount || 0 }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 文章封面 - 支持coverUrl和coverImage -->
    <div class="article-cover" v-if="article.coverUrl || article.coverImage">
      <img :src="article.coverUrl || article.coverImage" :alt="article.title" loading="lazy" />
      <div class="article-cover-overlay">
        <div class="article-category-tag" v-if="article.categoryName || category">
          <i class="el-icon-folder"></i>
          {{ article.categoryName || category.name }}
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

    <!-- 文章摘要 - 新增显示摘要 -->
    <div v-if="article.summary" class="article-summary">
      <div class="summary-label">摘要：</div>
      <div class="summary-content">{{ article.summary }}</div>
    </div>

    <!-- 文章内容 -->
    <div class="article-content">
      <div v-if="article.content && article.content !== '暂无内容'" class="content-wrapper" v-html="article.content"></div>
      <div v-else class="content-wrapper-empty">
        <el-empty description="暂无文章内容" />
      </div>
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
  import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getArticleDetail, updateArticleViewCount } from '@/api/blog/article'
import { getArticleComments, addBlogComment } from '@/api/blog/comment'
import BlogNav from '@/components/BlogNav.vue'

const route = useRoute()
const articleId = route.params.id

// 响应式数据
const article = ref({
  title: '',
  content: '',
  author: '',
  authorName: '',
  createTime: '',
  viewCount: 0,
  likeCount: 0,
  commentCount: 0,
  coverUrl: '',
  coverImage: '',
  summary: '',
  tags: []
})
const comments = ref([])
const prevArticle = ref(null)
const nextArticle = ref(null)
const commentContent = ref('')
const submittingComment = ref(false)
const replyTo = ref(null)
const category = ref(null)

// 获取文章详情
const loadArticleDetail = async () => {
  try {
    // 添加加载中消息，提升用户体验
    ElMessage.loading({
      message: '加载文章中...',
      duration: 0,
      loadingType: 'spinner'
    })
    
    const response = await getArticleDetail(articleId)
    
    // 关闭加载提示
    ElMessage.closeAll()
    
    if (response && response.code === 200 && response.data) {
      const data = response.data || {}
      
      // 确保文章数据完整，设置默认值避免显示问题
      const articleData = data.article || {}
      article.value = {
        id: articleData.id || 0,
        title: articleData.title || '无标题文章',
        content: articleData.content || '暂无内容',
        summary: articleData.summary || '',
        coverUrl: articleData.coverUrl || articleData.coverImage || '',
        coverImage: articleData.coverImage || articleData.coverUrl || '',
        categoryId: articleData.categoryId || 0,
        authorId: articleData.authorId || 0,
        author: articleData.author || articleData.authorName || '未知作者',
        authorName: articleData.authorName || articleData.author || '未知作者',
        isTop: articleData.isTop || 0,
        isRecommend: articleData.isRecommend || 0,
        status: articleData.status || 0,
        viewCount: articleData.viewCount || 0,
        likeCount: articleData.likeCount || 0,
        commentCount: articleData.commentCount || 0,
        createTime: articleData.createTime || new Date().toISOString(),
        updateTime: articleData.updateTime || '',
        delFlag: articleData.delFlag || 0,
        tags: articleData.tags || []
      }
      
      // 设置上下篇文章数据，确保字段安全访问
      if (data.extraInfo) {
        const prev = data.extraInfo.prevArticle
        const next = data.extraInfo.nextArticle
        const catInfo = data.extraInfo.category
        
        prevArticle.value = prev ? {
          id: prev.id || 0,
          title: prev.title || '上一篇'
        } : null
        
        nextArticle.value = next ? {
          id: next.id || 0,
          title: next.title || '下一篇'
        } : null
        
        // 设置分类信息
        if (catInfo) {
          category.value = catInfo
        }
      }
      
      // 更新阅读量（异步执行，不阻塞页面显示）
      updateArticleViewCount(articleId).catch(err => {
        console.warn('更新阅读量失败:', err)
      })
    } else {
      ElMessage.error(response?.msg || '获取文章详情失败')
      // 设置默认文章数据，避免页面空白
      article.value.title = '文章加载失败'
      article.value.content = '抱歉，无法加载文章内容，请稍后再试。'
    }
  } catch (error) {
    ElMessage.closeAll()
    console.error('获取文章详情失败:', error)
    ElMessage.error('加载文章失败，请检查网络连接或稍后再试')
    // 设置默认文章数据，避免页面空白
    article.value.title = '文章加载失败'
    article.value.content = '抱歉，无法加载文章内容，请稍后再试。'
  }
}

// 获取评论列表 - 优化错误处理和加载逻辑
  const loadComments = async () => {
    try {
      const response = await getArticleComments(articleId, {
        pageNum: 1,
        pageSize: 100
      })
      
      // 安全检查并过滤无效评论
      comments.value = response?.data?.rows?.filter(comment => comment && comment.id) || response.rows || []
    } catch (error) {
      console.error('获取评论失败:', error)
      ElMessage.warning('获取评论列表失败，显示本地评论数据')
      // 保持评论数组为空，避免显示错误数据
      comments.value = []
    }
  }

  // 处理图片加载失败 - 为文章内容中的图片添加错误处理
  const handleImageError = (event) => {
    const img = event.target
    img.onerror = null // 防止无限循环
    img.src = '/ruoyi-ui/images/error/image-error.png' // 设置默认错误图片
    img.alt = '图片加载失败'
    img.style.maxWidth = '100%'
    img.style.height = 'auto'
  }

  // 监听文章内容变化，为图片添加错误处理
  const setupImageErrorHandlers = async () => {
    await nextTick()
    const contentImages = document.querySelectorAll('.article-content img')
    contentImages.forEach(img => {
      img.onerror = handleImageError
      // 添加懒加载和响应式处理
      img.loading = 'lazy'
      img.style.maxWidth = '100%'
      img.style.height = 'auto'
      // 确保图片有alt属性
      if (!img.alt) {
        img.alt = '文章图片'
      }
    })
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

// 格式化日期 - 更强大的日期格式化函数
  const formatDate = (dateString) => {
    if (!dateString) return '未知日期'
    
    try {
      const date = new Date(dateString)
      
      // 检查是否为有效日期
      if (isNaN(date.getTime())) {
        return '日期格式错误'
      }
      
      const year = date.getFullYear()
      const month = (date.getMonth() + 1).toString().padStart(2, '0')
      const day = date.getDate().toString().padStart(2, '0')
      const hours = date.getHours().toString().padStart(2, '0')
      const minutes = date.getMinutes().toString().padStart(2, '0')
      
      // 返回格式：YYYY-MM-DD HH:mm
      return `${year}-${month}-${day} ${hours}:${minutes}`
    } catch (error) {
      console.warn('日期格式化错误:', error)
      return '日期格式错误'
    }
  }

  // 获取分类名称 - 支持从category对象获取分类名称
  const getCategoryName = () => {
    if (article.value.categoryName) return article.value.categoryName
    if (category.value && category.value.name) return category.value.name
    return '未分类'
  }

// 组件挂载时加载数据
onMounted(async () => {
  // 加载文章详情
  await loadArticleDetail()
  
  // 文章内容加载完成后，设置图片错误处理
  await setupImageErrorHandlers()
  
  // 加载评论（异步执行，不阻塞页面显示）
  loadComments().catch(err => {
    console.warn('评论加载失败:', err)
  })
  
  // 监听窗口大小变化，确保响应式布局
  window.addEventListener('resize', setupImageErrorHandlers)
})

// 组件卸载时清理事件监听器
onBeforeUnmount(() => {
  window.removeEventListener('resize', setupImageErrorHandlers)
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