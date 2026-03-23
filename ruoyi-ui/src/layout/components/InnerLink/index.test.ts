import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import InnerLink from './index.vue'

describe('InnerLink 组件测试', () => {
  it('应该导出 InnerLink 组件', () => {
    expect(InnerLink).toBeDefined()
    expect(typeof InnerLink).toBe('object')
  })
})
