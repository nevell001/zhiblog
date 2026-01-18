import { defineStore } from 'pinia'
import { processAvatarUrl } from '@/api/blog/avatar'

interface BlogSettingsState {
  blog_name: string
  blog_desc: string
  blog_author: string
  blog_email: string
  blog_url: string
  blog_start_time: string | null
  blog_avatar: string | null
  blog_signature: string
  blog_keywords: string
  blog_copyright: string
  blog_beian: string
  seo_title: string
  seo_description: string
  seo_canonical_url: string
  seo_robots: string
  seo_favicon: string
  theme_color: string
  header_background: string
  sidebar_style: string
  comment_enabled: boolean
  comment_review: boolean
  like_enabled: boolean
  view_count_enabled: boolean
  share_enabled: boolean
  search_enabled: boolean
  sidebar_enabled: boolean
  footer_enabled: boolean
  copyright_enabled: boolean
  page_size: number
  hot_article_count: number
  recent_comment_count: number
  greeting_message: string
  about_content: string
  author_title: string
  author_bio: string
  github_url: string
  weibo_url: string
  wechat_qr: string
  author_location: string
  personal_website: string
  skills?: string[]
}

interface BlogSettingsStoreState {
  blogSettings: BlogSettingsState
  lastUpdate: number
}

export const useBlogSettingsStore = defineStore('blogSettings', {
  state: (): BlogSettingsStoreState => ({
    blogSettings: {
      // 基本设置
      blog_name: '我的博客',
      blog_desc: '欢迎来到我的博客',
      blog_author: 'nevell',
      blog_email: '',
      blog_url: '',
      blog_start_time: null,
      blog_avatar: null,
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
    getBlogAvatar: (state: BlogSettingsStoreState) => {
      const avatar = state.blogSettings.blog_avatar

      if (!avatar || !avatar.trim()) {
        return "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='80' height='80' viewBox='0 0 80 80'%3E%3Crect width='80' height='80' fill='%23409EFF' rx='40'/%3E%3Ccircle cx='40' cy='30' r='14' fill='white'/%3E%3Cellipse cx='40' cy='58' rx='20' ry='16' fill='white'/%3E%3C/svg%3E"
      }

      try {
        const timestamp = state.lastUpdate || Date.now()
        let finalUrl = processAvatarUrl(avatar)

        const separator = finalUrl.includes('?') ? '&' : '?'
        finalUrl = `${finalUrl}${separator}t=${timestamp}`

        return finalUrl
      } catch (error) {
        console.error('处理头像URL失败:', error)
        return "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='80' height='80' viewBox='0 0 80 80'%3E%3Crect width='80' height='80' fill='%23409EFF' rx='40'/%3E%3Ccircle cx='40' cy='30' r='14' fill='white'/%3E%3Cellipse cx='40' cy='58' rx='20' ry='16' fill='white'/%3E%3C/svg%3E"
      }
    },

    getBlogAuthor: (state: BlogSettingsStoreState) => state.blogSettings.blog_author || 'nevell',
    getBlogName: (state: BlogSettingsStoreState) => state.blogSettings.blog_name || '我的博客'
  },

  actions: {
    updateBlogSettings(newSettings: Partial<BlogSettingsState>): void {
      this.blogSettings = { ...this.blogSettings, ...newSettings }
      this.lastUpdate = Date.now()
    },

    updateBlogAvatar(avatarUrl: string): void {
      this.blogSettings.blog_avatar = avatarUrl
      this.lastUpdate = Date.now()
    },

    setBlogSettings(settings: BlogSettingsState): void {
      this.blogSettings = settings
      this.lastUpdate = Date.now()
    },

    async refreshFromAdmin(): Promise<boolean> {
      try {
        const { listSetting } = await import('@/api/admin/blog/setting')
        const response = await listSetting({ pageSize: 100 })

        if (response && response.code === 200) {
          const settings = response.rows || []
          const newSettings: Record<string, any> = {}

          settings.forEach((setting: any) => {
            if (setting.configKey) {
              newSettings[setting.configKey] = setting.configValue
            }
          })

          this.updateBlogSettings(newSettings as Partial<BlogSettingsState>)
          return true
        }
      } catch (error) {
        console.error('从后台刷新博客设置失败:', error)
      }
      return false
    }
  }
})
