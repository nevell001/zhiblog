import { getConfigKey } from "@/api/system/config"
import useSettingsStore from '@/store/modules/settings'
import { handleThemeStyle } from '@/utils/theme'

/**
 * 初始化博客个性化设置
 */
export async function initBlogSettings() {
  try {
    // 获取博客个性化设置
    const [themeColor, logoUrl, sidebarStyle, customCss] = await Promise.all([
      getConfigKey('blog.custom.themeColor').catch(() => ({ data: '' })),
      getConfigKey('blog.custom.logo').catch(() => ({ data: '' })),
      getConfigKey('blog.custom.sidebarStyle').catch(() => ({ data: '' })),
      getConfigKey('blog.custom.customCss').catch(() => ({ data: '' }))
    ])
    
    const settingsStore = useSettingsStore()
    
    // 应用主题颜色
    if (themeColor.data && themeColor.data !== '') {
      settingsStore.changeSetting({ key: 'theme', value: themeColor.data })
      handleThemeStyle(themeColor.data)
    }
    
    // 应用Logo
    if (logoUrl.data && logoUrl.data !== '') {
      applyLogo(logoUrl.data)
    }
    
    // 应用侧边栏样式
    if (sidebarStyle.data && sidebarStyle.data !== '') {
      applySidebarStyle(sidebarStyle.data)
    }
    
    // 应用自定义CSS
    if (customCss.data && customCss.data !== '') {
      applyCustomCss(customCss.data)
    }
    
    console.log('博客个性化设置初始化完成')
  } catch (error) {
    // 静默处理错误，避免影响页面功能
    console.log('未登录或无法加载博客设置，使用默认配置')
  }
}

/**
 * 应用Logo
 * @param {string} logoUrl Logo URL
 */
function applyLogo(logoUrl) {
  const logoElements = document.querySelectorAll('.sidebar-logo-container img, .navbar-brand img')
  logoElements.forEach(element => {
    element.src = logoUrl
    element.style.height = '32px' // 设置合适的高度
    element.style.width = 'auto' // 自动宽度
  })
}

/**
 * 应用侧边栏样式
 * @param {string} style 侧边栏样式 (dark/light)
 */
function applySidebarStyle(style) {
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
 * @param {string} css 自定义CSS内容
 */
function applyCustomCss(css) {
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
export async function reloadBlogSettings() {
  await initBlogSettings()
  
  try {
    // 刷新SEO设置
    const { useDynamicTitle } = await import('./dynamicTitle')
    useDynamicTitle()
  } catch (error) {
    console.error('刷新SEO设置失败:', error)
  }
}