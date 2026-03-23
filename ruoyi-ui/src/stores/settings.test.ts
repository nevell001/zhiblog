import { describe, it, expect, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useSettingsStore } from './settings'

describe('Settings Store 测试', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })

  describe('初始状态', () => {
    it('应该初始化为默认值', () => {
      const store = useSettingsStore()
      expect(store.theme).toBeDefined()
      expect(store.sideTheme).toBeDefined()
      expect(store.showSettings).toBeDefined()
      expect(store.topNav).toBeDefined()
      expect(store.showTagsView).toBeDefined()
      expect(store.fixedHeader).toBeDefined()
      expect(store.showSidebarLogo).toBeDefined()
      expect(store.dynamicTitle).toBeDefined()
    })
  })

  describe('changeSetting', () => {
    it('应该更新设置', () => {
      const store = useSettingsStore()
      store.changeSetting({
        key: 'showSettings',
        value: true
      })
      expect(store.showSettings).toBe(true)
    })

    it('应该支持更新主题', () => {
      const store = useSettingsStore()
      store.changeSetting({
        key: 'theme',
        value: '#FF0000'
      })
      expect(store.theme).toBe('#FF0000')
    })

    it('应该支持切换侧边栏主题', () => {
      const store = useSettingsStore()
      store.changeSetting({
        key: 'sideTheme',
        value: 'theme-light'
      })
      expect(store.sideTheme).toBe('theme-light')
    })

    it('应该支持切换顶部导航', () => {
      const store = useSettingsStore()
      store.changeSetting({
        key: 'topNav',
        value: true
      })
      expect(store.topNav).toBe(true)
    })

    it('应该支持切换标签页视图', () => {
      const store = useSettingsStore()
      store.changeSetting({
        key: 'showTagsView',
        value: false
      })
      expect(store.showTagsView).toBe(false)
    })

    it('应该支持固定头部', () => {
      const store = useSettingsStore()
      store.changeSetting({
        key: 'fixedHeader',
        value: true
      })
      expect(store.fixedHeader).toBe(true)
    })
  })

  describe('setTitle', () => {
    it('应该设置标题', () => {
      const store = useSettingsStore()
      store.setTitle('New Title')
      expect(store.title).toBe('New Title')
    })
  })

  describe('toggleTheme', () => {
    it('应该切换主题', () => {
      const store = useSettingsStore()
      const initialTheme = store.sideTheme
      store.toggleTheme()
      expect(store.sideTheme).not.toBe(initialTheme)
    })
  })

  describe('setServerMessage', () => {
    it('应该设置服务器消息', () => {
      const store = useSettingsStore()
      store.setServerMessage('Test message')
      expect(store.serverMessage).toBe('Test message')
    })
  })

  describe('setIsShowUpload', () => {
    it('应该设置上传状态', () => {
      const store = useSettingsStore()
      store.setIsShowUpload(true)
      expect(store.isShowUpload).toBe(true)
    })
  })
})
