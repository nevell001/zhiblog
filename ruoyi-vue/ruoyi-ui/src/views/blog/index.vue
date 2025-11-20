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
          <h1 class="blog-title">{{ blogSettings.blog_name || '我的博客' }}</h1>
          <p class="blog-description">{{ blogSettings.blog_desc || '这是一个基于RuoYi-Vue的博客系统' }}</p>
          <div class="blog-stats">
            <span class="stat-item">
              <i class="el-icon-document-copy"></i>
              {{ totalArticles }} 篇文章
            </span>
            <span class="stat-item">
              <i class="el-icon-price-tag"></i>
              {{ tagCloud.length }} 个标签
            </span>
            <span class="stat-item">
              <i class="el-icon-date"></i>
              最后更新 {{ lastUpdateTime }}
            </span>
          </div>
        </div>

        <!-- 搜索框 -->
        <div class="search-container">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索文章标题或内容..."
            class="search-input"
            @keyup.enter="handleSearch"
            clearable
          >
            <template #append>
              <el-button icon="Search" @click="handleSearch" />
            </template>
          </el-input>
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
            <i class="el-icon-document-copy empty-icon"></i>
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
            <div class="article-cover" v-if="article.coverUrl">
              <img :src="article.coverUrl" :alt="article.title" loading="lazy" @error="handleImageError" />
              <div class="cover-overlay"></div>
              <div class="article-category-badge" v-if="article.categoryName">
                {{ article.categoryName }}
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
                  <i class="el-icon-date"></i>
                  {{ formatDate(article.createTime) }}
                </span>
                <span class="meta-item">
                  <i class="el-icon-view"></i>
                  {{ article.viewCount || 0 }} 阅读
                </span>
                <span class="meta-item" v-if="article.likeCount">
                  <i class="el-icon-star-off"></i>
                  {{ article.likeCount }} 点赞
                </span>
                <span class="meta-item" v-if="article.commentCount">
                  <i class="el-icon-chat-line-round"></i>
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
                  阅读全文 <i class="el-icon-arrow-right"></i>
                </router-link>
              </div>
            </div>
          </div>
        </div>

        <!-- 加载更多 -->
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

      <!-- 侧边栏 -->
      <div class="sidebar">
        <!-- 关于博主 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <i class="el-icon-user"></i>
            关于博主
          </h3>
          <div class="about-content">
            <div class="author-avatar">
              <img :src="blogSettings.blog_avatar || 'https://via.placeholder.com/80x80/409EFF/FFFFFF?text=博主'" :alt="blogSettings.blog_author" />
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
              <a :href="blogSettings.github_url || '#'" class="social-link" title="GitHub" target="_blank" rel="noopener">
                <i class="el-icon-s-promotion"></i>
              </a>
              <a :href="blogSettings.email ? `mailto:${blogSettings.email}` : '#'" class="social-link" title="邮箱">
                <i class="el-icon-message"></i>
              </a>
              <a href="#" class="social-link" title="微信" @click.prevent="showWechatQR = true">
                <i class="el-icon-chat-dot-round"></i>
              </a>
              <a :href="blogSettings.weibo_url || '#'" class="social-link" title="微博" target="_blank" rel="noopener">
                <i class="el-icon-star-off"></i>
              </a>
            </div>

            <!-- 联系按钮 -->
            <el-button type="primary" size="small" class="contact-btn" @click="showContactDialog = true">
              <i class="el-icon-chat-line-round"></i>
              联系我
            </el-button>
          </div>
        </div>

        <!-- 分类 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <i class="el-icon-menu"></i>
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
            <i class="el-icon-folder-opened"></i>
            <p>暂无分类</p>
          </div>
        </div>

        <!-- 标签云 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <i class="el-icon-collection-tag"></i>
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
            <i class="el-icon-star-on"></i>
            热门文章
          </h3>
          <ul class="hot-article-list" v-if="hotArticles.length > 0">
            <li v-for="(article, index) in hotArticles.slice(0, 8)" :key="article.id" class="hot-article-item">
              <span class="article-rank" :class="{ 'rank-top': index < 3 }">{{ index + 1 }}</span>
              <router-link :to="{ name: 'PublicBlogArticleDetail', params: { id: (article.id ?? article.articleId ?? article.uuid) } }" class="hot-article-link" :title="article.title">
                {{ article.title }}
              </router-link>
            </li>
          </ul>
          <div v-else class="no-data">
            <i class="el-icon-star-on"></i>
            <p>暂无热门文章</p>
          </div>
        </div>

        <!-- 文章归档 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <i class="el-icon-date"></i>
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
            <i class="el-icon-date"></i>
            <p>暂无归档</p>
          </div>
        </div>

        <!-- 最新评论 -->
        <div class="sidebar-widget">
          <h3 class="widget-title">
            <i class="el-icon-chat-dot-round"></i>
            最新评论
          </h3>
          <div class="recent-comments">
            <div v-if="recentComments.length === 0" class="no-comments">
              <i class="el-icon-chat-line-round"></i>
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
        <i class="el-icon-arrow-up"></i>
      </div>
    </transition>

    <!-- 微信二维码对话框 -->
    <el-dialog v-model="showWechatQR" title="微信二维码" width="300px" center>
      <div class="qr-code-container">
        <img :src="blogSettings.wechat_qr || 'https://via.placeholder.com/200x200/409EFF/FFFFFF?text=微信二维码'" alt="微信二维码" />
        <p>扫码添加微信</p>
      </div>
    </el-dialog>

    <!-- 联系对话框 -->
    <el-dialog v-model="showContactDialog" title="联系我" width="500px">
      <el-form :model="contactForm" label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="contactForm.name" placeholder="请输入您的姓名" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="contactForm.email" placeholder="请输入您的邮箱" type="email" />
        </el-form-item>
        <el-form-item label="主题">
          <el-input v-model="contactForm.subject" placeholder="请输入主题" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="contactForm.message" type="textarea" :rows="5" placeholder="请输入留言内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showContactDialog = false">取消</el-button>
        <el-button type="primary" @click="handleContactSubmit">发送</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getArticleList, getArticleListAnonymous, getHotArticles, searchArticles, getArticleArchive } from '@/api/blog/article'
import { getCategoryList } from '@/api/blog/category'
import { getBlogSettings, getBlogSettingsAnonymous } from '@/api/blog/setting'
import { getTagCloud } from '@/api/blog/tag'
import BlogNav from '@/components/BlogNav.vue'

// 响应式数据
const articleList = ref([])
const categoryList = ref([])
const hotArticles = ref([])
const blogSettings = ref({})
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
const showContactDialog = ref(false)
const contactForm = reactive({
  name: '',
  email: '',
  subject: '',
  message: ''
})

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

    let response;
    if (searchKeyword.value) {
      // 如果有搜索关键词，则执行搜索
      response = await searchArticles(searchKeyword.value, queryParams)
    } else {
      // 否则获取所有文章 - 优先使用匿名访问接口
      try {
        response = await getArticleList(queryParams)
      } catch (error) {
        // 如果标准接口失败，尝试匿名接口
        console.warn('标准文章接口访问失败，尝试匿名接口:', error)
        response = await getArticleListAnonymous(queryParams)
      }
    }

    console.log('文章列表响应:', response)
    
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
    
    if (append) {
      articleList.value = [...articleList.value, ...newArticles]
    } else {
      articleList.value = newArticles
    }
    total.value = totalCount
    totalArticles.value = totalCount
  } catch (error) {
    console.error('获取文章列表失败:', error)
    // 如果是网络错误或接口不存在，显示友好提示
    if (error.response && error.response.status === 404) {
      console.warn('文章接口暂未实现，使用模拟数据')
      // 使用模拟数据
      const mockArticles = [
        {
          id: 1,
          title: '欢迎使用博客系统',
          summary: '这是一个基于RuoYi-Vue的博客系统，支持文章管理、分类管理、标签管理等功能。',
          content: '这是一个基于RuoYi-Vue的博客系统...',
          coverUrl: 'https://via.placeholder.com/400x200/409EFF/FFFFFF?text=博客系统',
          categoryName: '系统公告',
          createTime: new Date().toISOString(),
          viewCount: 100,
          likeCount: 10,
          commentCount: 5,
          tags: [
            { id: 1, name: 'Vue', color: '#4FC08D' },
            { id: 2, name: 'Spring Boot', color: '#6DB33F' }
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
    // 优先使用匿名访问接口
    let response;
    try {
      response = await getBlogSettings()
    } catch (error) {
      // 如果标准接口失败，尝试匿名接口
      console.warn('标准博客设置接口访问失败，尝试匿名接口:', error)
      response = await getBlogSettingsAnonymous()
    }
    
    console.log('博客设置响应:', response)
    
    // 处理不同的响应格式
    if (response && response.code === 200) {
      blogSettings.value = response.data || {}
    } else if (response && typeof response === 'object') {
      blogSettings.value = response
    }
    
    // 设置最后更新时间
    lastUpdateTime.value = formatDate(new Date().toISOString())
  } catch (error) {
    console.error('获取博客设置失败:', error)
    if (error.response && error.response.status === 404 || error.response && error.response.status === 401) {
      console.warn('博客设置接口暂未实现或匿名访问失败，使用默认设置')
      blogSettings.value = {
        blog_name: '我的博客',
        blog_desc: '这是一个基于RuoYi-Vue的博客系统',
        blog_author: 'nevell',
        author_title: '全栈开发工程师',
        blog_avatar: 'https://via.placeholder.com/80x80/409EFF/FFFFFF?text=博主',
        skills: ['Vue.js', 'Spring Boot', 'Java', 'JavaScript', 'MySQL', 'Redis'],
        github_url: 'https://github.com',
        email: 'contact@example.com',
        wechat_qr: 'https://via.placeholder.com/200x200/409EFF/FFFFFF?text=微信二维码'
      }
    }
    // 设置最后更新时间
    lastUpdateTime.value = formatDate(new Date().toISOString())
  }
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

// 加载更多文章
const loadMoreArticles = () => {
  if (loadingMore.value || articleList.value.length >= total.value) return
  queryParams.pageNum++
  loadArticleList(true)
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

// 处理联系表单提交
const handleContactSubmit = () => {
  if (!contactForm.name || !contactForm.email || !contactForm.message) {
    ElMessage.warning('请填写完整信息')
    return
  }
  
  // 这里可以调用API发送邮件
  ElMessage.success('消息已发送，我会尽快回复您！')
  showContactDialog.value = false
  
  // 重置表单
  contactForm.name = ''
  contactForm.email = ''
  contactForm.subject = ''
  contactForm.message = ''
}

// 组件挂载时加载数据
onMounted(() => {
  loadArticleList()
  loadCategoryList()
  loadHotArticles()
  loadBlogSettings()
  loadTagCloud()
  loadArchiveData()
  
  // 添加滚动监听
  window.addEventListener('scroll', handleScroll)
})

// 组件卸载时移除监听
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
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
  padding: 80px 0;
  text-align: center;
  position: relative;
  overflow: hidden;
  animation: headerFadeIn 0.8s ease-out;
}

@keyframes headerFadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
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
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><circle cx="20" cy="20" r="2" fill="rgba(255,255,255,0.1)"/><circle cx="80" cy="30" r="1" fill="rgba(255,255,255,0.1)"/><circle cx="40" cy="70" r="1.5" fill="rgba(255,255,255,0.1)"/><circle cx="90" cy="80" r="1" fill="rgba(255,255,255,0.1)"/></svg>');
  opacity: 0.6;
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
  max-width: 500px;
  margin: 20px auto 0;
}

.search-input {
  width: 100%;
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
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  position: relative;
  animation: cardFadeIn 0.6s ease-out backwards;
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
}

.article-title {
  margin: 0 0 15px 0;
  font-size: 1.5rem;
  line-height: 1.4;
  font-weight: 600;
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
}

.article-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 18px;
  flex-wrap: wrap;
  margin-top: auto;
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
  margin-top: auto;
  padding-top: 15px;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
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

.contact-btn {
  width: 100%;
  margin-top: 15px;
  border-radius: 20px;
  font-weight: 600;
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
    padding: 40px 0;
  }

  .blog-title {
    font-size: 2.2rem;
  }

  .blog-description {
    font-size: 1.1rem;
    padding: 0 20px;
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

  .article-item {
    margin-bottom: 20px;
  }

  .article-content {
    padding: 20px;
  }

  .article-title {
    font-size: 1.4rem;
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
  }

  .search-input :deep(.el-input__inner) {
    font-size: 16px; /* 防止iOS Safari缩放 */
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

  .article-content {
    padding: 15px;
  }

  .article-title {
    font-size: 1.1rem;
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