import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import About from '../about.vue'

describe('About 视图组件测试', () => {
  it('应该导出 About 组件', () => {
    expect(About).toBeDefined()
    expect(typeof About).toBe('object')
  })
})
