import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import PostManage from './index.vue'

describe('PostManage 视图组件测试', () => {
  it('应该导出 PostManage 组件', () => {
    expect(PostManage).toBeDefined()
    expect(typeof PostManage).toBe('object')
  })
})
