import { getConfigKey } from '@/api/system/config'
import { useSettingsStore } from '@/stores/settings'
import { handleThemeStyle } from '@/utils/theme'
import { getToken } from '@/utils/auth'
import type { Config } from '@/api/system/config'

/**
 * 初始化博客个性化设置
 */
export async function initBlogSettings(): Promise<void> {
  try {
    // 检查是否有token，只有有token时才发起API请求
    if (!getToken()) {
      // 未登录状态，使用默认配置，不发起API请求
      return
    }

    // 获取博客个性化设置
    const [themeColor, logoUrl, sidebarStyle, customCss] = await Promise.all([
      getConfigKey('blog.custom.themeColor').catch(
        (): Config => ({ configKey: '', configValue: '', configName: '' })
      ),
      getConfigKey('blog.custom.logo').catch(
        (): Config => ({ configKey: '', configValue: '', configName: '' })
      ),
      getConfigKey('blog.custom.sidebarStyle').catch(
        (): Config => ({ configKey: '', configValue: '', configName: '' })
      ),
      getConfigKey('blog.custom.customCss').catch(
        (): Config => ({ configKey: '', configValue: '', configName: '' })
      )
    ])

    const settingsStore = useSettingsStore()

    // 应用主题颜色
    if (themeColor.configValue && themeColor.configValue !== '') {
      settingsStore.changeSetting({ key: 'theme', value: themeColor.configValue })
      handleThemeStyle(themeColor.configValue)
    }

    // 应用Logo
    if (logoUrl.configValue && logoUrl.configValue !== '') {
      applyLogo(logoUrl.configValue)
    }

    // 应用侧边栏样式
    if (sidebarStyle.configValue && sidebarStyle.configValue !== '') {
      applySidebarStyle(sidebarStyle.configValue)
    }

    // 应用自定义CSS
    if (customCss.configValue && customCss.configValue !== '') {
      applyCustomCss(customCss.configValue)
    }
  } catch {
    // 静默处理错误，避免影响页面功能
  }
}

/**
 * 应用Logo
 * @param logoUrl Logo URL
 */
function applyLogo(logoUrl: string): void {
  const logoElements = document.querySelectorAll('.sidebar-logo-container img, .navbar-brand img')
  logoElements.forEach((element: Element) => {
    const imgElement = element as HTMLImageElement
    imgElement.src = logoUrl
    imgElement.style.height = '32px' // 设置合适的高度
    imgElement.style.width = 'auto' // 自动宽度
  })
}

/**
 * 应用侧边栏样式
 * @param style 侧边栏样式 (dark/light)
 */
function applySidebarStyle(style: string): void {
  const sidebarElement = document.querySelector('.sidebar-container')
  if (sidebarElement) {
    if (style === 'dark') {
      sidebarElement.classList.add('theme-dark')
      sidebarElement.classList.remove('theme-light')
    } else {
      sidebarElement.classList.add('theme-light')
      sidebarElement.classList.remove('theme-dark')
    }

    const settingsStore = useSettingsStore()
    settingsStore.changeSetting({ key: 'sideTheme', value: `theme-${style}` })
  }
}

/**
 * 应用自定义CSS
 * @param css 自定义CSS内容
 */
function applyCustomCss(css: string): void {
  let styleElement = document.getElementById('blog-custom-css')
  if (!styleElement) {
    styleElement = document.createElement('style')
    styleElement.id = 'blog-custom-css'
    document.head.appendChild(styleElement)
  }
  styleElement.textContent = css
}

/**
 * 重新加载博客设置
 * 用于设置页面保存后刷新设置
 */
export async function reloadBlogSettings(): Promise<void> {
  await initBlogSettings()

  try {
    // 刷新SEO设置
    const { useDynamicTitle } = await import('./dynamicTitle')
    useDynamicTitle()
  } catch (error) {
    console.error('刷新SEO设置失败:', error)
  }
}
