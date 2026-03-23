import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Archive from './index.vue'

describe('Archive 视图组件测试', () => {
  it('应该导出 Archive 组件', () => {
    expect(Archive).toBeDefined()
    expect(typeof Archive).toBe('object')
  })
})
