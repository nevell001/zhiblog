import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Login from './Login.vue'

describe('Blog Login 视图组件测试', () => {
  it('应该导出 Login 组件', () => {
    expect(Login).toBeDefined()
    expect(typeof Login).toBe('object')
  })
})