import { defineStore } from 'pinia'

export const useBlogSettingsStore = defineStore('blogSettings', {
  state: () => ({
    blogSettings: {
      // 基本设置
      blog_name: '我的博客',
      blog_desc: '欢迎来到我的博客',
      blog_author: 'nevell',
      blog_email: '',
      blog_url: '',
      blog_start_time: null,
      blog_avatar: null, // 使用null而不是空字符串，便于区分默认状态
      blog_signature: '',
      blog_keywords: '',
      blog_copyright: '',
      blog_beian: '',

      // SEO设置
      seo_title: '',
      seo_description: '',
      seo_canonical_url: '',
      seo_robots: 'index,follow',
      seo_favicon: '',

      // 个性化设置
      theme_color: '#409EFF',
      header_background: '#304156',
      sidebar_style: 'dark',

      // 功能设置
      comment_enabled: true,
      comment_review: true,
      like_enabled: true,
      view_count_enabled: true,
      share_enabled: true,
      search_enabled: true,
      sidebar_enabled: true,
      footer_enabled: true,
      copyright_enabled: true,

      // 其他设置
      page_size: 10,
      hot_article_count: 5,
      recent_comment_count: 5,
      greeting_message: '欢迎来到我的博客！',
      about_content: '',

      // 博主信息
      author_title: '全栈开发工程师',
      author_bio: '',
      github_url: '',
      weibo_url: '',
      wechat_qr: '',
      author_location: '',
      personal_website: ''
    },
    lastUpdate: 0
  }),

  getters: {
    getBlogAvatar: state => {
      const avatar = state.blogSettings.blog_avatar

      // 如果没有设置头像，返回默认头像
      if (!avatar || !avatar.trim()) {
        return "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='80' height='80' viewBox='0 0 80 80'%3E%3Crect width='80' height='80' fill='%23409EFF' rx='40'/%3E%3Ccircle cx='40' cy='30' r='14' fill='white'/%3E%3Cellipse cx='40' cy='58' rx='20' ry='16' fill='white'/%3E%3C/svg%3E"
      }

      // 引入processAvatarUrl函数处理头像URL
      const { processAvatarUrl } = require('@/api/blog/avatar')

      // 添加时间戳避免缓存
      const timestamp = state.lastUpdate || Date.now()
      let finalUrl = processAvatarUrl(avatar)

      // 如果URL已经包含查询参数，添加时间戳参数
      const separator = finalUrl.includes('?') ? '&' : '?'
      finalUrl = `${finalUrl}${separator}t=${timestamp}`

      console.log('🔗 构建头像URL:', {
        originalAvatar: avatar,
        timestamp: timestamp,
        lastUpdate: state.lastUpdate,
        finalUrl: finalUrl
      })

      return finalUrl
    },
    getBlogAuthor: state => state.blogSettings.blog_author || 'nevell',
    getBlogName: state => state.blogSettings.blog_name || '我的博客'
  },

  actions: {
    updateBlogSettings(newSettings) {
      this.blogSettings = { ...this.blogSettings, ...newSettings }
      this.lastUpdate = Date.now()
      console.log('博客设置已更新:', this.blogSettings)
    },

    updateBlogAvatar(avatarUrl) {
      this.blogSettings.blog_avatar = avatarUrl
      this.lastUpdate = Date.now()
      console.log('博客头像已更新:', avatarUrl)
    },

    setBlogSettings(settings) {
      this.blogSettings = settings
      this.lastUpdate = Date.now()
    },

    // 从后台API更新设置
    async refreshFromAdmin() {
      try {
        // 这里可以调用后台API来获取最新设置
        const { listSetting } = await import('@/api/admin/blog/setting')
        const response = await listSetting({ pageSize: 100 })

        if (response && response.code === 200) {
          const settings = response.rows || response.data || []
          const newSettings = {}

          settings.forEach(setting => {
            if (setting.configKey) {
              newSettings[setting.configKey] = setting.configValue
            }
          })

          this.updateBlogSettings(newSettings)
          return true
        }
      } catch (error) {
        console.error('从后台刷新博客设置失败:', error)
      }
      return false
    }
  }
})
