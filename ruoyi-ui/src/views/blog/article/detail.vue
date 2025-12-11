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
      <!-- 文章头部 -->
      <div class="article-header">
        <!-- 分类标签 -->
        <div class="article-category" v-if="article.categoryName">
          <span class="category-badge">{{ article.categoryName }}</span>
        </div>

        <!-- 文章标题 -->
        <h1 class="article-title" v-animate="'fade-in-up'">{{ article.title }}</h1>

        <!-- 文章元信息 -->
        <div class="article-meta" v-animate="'fade-in-up'">
          <div class="meta-info">
            <span class="meta-item">
              <i class="el-icon-user"></i>
              <span>{{ article.authorName || article.author || '匿名作者' }}</span>
            </span>
            <span class="meta-item">
              <i class="el-icon-date"></i>
              <time :datetime="article.createTime">{{ formatDate(article.createTime) }}</time>
            </span>
            <span class="meta-item">
              <i class="el-icon-view"></i>
              <span>{{ article.viewCount || 0 }} 阅读</span>
            </span>
            <span class="meta-item" v-if="article.likeCount">
              <i class="el-icon-star-off"></i>
              <span>{{ article.likeCount }} 点赞</span>
            </span>
            <span class="meta-item" v-if="article.commentCount">
              <i class="el-icon-chat-line-round"></i>
              <span>{{ article.commentCount }} 评论</span>
            </span>
          </div>
        </div>

        <!-- 文章标签 -->
        <div class="article-tags" v-if="article.tags && article.tags.length" v-animate="'fade-in-up'">
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
          <router-link :to="{ name: 'PublicBlogArticleDetail', params: { id: (prevArticle.id ?? prevArticle.articleId ?? prevArticle.uuid) } }" class="nav-link">
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
          <router-link :to="{ name: 'PublicBlogArticleDetail', params: { id: (nextArticle.id ?? nextArticle.articleId ?? nextArticle.uuid) } }" class="nav-link">
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
            <router-link :to="{ name: 'PublicBlogArticleDetail', params: { id: (related.id ?? related.articleId ?? related.uuid) } }" class="related-link">
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
                <el-button size="mini" @click="handleReply(comment)" type="link">回复</el-button>
                <el-button size="mini" @click="handleLikeComment(comment)" type="link">
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

    <!-- 博客底部 -->
    <BlogFooter
      :blogSettings="blogSettings"
      :totalArticles="0"
      :categoryCount="0"
      :tagCount="0"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import useUserStore from '@/store/modules/user'
import { ElMessage } from 'element-plus'
import BlogNav from '@/components/BlogNav.vue'
import BlogFooter from '@/components/BlogFooter.vue'
import { getArticleDetail, getArticleComments, submitComment as apiSubmitComment } from '@/api/blog/article'
import { getBlogSettings, getBlogSettingsAnonymous } from '@/api/blog/setting'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

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
const isLoggedIn = ref(false)
const blogSettings = ref({})

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
    console.log('准备请求文章详情，ID:', articleId)
    
    // 获取文章详情
    const response = await getArticleDetail(articleId)
    console.log('文章详情响应:', response)
    
    // 正确解析API响应数据结构
    if (response.code === 200 && response.data) {
      // 提取文章主体数据
      article.value = response.data.article || null
      
      // 提取上下篇文章数据
      if (response.data.extraInfo) {
        prevArticle.value = response.data.extraInfo.prevArticle || null
        nextArticle.value = response.data.extraInfo.nextArticle || null

        // 设置分类名称
        if (response.data.extraInfo.category && response.data.extraInfo.category.name) {
          article.value.categoryName = response.data.extraInfo.category.name
        }
      }

    
      // 获取相关文章 - 暂时注释掉，因为后端API不存在
      // if (article.value) {
      //   const relatedResponse = await getRelatedArticles(articleId, { pageNum: 1, pageSize: 6 })
      //   relatedArticles.value = relatedResponse.rows || []
      // }
      
      // 暂时设置为空数组，避免页面显示问题
      relatedArticles.value = []
      
      // 获取评论列表
      await loadComments()
    } else {
      console.error('获取文章详情失败: 响应数据格式不正确')
      console.error('响应码:', response.code)
      console.error('响应消息:', response.msg)
      ElMessage.error(response.msg || '获取文章详情失败')
    }

  } catch (error) {
    console.error('获取文章详情失败，详细错误:', error)
    console.error('错误类型:', typeof error)
    console.error('错误状态:', error.response?.status)
    console.error('错误状态文本:', error.response?.statusText)
    console.error('请求URL:', error.config?.url)
    console.error('请求方法:', error.config?.method)
    
    // 更详细的错误提示
    const errorMsg = error.response?.status === 404 
      ? `系统接口404异常，请求路径: ${error.config?.url}` 
      : `获取文章详情失败: ${error.message || '未知错误'}`
    
    ElMessage.error(errorMsg)
  } finally {
    loading.value = false
  }
}

// 获取评论列表
const loadComments = async () => {
  try {
    const articleId = route.params.id
    const response = await getArticleComments(articleId)
    console.log('评论列表响应:', response)

    // 处理响应数据格式
    let comments = []
    if (response && response.code === 200) {
      comments = response.data || []
    } else if (response && Array.isArray(response)) {
      comments = response
    }

    commentList.value = comments
    totalComments.value = comments.length
  } catch (error) {
    console.error('获取评论列表失败:', error)
    commentList.value = []
    totalComments.value = 0
  }
}

// 点赞文章
const handleLike = async () => {
  if (likeLoading.value) return

  try {
    if (!isLoggedIn.value) {
      ElMessage.info('请先登录后再进行点赞')
      router.push(`/login?redirect=${encodeURIComponent(route.fullPath)}`)
      return
    }
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
  if (!isLoggedIn.value) {
    ElMessage.info('请先登录后再进行收藏')
    router.push(`/login?redirect=${encodeURIComponent(route.fullPath)}`)
    return
  }
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
    if (!isLoggedIn.value) {
      ElMessage.info('请先登录后再发表评论')
      router.push(`/login?redirect=${encodeURIComponent(route.fullPath)}`)
      return
    }
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

// 获取博客设置
const loadBlogSettings = async () => {
  try {
    let response;
    try {
      response = await getBlogSettings()
    } catch (error) {
      console.warn('标准博客设置接口访问失败，尝试匿名接口:', error)
      response = await getBlogSettingsAnonymous()
    }

    if (response && response.code === 200) {
      blogSettings.value = response.data || {}
    } else if (response && typeof response === 'object') {
      blogSettings.value = response
    }
  } catch (error) {
    console.error('获取博客设置失败:', error)
    blogSettings.value = {}
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadArticleDetail()
  loadComments()
  loadBlogSettings()
  isLoggedIn.value = !!userStore.token
})
</script>

<style scoped>
.article-detail-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.article-detail {
  max-width: 850px;
  margin: 0 auto;
  padding: 0;
  background: white;
  border-radius: 20px;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.08);
  margin-top: 20px;
  margin-bottom: 40px;
  overflow: hidden;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.article-header {
  background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  padding: 40px 50px 30px;
  position: relative;
  overflow: hidden;
}

.article-header::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -20%;
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(64, 158, 255, 0.05), transparent 70%);
  border-radius: 50%;
}

.article-category {
  margin-bottom: 20px;
  position: relative;
  z-index: 1;
}

.category-badge {
  background: linear-gradient(135deg, #409eff, #337ecc);
  color: white;
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 600;
  box-shadow: 0 4px 15px rgba(64, 158, 255, 0.3);
  display: inline-block;
  animation: categoryPulse 2s ease-in-out infinite alternate;
}

@keyframes categoryPulse {
  from { transform: scale(1); }
  to { transform: scale(1.05); }
}

.article-meta {
  margin-bottom: 25px;
  position: relative;
  z-index: 1;
}

.meta-info {
  display: flex;
  align-items: center;
  gap: 25px;
  flex-wrap: wrap;
  color: #666;
  font-size: 0.95rem;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  background: rgba(0, 0, 0, 0.03);
  padding: 6px 12px;
  border-radius: 15px;
  transition: all 0.3s ease;
}

.meta-item:hover {
  background: rgba(64, 158, 255, 0.1);
  color: #409eff;
  transform: translateY(-2px);
}

.meta-item i {
  font-size: 1rem;
  opacity: 0.8;
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
  font-size: 2.8rem;
  font-weight: 700;
  line-height: 1.3;
  margin: 25px 0;
  color: #1a1a1a;
  position: relative;
  z-index: 1;
  background: linear-gradient(135deg, #1a1a1a, #333);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.article-tags {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  margin-top: 25px;
  position: relative;
  z-index: 1;
}

.tag-item {
  color: white;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.tag-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s ease;
}

.tag-item:hover {
  transform: translateY(-3px) scale(1.05);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
}

.tag-item:hover::before {
  left: 100%;
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
  font-size: 1.05rem;
  padding: 0 50px 50px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;
  text-rendering: optimizeLegibility;
  -webkit-font-smoothing: antialiased;
}

/* 文章标题样式 */
.content-body :deep(h1),
.content-body :deep(h2),
.content-body :deep(h3),
.content-body :deep(h4),
.content-body :deep(h5),
.content-body :deep(h6) {
  margin-top: 2em;
  margin-bottom: 1em;
  font-weight: 700;
  color: #1a1a1a;
  position: relative;
}

.content-body :deep(h1) {
  font-size: 2em;
  border-bottom: 3px solid #409eff;
  padding-bottom: 10px;
  margin-top: 0;
}

.content-body :deep(h2) {
  font-size: 1.75em;
  border-left: 5px solid #409eff;
  padding-left: 15px;
  margin-top: 2.5em;
}

.content-body :deep(h3) {
  font-size: 1.5em;
  color: #333;
}

.content-body :deep(h4) {
  font-size: 1.25em;
  color: #444;
}

/* 段落样式 */
.content-body :deep(p) {
  margin-bottom: 1.2em;
  text-align: justify;
  word-wrap: break-word;
}

/* 图片样式 */
.content-body :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 12px;
  margin: 1.5em auto;
  display: block;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.content-body :deep(img):hover {
  transform: scale(1.02);
}

/* 代码块样式 */
.content-body :deep(pre) {
  background: linear-gradient(135deg, #282c34, #21252b);
  border-radius: 12px;
  padding: 20px;
  overflow-x: auto;
  margin: 1.5em 0;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  position: relative;
}

.content-body :deep(pre)::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 30px;
  background: linear-gradient(to right, #ff5f56, #ffbd2e, #27ca3f);
  border-radius: 12px 12px 0 0;
}

.content-body :deep(pre) code {
  background: none;
  padding: 0;
  color: #abb2bf;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 0.95rem;
  line-height: 1.5;
  white-space: pre;
}

/* 行内代码样式 */
.content-body :deep(code) {
  background: linear-gradient(135deg, #f6f8fa, #e9ecef);
  color: #e83e8c;
  padding: 3px 8px;
  border-radius: 6px;
  font-size: 0.9em;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  border: 1px solid #e9ecef;
  font-weight: 500;
}

/* 引用块样式 */
.content-body :deep(blockquote) {
  border-left: 5px solid #409eff;
  margin: 1.5em 0;
  padding: 15px 25px;
  background: linear-gradient(135deg, #f8f9fa, #ffffff);
  border-radius: 0 12px 12px 0;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.08);
  position: relative;
}

.content-body :deep(blockquote)::before {
  content: '"';
  position: absolute;
  top: -10px;
  left: 15px;
  font-size: 3em;
  color: #409eff;
  opacity: 0.3;
  font-family: Georgia, serif;
}

/* 表格样式 */
.content-body :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 1.5em 0;
  font-size: 0.95em;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
}

.content-body :deep(th),
.content-body :deep(td) {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #e9ecef;
}

.content-body :deep(th) {
  background: linear-gradient(135deg, #409eff, #337ecc);
  color: white;
  font-weight: 600;
}

.content-body :deep(tr:nth-child(even)) {
  background: #f8f9fa;
}

.content-body :deep(tr:hover) {
  background: rgba(64, 158, 255, 0.05);
}

/* 列表样式 */
.content-body :deep(ul),
.content-body :deep(ol) {
  margin: 1.2em 0;
  padding-left: 30px;
}

.content-body :deep(li) {
  margin-bottom: 0.5em;
  line-height: 1.6;
}

/* 链接样式 */
.content-body :deep(a) {
  color: #409eff;
  text-decoration: none;
  position: relative;
  transition: color 0.3s ease;
}

.content-body :deep(a)::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, #409eff, #337ecc);
  transition: width 0.3s ease;
}

.content-body :deep(a):hover {
  color: #337ecc;
}

.content-body :deep(a):hover::after {
  width: 100%;
}

/* 分隔线样式 */
.content-body :deep(hr) {
  border: none;
  height: 2px;
  background: linear-gradient(90deg, transparent, #409eff, transparent);
  margin: 3em 0;
  opacity: 0.5;
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

/* 移动端响应式优化 */
@media (max-width: 768px) {
  .article-detail {
    margin: 10px;
    border-radius: 15px;
  }

  .article-header {
    padding: 30px 25px 25px;
  }

  .article-title {
    font-size: 2rem;
    line-height: 1.3;
    margin: 20px 0;
  }

  .content-body {
    padding: 0 25px 40px;
    font-size: 1rem;
  }

  .content-body :deep(h1) {
    font-size: 1.75em;
  }

  .content-body :deep(h2) {
    font-size: 1.5em;
  }

  .content-body :deep(h3) {
    font-size: 1.3em;
  }

  .content-body :deep(pre) {
    padding: 15px;
    border-radius: 8px;
    font-size: 0.85rem;
  }

  .meta-info {
    gap: 15px;
    font-size: 0.85rem;
  }

  .meta-item {
    padding: 4px 8px;
    font-size: 0.8rem;
  }

  .tag-item {
    padding: 6px 12px;
    font-size: 0.8rem;
  }

  .article-actions {
    padding: 20px 25px;
    flex-direction: column;
    gap: 15px;
  }

  .action-buttons {
    justify-content: center;
    flex-wrap: wrap;
  }

  .article-navigation {
    padding: 20px 25px;
    flex-direction: column;
    gap: 15px;
  }

  .nav-item {
    width: 100%;
  }

  .nav-link {
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    gap: 15px;
    padding: 15px;
  }

  .related-articles {
    padding: 30px 25px;
  }

  .related-list {
    grid-template-columns: 1fr;
    gap: 15px;
  }

  .comment-section {
    padding: 30px 25px;
  }

  .comment-item {
    gap: 12px;
    padding: 15px 0;
  }
}

@media (max-width: 480px) {
  .article-title {
    font-size: 1.75rem;
  }

  .content-body {
    padding: 0 20px 30px;
    font-size: 0.95rem;
  }

  .article-header {
    padding: 25px 20px 20px;
  }

  .meta-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .content-body :deep(pre) {
    padding: 12px;
    font-size: 0.8rem;
  }

  .content-body :deep(table) {
    font-size: 0.85rem;
  }

  .content-body :deep(th),
  .content-body :deep(td) {
    padding: 8px 10px;
  }

  .action-buttons .el-button {
    padding: 8px 15px;
    font-size: 0.85rem;
  }

  .tag-item {
    padding: 4px 8px;
    font-size: 0.75rem;
  }
}
</style>