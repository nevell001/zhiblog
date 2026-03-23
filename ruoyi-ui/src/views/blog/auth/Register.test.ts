import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Register from './Register.vue'

describe('Blog Register 视图组件测试', () => {
  it('应该导出 Register 组件', () => {
    expect(Register).toBeDefined()
    expect(typeof Register).toBe('object')
  })
})
