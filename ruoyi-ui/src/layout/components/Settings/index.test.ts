import { describe, it, expect } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'
import Settings from './index.vue'

const sourcePath = resolve(process.cwd(), 'src/layout/components/Settings/index.vue')

describe('Settings 组件测试', () => {
  it('应该导出 Settings 组件', () => {
    expect(Settings).toBeDefined()
    expect(typeof Settings).toBe('object')
  })

  it('保存布局设置时应该持久化应用主题', () => {
    const source = readFileSync(sourcePath, 'utf-8')

    expect(source).toContain('appTheme: storeSettings.value.appTheme')
    expect(source).toContain("localStorage.setItem('layout-setting'")
  })

  it('重置布局设置时应该同时清除应用主题缓存', () => {
    const source = readFileSync(sourcePath, 'utf-8')

    expect(source).toContain("localStorage.removeItem('layout-setting')")
    expect(source).toContain('localStorage.removeItem(APP_THEME_STORAGE_KEY)')
    expect(source).toContain("localStorage.removeItem('admin-theme')")
  })
})
