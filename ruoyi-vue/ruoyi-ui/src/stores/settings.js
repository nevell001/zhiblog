import { defineStore } from 'pinia'
import defaultSettings from '@/settings'
import { useColorMode } from '@vueuse/core'
import { computed } from 'vue'

export const useSettingsStore = defineStore('settings', {
  state: () => ({
    title: defaultSettings.title,
    theme: defaultSettings.theme,
    sideTheme: defaultSettings.sideTheme,
    showSettings: defaultSettings.showSettings,
    showTagsView: defaultSettings.showTagsView,
    showSidebarLogo: defaultSettings.showSidebarLogo,
    fixedHeader: defaultSettings.fixedHeader,
    sidebarTextTheme: defaultSettings.sidebarTextTheme,
    serverMessage: '',
    isShowUpload: false
  }),
  getters: {
    colorMode: () => useColorMode(),
    isDark: () => useColorMode().value === 'dark',
    primary: computed(() => {
      return defaultSettings.theme
    })
  },
  actions: {
    changeSetting({ key, value }) {
      if (this.hasOwnProperty(key)) {
        this[key] = value
      }
    },
    setTitle(title) {
      this.title = title
    },
    toggleTheme() {
      const colorMode = this.colorMode
      if (colorMode.value === 'light') {
        colorMode.value = 'dark'
      } else {
        colorMode.value = 'light'
      }
    },
    setServerMessage(message) {
      this.serverMessage = message
    },
    setIsShowUpload(status) {
      this.isShowUpload = status
    }
  }
})