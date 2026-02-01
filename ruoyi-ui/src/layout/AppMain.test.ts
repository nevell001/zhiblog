import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import AppMain from './components/AppMain.vue'

describe('AppMain 组件测试', () => {
  it('应该导出 AppMain 组件', () => {
    expect(AppMain).toBeDefined()
    expect(typeof AppMain).toBe('object')
  })
})