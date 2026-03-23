import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Screenfull from './index.vue'

describe('Screenfull 组件测试', () => {
  it('应该导出 Screenfull 组件', () => {
    expect(Screenfull).toBeDefined()
    expect(typeof Screenfull).toBe('object')
  })
})
