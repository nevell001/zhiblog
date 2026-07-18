export type AppTheme = 'default' | 'mo-blog'

export const APP_THEME_STORAGE_KEY = 'app-theme'

const APP_THEMES: AppTheme[] = ['default', 'mo-blog']
const MO_BLOG_CLASS = 'theme-mo-blog'

export function normalizeAppTheme(value: unknown): AppTheme {
  return APP_THEMES.includes(value as AppTheme) ? (value as AppTheme) : 'default'
}

export function getStoredAppTheme(storage: Storage | undefined = localStorage): AppTheme {
  try {
    return normalizeAppTheme(storage?.getItem(APP_THEME_STORAGE_KEY))
  } catch {
    return 'default'
  }
}

export function applyAppTheme(
  theme: AppTheme,
  root: HTMLElement = document.documentElement,
  storage: Storage | undefined = localStorage
): void {
  const nextTheme = normalizeAppTheme(theme)
  root.classList.remove(MO_BLOG_CLASS)

  if (nextTheme === 'mo-blog') {
    root.classList.add(MO_BLOG_CLASS)
  }

  try {
    storage?.setItem(APP_THEME_STORAGE_KEY, nextTheme)
  } catch {
    // Storage can be unavailable in private mode or tests; class application still matters.
  }
}

// 处理主题样式
export function handleThemeStyle(theme: string): void {
  // 添加主题参数有效性检查，避免undefined导致的错误
  if (!theme || typeof theme !== 'string') {
    theme = '#409EFF' // 使用Element UI默认主色调作为备用
  }
  document.documentElement.style.setProperty('--el-color-primary', theme)
  for (let i = 1; i <= 9; i++) {
    document.documentElement.style.setProperty(
      `--el-color-primary-light-${i}`,
      `${getLightColor(theme, i / 10)}`
    )
  }
  for (let i = 1; i <= 9; i++) {
    document.documentElement.style.setProperty(
      `--el-color-primary-dark-${i}`,
      `${getDarkColor(theme, i / 10)}`
    )
  }
}

// hex颜色转rgb颜色
export function hexToRgb(str: string): number[] {
  str = str.replace('#', '')
  const hexs = str.match(/../g)
  if (!hexs) return [0, 0, 0]

  const rgb: number[] = []
  for (let i = 0; i < 3; i++) {
    const value = parseInt(hexs[i], 16)
    if (Number.isNaN(value)) {
      return [0, 0, 0]
    }
    rgb[i] = value
  }
  return rgb
}

// rgb颜色转Hex颜色
export function rgbToHex(r: number, g: number, b: number): string {
  const hexs = [r.toString(16), g.toString(16), b.toString(16)]
  for (let i = 0; i < 3; i++) {
    if (hexs[i].length === 1) {
      hexs[i] = `0${hexs[i]}`
    }
  }
  return `#${hexs.join('')}`
}

// 变浅颜色值
export function getLightColor(color: string, level: number): string {
  const rgb = hexToRgb(color)
  for (let i = 0; i < 3; i++) {
    rgb[i] = Math.floor((255 - rgb[i]) * level + rgb[i])
  }
  return rgbToHex(rgb[0], rgb[1], rgb[2])
}

// 变深颜色值
export function getDarkColor(color: string, level: number): string {
  const rgb = hexToRgb(color)
  for (let i = 0; i < 3; i++) {
    rgb[i] = Math.floor(rgb[i] * (1 - level))
  }
  return rgbToHex(rgb[0], rgb[1], rgb[2])
}
