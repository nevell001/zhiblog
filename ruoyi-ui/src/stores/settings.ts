import { defineStore } from 'pinia'
import defaultSettings from '@/settings'
import { applyAppTheme, getStoredAppTheme, normalizeAppTheme, type AppTheme } from '@/utils/theme'

const ADMIN_THEME_STORAGE_KEY = 'admin-theme'

interface SettingsState {
  title: string
  appTheme: AppTheme
  theme: string
  sideTheme: string
  showSettings: boolean
  showTagsView: boolean
  tagsView: boolean
  showSidebarLogo: boolean
  sidebarLogo: boolean
  fixedHeader: boolean
  sidebarTextTheme: string
  serverMessage: string
  isShowUpload: boolean
  topNav: boolean
  tagsIcon: boolean
  dynamicTitle: boolean
  footerVisible: boolean
  footerContent: string
  isDark: boolean
}

type StoredLayoutSetting = Partial<Omit<SettingsState, 'appTheme'>> & {
  appTheme?: unknown
}

function getStoredLayoutSetting(): StoredLayoutSetting {
  try {
    return JSON.parse(localStorage.getItem('layout-setting') || '{}') as StoredLayoutSetting
  } catch {
    return {}
  }
}

function resolveStoredBoolean(value: unknown, fallback: boolean): boolean {
  return typeof value === 'boolean' ? value : fallback
}

function getStoredAdminDarkMode(): boolean {
  try {
    return localStorage.getItem(ADMIN_THEME_STORAGE_KEY) === 'dark'
  } catch {
    return false
  }
}

function applyAdminDarkMode(isDark: boolean): void {
  document.documentElement.classList.toggle('dark', isDark)
  try {
    localStorage.setItem(ADMIN_THEME_STORAGE_KEY, isDark ? 'dark' : 'light')
  } catch {
    // Ignore storage failures; the DOM class is the visible source of truth.
  }
}

export const useSettingsStore = defineStore('settings', {
  state: (): SettingsState => {
    const storedLayout = getStoredLayoutSetting()
    const appTheme = normalizeAppTheme(storedLayout.appTheme ?? getStoredAppTheme())
    const isDark = getStoredAdminDarkMode()
    applyAppTheme(appTheme)
    applyAdminDarkMode(isDark)

    return {
      title: defaultSettings.title,
      appTheme,
      theme: typeof storedLayout.theme === 'string' ? storedLayout.theme : defaultSettings.theme,
      sideTheme:
        storedLayout.sideTheme === 'theme-light' || storedLayout.sideTheme === 'theme-dark'
          ? storedLayout.sideTheme
          : defaultSettings.sideTheme,
      showSettings: defaultSettings.showSettings,
      showTagsView: resolveStoredBoolean(storedLayout.tagsView, defaultSettings.tagsView),
      tagsView: resolveStoredBoolean(storedLayout.tagsView, defaultSettings.tagsView),
      showSidebarLogo: resolveStoredBoolean(storedLayout.sidebarLogo, defaultSettings.sidebarLogo),
      sidebarLogo: resolveStoredBoolean(storedLayout.sidebarLogo, defaultSettings.sidebarLogo),
      fixedHeader: resolveStoredBoolean(storedLayout.fixedHeader, defaultSettings.fixedHeader),
      sidebarTextTheme: defaultSettings.sidebarTextTheme,
      serverMessage: '',
      isShowUpload: false,
      topNav: resolveStoredBoolean(storedLayout.topNav, defaultSettings.topNav),
      tagsIcon: resolveStoredBoolean(storedLayout.tagsIcon, defaultSettings.tagsIcon),
      dynamicTitle: resolveStoredBoolean(storedLayout.dynamicTitle, defaultSettings.dynamicTitle),
      footerVisible: resolveStoredBoolean(
        storedLayout.footerVisible,
        defaultSettings.footerVisible
      ),
      footerContent: defaultSettings.footerContent,
      isDark
    }
  },

  getters: {
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
      this.isDark = !this.isDark
      applyAdminDarkMode(this.isDark)
    },

    setAppTheme(theme: unknown): void {
      const nextTheme = normalizeAppTheme(theme)
      this.appTheme = nextTheme
      applyAppTheme(nextTheme)
    },

    setServerMessage(message: string): void {
      this.serverMessage = message
    },

    setIsShowUpload(status: boolean): void {
      this.isShowUpload = status
    }
  }
})
