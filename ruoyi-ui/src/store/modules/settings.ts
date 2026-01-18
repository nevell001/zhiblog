import { defineStore } from 'pinia'
import defaultSettings from '@/settings'
import { useDark, useToggle } from '@vueuse/core'
import { useDynamicTitle } from '@/utils/dynamicTitle'

const {
  sideTheme,
  showSettings,
  topNav,
  tagsView,
  tagsIcon,
  fixedHeader,
  sidebarLogo,
  dynamicTitle,
  footerVisible,
  footerContent
} = defaultSettings

const storageSetting = JSON.parse(localStorage.getItem('layout-setting') || '{}')

interface SettingsState {
  title: string
  theme: string
  sideTheme: string
  showSettings: boolean
  topNav: boolean
  tagsView: boolean
  tagsIcon: boolean
  fixedHeader: boolean
  sidebarLogo: boolean
  dynamicTitle: boolean
  footerVisible: boolean
  footerContent: string
  isDark: boolean
}

const useSettingsStore = defineStore('settings', {
  state: (): SettingsState => ({
    title: '',
    theme: storageSetting.theme || '#409EFF',
    sideTheme: storageSetting.sideTheme || sideTheme,
    showSettings: showSettings,
    topNav: storageSetting.topNav === undefined ? topNav : storageSetting.topNav,
    tagsView: storageSetting.tagsView === undefined ? tagsView : storageSetting.tagsView,
    tagsIcon: storageSetting.tagsIcon === undefined ? tagsIcon : storageSetting.tagsIcon,
    fixedHeader:
      storageSetting.fixedHeader === undefined ? fixedHeader : storageSetting.fixedHeader,
    sidebarLogo:
      storageSetting.sidebarLogo === undefined ? sidebarLogo : storageSetting.sidebarLogo,
    dynamicTitle:
      storageSetting.dynamicTitle === undefined ? dynamicTitle : storageSetting.dynamicTitle,
    footerVisible:
      storageSetting.footerVisible === undefined ? footerVisible : storageSetting.footerVisible,
    footerContent: footerContent,
    isDark: false
  }),
  actions: {
    // 修改布局设置
    changeSetting(data: { key: string; value: any }): void {
      const { key, value } = data
      if (Object.prototype.hasOwnProperty.call(this, key)) {
        ;(this as any)[key] = value
      }
    },
    // 设置网页标题
    setTitle(title: string): void {
      this.title = title
      useDynamicTitle()
    },
    // 切换暗黑模式
    toggleTheme(): void {
      const isDark = useDark()
      const toggleDark = useToggle(isDark)
      this.isDark = !this.isDark
      toggleDark()
    }
  }
})

export { useSettingsStore }

export default useSettingsStore
