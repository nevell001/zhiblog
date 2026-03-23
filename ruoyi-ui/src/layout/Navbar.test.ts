import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Navbar from './components/Navbar.vue'

describe('Navbar 组件测试', () => {
  it('应该导出 Navbar 组件', () => {
    expect(Navbar).toBeDefined()
    expect(typeof Navbar).toBe('object')
  })
})
