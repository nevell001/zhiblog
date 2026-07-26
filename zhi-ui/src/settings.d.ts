/**
 * 应用设置配置类型定义
 */
export interface SettingsConfig {
  /**
   * 网页标题
   */
  title: string

  /**
   * 主题
   */
  theme: string

  /**
   * 侧边栏主题 深色主题theme-dark，浅色主题theme-light
   */
  sideTheme: 'theme-dark' | 'theme-light'

  /**
   * 是否系统布局配置
   */
  showSettings: boolean

  /**
   * 是否显示顶部导航
   */
  topNav: boolean

  /**
   * 是否显示 tagsView
   */
  tagsView: boolean

  /**
   * 显示页签图标
   */
  tagsIcon: boolean

  /**
   * 是否固定头部
   */
  fixedHeader: boolean

  /**
   * 是否显示logo
   */
  sidebarLogo: boolean

  /**
   * 是否显示动态标题
   */
  dynamicTitle: boolean

  /**
   * 是否显示底部版权
   */
  footerVisible: boolean

  /**
   * 底部版权文本内容
   */
  footerContent: string

  /**
   * 侧边栏文字主题
   */
  sidebarTextTheme: string
}
