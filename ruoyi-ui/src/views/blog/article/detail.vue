<template>
  <div class="article-detail-container">
    <!-- 博客导航 -->
    <BlogLayout>
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <div class="loading-skeleton">
          <el-skeleton :loading="loading" animated class="article-skeleton">
            <template #template>
              <div class="skeleton-content">
                <el-skeleton-item variant="h1" style="width: 60%; margin-bottom: 20px" />
                <el-skeleton-item variant="text" style="width: 100%; margin-bottom: 10px" />
                <el-skeleton-item variant="text" style="width: 90%; margin-bottom: 10px" />
                <el-skeleton-item variant="text" style="width: 80%; margin-bottom: 20px" />
                <el-skeleton-item
                  variant="rect"
                  style="width: 100%; height: 400px; margin-bottom: 20px"
                />
                <el-skeleton-item
                  variant="text"
                  style="width: 100%; height: 20px; margin-bottom: 8px"
                />
                <el-skeleton-item
                  variant="text"
                  style="width: 95%; height: 20px; margin-bottom: 8px"
                />
                <el-skeleton-item
                  variant="text"
                  style="width: 85%; height: 20px; margin-bottom: 8px"
                />
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

      <div v-else class="mo-article-page">
        <header class="mo-article-header">
          <span v-if="article.categoryName" class="cat-badge">{{ article.categoryName }}</span>
          <h1>{{ article.title }}</h1>
          <div class="a-meta">
            <span class="author">{{ article.authorName || article.author || '匿名作者' }}</span>
            <span>·</span>
            <time :datetime="article.createTime">{{ formatDate(article.createTime) }}</time>
            <span v-if="isFeatureEnabled('view_count_enabled')">·</span>
            <span v-if="isFeatureEnabled('view_count_enabled')">
              👁 {{ article.viewCount || 0 }} 阅读
            </span>
            <span>·</span>
            <span>⏱ 约 {{ readingMinutes }} 分钟</span>
          </div>
        </header>

        <main class="body-layout">
          <aside class="toc">
            <ArticleTOC
              v-if="article && article.content"
              :content="article.content"
              @toc-ready="handleTOCReady"
            />
          </aside>

          <article class="article-content">
            <div v-if="article.coverUrl" class="article-cover">
              <img :src="article.coverUrl" :alt="article.title" />
            </div>
            <div class="content-body" v-html="processedContent"></div>

            <div class="article-actions">
              <el-button
                v-if="isFeatureEnabled('like_enabled')"
                :loading="likeLoading"
                :type="article.isLiked ? 'success' : 'primary'"
                plain
                @click="handleLike"
              >
                👍 点赞 {{ article.likeCount || 0 }}
              </el-button>
              <el-button v-if="isFeatureEnabled('share_enabled')" plain @click="handleShare">
                分享
              </el-button>
              <el-button
                :type="article.isBookmarked ? 'warning' : 'default'"
                plain
                @click="handleBookmark"
              >
                {{ article.isBookmarked ? '已收藏' : '收藏' }}
              </el-button>
            </div>

            <div v-if="prevArticle || nextArticle" class="article-navigation">
              <router-link
                v-if="prevArticle"
                :to="{
                  name: 'PublicBlogArticleDetail',
                  params: { id: prevArticle.id ?? prevArticle.articleId ?? prevArticle.uuid }
                }"
                class="nav-item"
              >
                <span class="nav-label">上一篇</span>
                <span class="nav-title">{{ prevArticle.title }}</span>
              </router-link>
              <router-link
                v-if="nextArticle"
                :to="{
                  name: 'PublicBlogArticleDetail',
                  params: { id: nextArticle.id ?? nextArticle.articleId ?? nextArticle.uuid }
                }"
                class="nav-item next-article"
              >
                <span class="nav-label">下一篇</span>
                <span class="nav-title">{{ nextArticle.title }}</span>
              </router-link>
            </div>
          </article>

          <aside class="article-side">
            <div class="author-card">
              <div class="a-avatar">{{ authorInitial }}</div>
              <div class="a-name">{{ article.authorName || article.author || '匿名作者' }}</div>
              <div class="a-bio">
                {{ blogSettings.blog_desc || '记录技术、产品与生活里的认真思考。' }}
              </div>
              <button class="btn btn-primary btn-sm" type="button">+ 关注</button>
              <div class="a-stats">
                <div class="a-stat">
                  <div class="num">{{ relatedArticles.length || 0 }}</div>
                  <div class="lbl">相关</div>
                </div>
                <div class="a-stat">
                  <div class="num">{{ article.likeCount || 0 }}</div>
                  <div class="lbl">获赞</div>
                </div>
                <div class="a-stat">
                  <div class="num">{{ article.commentCount || totalComments || 0 }}</div>
                  <div class="lbl">评论</div>
                </div>
              </div>
            </div>

            <div v-if="article.tags && article.tags.length" class="side-widget">
              <div class="wt">文章标签</div>
              <div class="tag-cloud">
                <span v-for="tag in article.tags" :key="tag.id" class="tc">{{ tag.name }}</span>
              </div>
            </div>

            <div v-if="relatedArticles.length > 0" class="side-widget">
              <div class="wt">相关推荐</div>
              <div class="related-mini-list">
                <router-link
                  v-for="related in relatedArticles.slice(0, 4)"
                  :key="related.id"
                  :to="{
                    name: 'PublicBlogArticleDetail',
                    params: { id: related.id ?? related.articleId ?? related.uuid }
                  }"
                  class="related-mini"
                >
                  {{ related.title }}
                </router-link>
              </div>
            </div>
          </aside>
        </main>

        <section v-if="isFeatureEnabled('comment_enabled')" class="comment-section">
          <h3>💬 评论 ({{ totalComments }})</h3>

          <div class="comment-input">
            <el-form
              ref="commentFormRef"
              :model="commentForm"
              :rules="commentRules"
              label-width="0"
            >
              <div v-if="!isLoggedIn" class="guest-fields">
                <el-form-item prop="nickname">
                  <el-input v-model="commentForm.nickname" placeholder="昵称" />
                </el-form-item>
                <el-form-item prop="email">
                  <el-input v-model="commentForm.email" placeholder="邮箱（可选）" />
                </el-form-item>
              </div>
              <el-form-item prop="content">
                <el-input
                  v-model="commentForm.content"
                  type="textarea"
                  :placeholder="
                    replyTarget
                      ? `回复 ${replyTarget.nickname}...`
                      : '写下你的想法... 支持 Markdown 语法'
                  "
                  :rows="3"
                  maxlength="500"
                  show-word-limit
                />
              </el-form-item>
              <div class="actions">
                <span class="md-hint">支持 Markdown · Ctrl/⌘ + Enter 发送</span>
                <el-button v-if="replyTarget" plain @click="cancelReply">取消回复</el-button>
                <el-button type="primary" :loading="commentSubmitting" @click="submitComment">
                  发表评论
                </el-button>
              </div>
            </el-form>
          </div>

          <div v-if="commentList.length > 0" class="comment-list">
            <div v-for="comment in commentList" :key="comment.id" class="comment-item">
              <div class="c-avatar">{{ (comment.nickname || '匿').charAt(0) }}</div>
              <div class="c-body">
                <div class="c-head">
                  <span class="c-name">{{ comment.nickname || '匿名' }}</span>
                  <span class="c-time">{{ formatDate(comment.createTime) }}</span>
                </div>
                <div class="c-text">{{ comment.content }}</div>
                <div class="c-actions">
                  <span @click="handleLikeComment(comment)">👍 {{ comment.likeCount || 0 }}</span>
                  <span @click="handleReply(comment)">💬 回复</span>
                </div>

                <div v-if="comment.replies && comment.replies.length > 0" class="c-reply">
                  <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
                    <div class="c-avatar small">{{ (reply.nickname || '匿').charAt(0) }}</div>
                    <div class="c-body">
                      <div class="c-head">
                        <span class="c-name">{{ reply.nickname || '匿名' }}</span>
                        <span class="c-time">{{ formatDate(reply.createTime) }}</span>
                      </div>
                      <div class="c-text">{{ reply.content }}</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
      </div>

      <!-- 博客底部 -->
    </BlogLayout>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted, watch, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useBlogSettingsStore } from '@/stores/blogSettings'
import { ElMessage } from 'element-plus'
import BlogLayout from '@/components/BlogLayout.vue'
import ArticleTOC from '@/components/ArticleTOC.vue'
import { getArticleDetail, getRelatedArticles } from '@/api/blog/article'
import { likeArticle } from '@/api/admin/blog/article'
import { toggleBookmark } from '@/api/blog/bookmark'

import { getArticleComments, addBlogComment as apiSubmitComment } from '@/api/blog/comment'
import { getBlogSettings, getBlogSettingsAnonymous } from '@/api/blog/setting'
import { sanitizeArticleContent } from '@/utils/sanitize'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const blogSettingsStore = useBlogSettingsStore()

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
const blogSettings = computed(() => blogSettingsStore.blogSettings)
const isFeatureEnabled = (feature: string) => blogSettingsStore.isFeatureEnabled(feature)
const tocItems = ref([])

// 处理文章内容，为标题添加ID并消毒内容
const processedContent = computed(() => {
  if (!article.value || !article.value.content) return ''

  // 首先消毒HTML内容，防止XSS攻击
  const sanitizedContent = sanitizeArticleContent(article.value.content)

  // 然后为标题添加ID，用于目录导航
  const tempDiv = document.createElement('div')
  tempDiv.innerHTML = sanitizedContent

  const headingElements = tempDiv.querySelectorAll('h1, h2, h3, h4, h5, h6')
  const idSet = new Set()

  headingElements.forEach((heading, index) => {
    let id = `heading-${index}`
    if (idSet.has(id)) {
      let counter = 1
      while (idSet.has(`${id}-${counter}`)) {
        counter++
      }
      id = `${id}-${counter}`
    }
    idSet.add(id)
    heading.id = id
  })

  return tempDiv.innerHTML
})

const readingMinutes = computed(() => {
  const text = article.value?.content?.replace(/<[^>]+>/g, '') || ''
  return Math.max(1, Math.ceil(text.length / 500))
})

const authorInitial = computed(() => {
  const name = article.value?.authorName || article.value?.author || '匿'
  return name.charAt(0)
})

// 处理目录就绪事件
const handleTOCReady = items => {
  tocItems.value = items
}

// 评论表单
const commentForm = reactive({
  nickname: '',
  email: '',
  content: '',
  parentId: null as number | null
})

// 回复目标评论
const replyTarget = ref<{ id: number; nickname: string } | null>(null)

const commentRules = {
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  content: [
    { required: true, message: '请输入评论内容', trigger: 'blur' },
    { min: 1, max: 500, message: '评论内容长度在1到500个字符', trigger: 'blur' }
  ]
}

const commentFormRef = ref<any>(null)

// 获取文章详情
const loadArticleDetail = async () => {
  try {
    loading.value = true
    const articleId = Number(Array.isArray(route.params.id) ? route.params.id[0] : route.params.id)

    // 验证文章ID
    if (!articleId) {
      console.error('文章ID为空')
      ElMessage.error('文章ID不能为空')
      return
    }

    // 确保ID是数字
    const numericId = Number(articleId)
    if (isNaN(numericId) || numericId <= 0) {
      console.error('文章ID格式不正确:', articleId)
      ElMessage.error('文章ID格式不正确')
      return
    }

    // 获取文章详情
    const response = await getArticleDetail(numericId)

    // 检查响应状态
    if (response.code !== 200) {
      console.error('API调用失败:', response.code, response.msg)
      return
    }

    if (!response.data) {
      console.error('响应数据为空')
      return
    }

    // 正确解析API响应数据结构
    if (response.data.article) {
      // 提取文章主体数据
      article.value = response.data.article

      // 提取上下篇文章数据
      if (response.data.extraInfo) {
        prevArticle.value = response.data.extraInfo.prevArticle || null
        nextArticle.value = response.data.extraInfo.nextArticle || null

        // 设置分类名称
        if (response.data.extraInfo.category && response.data.extraInfo.category.name) {
          article.value.categoryName = response.data.extraInfo.category.name
        }
      }

      // 获取相关文章
      try {
        const relatedResponse = await getRelatedArticles(articleId)
        relatedArticles.value = relatedResponse.data || []
      } catch (e) {
        // 相关文章加载失败不影响主内容
        relatedArticles.value = []
      }

      // 获取评论列表
      await loadComments()
    } else {
      console.error('未找到文章数据，响应数据:', response.data)
      article.value = null
    }
  } catch (error) {
    console.error('获取文章详情失败，详细错误:', error)
    console.error('错误类型:', typeof error)
    console.error('错误状态:', error.response?.status)
    console.error('错误状态文本:', error.response?.statusText)
    console.error('请求URL:', error.config?.url)
    console.error('请求方法:', error.config?.method)

    // 更详细的错误提示
    const errorMsg =
      error.response?.status === 404
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
    const articleId = Number(Array.isArray(route.params.id) ? route.params.id[0] : route.params.id)
    const response = await getArticleComments(articleId)

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

    // 调用点赞API
    await likeArticle(article.value.id)

    article.value.isLiked = true
    article.value.likeCount = (article.value.likeCount || 0) + 1

    ElMessage.success('点赞成功')
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
    navigator.clipboard
      .writeText(url)
      .then(() => {
        ElMessage.success('链接已复制到剪贴板')
      })
      .catch(() => {
        ElMessage.warning('请手动复制链接：' + url)
      })
  }
}

// 收藏文章
const handleBookmark = async () => {
  if (!isLoggedIn.value) {
    ElMessage.info('请先登录后再进行收藏')
    router.push(`/login?redirect=${encodeURIComponent(route.fullPath)}`)
    return
  }

  try {
    const response = await toggleBookmark(article.value.id)
    if (response.code === 200) {
      article.value.isBookmarked = response.data.bookmarked
      ElMessage.success(article.value.isBookmarked ? '收藏成功' : '取消收藏')
    }
  } catch (error) {
    console.error('收藏失败:', error)
    ElMessage.error('操作失败')
  }
}

// 回复评论
const handleReply = comment => {
  if (!isLoggedIn.value) {
    ElMessage.info('请先登录后再进行回复')
    router.push(`/login?redirect=${encodeURIComponent(route.fullPath)}`)
    return
  }
  replyTarget.value = { id: comment.id, nickname: comment.nickname || '匿名' }
  // 滚动到评论表单
  const formElement = document.querySelector('.comment-form')
  if (formElement) {
    formElement.scrollIntoView({ behavior: 'smooth' })
  }
  ElMessage.info(`正在回复 ${replyTarget.value.nickname}，请输入回复内容`)
}

// 取消回复
const cancelReply = () => {
  replyTarget.value = null
  commentForm.parentId = null
}

// 点赞评论
const handleLikeComment = comment => {
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

    const commentData: any = {
      articleId: article.value.id,
      content: commentForm.content
    }

    // 如果是回复评论，添加 parentId
    if (replyTarget.value) {
      commentData.parentId = replyTarget.value.id
    }

    await apiSubmitComment(commentData)

    ElMessage.success(replyTarget.value ? '回复发表成功' : '评论发表成功')
    // 重置表单
    commentForm.content = ''
    commentForm.parentId = null
    replyTarget.value = null
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
const formatDate = dateString => {
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
    let response
    try {
      response = await getBlogSettings()
    } catch (error) {
      console.warn('标准博客设置接口访问失败，尝试匿名接口:', error)
      response = await getBlogSettingsAnonymous()
    }

    let settings = {}
    if (response && response.code === 200) {
      settings = response.data || {}
    } else if (response && typeof response === 'object') {
      settings = response
    }

    // 更新 blogSettingsStore
    blogSettingsStore.updateBlogSettings(settings)
  } catch (error) {
    console.error('获取博客设置失败:', error)
    // 使用默认值
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadArticleDetail()
  loadComments()
  loadBlogSettings()
  isLoggedIn.value = !!userStore.token
})

// 监听路由参数变化，当文章ID变化时重新加载，Vue 3 会自动清理
watch(
  () => route.params.id,
  (newId, oldId) => {
    if (newId && newId !== oldId && newId !== oldId?.toString()) {
      // 滚动到顶部
      window.scrollTo({ top: 0, behavior: 'smooth' })
      // 重置文章数据
      article.value = null
      prevArticle.value = null
      nextArticle.value = null
      relatedArticles.value = []
      commentList.value = []
      totalComments.value = 0
      // 重新加载文章详情
      loadArticleDetail()
      loadComments()
    }
  },
  { immediate: false }
)
</script>

<style scoped>
.mo-article-page {
  --p50: #eef2ff;
  --p100: #e0e7ff;
  --p300: #a5b4fc;
  --p400: #818cf8;
  --p500: #6366f1;
  --p600: #4f46e5;
  --p700: #4338ca;
  --n50: #fafaf9;
  --n100: #f5f5f4;
  --n200: #e7e5e4;
  --n300: #d6d3d1;
  --n400: #a8a29e;
  --n500: #78716c;
  --n600: #57534e;
  --n700: #44403c;
  --n800: #292524;
  --n900: #1c1917;
  --a100: #fce7f3;
  --a600: #db2777;
  --font-sans: Inter, 'PingFang SC', 'Microsoft YaHei', system-ui, sans-serif;
  --font-serif: 'Noto Serif SC', Georgia, 'Songti SC', serif;
  --font-mono: 'JetBrains Mono', 'Fira Code', Consolas, monospace;
  --r-sm: 6px;
  --r-md: 8px;
  --r-lg: 12px;
  --r-full: 9999px;
  min-height: 100vh;
  padding-top: 60px;
  background: var(--n50);
  color: var(--n800);
  font-family: var(--font-sans);
}

html.dark .mo-article-page {
  --p50: rgba(79, 70, 229, 0.18);
  --p100: rgba(79, 70, 229, 0.24);
  --p300: #a5b4fc;
  --p400: #818cf8;
  --p500: #818cf8;
  --p600: #a5b4fc;
  --p700: #c7d2fe;
  --n50: #1c1917;
  --n100: #292524;
  --n200: #44403c;
  --n300: #57534e;
  --n400: #a8a29e;
  --n500: #d6d3d1;
  --n600: #e7e5e4;
  --n700: #f5f5f4;
  --n800: #fafaf9;
  --n900: #fff;
  --a100: rgba(219, 39, 119, 0.18);
  --a600: #f9a8d4;
}

.mo-article-page .mo-article-header {
  max-width: 720px;
  margin: 0 auto;
  padding: 40px 32px 24px;
  text-align: center;
}

.mo-article-page .cat-badge {
  display: inline-block;
  margin-bottom: 14px;
  padding: 4px 14px;
  border-radius: var(--r-full);
  background: var(--p50);
  color: var(--p700);
  font-size: 12px;
  font-weight: 500;
}

.mo-article-page .mo-article-header h1 {
  margin: 0 0 14px;
  color: var(--n900);
  font-size: 28px;
  font-weight: 700;
  line-height: 1.4;
  letter-spacing: 0;
}

.mo-article-page .a-meta {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  flex-wrap: wrap;
  color: var(--n500);
  font-size: 13px;
}

.mo-article-page .a-meta .author {
  color: var(--n700);
  font-weight: 500;
}

.mo-article-page .body-layout {
  display: grid;
  grid-template-columns: 180px minmax(0, 1fr) 260px;
  gap: 28px;
  max-width: 1240px;
  margin: 0 auto;
  padding: 0 32px 40px;
}

.mo-article-page .toc {
  position: sticky;
  top: 80px;
  align-self: start;
}

.mo-article-page .toc :deep(.article-toc) {
  position: static;
  width: auto;
  padding: 0;
  border: none;
  border-radius: 0;
  background: transparent;
  box-shadow: none;
}

.mo-article-page .toc :deep(.toc-header) {
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--n200);
}

.mo-article-page .toc :deep(.toc-title) {
  color: var(--n700);
  font-size: 13px;
  font-weight: 600;
}

.mo-article-page .toc :deep(.toc-toggle) {
  display: none;
}

.mo-article-page .toc :deep(.toc-list) {
  padding: 0;
}

.mo-article-page .toc :deep(.toc-item) {
  padding: 5px 0 5px 14px;
  border-left: 2px solid transparent;
  color: var(--n500);
  font-size: 13px;
}

.mo-article-page .toc :deep(.toc-item:hover),
.mo-article-page .toc :deep(.toc-item-active) {
  border-left-color: var(--p500);
  color: var(--p600);
  background: transparent;
  font-weight: 500;
}

.mo-article-page .toc :deep(.toc-dot) {
  display: none;
}

.mo-article-page .article-content {
  min-width: 0;
  max-width: 680px;
  margin: 0;
  color: var(--n700);
  font-family: var(--font-serif);
  font-size: 17px;
  line-height: 1.9;
}

.mo-article-page .article-cover {
  margin-bottom: 22px;
  overflow: hidden;
  border-radius: var(--r-lg);
  border: 1px solid var(--n200);
}

.mo-article-page .content-body {
  max-width: none;
  color: var(--n700);
  font-family: var(--font-serif);
  font-size: 17px;
  line-height: 1.9;
}

.mo-article-page .content-body :deep(h2) {
  margin: 32px 0 14px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--n200);
  color: var(--n900);
  font-family: var(--font-sans);
  font-size: 22px;
  font-weight: 600;
}

.mo-article-page .content-body :deep(h3) {
  margin: 22px 0 10px;
  color: var(--n800);
  font-family: var(--font-sans);
  font-size: 18px;
  font-weight: 600;
}

.mo-article-page .content-body :deep(p) {
  margin-bottom: 18px;
}

.mo-article-page .content-body :deep(code) {
  padding: 2px 6px;
  border-radius: 4px;
  background: var(--n100);
  color: var(--p600);
  font-family: var(--font-mono);
  font-size: 14px;
}

.mo-article-page .content-body :deep(pre) {
  margin: 16px 0;
  padding: 18px;
  overflow-x: auto;
  border-radius: var(--r-md);
  background: var(--n900);
  color: var(--n200);
  font-family: var(--font-mono);
  font-size: 13px;
  line-height: 1.7;
}

.mo-article-page .content-body :deep(blockquote) {
  margin: 18px 0;
  padding: 14px 20px;
  border-left: 3px solid var(--p400);
  border-radius: 0 var(--r-md) var(--r-md) 0;
  background: var(--p50);
  color: var(--n600);
  font-style: italic;
}

.mo-article-page .author-card,
.mo-article-page .side-widget {
  border: 1px solid var(--n200);
  border-radius: var(--r-lg);
  background: var(--n50);
}

.mo-article-page .article-side {
  min-width: 0;
}

.mo-article-page .author-card {
  padding: 22px;
  text-align: center;
}

.mo-article-page .a-avatar {
  width: 60px;
  height: 60px;
  margin: 0 auto 12px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--p100);
  color: var(--p600);
  font-size: 22px;
  font-weight: 700;
}

.mo-article-page .a-name {
  color: var(--n900);
  font-size: 15px;
  font-weight: 600;
}

.mo-article-page .a-bio {
  margin: 4px 0 14px;
  color: var(--n500);
  font-size: 12px;
  line-height: 1.6;
}

.mo-article-page .a-stats {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 14px;
  padding-top: 14px;
  border-top: 1px solid var(--n100);
}

.mo-article-page .a-stat .num {
  color: var(--n800);
  font-size: 16px;
  font-weight: 700;
}

.mo-article-page .a-stat .lbl {
  color: var(--n400);
  font-size: 11px;
}

.mo-article-page .side-widget {
  margin-top: 16px;
  padding: 18px;
}

.mo-article-page .wt {
  display: flex;
  gap: 6px;
  align-items: center;
  margin-bottom: 12px;
  color: var(--n800);
  font-size: 13px;
  font-weight: 600;
}

.mo-article-page .wt::before {
  content: '';
  width: 3px;
  height: 14px;
  border-radius: 2px;
  background: var(--p500);
}

.mo-article-page .tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.mo-article-page .tc {
  padding: 4px 12px;
  border-radius: var(--r-full);
  background: var(--n100);
  color: var(--n600);
  font-size: 12px;
}

.mo-article-page .related-mini-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.mo-article-page .related-mini {
  color: var(--n700);
  font-size: 13px;
  line-height: 1.4;
}

.mo-article-page .related-mini:hover {
  color: var(--p600);
}

.mo-article-page .article-actions,
.mo-article-page .article-navigation {
  margin-top: 24px;
}

.mo-article-page .article-actions {
  display: flex;
  justify-content: center;
  padding: 18px 0;
}

.mo-article-page .article-navigation {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
}

.mo-article-page .nav-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 14px;
  border: 1px solid var(--n200);
  border-radius: var(--r-md);
  background: var(--n50);
}

.mo-article-page .nav-label {
  color: var(--n400);
  font-size: 12px;
}

.mo-article-page .nav-title {
  color: var(--n800);
  font-size: 13px;
  line-height: 1.4;
}

.mo-article-page .comment-section {
  max-width: 680px;
  margin: 0 auto;
  padding: 32px;
  border-top: 1px solid var(--n200);
  background: var(--n50);
}

.mo-article-page .comment-section h3 {
  margin-bottom: 18px;
  font-size: 18px;
  font-weight: 600;
}

.mo-article-page .comment-input {
  margin-bottom: 24px;
  padding: 14px;
  border: 1px solid var(--n200);
  border-radius: var(--r-lg);
  background: var(--n50);
}

.mo-article-page .guest-fields {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px;
}

.mo-article-page .actions {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
}

.mo-article-page .md-hint {
  margin-right: auto;
  color: var(--n400);
  font-size: 12px;
}

.mo-article-page .comment-item {
  display: flex;
  gap: 12px;
  padding: 18px 0;
  border-bottom: 1px solid var(--n100);
}

.mo-article-page .c-avatar {
  width: 40px;
  height: 40px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: var(--a100);
  color: var(--a600);
  font-size: 14px;
  font-weight: 600;
}

.mo-article-page .c-avatar.small {
  width: 32px;
  height: 32px;
  font-size: 12px;
}

.mo-article-page .c-body {
  min-width: 0;
  flex: 1;
}

.mo-article-page .c-head {
  display: flex;
  align-items: center;
  gap: 8px;
}

.mo-article-page .c-name {
  color: var(--n800);
  font-size: 14px;
  font-weight: 600;
}

.mo-article-page .c-time,
.mo-article-page .c-actions {
  color: var(--n400);
  font-size: 12px;
}

.mo-article-page .c-text {
  margin-top: 5px;
  color: var(--n600);
  font-size: 14px;
  line-height: 1.7;
}

.mo-article-page .c-actions {
  display: flex;
  gap: 16px;
  margin-top: 8px;
}

.mo-article-page .c-actions span {
  cursor: pointer;
}

.mo-article-page .c-reply {
  margin-top: 8px;
  padding: 14px;
  border-radius: var(--r-md);
  background: var(--n50);
}

.mo-article-page .reply-item {
  display: flex;
  gap: 12px;
}

.mo-article-page .btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 8px 18px;
  border: 1px solid transparent;
  border-radius: var(--r-md);
  font-size: 14px;
  font-weight: 500;
}

.mo-article-page .btn-primary {
  background: var(--p600);
  color: #fff;
}

.mo-article-page .btn-sm {
  width: 100%;
  padding: 5px 12px;
  font-size: 13px;
}

@media (max-width: 1024px) {
  .mo-article-page .body-layout {
    grid-template-columns: minmax(0, 1fr) 260px;
  }

  .mo-article-page .toc {
    display: none;
  }
}

@media (max-width: 768px) {
  .mo-article-page .body-layout {
    grid-template-columns: 1fr;
    padding: 0 16px 24px;
  }

  .mo-article-page .article-side {
    display: none;
  }

  .mo-article-page .article-content {
    font-size: 16px;
  }

  .mo-article-page .mo-article-header {
    padding: 24px 16px 16px;
  }

  .mo-article-page .mo-article-header h1 {
    font-size: 22px;
  }

  .mo-article-page .comment-section {
    padding: 24px 16px;
  }

  .mo-article-page .guest-fields,
  .mo-article-page .article-navigation {
    grid-template-columns: 1fr;
  }
}

.article-detail-container {
  min-height: 100vh;
  background: #fafaf9;
}

html.dark .article-detail-container {
  background: #1c1917;
}

.mo-article-page .article-cover img {
  display: block;
  width: 100%;
  height: auto;
}

.mo-article-page .content-body :deep(h1),
.mo-article-page .content-body :deep(h4),
.mo-article-page .content-body :deep(h5),
.mo-article-page .content-body :deep(h6) {
  margin: 28px 0 12px;
  color: var(--n900);
  font-family: var(--font-sans);
  font-weight: 600;
  line-height: 1.35;
}

.mo-article-page .content-body :deep(h1) {
  font-size: 26px;
}

.mo-article-page .content-body :deep(h4) {
  font-size: 16px;
}

.mo-article-page .content-body :deep(h5),
.mo-article-page .content-body :deep(h6) {
  font-size: 15px;
}

.mo-article-page .content-body :deep(ul),
.mo-article-page .content-body :deep(ol) {
  margin: 0 0 18px 22px;
  padding: 0;
}

.mo-article-page .content-body :deep(li) {
  margin-bottom: 8px;
  color: var(--n700);
}

.mo-article-page .content-body :deep(a) {
  color: var(--p600);
  text-decoration: underline;
  text-decoration-thickness: 1px;
  text-underline-offset: 3px;
}

.mo-article-page .content-body :deep(img) {
  display: block;
  max-width: 100%;
  height: auto;
  margin: 22px auto;
  border-radius: var(--r-lg);
}

.mo-article-page .content-body :deep(table) {
  display: block;
  width: 100%;
  max-width: 100%;
  margin: 22px 0;
  overflow-x: auto;
  border: 1px solid var(--n200);
  border-collapse: collapse;
  border-radius: var(--r-md);
  font-family: var(--font-sans);
  font-size: 14px;
}

.mo-article-page .content-body :deep(th),
.mo-article-page .content-body :deep(td) {
  padding: 11px 12px;
  border: 1px solid var(--n200);
  text-align: left;
}

.mo-article-page .content-body :deep(th) {
  background: var(--n100);
  color: var(--n800);
  font-weight: 600;
}

.mo-article-page .content-body :deep(hr) {
  height: 1px;
  margin: 30px 0;
  border: 0;
  background: var(--n200);
}

.loading-container {
  min-height: calc(100vh - 60px);
  padding: 96px 20px;
  background: #fafaf9;
}

.loading-skeleton {
  max-width: 760px;
  margin: 0 auto;
}

.article-skeleton {
  width: 100%;
}

.skeleton-content {
  padding: 20px 0;
}

.not-found-container {
  min-height: calc(100vh - 60px);
  padding: 96px 20px;
  background: #fafaf9;
  text-align: center;
}

.not-found-content {
  max-width: 420px;
  margin: 0 auto;
  padding: 32px;
  border: 1px solid #e7e5e4;
  border-radius: 12px;
  background: #fff;
}

.not-found-icon {
  margin-bottom: 20px;
  color: #a8a29e;
  font-size: 64px;
}

.not-found-content h2 {
  margin: 0 0 12px;
  color: #1c1917;
  font-size: 22px;
  font-weight: 700;
}

.not-found-content p {
  margin: 0;
  color: #78716c;
  font-size: 14px;
  line-height: 1.7;
}

.back-home-btn {
  display: inline-block;
  margin-top: 20px;
  text-decoration: none;
}
</style>
