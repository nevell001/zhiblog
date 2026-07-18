import { describe, it, expect, beforeEach, vi } from 'vitest'
import {
  APP_THEME_STORAGE_KEY,
  applyAppTheme,
  getDarkColor,
  getLightColor,
  handleThemeStyle,
  hexToRgb,
  normalizeAppTheme,
  rgbToHex
} from './theme'

// Mock document
const mockStyle: any = {
  setProperty: vi.fn()
}

function createRootStub(): HTMLElement {
  const classes = new Set<string>()
  return {
    classList: {
      add: (className: string) => classes.add(className),
      remove: (className: string) => classes.delete(className),
      contains: (className: string) => classes.has(className)
    }
  } as HTMLElement
}

vi.stubGlobal('document', {
  documentElement: {
    style: mockStyle
  }
})

describe('theme 工具测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('hexToRgb', () => {
    it('应该将 hex 颜色转换为 rgb 数组', () => {
      expect(hexToRgb('#ffffff')).toEqual([255, 255, 255])
      expect(hexToRgb('#000000')).toEqual([0, 0, 0])
      expect(hexToRgb('#409eff')).toEqual([64, 158, 255])
    })

    it('应该处理没有 # 前缀的颜色', () => {
      expect(hexToRgb('ffffff')).toEqual([255, 255, 255])
      expect(hexToRgb('409eff')).toEqual([64, 158, 255])
    })

    it('应该处理无效颜色并返回默认值', () => {
      // hexToRgb 函数对无效输入返回 [0, 0, 0]
      expect(hexToRgb('')).toEqual([0, 0, 0])
      expect(hexToRgb('invalid')).toEqual([0, 0, 0]) // 匹配3个字符，转换为有效的hex
      expect(hexToRgb('#')).toEqual([0, 0, 0])
    })
  })

  describe('rgbToHex', () => {
    it('应该将 rgb 颜色转换为 hex 颜色', () => {
      expect(rgbToHex(255, 255, 255)).toBe('#ffffff')
      expect(rgbToHex(0, 0, 0)).toBe('#000000')
      expect(rgbToHex(64, 158, 255)).toBe('#409eff')
    })

    it('应该正确处理单个数字的十六进制值', () => {
      expect(rgbToHex(1, 1, 1)).toBe('#010101')
      expect(rgbToHex(15, 15, 15)).toBe('#0f0f0f')
    })
  })

  describe('getLightColor', () => {
    it('应该生成变浅的颜色', () => {
      const lightColor = getLightColor('#409eff', 0.1)
      expect(lightColor).toMatch(/^#[0-9a-fA-F]{6}$/)
    })

    it('应该根据 level 生成不同深度的浅色', () => {
      const color1 = getLightColor('#409eff', 0.1)
      const color2 = getLightColor('#409eff', 0.5)
      expect(color1).not.toBe(color2)
    })
  })

  describe('getDarkColor', () => {
    it('应该生成变深的颜色', () => {
      const darkColor = getDarkColor('#409eff', 0.1)
      expect(darkColor).toMatch(/^#[0-9a-fA-F]{6}$/)
    })

    it('应该根据 level 生成不同深度的深色', () => {
      const color1 = getDarkColor('#409eff', 0.1)
      const color2 = getDarkColor('#409eff', 0.5)
      expect(color1).not.toBe(color2)
    })
  })

  describe('handleThemeStyle', () => {
    it('应该设置主色调', () => {
      handleThemeStyle('#409eff')
      expect(mockStyle.setProperty).toHaveBeenCalledWith('--el-color-primary', '#409eff')
    })

    it('应该设置浅色变体', () => {
      handleThemeStyle('#409eff')
      expect(mockStyle.setProperty).toHaveBeenCalledWith(
        '--el-color-primary-light-1',
        expect.any(String)
      )
    })

    it('应该设置深色变体', () => {
      handleThemeStyle('#409eff')
      expect(mockStyle.setProperty).toHaveBeenCalledWith(
        '--el-color-primary-dark-1',
        expect.any(String)
      )
    })

    it('应该处理无效的主题并使用默认值', () => {
      handleThemeStyle('')
      expect(mockStyle.setProperty).toHaveBeenCalledWith('--el-color-primary', '#409EFF')
    })

    it('应该处理 undefined 主题并使用默认值', () => {
      handleThemeStyle(undefined as any)
      expect(mockStyle.setProperty).toHaveBeenCalledWith('--el-color-primary', '#409EFF')
    })

    it('应该生成所有9种浅色变体', () => {
      handleThemeStyle('#409eff')
      for (let i = 1; i <= 9; i++) {
        expect(mockStyle.setProperty).toHaveBeenCalledWith(
          `--el-color-primary-light-${i}`,
          expect.any(String)
        )
      }
    })

    it('应该生成所有9种深色变体', () => {
      handleThemeStyle('#409eff')
      for (let i = 1; i <= 9; i++) {
        expect(mockStyle.setProperty).toHaveBeenCalledWith(
          `--el-color-primary-dark-${i}`,
          expect.any(String)
        )
      }
    })
  })

  describe('app theme helpers', () => {
    it('应该把未知主题归一化为 default', () => {
      expect(normalizeAppTheme('mo-blog')).toBe('mo-blog')
      expect(normalizeAppTheme('default')).toBe('default')
      expect(normalizeAppTheme('unknown')).toBe('default')
      expect(normalizeAppTheme(undefined)).toBe('default')
    })

    it('应该在根节点挂载 mo-blog class 并持久化', () => {
      const root = createRootStub()
      const storage = window.localStorage
      storage.clear()

      applyAppTheme('mo-blog', root, storage)

      expect(root.classList.contains('theme-mo-blog')).toBe(true)
      expect(storage.getItem(APP_THEME_STORAGE_KEY)).toBe('mo-blog')
    })

    it('应该切回 default 并移除 mo-blog class', () => {
      const root = createRootStub()
      root.classList.add('theme-mo-blog')
      const storage = window.localStorage

      applyAppTheme('default', root, storage)

      expect(root.classList.contains('theme-mo-blog')).toBe(false)
      expect(storage.getItem(APP_THEME_STORAGE_KEY)).toBe('default')
    })
  })
})
