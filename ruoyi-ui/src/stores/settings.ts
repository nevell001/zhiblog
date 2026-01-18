import { defineStore } from 'pinia'
import defaultSettings from '@/settings'

interface SettingsState {
  title: string
  theme: string
  sideTheme: string
  showSettings: boolean
  showTagsView: boolean
  showSidebarLogo: boolean
  fixedHeader: boolean
  sidebarTextTheme: string
  serverMessage: string
  isShowUpload: boolean
  topNav: boolean
  tagsIcon: boolean
  dynamicTitle: boolean
  footerVisible: boolean
  footerContent: string
}

export const useSettingsStore = defineStore('settings', {
  state: (): SettingsState => ({
    title: defaultSettings.title,
    theme: defaultSettings.theme,
    sideTheme: defaultSettings.sideTheme,
    showSettings: defaultSettings.showSettings,
    showTagsView: defaultSettings.tagsView,
    showSidebarLogo: defaultSettings.sidebarLogo,
    fixedHeader: defaultSettings.fixedHeader,
    sidebarTextTheme: defaultSettings.sidebarTextTheme,
    serverMessage: '',
    isShowUpload: false,
    topNav: defaultSettings.topNav,
    tagsIcon: defaultSettings.tagsIcon,
    dynamicTitle: defaultSettings.dynamicTitle,
    footerVisible: defaultSettings.footerVisible,
    footerContent: defaultSettings.footerContent
  }),

  getters: {
    isDark(): boolean {
      // 暂时返回 false，避免在非组件上下文中调用 useColorMode
      return false
    },
    primary(): string {
      return defaultSettings.title || ''
    }
  },

  actions: {
    changeSetting(payload: { key: string; value: any }): void {
      if (Object.prototype.hasOwnProperty.call(this, payload.key)) {
        this[payload.key] = payload.value
      }
    },

    setTitle(title: string): void {
      this.title = title
    },

    toggleTheme(): void {
      // 简化主题切换逻辑，避免依赖 useColorMode
      this.sideTheme = this.sideTheme === 'theme-dark' ? 'theme-light' : 'theme-dark'
    },

    setServerMessage(message: string): void {
      this.serverMessage = message
    },

    setIsShowUpload(status: boolean): void {
      this.isShowUpload = status
    }
  }
})
