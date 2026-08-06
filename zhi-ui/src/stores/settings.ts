import { defineStore } from 'pinia'
import defaultSettings from '@/settings'
import { applyAppTheme, getStoredAppTheme, normalizeAppTheme, type AppTheme } from '@/utils/theme'

const ADMIN_THEME_STORAGE_KEY = 'admin-theme'
const THEME_MODE_KEY = 'admin-theme-mode'

// 主题模式类型
export type ThemeMode = 'light' | 'dark' | 'system'

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
  themeMode: ThemeMode
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

// 检测系统主题偏好
function getSystemThemePreference(): boolean {
  if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
    return true
  }
  return false
}

// 获取存储的主题模式
function getStoredThemeMode(): ThemeMode {
  try {
    const stored = localStorage.getItem(THEME_MODE_KEY)
    if (stored === 'light' || stored === 'dark' || stored === 'system') {
      return stored as ThemeMode
    }
  } catch {
    // Ignore
  }
  return 'system' // 默认跟随系统
}

// 获取实际的主题状态（考虑系统偏好）
function getEffectiveDarkMode(mode: ThemeMode): boolean {
  if (mode === 'system') {
    return getSystemThemePreference()
  }
  return mode === 'dark'
}

function applyAdminDarkMode(isDark: boolean): void {
  document.documentElement.classList.toggle('dark', isDark)
}

// 持久化主题模式
function persistThemeMode(mode: ThemeMode): void {
  try {
    localStorage.setItem(THEME_MODE_KEY, mode)
  } catch {
    // Ignore storage failures
  }
}

// 切换应用主题时同步更新 layout-setting 中的 appTheme 字段，
// 避免刷新后因 layout-setting 旧值回退到之前的主题。
function persistLayoutSettingAppTheme(theme: AppTheme): void {
  try {
    const stored = getStoredLayoutSetting()
    const merged = { ...stored, appTheme: theme }
    localStorage.setItem('layout-setting', JSON.stringify(merged))
  } catch {
    // Ignore storage failures; applyAppTheme already persisted the app-theme key.
  }
}

// 系统主题变化监听器
let mediaQueryListener: ((this: MediaQueryList, ev: MediaQueryListEvent) => any) | null = null

function setupSystemThemeListener(callback: (isDark: boolean) => void): void {
  if (window.matchMedia) {
    // 移除旧的监听器
    if (mediaQueryListener) {
      const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)')
      mediaQuery.removeEventListener('change', mediaQueryListener)
    }

    // 添加新的监听器
    mediaQueryListener = function (this: MediaQueryList, ev: MediaQueryListEvent) {
      callback(ev.matches)
    }

    const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)')
    mediaQuery.addEventListener('change', mediaQueryListener)
  }
}

export const useSettingsStore = defineStore('settings', {
  state: (): SettingsState => {
    const storedLayout = getStoredLayoutSetting()
    const appTheme = normalizeAppTheme(storedLayout.appTheme ?? getStoredAppTheme())
    const themeMode = getStoredThemeMode()
    const isDark = getEffectiveDarkMode(themeMode)

    applyAppTheme(appTheme)
    applyAdminDarkMode(isDark)

    // 如果是跟随系统模式，设置监听器
    if (themeMode === 'system') {
      setupSystemThemeListener(systemIsDark => {
        const settingsStore = useSettingsStore()
        settingsStore.isDark = systemIsDark
        applyAdminDarkMode(systemIsDark)
      })
    }

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
      isDark,
      themeMode
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

    // 循环切换主题模式: light -> dark -> system -> light
    toggleTheme(): void {
      const modeCycle: ThemeMode[] = ['light', 'dark', 'system']
      const currentIndex = modeCycle.indexOf(this.themeMode)
      const nextMode = modeCycle[(currentIndex + 1) % modeCycle.length]
      this.setThemeMode(nextMode)
    },

    // 设置主题模式
    setThemeMode(mode: ThemeMode): void {
      this.themeMode = mode
      persistThemeMode(mode)

      const isDark = getEffectiveDarkMode(mode)
      this.isDark = isDark
      applyAdminDarkMode(isDark)

      // 如果是跟随系统模式，设置监听器
      if (mode === 'system') {
        setupSystemThemeListener(systemIsDark => {
          this.isDark = systemIsDark
          applyAdminDarkMode(systemIsDark)
        })
      }
    },

    setAppTheme(theme: unknown): void {
      const nextTheme = normalizeAppTheme(theme)
      this.appTheme = nextTheme
      applyAppTheme(nextTheme)
      persistLayoutSettingAppTheme(nextTheme)
    },

    setServerMessage(message: string): void {
      this.serverMessage = message
    },

    setIsShowUpload(status: boolean): void {
      this.isShowUpload = status
    }
  }
})
