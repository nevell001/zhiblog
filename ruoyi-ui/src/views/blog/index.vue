<template>
  <div class="blog-container">
    <!-- 滚动进度条 -->
    <div class="scroll-progress" :style="{ width: scrollProgress + '%' }"></div>
    
    <!-- 博客导航 -->
    <BlogNav />

    <!-- 博客头部 -->
    <div class="blog-header">
      <div class="header-content">
        <div class="blog-info">
          <div class="blog-text-info">
            <h1 class="blog-title">{{ blogSettings.blog_name || '我的博客' }}</h1>
            <p class="blog-description">{{ blogSettings.blog_desc || '这是一个基于RuoYi-Vue的博客系统' }}</p>
            <div class="blog-stats">
              <span class="stat-item">
                <el-icon :size="16"><DocumentCopy /></el-icon>
                {{ totalArticles }} 篇文章
              </span>
              <span class="stat-item">
                <el-icon :size="16"><PriceTag /></el-icon>
                {{ tagCloud.length }} 个标签
              </span>
              <span class="stat-item">
                <el-icon :size="16"><Calendar /></el-icon>
                最后更新 {{ lastUpdateTime }}
              </span>
            </div>
          </div>
        </div>

        <!-- 搜索框 -->
        <div class="search-container">
          <div class="search-wrapper">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索文章标题或内容..."
              class="search-input"
              @keyup.enter="handleSearch"
              clearable
            />
            <button class="search-button" @click="handleSearch">
              <el-icon><Search /></el-icon>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="blog-main">
      <div class="main-content">
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-container">
          <div class="loading-grid">
            <el-skeleton v-for="i in 6" :key="i" :loading="loading" animated class="skeleton-item">
              <template #template>
                <div class="article-item">
                  <div class="article-cover">
                    <el-skeleton-item variant="image" style="width: 100%; height: 200px;" />
                  </div>
                  <div class="article-content">
                    <el-skeleton-item variant="h3" style="width: 70%; margin-bottom: 15px;" />
                    <el-skeleton-item variant="text" style="width: 100%; margin-bottom: 10px;" />
                    <el-skeleton-item variant="text" style="width: 90%; margin-bottom: 10px;" />
                    <el-skeleton-item variant="text" style="width: 60%; margin-bottom: 15px;" />
                    <div style="display: flex; gap: 8px; margin-bottom: 15px;">
                      <el-skeleton-item variant="text" style="width: 60px; height: 24px;" />
                      <el-skeleton-item variant="text" style="width: 50px; height: 24px;" />
                    </div>
                    <el-skeleton-item variant="text" style="width: 80px; height: 20px;" />
                  </div>
                </div>
              </template>
            </el-skeleton>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-else-if="articleList.length === 0" class="empty-state">
          <div class="empty-content">
            <el-icon :size="60" class="empty-icon"><DocumentCopy /></el-icon>
            <h3>{{ searchKeyword ? '未找到相关文章' : '暂无文章' }}</h3>
            <p>{{ searchKeyword ? `没有找到包含"${searchKeyword}"的文章，请尝试其他关键词` : '还没有发布任何文章，敬请期待...' }}</p>
            <el-button v-if="searchKeyword" type="primary" @click="clearSearch" style="margin-top: 15px;">
              清除搜索
            </el-button>
          </div>
        </div>

        <!-- 文章列表 -->
        <div v-else class="article-list">
          <div v-for="article in articleList" :key="article.id" class="article-item">
            <div class="article-cover" v-if="article.coverUrl || article.coverImage">
              <img :src="article.coverUrl || article.coverImage || 'https://via.placeholder.com/400x200/409EFF/FFFFFF?text=暂无图片'" :alt="article.title" loading="lazy" @error="handleImageError" />
              <div class="cover-overlay"></div>
              <div class="article-category-badge" v-if="article.tags && article.tags.length > 0">
                {{ article.tags[0].name }}
              </div>
            </div>
            <div class="article-content">
              <h2 class="article-title">
                <router-link :to="{ name: 'PublicBlogArticleDetail', params: { id: (article.id ?? article.articleId ?? article.uuid) } }" :title="article.title">
                  {{ article.title }}
                </router-link>
              </h2>
              <div class="article-meta">
                <span class="meta-item">
                  <el-icon :size="14"><Calendar /></el-icon>
                  {{ formatDate(article.createTime) }}
                </span>
                <span class="meta-item">
                  <el-icon :size="14"><View /></el-icon>
                  {{ article.viewCount || 0 }} 阅读
                </span>
                <span class="meta-item" v-if="article.likeCount">
                  <el-icon :size="14"><Star /></el-icon>
                  {{ article.likeCount }} 点赞
                </span>
                <span class="meta-item" v-if="article.commentCount">
                  <el-icon :size="14"><ChatLineRound /></el-icon>
                  {{ article.commentCount }} 评论
                </span>
              </div>
              <p class="article-summary">{{ article.summary || (article.content ? stripHtmlTags(article.content).substring(0, 150) + '...' : '暂无摘要') }}</p>
              <div class="article-tags" v-if="article.tags && article.tags.length">
                <span v-for="tag in article.tags.slice(0, 3)" :key="tag.id" class="tag-badge" :style="{ backgroundColor: tag.color || '#409EFF' }">
                  {{ tag.name }}
                </span>
              </div>
              <div class="article-footer">
                <router-link :to="{ name: 'PublicBlogArticleDetail', params: { id: (article.id ?? article.articleId ?? article.uuid) } }" class="read-more">
                  阅读全文 <el-icon :size="14"><ArrowRight /></el-icon>
                </router-link>
              </div>
            </div>
          </div>

          <!-- 加载更多按钮 -->
          <div v-if="articleList.length < total && !loading" class="load-more-container">
            <el-button
              type="primary"
              @click="loadMoreArticles"
              :loading="loadingMore"
              round
            >
              {{ loadingMore ? '加载中...' : '加载更多' }}
            </el-button>
          </div>
        </div>
      </div>

      <!-- 侧边栏 -->
      <div class="sidebar">
        <!-- 关于博主 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <el-icon :size="18"><User /></el-icon>
            关于博主
          </h3>
          <div class="about-content">
            <div class="author-avatar">
              <img
                :key="'avatar-' + (blogSettings.blog_avatar || 'default')"
                :src="getAvatarUrl()"
                :alt="blogSettings.blog_author || 'nevell'"
                @error="handleAvatarError"
                :style="{ transition: 'all 0.3s ease' }"
              />
            </div>
            <h4 class="author-name">{{ blogSettings.blog_author || 'nevell' }}</h4>
            <p class="author-title">{{ blogSettings.author_title || '全栈开发工程师' }}</p>
            <p class="about-desc">{{ blogSettings.blog_desc || '热爱技术，热爱生活，记录学习成长路上的点点滴滴' }}</p>
            
            <!-- 统计信息 -->
            <div class="author-stats">
              <div class="stat-box">
                <div class="stat-number">{{ totalArticles }}</div>
                <div class="stat-label">文章</div>
              </div>
              <div class="stat-box">
                <div class="stat-number">{{ categoryList.length }}</div>
                <div class="stat-label">分类</div>
              </div>
              <div class="stat-box">
                <div class="stat-number">{{ tagCloud.length }}</div>
                <div class="stat-label">标签</div>
              </div>
            </div>

            <!-- 技能标签 -->
            <div class="skills-section" v-if="blogSettings.skills && blogSettings.skills.length">
              <div class="skills-title">技能专长</div>
              <div class="skills-tags">
                <span v-for="(skill, index) in blogSettings.skills" :key="index" class="skill-tag">
                  {{ skill }}
                </span>
              </div>
            </div>

            <!-- 社交链接 -->
            <div class="social-links">
              <a v-if="blogSettings.github_url" :href="blogSettings.github_url" class="social-link" title="GitHub" target="_blank" rel="noopener">
                <el-icon :size="18"><Promotion /></el-icon>
              </a>
              <a v-if="blogSettings.blog_email" :href="`mailto:${blogSettings.blog_email}`" class="social-link" title="邮箱">
                <el-icon :size="18"><Message /></el-icon>
              </a>
              <a v-if="blogSettings.wechat_qr" href="#" class="social-link" title="微信" @click.prevent="showWechatQR = true">
                <el-icon :size="18"><ChatDotRound /></el-icon>
              </a>
              <a v-if="blogSettings.weibo_url" :href="blogSettings.weibo_url" class="social-link" title="微博" target="_blank" rel="noopener">
                <el-icon :size="18"><Star /></el-icon>
              </a>
              <!-- 如果没有任何社交链接，显示默认提示 -->
              <div v-if="!blogSettings.github_url && !blogSettings.blog_email && !blogSettings.wechat_qr && !blogSettings.weibo_url" class="no-social-links" style="color: #999; font-size: 0.9rem; margin-top: 10px;">
                暂未配置社交链接
              </div>
            </div>

            </div>
        </div>

        <!-- 分类 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <el-icon :size="18"><Menu /></el-icon>
            文章分类
          </h3>
          <ul class="category-list" v-if="categoryList.length > 0">
            <li v-for="category in categoryList" :key="category.id" class="category-item">
              <router-link :to="{ name: 'PublicBlogCategory', params: { id: category.id } }" class="category-link">
                <span class="category-name">{{ category.name }}</span>
                <span class="category-count">({{ category.articleCount || 0 }})</span>
              </router-link>
            </li>
          </ul>
          <div v-else class="no-data">
            <el-icon :size="40"><FolderOpened /></el-icon>
            <p>暂无分类</p>
          </div>
        </div>

        <!-- 标签云 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <el-icon :size="18"><CollectionTag /></el-icon>
            标签云
          </h3>
          <div class="tag-cloud">
            <router-link
              v-for="tag in tagCloud.slice(0, 15)"
              :key="tag.id"
              :to="{ name: 'PublicBlogTag', params: { id: tag.id } }"
              class="tag-item"
              :style="{
                fontSize: getTagFontSize(tag.article_count) + 'px',
                backgroundColor: tag.color || '#409EFF',
                color: 'white',
                transform: `scale(${getTagScale(tag.article_count)})`
              }"
              :title="`${tag.name} (${tag.article_count}篇文章)`"
            >
              {{ tag.name }}
            </router-link>
          </div>
        </div>

        <!-- 热门文章 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <el-icon :size="18"><StarFilled /></el-icon>
            热门文章
          </h3>
          <ul class="hot-article-list" v-if="hotArticles.length > 0">
            <li v-for="(article, index) in hotArticles.slice(0, 8)" :key="article.id" class="hot-article-item">
              <span class="article-rank" :class="{ 'rank-top': index < 3 }">{{ index + 1 }}</span>
              <div class="hot-article-info">
                <router-link :to="{ name: 'PublicBlogArticleDetail', params: { id: (article.id ?? article.articleId ?? article.uuid) } }" class="hot-article-link" :title="article.title">
                  {{ article.title }}
                </router-link>
                <span class="hot-article-meta">
                  <el-icon :size="12"><View /></el-icon>
                  {{ article.viewCount || 0 }}
                </span>
              </div>
            </li>
          </ul>
          <div v-else class="no-data">
            <el-icon :size="40"><StarFilled /></el-icon>
            <p>暂无热门文章</p>
          </div>
        </div>

        <!-- 文章归档 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <el-icon :size="18"><Calendar /></el-icon>
            文章归档
          </h3>
          <ul class="archive-list" v-if="archiveList.length > 0">
            <li v-for="archive in archiveList.slice(0, 6)" :key="archive.archive_date" class="archive-item">
              <router-link to="/blog/archive" class="archive-link">
                <span class="archive-date">{{ formatArchiveDate(archive.archive_date) }}</span>
                <span class="archive-count">({{ archive.article_count }})</span>
              </router-link>
            </li>
          </ul>
          <div v-else class="no-data">
            <el-icon :size="40"><Calendar /></el-icon>
            <p>暂无归档</p>
          </div>
        </div>

        <!-- 最新评论 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <el-icon :size="18"><ChatDotRound /></el-icon>
            最新评论
          </h3>
          <div class="recent-comments">
            <div v-if="recentComments.length === 0" class="no-comments">
              <el-icon :size="40"><ChatLineRound /></el-icon>
              <p>暂无评论</p>
            </div>
            <div v-for="comment in recentComments.slice(0, 5)" :key="comment.id" class="comment-item">
              <div class="comment-content">
                <p class="comment-text">{{ truncateText(comment.content, 30) }}</p>
                <div class="comment-meta">
                  <span class="comment-author">{{ comment.nickname || '匿名' }}</span>
                  <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 回到顶部按钮 -->
    <transition name="fade">
      <div v-show="showBackToTop" class="back-to-top" @click="scrollToTop" title="回到顶部">
        <el-icon :size="20"><ArrowUp /></el-icon>
      </div>
    </transition>

    <!-- 微信二维码对话框 -->
    <el-dialog v-model="showWechatQR" title="微信二维码" width="300px" center>
      <div class="qr-code-container">
        <img :src="blogSettings.wechat_qr || 'https://via.placeholder.com/200x200/409EFF/FFFFFF?text=微信二维码'" alt="微信二维码" />
        <p>扫码添加微信</p>
      </div>
    </el-dialog>

  
    <!-- 博客底部 -->
    <BlogFooter
      :blogSettings="blogSettings"
      :totalArticles="totalArticles"
      :categoryCount="categoryList.length"
      :tagCount="tagCloud.length"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, computed, nextTick, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Search, Promotion, Message, ChatDotRound, Star, StarFilled, Menu,
  DocumentCopy, PriceTag, Calendar, View, ChatLineRound, ArrowRight, User,
  FolderOpened, CollectionTag, ArrowUp
} from '@element-plus/icons-vue'
import { getArticleList, getArticleListAnonymous, getHotArticles, getArticleArchive } from '@/api/blog/article'
import { getCategoryList } from '@/api/blog/category'
import { getBlogSettings, getBlogSettingsAnonymous } from '@/api/blog/setting'
import { getTagCloud } from '@/api/blog/tag'
import { processAvatarUrl } from '@/api/blog/avatar'
import BlogNav from '@/components/BlogNav.vue'
import BlogFooter from '@/components/BlogFooter.vue'
import { useBlogSettingsStore } from '@/stores/blogSettings'

// 初始化博客设置全局状态
const blogSettingsStore = useBlogSettingsStore()

// 获取路由实例
const route = useRoute()

// 响应式数据
const articleList = ref([])
const categoryList = ref([])
const hotArticles = ref([])
// 直接使用store中的数据，避免数据不一致
const blogSettings = computed(() => blogSettingsStore.blogSettings)
const total = ref(0)
const totalArticles = ref(0)
const lastUpdateTime = ref('')
const searchKeyword = ref('')
const tagCloud = ref([])
const archiveList = ref([])
const recentComments = ref([])
const loading = ref(false)
const loadingMore = ref(false)
const showBackToTop = ref(false)
const scrollProgress = ref(0)
const showWechatQR = ref(false)

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  status: 1 // 只显示已发布的文章
})

// 获取文章列表
const loadArticleList = async (append = false) => {
  try {
    loading.value = !append
    if (append) loadingMore.value = true

    if (process.env.NODE_ENV === 'development') {
      console.log('开始加载文章列表, append:', append, 'searchKeyword:', searchKeyword.value)
    }

    let response;
    try {
      response = await getArticleList(queryParams)
    } catch (error) {
      // 如果标准接口失败，尝试匿名接口
      console.warn('标准文章接口访问失败，尝试匿名接口:', error)
      response = await getArticleListAnonymous(queryParams)
    }

    if (process.env.NODE_ENV === 'development') {
      console.log('文章列表响应:', response)
    }
    
    // 处理不同的响应格式
    let newArticles = []
    let totalCount = 0
    
    if (response && response.code === 200) {
      // RuoYi标准响应格式：{code: 200, msg: "", total: 0, rows: [...]}
      newArticles = response.rows || response.data || []
      totalCount = response.total || 0
    } else if (response && Array.isArray(response)) {
      // 直接返回数组
      newArticles = response
      totalCount = response.length
    } else if (response && response.data) {
      // 其他格式：{data: [...]}
      newArticles = Array.isArray(response.data) ? response.data : []
      totalCount = response.total || newArticles.length
    }
    
    console.log('处理后的文章数据:', {
      newArticles: newArticles.length,
      totalCount,
      articleListLength: articleList.value.length
    })

    if (append) {
      articleList.value = [...articleList.value, ...newArticles]
    } else {
      articleList.value = newArticles
    }
    total.value = totalCount
    totalArticles.value = totalCount

    console.log('最终文章列表状态:', {
      articleListLength: articleList.value.length,
      loading: loading.value,
      loadingMore: loadingMore.value
    })
  } catch (error) {
    console.error('获取文章列表失败:', error)
    // 如果是网络错误或接口不存在，显示友好提示
    if (error.response && (error.response.status === 404 || error.response.status === 401)) {
      console.warn('文章接口暂未实现，使用模拟数据')
      // 使用丰富的模拟数据
      const mockArticles = [
        {
          id: 1,
          title: '欢迎使用Thumbnailator图片处理博客系统',
          summary: '这是一个基于RuoYi-Vue的现代化博客系统，集成了Thumbnailator专业图片处理库，支持高质量的图片压缩、格式转换和水印添加等功能。',
          content: '这是一个基于RuoYi-Vue的博客系统，集成了Thumbnailator图片处理库...',
          coverUrl: '/profile/upload/2025/12/18/7558113286dd4f65a03c9c6b6a32fdea.jpg',
          categoryName: '系统公告',
          createTime: new Date().toISOString(),
          viewCount: 156,
          likeCount: 23,
          commentCount: 8,
          tags: [
            { id: 1, name: 'Vue.js', color: '#4FC08D' },
            { id: 2, name: 'Spring Boot', color: '#6DB33F' },
            { id: 3, name: 'Thumbnailator', color: '#FF6B6B' }
          ]
        },
        {
          id: 2,
          title: '如何使用Thumbnailator进行图片压缩',
          summary: 'Thumbnailator是一个强大的Java图片处理库，本文将详细介绍如何在项目中集成和使用Thumbnailator进行图片压缩、格式转换等操作。',
          content: 'Thumbnailator是一个强大的Java图片处理库...',
          coverUrl: 'https://via.placeholder.com/400x200/67C23A/FFFFFF?text=图片压缩',
          categoryName: '技术分享',
          createTime: new Date(Date.now() - 86400000).toISOString(),
          viewCount: 89,
          likeCount: 15,
          commentCount: 3,
          tags: [
            { id: 4, name: 'Java', color: '#ED8B00' },
            { id: 5, name: '图片处理', color: '#9C27B0' }
          ]
        },
        {
          id: 3,
          title: 'Spring Boot最佳实践指南',
          summary: '本文总结了Spring Boot开发中的最佳实践，包括项目结构设计、配置管理、异常处理等方面的经验分享。',
          content: 'Spring Boot是Java开发中最流行的框架之一...',
          coverUrl: 'https://via.placeholder.com/400x200/6DB33F/FFFFFF?text=Spring Boot',
          categoryName: '技术分享',
          createTime: new Date(Date.now() - 172800000).toISOString(),
          viewCount: 234,
          likeCount: 45,
          commentCount: 12,
          tags: [
            { id: 2, name: 'Spring Boot', color: '#6DB33F' },
            { id: 6, name: '最佳实践', color: '#FF5722' },
            { id: 7, name: 'Java', color: '#ED8B00' }
          ]
        }
      ]
      if (!append) {
        articleList.value = mockArticles
        total.value = mockArticles.length
        totalArticles.value = mockArticles.length
      }
    } else {
      ElMessage.error('获取文章列表失败: ' + (error.message || '网络错误'))
    }
  } finally {
    loading.value = false
    loadingMore.value = false
  }
}

// 获取分类列表
const loadCategoryList = async () => {
  try {
    const response = await getCategoryList({})
    console.log('分类列表响应:', response)
    
    // 处理不同的响应格式
    if (response && response.code === 200) {
      categoryList.value = response.data || response.rows || []
    } else if (response && Array.isArray(response)) {
      categoryList.value = response
    } else if (response && response.data) {
      categoryList.value = Array.isArray(response.data) ? response.data : []
    }
  } catch (error) {
    console.error('获取分类列表失败:', error)
    if (error.response && error.response.status === 404) {
      console.warn('分类接口暂未实现，使用模拟数据')
      categoryList.value = [
        { id: 1, name: '技术分享', articleCount: 5 },
        { id: 2, name: '生活随笔', articleCount: 3 },
        { id: 3, name: '学习笔记', articleCount: 8 }
      ]
    }
  }
}

// 获取热门文章
const loadHotArticles = async () => {
  try {
    // 获取前5个热门文章
    const response = await getHotArticles({ pageNum: 1, pageSize: 5 })
    console.log('热门文章响应:', response)
    
    // 处理不同的响应格式
    if (response && response.code === 200) {
      hotArticles.value = response.rows || response.data || []
    } else if (response && Array.isArray(response)) {
      hotArticles.value = response
    } else if (response && response.data) {
      hotArticles.value = Array.isArray(response.data) ? response.data : []
    }
  } catch (error) {
    console.error('获取热门文章失败:', error)
    if (error.response && error.response.status === 404) {
      console.warn('热门文章接口暂未实现，使用模拟数据')
      hotArticles.value = [
        { id: 1, title: '欢迎使用博客系统' },
        { id: 2, title: 'Vue 3 开发指南' },
        { id: 3, title: 'Spring Boot 最佳实践' }
      ]
    }
  }
}

// 获取博客设置
const loadBlogSettings = async () => {
  try {
    if (process.env.NODE_ENV === 'development') {
      console.log('开始加载博客设置...')
    }

    // 强制清除可能的浏览器缓存
    const timestamp = Date.now()
    console.log('加载设置时间戳:', timestamp)

    // 优先使用匿名访问接口
    let response;
    try {
      response = await getBlogSettings()
    } catch (error) {
      // 如果标准接口失败，尝试匿名接口
      console.warn('标准博客设置接口访问失败，尝试匿名接口:', error)
      response = await getBlogSettingsAnonymous()
    }

    if (process.env.NODE_ENV === 'development') {
      console.log('博客设置响应:', response)
    }

    let settings = {}

    // 处理不同的响应格式
    if (response && response.code === 200) {
      settings = response.data || {}
    } else if (response && typeof response === 'object') {
      settings = response
    }

    // 类型转换：将字符串值转换为正确的类型
    const processedSettings = {}
    Object.keys(settings).forEach(key => {
      let value = settings[key]

      // 处理布尔值转换
      if (value === 'true') {
        value = true
      } else if (value === 'false') {
        value = false
      }
      // 处理数字转换
      else if (key.includes('_count') || key.includes('_size')) {
        const numValue = Number(value)
        if (!isNaN(numValue)) {
          value = numValue
        }
      }
      // 处理日期转换
      else if (key === 'blog_start_time' && value && typeof value === 'string') {
        const dateValue = new Date(value)
        if (!isNaN(dateValue.getTime())) {
          value = dateValue
        }
      }

      processedSettings[key] = value
    })

    // 更新全局状态
    blogSettingsStore.setBlogSettings(processedSettings)

    // 强制更新组件，确保头像等设置立即生效
    nextTick(() => {
      // 强制触发Vue的响应式更新
      if (settings.blog_avatar) {
        console.log('头像设置已更新，强制重新渲染:', settings.blog_avatar)
        // 可以在这里添加其他需要触发的更新逻辑
      }
    })

    // 设置最后更新时间
    lastUpdateTime.value = formatDate(new Date().toISOString())
  } catch (error) {
    console.error('获取博客设置失败:', error)
    if (error.response && error.response.status === 404 || error.response && error.response.status === 401) {
      console.warn('博客设置接口暂未实现或匿名访问失败，使用默认设置')
      const defaultSettings = {
        blog_name: '我的博客',
        blog_desc: '这是一个基于RuoYi-Vue的博客系统',
        blog_author: 'nevell',
        author_title: '全栈开发工程师',
        blog_avatar: '/profile/upload/2025/12/18/7558113286dd4f65a03c9c6b6a32fdea.jpg', // 使用实际存在的头像
        skills: ['Vue.js', 'Spring Boot', 'Java', 'JavaScript', 'MySQL', 'Redis'],
        github_url: 'https://github.com',
        blog_email: 'contact@example.com',
        weibo_url: 'https://weibo.com',
        wechat_qr: 'https://via.placeholder.com/200x200/409EFF/FFFFFF?text=微信二维码'
      }
      blogSettingsStore.setBlogSettings(defaultSettings)
    }
    // 设置最后更新时间
    lastUpdateTime.value = formatDate(new Date().toISOString())
  }
}

// 监听博客设置更新事件
const handleBlogSettingsUpdate = (event) => {
  console.log('收到博客设置更新事件:', event.detail)

  // 直接更新全局状态，无需更新本地状态（因为使用computed）
  blogSettingsStore.updateBlogSettings(event.detail)

  // 强制重新渲染
  lastUpdateTime.value = formatDate(new Date().toISOString())

  // 强制刷新所有头像显示（强制重新计算computed属性）
  nextTick(() => {
    // 强制触发响应式更新
    const currentAvatar = blogSettingsStore.blogSettings.blog_avatar
    if (currentAvatar) {
      blogSettingsStore.updateBlogAvatar(currentAvatar + '?force=' + Date.now())
    }
  })

  // 显示提示
  ElMessage.success('博客设置已更新，头像等信息将立即生效')
}

// 处理跨标签页storage变化事件
const handleStorageChange = (event) => {
  if (event.key === 'blog-settings-update') {
    try {
      const updateData = JSON.parse(event.newValue || event.oldValue || '{}')
      if (updateData.type === 'avatar_update' && updateData.avatarUrl) {
        console.log('🔔 收到跨标签页头像更新事件:', updateData)
        handleBlogSettingsUpdate({ detail: { blog_avatar: updateData.avatarUrl } })
      }
    } catch (error) {
      console.error('解析storage事件失败:', error)
    }
  }
}

// 强制刷新博客设置（供外部调用）
const refreshBlogSettings = async () => {
  console.log('强制刷新博客设置...')
  await loadBlogSettings()
  ElMessage.success('博客设置已刷新')
}

// 获取标签云
const loadTagCloud = async () => {
  try {
    const response = await getTagCloud()
    console.log('标签云响应:', response)
    
    // 处理不同的响应格式
    if (response && response.code === 200) {
      tagCloud.value = response.data || []
    } else if (response && Array.isArray(response)) {
      tagCloud.value = response
    } else if (response && response.data) {
      tagCloud.value = Array.isArray(response.data) ? response.data : []
    }
  } catch (error) {
    console.error('获取标签云失败:', error)
    if (error.response && error.response.status === 404) {
      console.warn('标签云接口暂未实现，使用模拟数据')
      tagCloud.value = [
        { id: 1, name: 'Vue', color: '#4FC08D', article_count: 5 },
        { id: 2, name: 'Spring Boot', color: '#6DB33F', article_count: 3 },
        { id: 3, name: 'JavaScript', color: '#F7DF1E', article_count: 8 },
        { id: 4, name: 'Java', color: '#ED8B00', article_count: 6 }
      ]
    }
  }
}

// 获取文章归档
const loadArchiveData = async () => {
  try {
    const response = await getArticleArchive()
    console.log('归档数据响应:', response)
    
    // 处理不同的响应格式
    let archiveData = []
    if (response && response.code === 200) {
      archiveData = response.data || []
    } else if (response && Array.isArray(response)) {
      archiveData = response
    } else if (response && response.data) {
      archiveData = Array.isArray(response.data) ? response.data : []
    }
    
    archiveList.value = archiveData.slice(0, 10) // 只显示前10个归档
  } catch (error) {
    console.error('获取归档数据失败:', error)
    if (error.response && error.response.status === 404) {
      console.warn('归档接口暂未实现，使用模拟数据')
      const currentDate = new Date()
      archiveList.value = [
        {
          archive_date: `${currentDate.getFullYear()}-${String(currentDate.getMonth() + 1).padStart(2, '0')}`,
          article_count: 5
        },
        {
          archive_date: `${currentDate.getFullYear()}-${String(currentDate.getMonth()).padStart(2, '0')}`,
          article_count: 3
        }
      ]
    }
  }
}

// 搜索处理
const handleSearch = () => {
  // 重置页码为第一页
  queryParams.pageNum = 1
  loadArticleList()
}

// 清除搜索
const clearSearch = () => {
  searchKeyword.value = ''
  queryParams.pageNum = 1
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

// 格式化归档日期
const formatArchiveDate = (dateString) => {
  if (!dateString) return ''
  const [year, month] = dateString.split('-')
  const monthNames = ['一月', '二月', '三月', '四月', '五月', '六月', 
                     '七月', '八月', '九月', '十月', '十一月', '十二月']
  return `${year}年 ${monthNames[parseInt(month) - 1]}`
}

// 根据文章数量计算标签字体大小
const getTagFontSize = (count) => {
  if (count >= 10) return 16
  if (count >= 5) return 14
  if (count >= 2) return 12
  return 11
}

// 根据标签数量计算缩放比例
const getTagScale = (count) => {
  if (count >= 10) return 1.1
  if (count >= 5) return 1.05
  if (count >= 2) return 1.0
  return 0.95
}

// 文本截断
const truncateText = (text, maxLength) => {
  if (!text) return ''
  if (text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
}


// 优化的加载更多文章
const loadMoreArticles = async () => {
  if (loadingMore.value || articleList.value.length >= total.value) {
    console.log('加载条件不满足:', {
      loadingMore: loadingMore.value,
      hasMore: articleList.value.length < total.value
    })
    return
  }
  
  loadingMore.value = true
  queryParams.pageNum++
  
  try {
    const response = await getArticleListAnonymous(queryParams)
    if (response.code === 200 && response.rows) {
      // 合并新数据到现有列表
      const newArticles = response.rows.map(article => ({
        ...article,
        // 预处理图片懒加载
        content: article.content ? addLazyLoadToImages(article.content) : article.content
      }))
      
      articleList.value = [...articleList.value, ...newArticles]
      total.value = response.total
      
      // 更新URL和状态
      const url = new URL(window.location)
      url.searchParams.set('page', queryParams.pageNum)
      window.history.replaceState({}, '', url)
    }
  } catch (error) {
    console.error('加载更多文章失败:', error)
    ElMessage.error('加载文章失败，请稍后重试')
    queryParams.pageNum-- // 回滚页码
  } finally {
    loadingMore.value = false
  }
}

// 为图片添加懒加载属性
const addLazyLoadToImages = (content) => {
  return content.replace(/<img([^>]+)src=/gi, '<img$1src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMSIgaGVpZ2h0PSIxIiB2aWV3Qm94PSIwIDAgMSAxIiBmaWxsPSJ0cmFuc3BhcmVudCI+PC9zdmc+" loading="lazy" src=')
}

// 回到顶部
const scrollToTop = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 处理滚动事件
const handleScroll = () => {
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop
  const scrollHeight = document.documentElement.scrollHeight - document.documentElement.clientHeight
  
  // 计算滚动进度
  scrollProgress.value = (scrollTop / scrollHeight) * 100
  
  // 显示/隐藏回到顶部按钮
  showBackToTop.value = scrollTop > 300
}

// 去除HTML标签
const stripHtmlTags = (html) => {
  if (!html) return ''
  return html.replace(/<[^>]*>/g, '')
}

// 图片加载错误处理
const handleImageError = (e) => {
  e.target.src = 'https://via.placeholder.com/400x200/409EFF/FFFFFF?text=暂无图片'
}

// 获取头像URL（使用优化的处理函数）
const getAvatarUrl = () => {
  const avatarUrl = blogSettings.value.blog_avatar;
  if (process.env.NODE_ENV === 'development') {
    console.log('获取头像URL:', avatarUrl);
  }
  const processedUrl = processAvatarUrl(avatarUrl);
  if (process.env.NODE_ENV === 'development') {
    console.log('处理后的头像URL:', processedUrl);
  }
  return processedUrl;
}

// 头像加载错误处理
const handleAvatarError = (e) => {
  console.warn('头像加载失败，使用默认头像');
  const defaultAvatar = processAvatarUrl('');

  // 防止循环：如果已经是默认头像了，不要再设置
  if (!e.target.src.includes('data:image/svg+xml')) {
    e.target.src = defaultAvatar;
  }
  // 移除错误监听器，防止无限循环
  e.target.onerror = null;
}


// 定期刷新博客设置（每5分钟）
let settingsRefreshTimer = null

const startSettingsRefresh = () => {
  settingsRefreshTimer = setInterval(() => {
    loadBlogSettings()
  }, 5 * 60 * 1000) // 5分钟
}

const stopSettingsRefresh = () => {
  if (settingsRefreshTimer) {
    clearInterval(settingsRefreshTimer)
    settingsRefreshTimer = null
  }
}

// 页面可见性变化时重新加载设置
const handleVisibilityChange = () => {
  if (document.visibilityState === 'visible') {
    // 页面重新可见时，立即刷新设置
    loadBlogSettings()
  }
}

// 组件挂载时加载数据
onMounted(() => {
  console.log('首页组件已挂载，开始加载数据...')
  loadArticleList()
  loadCategoryList()
  loadHotArticles()
  loadBlogSettings()
  loadTagCloud()
  loadArchiveData()

  // 启动定期刷新
  startSettingsRefresh()
  startSettingsCheck()

  // 监听页面可见性变化
  document.addEventListener('visibilitychange', handleVisibilityChange)

  // 添加滚动监听
  window.addEventListener('scroll', handleScroll)

  // 添加博客设置更新监听器
  window.addEventListener('blog-settings-update', handleBlogSettingsUpdate)

  // 添加跨标签页storage监听器
  window.addEventListener('storage', handleStorageChange)

  console.log('博客设置更新监听器已添加')
})

// 监听路由变化，当从文章详情页返回首页时刷新数据
watch(route, (to, from) => {
  // 检查是否从文章详情页返回首页
  if (from.path?.includes('/blog/article/') && to.path === '/blog') {
    console.log('从文章详情页返回首页，刷新数据...')
    loadArticleList()
    loadHotArticles()
  }
})

// 定期检查博客设置的定时器引用
let checkBlogSettingsInterval = null

// 定期检查博客设置更新（每30秒检查一次，确保头像等设置的最新状态）
const startSettingsCheck = () => {
  // 清除旧的定时器
  if (checkBlogSettingsInterval) {
    clearInterval(checkBlogSettingsInterval)
  }

  checkBlogSettingsInterval = setInterval(async () => {
    try {
      // 获取当前数据库中的头像设置
      const response = await getBlogSettings()
      if (response && response.code === 200 && response.data) {
        const currentAvatar = response.data.blog_avatar
        const storeAvatar = blogSettingsStore.blogSettings.blog_avatar

        // 如果头像发生变化，更新store
        if (currentAvatar && currentAvatar !== storeAvatar) {
          console.log('🔄 检测到头像变化，更新store:', {
            old: storeAvatar,
            new: currentAvatar
          })
          blogSettingsStore.updateBlogSettings({ blog_avatar: currentAvatar })
        }
      }
    } catch (error) {
      console.log('定期检查博客设置时出错:', error)
    }
  }, 30000) // 30秒检查一次
}

// 组件卸载时移除监听
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)

  // 移除博客设置更新监听器
  window.removeEventListener('blog-settings-update', handleBlogSettingsUpdate)

  // 移除storage监听器
  window.removeEventListener('storage', handleStorageChange)

  // 移除页面可见性监听器
  document.removeEventListener('visibilitychange', handleVisibilityChange)

  // 清除定期刷新定时器
  stopSettingsRefresh()

  // 清除设置检查定时器
  if (checkBlogSettingsInterval) {
    clearInterval(checkBlogSettingsInterval)
    checkBlogSettingsInterval = null
  }

  console.log('博客设置更新监听器已移除')
})

// 导出函数供外部调用
defineExpose({
  refreshBlogSettings
})
</script>

<style scoped>
.blog-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  position: relative;
}

/* 滚动进度条 */
.scroll-progress {
  position: fixed;
  top: 0;
  left: 0;
  height: 3px;
  background: linear-gradient(90deg, #409eff, #667eea, #764ba2);
  z-index: 9999;
  transition: width 0.1s ease;
  box-shadow: 0 2px 4px rgba(64, 158, 255, 0.3);
}

/* 回到顶部按钮 */
.back-to-top {
  position: fixed;
  bottom: 40px;
  right: 40px;
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #409eff, #337ecc);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 16px rgba(64, 158, 255, 0.4);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 1000;
  font-size: 1.5rem;
}

.back-to-top:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(64, 158, 255, 0.5);
  background: linear-gradient(135deg, #337ecc, #2575fc);
}

.back-to-top:active {
  transform: translateY(-3px);
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.fade-enter-from, .fade-leave-to {
  opacity: 0;
  transform: translateY(20px);
}

.blog-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 100px 0 80px 0;
  text-align: center;
  position: relative;
  overflow: hidden;
  animation: headerFadeIn 0.8s ease-out;
}

@keyframes headerFadeIn {
  from {
    opacity: 0;
    transform: translateY(-30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.blog-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background:
    radial-gradient(circle at 20% 30%, rgba(255,255,255,0.1) 2px, transparent 3px),
    radial-gradient(circle at 70% 65%, rgba(255,255,255,0.1) 1px, transparent 2px),
    radial-gradient(circle at 40% 80%, rgba(255,255,255,0.1) 1.5px, transparent 2.5px),
    radial-gradient(circle at 90% 10%, rgba(255,255,255,0.1) 1px, transparent 2px),
    radial-gradient(circle at 15% 85%, rgba(255,255,255,0.1) 1px, transparent 2px);
  background-size: 100px 100px, 80px 80px, 120px 120px, 60px 60px, 90px 90px;
  animation: floatStars 20s linear infinite;
}

@keyframes floatStars {
  0% { transform: translate(0, 0); }
  100% { transform: translate(-100px, -100px); }
}

.blog-header::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 80px;
  background: linear-gradient(to top, #f5f5f5, transparent);
  opacity: 0.1;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  position: relative;
  z-index: 1;
}

.blog-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}


.blog-text-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  text-align: center;
}


@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.blog-title {
  font-size: 3.5rem;
  margin: 0;
  font-weight: 700;
  background: linear-gradient(45deg, #fff, #f0f8ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: 0 2px 4px rgba(0,0,0,0.1);
  animation: titleSlideIn 1s ease-out 0.2s both;
  letter-spacing: -0.5px;
}

@keyframes titleSlideIn {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.blog-description {
  font-size: 1.3rem;
  opacity: 0.95;
  margin: 0;
  max-width: 600px;
  line-height: 1.6;
}

.blog-stats {
  display: flex;
  gap: 30px;
  justify-content: center;
  flex-wrap: wrap;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 1rem;
  opacity: 0.9;
}

.stat-item i {
  font-size: 1.1rem;
}

.search-container {
  max-width: 600px;
  margin: 30px auto 0;
  position: relative;
  z-index: 10;
}

.search-wrapper {
  position: relative;
  width: 100%;
  animation: searchSlideIn 0.8s ease-out 0.4s both;
}

.search-input {
  width: 100%;
}

.search-button {
  position: absolute;
  right: 2px;
  top: 2px;
  bottom: 2px;
  background: linear-gradient(135deg, #409eff, #337ecc);
  border: none;
  border-radius: 0 46px 46px 0;
  width: 46px;
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 2;
}

.search-button:hover {
  background: linear-gradient(135deg, #337ecc, #2575fc);
}

.search-button:active {
  transform: scale(0.98);
}

/* 设置搜索图标为透明，让按钮背景色透过 */
.search-button .el-icon {
  color: rgba(255, 255, 255, 0.9);
  font-size: 18px;
}

@keyframes searchSlideIn {
  from {
    opacity: 0;
    transform: translateY(20px) scale(0.9);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 50px;
  padding: 4px 20px 4px 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.search-wrapper:hover .search-input :deep(.el-input__wrapper) {
  transform: translateY(-2px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.25);
  border-color: rgba(255, 255, 255, 0.5);
}

.search-wrapper:hover .search-button {
  transform: translateY(-2px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.25);
}

.search-input :deep(.el-input__wrapper.is-focus) {
  transform: translateY(-2px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.25);
  border-color: rgba(255, 255, 255, 0.8);
}

.search-input :deep(.el-input__wrapper.is-focus) + .search-button {
  transform: translateY(-2px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.25);
}

.search-input :deep(.el-input__inner) {
  font-size: 16px;
  color: #333;
  font-weight: 500;
}

.search-input :deep(.el-input__inner::placeholder) {
  color: #666;
  font-weight: 400;
}


.blog-main {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
  display: flex;
  gap: 40px;
}

.main-content {
  flex: 1;
}

.sidebar {
  width: 300px;
}

.article-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
  gap: 30px;
  margin-bottom: 20px;
  animation: fadeInUp 0.6s ease-out;
  align-items: stretch;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.article-item {
  background: white;
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  position: relative;
  animation: cardFadeIn 0.6s ease-out backwards;
  backdrop-filter: blur(10px);
  min-height: 480px;
}

.article-item:nth-child(1) { animation-delay: 0.1s; }
.article-item:nth-child(2) { animation-delay: 0.2s; }
.article-item:nth-child(3) { animation-delay: 0.3s; }
.article-item:nth-child(4) { animation-delay: 0.4s; }
.article-item:nth-child(5) { animation-delay: 0.5s; }
.article-item:nth-child(6) { animation-delay: 0.6s; }

@keyframes cardFadeIn {
  from {
    opacity: 0;
    transform: translateY(20px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.article-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.05), rgba(118, 75, 162, 0.05));
  opacity: 0;
  transition: opacity 0.4s ease;
  pointer-events: none;
  z-index: 1;
}

.article-item:hover {
  transform: translateY(-8px) scale(1.02);
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.15);
  border-color: rgba(64, 158, 255, 0.2);
}

.article-item:hover::before {
  opacity: 1;
}

.article-cover {
  position: relative;
  height: 200px;
  overflow: hidden;
  background: linear-gradient(45deg, #f0f0f0, #e0e0e0);
  flex-shrink: 0;
}

.article-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  filter: brightness(1);
}

.cover-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(180deg, transparent 0%, rgba(0, 0, 0, 0.3) 100%);
  opacity: 0;
  transition: opacity 0.4s ease;
  pointer-events: none;
}

.article-item:hover .article-cover img {
  transform: scale(1.08);
  filter: brightness(1.1);
}

.article-item:hover .cover-overlay {
  opacity: 1;
}

.article-category-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.95), rgba(37, 117, 252, 0.95));
  color: white;
  padding: 6px 10px;
  border-radius: 6px;
  font-size: 0.75rem;
  font-weight: 600;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.article-content {
  padding: 25px;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.article-title {
  margin: 0 0 15px 0;
  font-size: 1.5rem;
  line-height: 1.4;
  font-weight: 600;
  min-height: 2.8rem;
}

.article-title a {
  color: #1a1a1a;
  text-decoration: none;
  transition: all 0.3s ease;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  background: linear-gradient(135deg, #1a1a1a, #333);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.article-title a:hover {
  background: linear-gradient(135deg, #409eff, #337ecc);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  transform: translateX(4px);
}

.article-meta {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
  font-size: 0.9rem;
  color: #666;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  background: rgba(102, 102, 102, 0.05);
  padding: 4px 8px;
  border-radius: 6px;
  transition: background-color 0.3s ease;
}

.meta-item:hover {
  background: rgba(64, 158, 255, 0.1);
  color: #409eff;
}

.meta-item i {
  font-size: 1rem;
  opacity: 0.8;
}

.article-summary {
  color: #555;
  line-height: 1.7;
  margin-bottom: 18px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  flex: 1;
  font-size: 0.95rem;
  min-height: 4.8rem;
}

.article-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 18px;
  flex-wrap: wrap;
}

.tag-badge {
  color: white;
  padding: 4px 10px;
  border-radius: 14px;
  font-size: 0.8rem;
  font-weight: 500;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.tag-badge::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s ease;
}

.tag-badge:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.tag-badge:hover::before {
  left: 100%;
}

.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 15px;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
  margin-top: auto;
}

.read-more {
  color: #409eff;
  text-decoration: none;
  font-weight: 600;
  font-size: 0.9rem;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  position: relative;
}

.read-more::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, #409eff, #337ecc);
  transition: width 0.3s ease;
}

.read-more:hover {
  color: #337ecc;
  transform: translateX(4px);
}

.read-more:hover::after {
  width: 100%;
}

.read-more i {
  transition: transform 0.3s ease;
}

.read-more:hover i {
  transform: translateX(3px);
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

.sidebar-widget {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  padding: 24px;
  margin-bottom: 20px;
  transition: all 0.3s ease;
  animation: slideInRight 0.6s ease-out backwards;
}

.sidebar-widget:nth-child(1) { animation-delay: 0.2s; }
.sidebar-widget:nth-child(2) { animation-delay: 0.3s; }
.sidebar-widget:nth-child(3) { animation-delay: 0.4s; }
.sidebar-widget:nth-child(4) { animation-delay: 0.5s; }
.sidebar-widget:nth-child(5) { animation-delay: 0.6s; }
.sidebar-widget:nth-child(6) { animation-delay: 0.7s; }

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.sidebar-widget:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.widget-title {
  font-size: 1.2rem;
  margin: 0 0 15px 0;
  padding-bottom: 10px;
  border-bottom: 2px solid #409eff;
  color: #333;
}

.about-content {
  text-align: center;
}

.author-title {
  color: #409eff;
  font-size: 0.85rem;
  margin: 5px 0 10px 0;
  font-weight: 500;
}

.about-desc {
  color: #666;
  font-size: 0.9rem;
  margin: 15px 0;
  line-height: 1.6;
}

.author-stats {
  display: flex;
  justify-content: space-around;
  margin: 20px 0;
  padding: 15px 0;
  border-top: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;
}

.stat-box {
  text-align: center;
  flex: 1;
}

.stat-number {
  font-size: 1.5rem;
  font-weight: 700;
  color: #409eff;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 0.8rem;
  color: #999;
}

.skills-section {
  margin: 20px 0;
}

.skills-title {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 10px;
  font-weight: 600;
}

.skills-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.skill-tag {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 500;
  transition: all 0.3s ease;
}

.skill-tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}


.qr-code-container {
  text-align: center;
  padding: 20px;
}

.qr-code-container img {
  width: 200px;
  height: 200px;
  border-radius: 8px;
  margin-bottom: 15px;
}

.qr-code-container p {
  color: #666;
  font-size: 0.9rem;
  margin: 0;
}

.category-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.category-item {
  margin-bottom: 10px;
}

.category-link {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  color: #333;
  text-decoration: none;
  transition: color 0.3s ease;
}

.category-link:hover {
  color: #409eff;
}

.category-count {
  color: #999;
  font-size: 0.9rem;
}

.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  padding: 15px 0;
}

.tag-item {
  color: white;
  text-decoration: none;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 500;
  position: relative;
  overflow: hidden;
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  animation: tagPop 0.5s ease-out backwards;
}

.tag-item:nth-child(1) { animation-delay: 0.1s; }
.tag-item:nth-child(2) { animation-delay: 0.15s; }
.tag-item:nth-child(3) { animation-delay: 0.2s; }
.tag-item:nth-child(4) { animation-delay: 0.25s; }
.tag-item:nth-child(5) { animation-delay: 0.3s; }
.tag-item:nth-child(6) { animation-delay: 0.35s; }
.tag-item:nth-child(7) { animation-delay: 0.4s; }
.tag-item:nth-child(8) { animation-delay: 0.45s; }

@keyframes tagPop {
  from {
    opacity: 0;
    transform: scale(0.8);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

.tag-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.6s ease;
}

.tag-item::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  transform: translate(-50%, -50%);
  transition: width 0.4s ease, height 0.4s ease;
}

.tag-item:hover {
  transform: translateY(-4px) scale(1.05);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
}

.tag-item:hover::before {
  left: 100%;
}

.tag-item:hover::after {
  width: 100%;
  height: 100%;
}

.hot-article-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.hot-article-item {
  margin-bottom: 8px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.hot-article-item:last-child {
  margin-bottom: 0;
  border-bottom: none;
}

.hot-article-link {
  color: #333;
  text-decoration: none;
  font-size: 0.9rem;
  line-height: 1.4;
  transition: color 0.3s ease;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.hot-article-link:hover {
  color: #409eff;
}

.archive-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.archive-item {
  margin-bottom: 10px;
}

.archive-link {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  color: #333;
  text-decoration: none;
  transition: color 0.3s ease;
}

.archive-link:hover {
  color: #409eff;
}

.archive-count {
  color: #999;
  font-size: 0.9rem;
}

.loading-container {
  padding: 60px 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.loading-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
  gap: 30px;
  width: 100%;
  max-width: 1200px;
}

.skeleton-item {
  width: 100%;
}

.author-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 3px solid #409eff;
  margin-bottom: 15px;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #409eff, #337ecc);
  padding: 3px;
}

.author-avatar img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid white;
}

.author-avatar::before {
  content: '';
  position: absolute;
  top: -3px;
  left: -3px;
  right: -3px;
  bottom: -3px;
  border-radius: 50%;
  background: linear-gradient(45deg, #409eff, #764ba2, #667eea);
  z-index: -1;
  animation: rotate 3s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.author-name {
  font-size: 1.1rem;
  font-weight: 600;
  margin: 10px 0 5px 0;
  color: #333;
}

.social-links {
  display: flex;
  gap: 15px;
  justify-content: center;
  margin-top: 15px;
}

.social-link {
  color: #666;
  font-size: 1.2rem;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: rgba(64, 158, 255, 0.05);
  position: relative;
  overflow: hidden;
}

.social-link::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  border-radius: 50%;
  background: rgba(64, 158, 255, 0.1);
  transform: translate(-50%, -50%);
  transition: width 0.4s ease, height 0.4s ease;
}

.social-link:hover {
  color: #409eff;
  background: rgba(64, 158, 255, 0.1);
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
}

.social-link:hover::before {
  width: 100%;
  height: 100%;
}

.article-rank {
  display: inline-block;
  width: 20px;
  height: 20px;
  background: #409eff;
  color: white;
  border-radius: 50%;
  text-align: center;
  line-height: 20px;
  font-size: 0.8rem;
  margin-right: 10px;
  flex-shrink: 0;
}

.hot-article-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.hot-article-item:last-child {
  margin-bottom: 0;
  border-bottom: none;
}

.hot-article-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.hot-article-meta {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 0.75rem;
  color: #999;
}

.recent-comments {
  max-height: 300px;
  overflow-y: auto;
}

.comment-item {
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f5;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-content p {
  margin: 0 0 5px 0;
  color: #666;
  font-size: 0.9rem;
  line-height: 1.4;
}

.comment-meta {
  display: flex;
  justify-content: space-between;
  font-size: 0.8rem;
  color: #999;
}

.no-comments {
  text-align: center;
  padding: 30px 0;
  color: #999;
}

.no-comments i {
  font-size: 2rem;
  margin-bottom: 10px;
  display: block;
}

.load-more-container {
  display: flex;
  justify-content: center;
  margin-top: 40px;
  padding-bottom: 20px;
}

.empty-state {
  text-align: center;
  padding: 80px 20px;
  color: #666;
}

.empty-content {
  max-width: 400px;
  margin: 0 auto;
}

.empty-icon {
  font-size: 4rem;
  color: #c0c4cc;
  margin-bottom: 20px;
  display: block;
}

.empty-content h3 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 1.5rem;
}

.empty-content p {
  margin: 0;
  font-size: 0.95rem;
  line-height: 1.5;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .blog-main {
    max-width: 100%;
    padding: 30px 20px;
    gap: 30px;
  }

  .sidebar {
    width: 260px;
  }
}

@media (max-width: 1024px) {
  .blog-main {
    gap: 25px;
  }

  .sidebar {
    width: 240px;
  }

  .article-cover {
    height: 180px;
  }
}

@media (max-width: 768px) {
  .blog-header {
    padding: 60px 0 40px 0;
  }

  
  .blog-title {
    font-size: 2.2rem;
    line-height: 1.2;
    margin-bottom: 15px;
  }

  .blog-description {
    font-size: 1.1rem;
    padding: 0 20px;
    line-height: 1.5;
    margin-bottom: 25px;
  }

  .blog-stats {
    gap: 20px;
  }

  .blog-main {
    flex-direction: column;
    padding: 20px 15px;
    gap: 25px;
  }

  .main-content {
    order: 1;
  }

  /* 移动端侧边栏重新设计 - 使用网格布局 */
  .sidebar {
    width: 100%;
    order: 2;
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 15px;
  }

  .sidebar-widget {
    margin-bottom: 0;
    min-height: 200px;
  }

  .article-list {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .article-item {
    margin-bottom: 20px;
    min-height: 420px;
  }

  .article-cover {
    height: 180px;
  }

  .article-content {
    padding: 20px;
  }

  .article-title {
    font-size: 1.4rem;
    min-height: 2.5rem;
  }

  .article-meta {
    flex-direction: row;
    flex-wrap: wrap;
    gap: 15px;
    font-size: 0.85rem;
  }

  .article-summary {
    font-size: 0.9rem;
    line-height: 1.5;
    min-height: 4.5rem;
  }

  .article-tags {
    gap: 6px;
  }

  .tag-badge {
    font-size: 0.75rem;
    padding: 3px 8px;
  }

  .article-footer {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }

  .social-links {
    gap: 20px;
  }

  .hot-article-item {
    flex-direction: row;
    align-items: center;
    gap: 8px;
    margin-bottom: 10px;
  }

  .article-rank {
    width: 24px;
    height: 24px;
    line-height: 24px;
    font-size: 0.85rem;
    margin-right: 8px;
    margin-bottom: 0;
  }

  .hot-article-link {
    font-size: 0.9rem;
    flex: 1;
  }

  .tag-cloud {
    gap: 8px;
  }

  .tag-item {
    padding: 4px 8px;
    font-size: 0.85rem;
  }
}

@media (max-width: 480px) {
  .header-content {
    padding: 0 15px;
  }

  .blog-title {
    font-size: 1.8rem;
    line-height: 1.2;
  }

  .blog-description {
    font-size: 1rem;
    line-height: 1.4;
  }

  .blog-stats {
    flex-direction: column;
    gap: 12px;
    align-items: center;
  }

  .stat-item {
    font-size: 0.9rem;
  }

  .search-container {
    margin: 20px auto 0;
    max-width: 100%;
    padding: 0 10px;
  }

  .search-wrapper {
    position: relative;
    width: 100%;
  }

  .search-input :deep(.el-input__wrapper) {
    padding: 12px 50px 12px 18px;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
    border-radius: 24px;
  }

  .search-input :deep(.el-input__inner) {
    font-size: 16px;
    height: 24px;
    line-height: 24px;
  }

  .search-input :deep(.el-input__inner::placeholder) {
    font-size: 15px;
  }

  .search-button {
    width: 44px;
    height: 44px;
    right: 4px;
    top: 50%;
    transform: translateY(-50%);
    border-radius: 22px;
  }

  .search-button .el-icon {
    font-size: 20px;
  }

  .search-wrapper:hover .search-input :deep(.el-input__wrapper),
  .search-input :deep(.el-input__wrapper.is-focus) {
    transform: translateY(-1px);
    box-shadow: 0 12px 32px rgba(0, 0, 0, 0.2);
  }

  .blog-main {
    padding: 15px 10px;
  }

  .article-item {
    border-radius: 6px;
    box-shadow: 0 1px 8px rgba(0, 0, 0, 0.08);
  }

  .article-cover {
    height: 160px;
  }

  .article-content {
    padding: 18px;
  }

  .article-title {
    font-size: 1.25rem;
    margin-bottom: 12px;
  }

  .article-summary {
    font-size: 0.9rem;
    margin-bottom: 12px;
    -webkit-line-clamp: 2;
  }

  .sidebar {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .sidebar-widget {
    padding: 15px;
    border-radius: 6px;
  }

  .widget-title {
    font-size: 1rem;
    margin-bottom: 12px;
  }

  .about-content {
    padding: 0 10px;
  }

  .author-avatar {
    width: 70px;
    height: 70px;
    margin-bottom: 12px;
  }

  .author-name {
    font-size: 1rem;
  }

  .about-desc {
    font-size: 0.85rem;
  }

  .social-links {
    gap: 18px;
  }

  .social-link {
    font-size: 1.1rem;
  }

  .category-list,
  .hot-article-list,
  .archive-list {
    font-size: 0.9rem;
  }

  .recent-comments {
    max-height: 250px;
  }

  .comment-item {
    padding: 10px 0;
  }

  .back-to-top {
    bottom: 15px;
    right: 15px;
    width: 40px;
    height: 40px;
    font-size: 1.2rem;
  }
  .load-more-container {
    margin-top: 30px;
  }
}

/* 平板响应式优化 */
@media (min-width: 769px) and (max-width: 1024px) {
  .article-list {
    gap: 25px;
  }

  .article-item {
    display: flex;
    flex-direction: row;
    height: auto;
  }

  .article-cover {
    width: 280px;
    height: 180px;
    flex-shrink: 0;
  }

  .article-content {
    flex: 1;
    padding: 20px 25px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }

  .article-title {
    margin-bottom: 10px;
  }

  .article-meta {
    margin-bottom: 12px;
  }

  .article-summary {
    flex: 1;
    margin-bottom: 12px;
  }

  .article-tags {
    margin-bottom: 12px;
  }
}

/* 超小屏幕优化 */
@media (max-width: 360px) {
  .blog-main {
    padding: 10px 8px;
  }

  .article-item {
    min-height: 400px;
  }

  .article-cover {
    height: 160px;
  }

  .article-content {
    padding: 15px;
  }

  .article-title {
    font-size: 1.1rem;
    min-height: 2.2rem;
  }

  .article-summary {
    min-height: 4.2rem;
  }

  .sidebar-widget {
    padding: 12px;
  }

  .tag-cloud {
    gap: 6px;
  }

  .tag-item {
    padding: 3px 6px;
    font-size: 0.8rem;
  }
}

/* 新增样式 */
.no-data {
  text-align: center;
  padding: 30px 0;
  color: #999;
}

.no-data i {
  font-size: 2rem;
  margin-bottom: 10px;
  display: block;
  opacity: 0.6;
}

.no-data p {
  margin: 0;
  font-size: 0.9rem;
}

.rank-top {
  background: linear-gradient(135deg, #ff6b6b, #ee5a24) !important;
  box-shadow: 0 2px 8px rgba(255, 107, 107, 0.3);
}

.hot-article-item:nth-child(1) .rank-top {
  background: linear-gradient(135deg, #ffd700, #ffb347) !important;
}

.hot-article-item:nth-child(2) .rank-top {
  background: linear-gradient(135deg, #c0c0c0, #a8a8a8) !important;
}

.hot-article-item:nth-child(3) .rank-top {
  background: linear-gradient(135deg, #cd7f32, #b8860b) !important;
}
</style>