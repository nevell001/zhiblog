import { defineStore } from 'pinia'

export const useBlogSettingsStore = defineStore('blogSettings', {
  state: () => ({
    blogSettings: {
      blog_name: '我的博客',
      blog_desc: '欢迎来到我的博客',
      blog_author: 'nevell',
      blog_avatar: '',
      blog_signature: '',
      blog_email: '',
      blog_url: '',
      github_url: '',
      weibo_url: '',
      wechat_qr: '',
      author_title: '全栈开发工程师',
      author_bio: '',
      author_location: '',
      personal_website: ''
    },
    lastUpdate: 0
  }),

  getters: {
    getBlogAvatar: (state) => {
      const avatar = state.blogSettings.blog_avatar;
      if (avatar && avatar.trim() && avatar !== 'https://via.placeholder.com/80x80/409EFF/FFFFFF?text=博主') {
        return avatar;
      }
      // 返回默认头像（使用Data URL）
      return 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iODAiIGhlaWdodD0iODAiIHZpZXdCb3g9IjAgMCA4MCA4MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjgwIiBoZWlnaHQ9IjgwIiByeD0iNDAiIGZpbGw9IiM0MDlFRkYiLz4KPGNpcmNsZSBjeD0iNDAiIGN5PSIzMCIgcj0iMTQiIGZpbGw9IndoaXRlIi8+CjxlbGxpcHNlIGN4PSI0MCIgY3k9IjU4IiByeD0iMjAiIHJ5PSIxNiIgZmlsbD0id2hpdGUiLz4KPHN2Zz4K';
    },
    getBlogAuthor: (state) => state.blogSettings.blog_author || 'nevell',
    getBlogName: (state) => state.blogSettings.blog_name || '我的博客'
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