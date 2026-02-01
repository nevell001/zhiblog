import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Link from './Link.vue'

describe('Sidebar Link 组件测试', () => {
  it('应该导出 Link 组件', () => {
    expect(Link).toBeDefined()
    expect(typeof Link).toBe('object')
  })
})