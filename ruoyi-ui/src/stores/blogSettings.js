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

      // 优先使用实际存在的头像
      if (avatar && avatar.trim() && !avatar.startsWith('data:') && avatar !== 'https://via.placeholder.com/80x80/409EFF/FFFFFF?text=博主') {
        return avatar;
      }

      // 拒绝Base64格式，使用内嵌SVG默认头像
      if (avatar && avatar.startsWith('data:')) {
        return "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='80' height='80' viewBox='0 0 80 80'%3E%3Crect width='80' height='80' fill='%23409EFF' rx='40'/%3E%3Ccircle cx='40' cy='30' r='14' fill='white'/%3E%3Cellipse cx='40' cy='58' rx='20' ry='16' fill='white'/%3E%3C/svg%3E";
      }

      // 使用已知的实际头像
      return '/profile/upload/2025/12/18/7558113286dd4f65a03c9c6b6a32fdea.jpg';
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